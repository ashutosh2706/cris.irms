package in.gov.irms.train.dto;

import java.util.List;

public class AvlTrainsBtwStnEnquiryResponseDTO {

        public record DirectConnection(
                List<TrainsBtwnStationResponseDTO> trainBtwnStnsList,
                String timeStamp
        ) {}

        public record IndirectConnectionWithSingleTransfer(
                List<ConnectionInfo> connections,
                String timeStamp
        ) {}


        public record ConnectionInfo (
                TrainsBtwnStationResponseDTO firstTrain,
                TrainsBtwnStationResponseDTO secondTrain,
                String totalJourneyDurationMin
        ) {}

}
