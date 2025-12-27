package in.gov.irms.train.dto;

import java.time.LocalTime;

public record InterStationConnectionWithOneTransfer(
    Integer trainNumber_A,
    Integer trainNumber_B,
    long startStationId,
    long transferStationId,
    long endStationId,
    LocalTime departureTimeFromStart_A,
    Integer departureOffsetFromStart_A,
    Integer runningDaysFromStart_A,
    LocalTime arrivalTimeAtTransfer_A,
    Integer arrivalOffsetAtTransfer_A,
    LocalTime departureTimeFromTransfer_B,
    Integer departureOffsetFromTransfer_B,
    Integer runningDaysFromTransfer_B,
    LocalTime arrivalTimeAtEnd_B,
    Integer arrivalOffsetAtEnd_B
) {}
