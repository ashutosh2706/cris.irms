package in.gov.irms.train.service;

import in.gov.irms.train.dto.AvlClassEnquiry;
import in.gov.irms.train.exception.InvalidTrainNumberException;

public interface TrainMasterService {

    public AvlClassEnquiry getAvailableClass(Integer trainNumber) throws InvalidTrainNumberException;

}
