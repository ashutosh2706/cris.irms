package in.gov.irms.train.service;

import in.gov.irms.train.client.StationServiceClient;
import in.gov.irms.train.dto.DirectTrainsBtwStnReqDTO;
import in.gov.irms.train.dto.InterStationConnection;
import in.gov.irms.train.dto.PagedResponseDTO;
import in.gov.irms.train.dto.TrainsBtwnStationResponseDTO;
import in.gov.irms.train.exception.InvalidTrainNumberException;
import in.gov.irms.train.exception.StationServiceException;
import in.gov.irms.train.mapper.CoachInfoMapper;
import in.gov.irms.train.mapper.InterStationConnectionsMapper;
import in.gov.irms.train.model.RouteDetail;
import in.gov.irms.train.repository.RouteDetailRepository;
import in.gov.irms.train.repository.TrainMasterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Date;

@Service
public class RouteDetailServiceImpl implements RouteDetailService {

    private static final Logger log = LoggerFactory.getLogger(RouteDetailServiceImpl.class);
    private final RouteDetailRepository routeDetailRepository;
    private final StationServiceClient stationServiceClient;
    private final TrainMasterRepository trainMasterRepository;

    public RouteDetailServiceImpl(
            RouteDetailRepository routeDetailRepository,
            StationServiceClient stationServiceClient,
            TrainMasterRepository trainMasterRepository
    ) {
        this.routeDetailRepository = routeDetailRepository;
        this.stationServiceClient = stationServiceClient;
        this.trainMasterRepository = trainMasterRepository;
    }

    @Override
    public PagedResponseDTO<RouteDetail> getRouteListByTrainNumber(int trainNumber, int pageNumber, int pageSize) {
        pageNumber = Math.max(1, pageNumber);
        pageSize = Math.max(10, pageSize);
        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
        var routeList = routeDetailRepository.findRouteListByTrainNumber(trainNumber, pageRequest);
        return new PagedResponseDTO<>(
                pageNumber,
                routeList.getNumberOfElements(),
                routeList.getTotalPages(),
                routeList.getTotalElements(),
                routeList.getContent()
        );
    }

    @Override
    public RouteDetail getBeginStationDetail(int trainNumber) throws InvalidTrainNumberException {
        var page = getRouteListByTrainNumber(trainNumber, 1, 10);
        if (page.data().isEmpty()) throw new InvalidTrainNumberException(String.format("TrainNumber: %s is invalid", trainNumber));
        return page.data().get(0);
    }

    @Override
    public RouteDetail getEndStationDetail(int trainNumber) throws InvalidTrainNumberException {
        var page = getRouteListByTrainNumber(trainNumber, 1, Integer.MAX_VALUE-1);
        if (page.data().isEmpty()) throw new InvalidTrainNumberException(String.format("TrainNumber: %s is invalid", trainNumber));
        return page.data().get((int) page.pageSize() - 1);
    }

    @Override
    public Map<String, Object> getDirectTrainsBtwStations(DirectTrainsBtwStnReqDTO reqDTO) throws StationServiceException, InvalidTrainNumberException {
        String stationFrom = reqDTO.stationFrom();
        String stationTo = reqDTO.stationTo();
        String onDate = reqDTO.date();
        LocalDate date = LocalDate.parse(onDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        int dayOfWeek = date.getDayOfWeek().getValue() % 7 + 1; // starting Sunday
        var fromStationData = stationServiceClient.getStnDetailByStnCode(stationFrom);
        var toStationData = stationServiceClient.getStnDetailByStnCode(stationTo);
        java.util.List<InterStationConnection> stationConnections = routeDetailRepository.findDirectTrainsBetweenTwoStn(
                fromStationData.stationId(),
                toStationData.stationId()
        ).stream()
                .map(InterStationConnectionsMapper::toInterStationConnection)
                .filter(connection -> (connection.departsOnWeekDays() & (1 << (7-dayOfWeek))) > 0)
                .toList();
        log.info("Connections.length: {}", stationConnections.size());

        List<TrainsBtwnStationResponseDTO> trainsBtwnStationList = new ArrayList<>();
        for(var connection: stationConnections) {
            var trainMasterData = trainMasterRepository.findByTrainNumber(connection.trainNumber())
                    .orElseThrow(() -> new InvalidTrainNumberException(String.format("TrainNumber: %s is invalid", connection.trainNumber())));

            LocalTime arrivalAtDest = connection.arrivalTime();
            LocalTime departureFromSrc = connection.departureTime();
            int lapseDayCount = connection.journeyEndOn() - connection.journeyStartOn();
            int totalDurationMin = 0;
            if (lapseDayCount >= 1) {
                // multi-day journey
                totalDurationMin = ((lapseDayCount-1)*24*60) +
                        ((24*60) - departureFromSrc.getHour()*60 - departureFromSrc.getMinute()) +
                        arrivalAtDest.getHour()*60 + arrivalAtDest.getMinute();
            } else {
                // one day journey
                totalDurationMin = (arrivalAtDest.getHour()*60 + arrivalAtDest.getMinute()) - (departureFromSrc.getHour()*60 + departureFromSrc.getMinute());
            }
            trainsBtwnStationList.add(new TrainsBtwnStationResponseDTO(
                    connection.trainNumber().toString(),
                    trainMasterData.getTrainName(),
                    stationFrom,
                    stationTo,
                    connection.arrivalTime().toString(),
                    connection.departureTime().toString(),
                    String.format("%sKM", connection.distanceCovered()/1000),
                    totalDurationMin > 0 ? String.format("%s hours %s minutes", totalDurationMin / 60, totalDurationMin % 60) : "--:--",    // calculate duration based on departure time, arrival time and day offset diff
                    (connection.departsOnWeekDays() & (1<<5)) > 0 ? "Y" : "N",  // monday
                    (connection.departsOnWeekDays() & (1<<4)) > 0 ? "Y" : "N",
                    (connection.departsOnWeekDays() & (1<<3)) > 0 ? "Y" : "N",
                    (connection.departsOnWeekDays() & (1<<2)) > 0 ? "Y" : "N",
                    (connection.departsOnWeekDays() & (1<<1)) > 0 ? "Y" : "N",
                    (connection.departsOnWeekDays() & 1) > 0 ? "Y" : "N",
                    (connection.departsOnWeekDays() & (1<<6)) > 0 ? "Y" : "N",  // sunday
                    CoachInfoMapper.toAvlClassSimpleList(trainMasterData.getCoachInfo()),
                    trainMasterData.getTrainType().toString()
            ));
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("trainBtwnStnsList", trainsBtwnStationList);
        response.put("timeStamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return response;
    }
}
