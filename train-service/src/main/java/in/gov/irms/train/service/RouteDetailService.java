package in.gov.irms.train.service;

import in.gov.irms.train.dto.DirectTrainsBtwStnReqDTO;
import in.gov.irms.train.dto.InterStationConnection;
import in.gov.irms.train.dto.PagedResponseDTO;
import in.gov.irms.train.exception.InvalidTrainNumberException;
import in.gov.irms.train.exception.StationServiceException;
import in.gov.irms.train.model.RouteDetail;

import java.util.List;


public interface RouteDetailService {

    PagedResponseDTO<RouteDetail> getRouteListByTrainNumber(int trainNumber, int pageNumber, int pageSize);
    RouteDetail getBeginStationDetail(int trainNumber) throws InvalidTrainNumberException;
    RouteDetail getEndStationDetail(int trainNumber) throws InvalidTrainNumberException;
    List<InterStationConnection> getDirectTrainsBtwStations(DirectTrainsBtwStnReqDTO reqDTO) throws StationServiceException;
}
