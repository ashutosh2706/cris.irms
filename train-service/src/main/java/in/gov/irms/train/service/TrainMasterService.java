package in.gov.irms.train.service;

import in.gov.irms.train.dto.AvlClassResponseDto;
import in.gov.irms.train.exception.InvalidTrainNumberException;

public interface TrainMasterService {

    public AvlClassResponseDto getAvailableClass(Integer trainNumber) throws InvalidTrainNumberException;

}
