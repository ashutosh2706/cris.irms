package in.gov.irms.train.dto;

public record RouteStationInfo(
        String stationCode,
        String stationName,
        String arrivalTime,
        String departureTime,
        String haltTime,
        String distance,
        String dayCount,
        String stnSerialNumber,
        String boardingDisabled

) {}
