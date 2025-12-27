package in.gov.irms.train.service;

import in.gov.irms.train.client.StationServiceClient;
import in.gov.irms.train.dto.AvlTrainsBtwStnEnquiryDTO;
import in.gov.irms.train.dto.AvlTrainsBtwStnEnquiryResponseDTO;
import in.gov.irms.train.dto.InterStationConnectionWithOneTransfer;
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
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Arrays;
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
    public AvlTrainsBtwStnEnquiryResponseDTO.DirectConnection getDirectTrainsBtwStations(AvlTrainsBtwStnEnquiryDTO reqDTO) throws StationServiceException, InvalidTrainNumberException {
        String stationFrom = reqDTO.stationFrom();
        String stationTo = reqDTO.stationTo();
        String onDate = reqDTO.date();
        LocalDate date = LocalDate.parse(onDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        int dayOfWeek = date.getDayOfWeek().getValue() % 7 + 1; // starting Sunday
        var fromStationData = stationServiceClient.getStnDetailByStnCode(stationFrom);
        var toStationData = stationServiceClient.getStnDetailByStnCode(stationTo);
        java.util.List<InterStationConnection> stationConnections = routeDetailRepository.findDirectTrainsBetweenStn(
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

        return new AvlTrainsBtwStnEnquiryResponseDTO.DirectConnection (
                trainsBtwnStationList,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );
    }

    @Override
    public AvlTrainsBtwStnEnquiryResponseDTO.IndirectConnectionWithSingleTransfer getIndirectTrainBtwStn(AvlTrainsBtwStnEnquiryDTO enquiryDTO, boolean oneTransferJourney)
            throws StationServiceException, InvalidTrainNumberException {
        String stationFrom = enquiryDTO.stationFrom();
        String stationTo = enquiryDTO.stationTo();
        String onDate = enquiryDTO.date();
        log.info("One transfer request: {}", oneTransferJourney);
        LocalDate date = LocalDate.parse(onDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        int dayOfWeek = date.getDayOfWeek().getValue() % 7 + 1; // starting Sunday
        var fromStationData = stationServiceClient.getStnDetailByStnCode(stationFrom);
        var toStationData = stationServiceClient.getStnDetailByStnCode(stationTo);
        if (!oneTransferJourney) return null;   // doesn't make any sense to implement this
        java.util.List<InterStationConnectionWithOneTransfer> singleTransferConnections = routeDetailRepository.findIndirectTrainsBtwStnWithOneTransfer(
                fromStationData.stationId(), toStationData.stationId())
                .stream()
                .map(InterStationConnectionsMapper::toInterStationConnectionOneTransfer)
                .filter(connection -> (connection.runningDaysFromStart_A() & (1 << (7-dayOfWeek))) > 0)
                // ignore those connections where trainB departs from transfer after more than one day of arrival of trainA
                .filter(connection -> {
                    var arrivalDayAtTransfer_A = dayOfWeek + (connection.arrivalOffsetAtTransfer_A() - connection.departureOffsetFromStart_A());
                    arrivalDayAtTransfer_A = arrivalDayAtTransfer_A > 7 ?
                            arrivalDayAtTransfer_A % 7 :
                            arrivalDayAtTransfer_A;
                    var nextDayOfArrivalAtTransfer_A = (arrivalDayAtTransfer_A+1) > 7 ?
                            (arrivalDayAtTransfer_A+1)%7 :
                            (arrivalDayAtTransfer_A+1);
                    if ((connection.runningDaysFromTransfer_B() & (1 << (7-arrivalDayAtTransfer_A))) > 0) {
                        if (connection.arrivalTimeAtTransfer_A().isBefore(connection.departureTimeFromTransfer_B())) {
                            log.info("TrainB is departing after arrival of trainA on same day");
                            return true;
                        }
                    }
                    // check if trainB is departing on next day of arrival
                    return ((1<<(7-nextDayOfArrivalAtTransfer_A)) & connection.runningDaysFromTransfer_B()) > 0;
                })
                .toList();

        // Map definition
        // <trainNumber_A, trainNumber_B> {duration_A, duration_B, totalDuration}
        Map<List<Integer>, List<Long>> journeyDurationMap = new HashMap<>();
        singleTransferConnections.forEach(connection -> {
            // calculate duration for first half journey by train A
            var departureTimeFromStartOfTrainA = connection.departureTimeFromStart_A();
            var arrivalTimeAtTransferOfTrainA = connection.arrivalTimeAtTransfer_A();
            long lapsedDayOffsetOfTrainA = connection.arrivalOffsetAtTransfer_A()-connection.departureOffsetFromStart_A();
            long totalDurationMin_A = 0, totalDurationMin_B = 0, totalWaitDurationMin = 0;
            if (lapsedDayOffsetOfTrainA >= 1)
                totalDurationMin_A += (lapsedDayOffsetOfTrainA-1)*1440 + (1440 - departureTimeFromStartOfTrainA.getHour()*60 - departureTimeFromStartOfTrainA.getMinute()) +
                        arrivalTimeAtTransferOfTrainA.getHour()*60 + arrivalTimeAtTransferOfTrainA.getMinute();
            else
                totalDurationMin_A += (arrivalTimeAtTransferOfTrainA.getHour()*60 + arrivalTimeAtTransferOfTrainA.getMinute()) -
                        (departureTimeFromStartOfTrainA.getHour()*60 + departureTimeFromStartOfTrainA.getMinute());

            // for second half of journey by train B
            var departureTimeFromTransferOfTrainB = connection.departureTimeFromTransfer_B();
            var arrivalTimeAtEndOfTrainB = connection.arrivalTimeAtEnd_B();
            long lapsedDayOffsetOfTrainB = connection.arrivalOffsetAtEnd_B() - connection.departureOffsetFromTransfer_B();
            if (lapsedDayOffsetOfTrainB >= 1)
                totalDurationMin_B += (lapsedDayOffsetOfTrainB-1)*1440 + (1440 - departureTimeFromTransferOfTrainB.getHour()*60 - departureTimeFromTransferOfTrainB.getMinute()) +
                        arrivalTimeAtEndOfTrainB.getHour()*60 + arrivalTimeAtEndOfTrainB.getMinute();
            else
                totalDurationMin_B += (arrivalTimeAtEndOfTrainB.getHour()*60 + arrivalTimeAtEndOfTrainB.getMinute()) -
                        (departureTimeFromTransferOfTrainB.getHour()*60 + departureTimeFromTransferOfTrainB.getMinute());

            var arrivalDayAtTransfer_A = dayOfWeek+(connection.arrivalOffsetAtTransfer_A()-connection.departureOffsetFromStart_A());
            arrivalDayAtTransfer_A = arrivalDayAtTransfer_A > 7 ? arrivalDayAtTransfer_A % 7 : arrivalDayAtTransfer_A;
            if ((connection.runningDaysFromTransfer_B() & (1 << (7-arrivalDayAtTransfer_A))) > 0) {
                // account for wait time at transfer
                // train B will always depart after arrival of train A
                totalWaitDurationMin += (departureTimeFromTransferOfTrainB.getHour()*60 + departureTimeFromTransferOfTrainB.getMinute()) -
                        (arrivalTimeAtTransferOfTrainA.getHour()*60 + arrivalTimeAtTransferOfTrainA.getMinute());
            } else {
                // overnight transfer wait time
                totalWaitDurationMin += (1440 - arrivalTimeAtTransferOfTrainA.getHour()*60 - arrivalTimeAtTransferOfTrainA.getMinute()) +
                        departureTimeFromTransferOfTrainB.getHour()*60 + departureTimeFromTransferOfTrainB.getMinute();
            }

            var trainNumbers = Arrays.asList(connection.trainNumber_A(), connection.trainNumber_B());
            var totalJourneyDuration = totalDurationMin_A + totalDurationMin_B + totalWaitDurationMin;
            if (!journeyDurationMap.containsKey(trainNumbers))
                journeyDurationMap.put(trainNumbers, Arrays.asList(totalDurationMin_A, totalDurationMin_B, totalJourneyDuration));
            else {
                if(journeyDurationMap.get(trainNumbers).get(2) > totalJourneyDuration)
                    journeyDurationMap.put(trainNumbers, Arrays.asList(totalDurationMin_A, totalDurationMin_B, totalJourneyDuration));
            }
        });

        // map each connection to dto taking total travel duration from journeyDurationMap
        /**
         * TODO: modify query to return distance covered by both trains in their respective journey
         */
        List<AvlTrainsBtwStnEnquiryResponseDTO.ConnectionInfo> connInfoList = new ArrayList<>();
        for (var connection: singleTransferConnections) {

            var durationFromMap = journeyDurationMap.get(Arrays.asList(connection.trainNumber_A(), connection.trainNumber_B()));
            var trainMasterData_A = trainMasterRepository.findByTrainNumber(connection.trainNumber_A())
                    .orElseThrow(() -> new InvalidTrainNumberException(String.format("TrainNumber: %s is invalid", connection.trainNumber_A())));
            var trainMasterData_B = trainMasterRepository.findByTrainNumber(connection.trainNumber_B())
                    .orElseThrow(() -> new InvalidTrainNumberException(String.format("TrainNumber: %s is invalid", connection.trainNumber_B())));

            var train_A_Details = new TrainsBtwnStationResponseDTO(
                    String.valueOf(trainMasterData_A.getTrainNumber()),
                    trainMasterData_A.getTrainName(),
                    enquiryDTO.stationFrom(),
                    enquiryDTO.stationTo(),         // transfer
                    connection.arrivalTimeAtTransfer_A().toString(),
                    connection.departureTimeFromStart_A().toString(),
                    "--KM",
                    durationFromMap.get(0) > 0 ? String.format("%s:%s", durationFromMap.get(0)/60, durationFromMap.get(0)%60) : "--:--",
                    (connection.runningDaysFromStart_A() & 32) > 0 ? "Y":"N", // monday
                    (connection.runningDaysFromStart_A() & 16) > 0 ? "Y":"N",
                    (connection.runningDaysFromStart_A() & 8) > 0 ? "Y":"N",
                    (connection.runningDaysFromStart_A() & 4) > 0 ? "Y":"N",
                    (connection.runningDaysFromStart_A() & 2) > 0 ? "Y":"N",
                    (connection.runningDaysFromStart_A() & 1) > 0 ? "Y":"N",
                    (connection.runningDaysFromStart_A() & 64) > 0 ? "Y":"N", // sunday
                    CoachInfoMapper.toAvlClassSimpleList(trainMasterData_A.getCoachInfo()),
                    trainMasterData_A.getTrainType().toString()
            );

            var train_B_Details = new TrainsBtwnStationResponseDTO(
                    String.valueOf(trainMasterData_B.getTrainNumber()),
                    trainMasterData_B.getTrainName(),
                    enquiryDTO.stationFrom(),   // transfer
                    enquiryDTO.stationTo(),     // end
                    connection.arrivalTimeAtEnd_B().toString(),
                    connection.departureTimeFromTransfer_B().toString(),
                    "--KM",
                    durationFromMap.get(1) > 0 ? String.format("%s:%s", durationFromMap.get(1)/60, durationFromMap.get(1)%60) : "--:--",
                    (connection.runningDaysFromTransfer_B() & 32) > 0 ? "Y":"N", // monday
                    (connection.runningDaysFromTransfer_B() & 16) > 0 ? "Y":"N",
                    (connection.runningDaysFromTransfer_B() & 8) > 0 ? "Y":"N",
                    (connection.runningDaysFromTransfer_B() & 4) > 0 ? "Y":"N",
                    (connection.runningDaysFromTransfer_B() & 2) > 0 ? "Y":"N",
                    (connection.runningDaysFromTransfer_B() & 1) > 0 ? "Y":"N",
                    (connection.runningDaysFromTransfer_B() & 64) > 0 ? "Y":"N", // sunday
                    CoachInfoMapper.toAvlClassSimpleList(trainMasterData_B.getCoachInfo()),
                    trainMasterData_B.getTrainType().toString()
            );

            connInfoList.add(new AvlTrainsBtwStnEnquiryResponseDTO.ConnectionInfo(
                    train_A_Details,
                    train_B_Details,
                    durationFromMap.get(2).toString()
            ));
        }

        log.info("MAP: {}", journeyDurationMap);
        log.info("Single Transfer Conn: {}", connInfoList);
        return new AvlTrainsBtwStnEnquiryResponseDTO.IndirectConnectionWithSingleTransfer(
                connInfoList,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );
    }
}
