package in.gov.irms.train.service;

import in.gov.irms.train.dto.AvlClassResponseDto;
import in.gov.irms.train.dto.TrainMasterEnquiryResponseDTO;
import in.gov.irms.train.exception.InvalidTrainNumberException;
import in.gov.irms.train.exception.StationServiceException;

public interface TrainMasterService {

    AvlClassResponseDto getAvailableClass(Integer trainNumber) throws InvalidTrainNumberException;
    TrainMasterEnquiryResponseDTO getTrainInfo(int trainNumber) throws InvalidTrainNumberException, StationServiceException;

}
