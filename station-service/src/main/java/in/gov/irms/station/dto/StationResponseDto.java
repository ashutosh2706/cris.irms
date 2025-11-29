package in.gov.irms.station.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.gov.irms.station.enums.StationType;
import jakarta.annotation.Nullable;

public record StationResponseDto(
        String stationCode,
        String stationName,
        String stateName,
        String division,
        StationType stationType,
        Integer elevationFromSea,
        int platformCount,
        boolean boardingDisabled
) {}
