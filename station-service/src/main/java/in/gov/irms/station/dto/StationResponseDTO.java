package in.gov.irms.station.dto;

import in.gov.irms.station.enums.StationType;

public record StationResponseDTO(
        String stationCode,
        String stationName,
        String stateName,
        String division,
        StationType stationType,
        Integer elevationFromSea,
        int platformCount,
        boolean boardingDisabled
) {}
