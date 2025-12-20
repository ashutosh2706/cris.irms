package in.gov.irms.train.service;

import in.gov.irms.train.client.StationServiceClient;
import in.gov.irms.train.dto.DirectTrainsBtwStnReqDTO;
import in.gov.irms.train.dto.InterStationConnection;
import in.gov.irms.train.dto.PagedResponseDTO;
import in.gov.irms.train.exception.InvalidTrainNumberException;
import in.gov.irms.train.exception.StationServiceException;
import in.gov.irms.train.mapper.InterStationConnectionsMapper;
import in.gov.irms.train.model.RouteDetail;
import in.gov.irms.train.repository.RouteDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteDetailServiceImpl implements RouteDetailService {

    private static final Logger log = LoggerFactory.getLogger(RouteDetailServiceImpl.class);
    private final RouteDetailRepository routeDetailRepository;
    private final StationServiceClient stationServiceClient;

    public RouteDetailServiceImpl(RouteDetailRepository routeDetailRepository, StationServiceClient stationServiceClient) {
        this.routeDetailRepository = routeDetailRepository;
        this.stationServiceClient = stationServiceClient;
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
    public List<InterStationConnection> getDirectTrainsBtwStations(DirectTrainsBtwStnReqDTO reqDTO) throws StationServiceException {
        String stationFrom = reqDTO.stationFrom();
        String stationTo = reqDTO.stationTo();
        String onDate = reqDTO.date();
        var fromStationData = stationServiceClient.getStnDetailByStnCode(stationFrom);
        var toStationData = stationServiceClient.getStnDetailByStnCode(stationTo);
        java.util.List<InterStationConnection> stationConnections = routeDetailRepository.findDirectTrainsBetweenTwoStn(
                fromStationData.stationId(),
                toStationData.stationId()
        ).stream()
                .map(InterStationConnectionsMapper::toInterStationConnection)
                .toList();
        log.info("Connections length: {}", stationConnections.size());
        return stationConnections;
    }
}
