package in.gov.irms.train.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class StationServiceApi {
    public record BulkStationDetailRequestDTO(
            @NotNull(message = "Parameter 'stationId' is required")
            @NotEmpty(message = "List 'stationId' must not be empty")
            List<Long> stationId
    ) {}

    public record StationResponseDTO(
            Long stationId,
            String stationCode,
            String stationName,
            String stateName,
            String division,
            String stationType,
            Integer elevationFromSea,
            int platformCount,
            boolean boardingDisabled
    ) {}
}
