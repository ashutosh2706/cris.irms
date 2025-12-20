package in.gov.irms.train.dto;

import java.time.LocalTime;

public record InterStationConnection (
        Integer trainNumber,
        Long stationIdFrom,
        LocalTime departureTime,
        Integer journeyStartOn,
        Long stationIdTo,
        LocalTime arrivalTime,
        Integer journeyEndOn,
        Long distanceCovered,
        Integer departsOnWeekDays

) {}
