package in.gov.irms.train.service;

import in.gov.irms.train.dto.AvlTrainsBtwStnEnquiryDTO;
import in.gov.irms.train.dto.AvlTrainsBtwStnEnquiryResponseDTO;
import in.gov.irms.train.dto.PagedResponseDTO;
import in.gov.irms.train.exception.InvalidTrainNumberException;
import in.gov.irms.train.exception.StationServiceException;
import in.gov.irms.train.model.RouteDetail;

import java.util.List;


public interface RouteDetailService {

    PagedResponseDTO<RouteDetail> getRouteListByTrainNumber(int trainNumber, int pageNumber, int pageSize);
    RouteDetail getBeginStationDetail(int trainNumber) throws InvalidTrainNumberException;
    RouteDetail getEndStationDetail(int trainNumber) throws InvalidTrainNumberException;
    AvlTrainsBtwStnEnquiryResponseDTO.DirectConnection getDirectTrainsBtwStations(AvlTrainsBtwStnEnquiryDTO reqDTO) throws StationServiceException, InvalidTrainNumberException;
    AvlTrainsBtwStnEnquiryResponseDTO.IndirectConnectionWithSingleTransfer getIndirectTrainBtwStn(AvlTrainsBtwStnEnquiryDTO enquiryDTO, boolean oneTransferJourney) throws StationServiceException, InvalidTrainNumberException;
}
