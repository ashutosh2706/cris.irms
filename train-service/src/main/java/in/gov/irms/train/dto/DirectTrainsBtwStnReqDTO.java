package in.gov.irms.train.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DirectTrainsBtwStnReqDTO (
        @NotBlank(message = "Parameter 'stationFrom' can't be empty")
        @NotNull(message = "Parameter 'stationFrom' is required")
        String stationFrom,
        @NotBlank(message = "Parameter 'stationTo' can't be empty")
        @NotNull(message = "Parameter 'stationTo' is required")
        String stationTo,
        @NotBlank(message = "Parameter 'date' can't be empty")
        @NotNull(message = "Parameter 'date' is required")
        String date
) {}
