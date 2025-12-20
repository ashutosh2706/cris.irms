package in.gov.irms.train.mapper;

import in.gov.irms.train.dto.InterStationConnection;

import java.time.LocalTime;

public class InterStationConnectionsMapper {
    public static InterStationConnection toInterStationConnection(Object[] queryResult) {
        return new InterStationConnection(
                // trainNumber
                (Integer) queryResult[0],
                // stationIdFrom
                ((Number) queryResult[1]).longValue(),
                // departureTime (in sql.time)
                ((java.sql.Time) queryResult[2]).toLocalTime(),
                // journeyStartOn
                (Integer) queryResult[3],
                // stationIdTo
                ((Number) queryResult[4]).longValue(),
                // arrivalTime (in sql.time)
                ((java.sql.Time) queryResult[5]).toLocalTime(),
                // journeyEndOn
                (Integer) queryResult[6],
                // distanceCovered
                ((Number) queryResult[7]).longValue(),
                // departOnWeekDays
                (Integer) queryResult[8]
        );
    }
}
