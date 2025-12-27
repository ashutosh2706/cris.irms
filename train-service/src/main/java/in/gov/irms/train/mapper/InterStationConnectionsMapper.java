package in.gov.irms.train.mapper;

import in.gov.irms.train.dto.InterStationConnection;
import in.gov.irms.train.dto.InterStationConnectionWithOneTransfer;

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

    public static InterStationConnectionWithOneTransfer toInterStationConnectionOneTransfer(Object[] queryResult) {
        return new InterStationConnectionWithOneTransfer(
                // trainNumber A
                (Integer) queryResult[0],
                // trainNumber B
                (Integer) queryResult[1],
                // startStationId
                ((Number) queryResult[2]).longValue(),
                // transferStationId
                ((Number) queryResult[3]).longValue(),
                // endStationId
                ((Number) queryResult[4]).longValue(),
                // departureTimeFromStart A
                ((java.sql.Time) queryResult[5]).toLocalTime(),
                // departureOffsetFromStart A
                (Integer) queryResult[6],
                // runningDaysFromStart A
                (Integer) queryResult[7],
                // arrivalTimeAtTransfer A
                ((java.sql.Time) queryResult[8]).toLocalTime(),
                // arrivalOffsetAtTransfer A
                (Integer) queryResult[9],
                // departureTimeFromTransfer B
                ((java.sql.Time) queryResult[10]).toLocalTime(),
                // departureOffsetFromTransfer B
                (Integer) queryResult[11],
                // runningDaysFromTransfer B
                (Integer) queryResult[12],
                // arrivalTimeAtEnd B
                ((java.sql.Time) queryResult[13]).toLocalTime(),
                // arrivalOffsetAtEnd B
                (Integer) queryResult[14]
        );
    }
}
