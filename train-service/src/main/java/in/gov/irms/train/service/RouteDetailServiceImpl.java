package in.gov.irms.train.service;

import in.gov.irms.train.client.StationServiceClient;
import in.gov.irms.train.dto.DirectTrainsBtwStnReqDTO;
import in.gov.irms.train.dto.PagedResponseDTO;
import in.gov.irms.train.exception.InvalidTrainNumberException;
import in.gov.irms.train.exception.StationServiceException;
import in.gov.irms.train.model.RouteDetail;
import in.gov.irms.train.repository.RouteDetailRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class RouteDetailServiceImpl implements RouteDetailService {

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

    public void getDirectTrainsBtwStations(DirectTrainsBtwStnReqDTO reqDTO) throws StationServiceException {
        String fromStation = reqDTO.fromStation();
        String toStation = reqDTO.toStation();
        var fromStationData = stationServiceClient.getStnDetailByStnCode(fromStation);
        var toStationData = stationServiceClient.getStnDetailByStnCode(toStation);

    }
}
