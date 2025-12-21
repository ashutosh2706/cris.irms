package in.gov.irms.train.dto;

import java.util.List;

/*
this response dto has exact same field order and names as present in official irctc api response
 */
public record TrainsBtwnStationResponseDTO (
        String trainNumber,
        String trainName,
        String fromStnCode,
        String toStnCode,
        String arrivalTime,
        String departureTime,
        String distance,
        String duration,
        String runningMon,
        String runningTue,
        String runningWed,
        String runningThu,
        String runningFri,
        String runningSat,
        String runningSun,
        List<String> avlClasses,
        String trainType
) {
}