package in.gov.irms.station.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BulkStationDetailRequestDTO(
        @NotNull(message = "Parameter 'stationId' is required")
        @NotEmpty(message = "List 'stationId' must not be empty")
        List<Long> stationId
) {}
