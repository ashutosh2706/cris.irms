package in.gov.irms.train.service;

import in.gov.irms.train.client.StationServiceClient;
import in.gov.irms.train.dto.AvlClassResponseDto;
import in.gov.irms.train.dto.TrainEnquiryResponseDTO;
import in.gov.irms.train.dto.PagedResponseDTO;
import in.gov.irms.train.dto.RouteStationInfo;
import in.gov.irms.train.dto.StationServiceApi;
import in.gov.irms.train.exception.InvalidTrainNumberException;
import in.gov.irms.train.exception.StationServiceException;
import in.gov.irms.train.mapper.CoachInfoMapper;
import in.gov.irms.train.model.RouteDetail;
import in.gov.irms.train.model.TrainMaster;
import in.gov.irms.train.repository.CoachInfoRepository;
import in.gov.irms.train.repository.TrainMasterRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class TrainMasterServiceImpl implements TrainMasterService {

    private final CoachInfoRepository coachInfoRepository;
    private final TrainMasterRepository trainMasterRepository;
    private final RouteDetailService routeDetailService;
    private final StationServiceClient stationServiceClient;

    public TrainMasterServiceImpl(
            CoachInfoRepository coachInfoRepository,
            TrainMasterRepository trainMasterRepository,
            RouteDetailService routeDetailService,
            StationServiceClient stationServiceClient
    )
    {
        this.coachInfoRepository = coachInfoRepository;
        this.trainMasterRepository = trainMasterRepository;
        this.routeDetailService = routeDetailService;
        this.stationServiceClient = stationServiceClient;
    }

    @Override
    public AvlClassResponseDto getAvailableClass(Integer trainNumber) throws InvalidTrainNumberException {
        var coachInformation = coachInfoRepository.findCoachInfoByTrainNumber(trainNumber);
        if (coachInformation.isEmpty()) throw new InvalidTrainNumberException(String.format("Train Number: %s is invalid", trainNumber));
        var coachInfo = coachInformation.get();
        return CoachInfoMapper.toAvlClassResponseDTO(coachInfo);
    }

    @Override
    public TrainEnquiryResponseDTO getTrainInfo(int trainNumber) throws InvalidTrainNumberException, StationServiceException {
        TrainMaster trainMaster = trainMasterRepository.findByTrainNumber(trainNumber).orElseThrow(
                () -> new InvalidTrainNumberException(String.format("TrainNumber: %s is invalid", trainNumber))
        );
        var avlClassList = CoachInfoMapper.toAvlClass(trainMaster.getCoachInfo());
        PagedResponseDTO<RouteDetail> routeDetailPage = routeDetailService.getRouteListByTrainNumber(trainNumber, 1, 150);
        var stationIdList = routeDetailPage.data().stream().map(RouteDetail::getStationId).toList();
        Map<Long, StationServiceApi.StationResponseDTO> responseDTOMap = stationServiceClient.getStationDetailByBulkId(
                new StationServiceApi.BulkStationDetailRequestDTO(stationIdList)
        );

        var from = routeDetailService.getBeginStationDetail(trainNumber);
        var to = routeDetailService.getEndStationDetail(trainNumber);
        String fromStationCode = responseDTOMap.getOrDefault(from.getStationId(), null) == null ? "" : responseDTOMap.get(from.getStationId()).stationCode();
        String toStationCode = responseDTOMap.getOrDefault(to.getStationId(), null) == null ? "" : responseDTOMap.get(to.getStationId()).stationCode();
        int runningDaysBit = from.getDepartsOnWeekDays();
        // starts with saturday...sunday
        List<String> runningDays = Stream.iterate(0, n -> n<7, n->n+1).map(n ->
            ((runningDaysBit & (1 << n)) > 0) ? "Y" : "N"
        ).toList();

        List<RouteStationInfo> routeInfoList = new ArrayList<>();
        routeDetailPage.data().forEach(route -> {
            var station = responseDTOMap.getOrDefault(route.getStationId(), null);
            var stationInfo = new RouteStationInfo(
                    station == null ? "" : station.stationCode(),
                    station == null ? "" : station.stationName(),
                    station == null ? "" : station.stateName(),
                    route.getArrivalSequence() == 0 ? "--:--" : route.getArrivalTime().toString(),
                    route.getArrivalSequence() == (routeDetailPage.data().size()-1) ? "--:--" : route.getDepartureTime().toString(),
                    LocalTime.MIDNIGHT.plus(Duration.between(route.getArrivalTime(), route.getDepartureTime())).toString(),
                    (route.getDistanceCovered()/1000) + "KM",
                    String.valueOf(route.getDayOffset()+1),
                    String.valueOf(route.getArrivalSequence()+1),
                    station == null ? "" : station.boardingDisabled() ? "true" : "false"
            );
            routeInfoList.add(stationInfo);
        });

        return new TrainEnquiryResponseDTO(
                trainMaster.getTrainNumber(),
                trainMaster.getTrainName(),
                trainMaster.getTrainType().toString(),
                fromStationCode,
                toStationCode,
                runningDays.get(6),
                runningDays.get(5),
                runningDays.get(4),
                runningDays.get(3),
                runningDays.get(2),
                runningDays.get(1),
                runningDays.get(0),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                avlClassList,
                routeInfoList
        );
    }
}
