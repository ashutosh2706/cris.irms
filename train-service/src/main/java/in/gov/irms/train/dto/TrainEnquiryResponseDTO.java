package in.gov.irms.train.dto;

import java.util.List;

public record TrainEnquiryResponseDTO(
   Integer trainNumber,
   String trainName,
   String trainType,
   String stationFrom,
   String stationTo,
   String trainRunsOnSun,
   String trainRunsOnMon,
   String trainRunsOnTue,
   String trainRunsOnWed,
   String trainRunsOnThu,
   String trainRunsOnFri,
   String trainRunsOnSat,
   String enquiryTimeStamp,
   Object avlClasses,
   List<RouteStationInfo> stationsList
) {}
