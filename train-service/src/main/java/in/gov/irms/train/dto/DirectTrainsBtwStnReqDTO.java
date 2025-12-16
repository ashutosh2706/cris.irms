package in.gov.irms.train.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DirectTrainsBtwStnReqDTO (
        @NotBlank(message = "Parameter 'fromStation' can't be empty")
        @NotNull(message = "Parameter 'fromStation' is required")
        String fromStation,
        @NotBlank(message = "Parameter 'toStation' can't be empty")
        @NotNull(message = "Parameter 'toStation' is required")
        String toStation,
        @NotBlank(message = "Parameter 'onDate' can't be empty")
        @NotNull(message = "Parameter 'onDate' is required")
        String onDate
) {}
