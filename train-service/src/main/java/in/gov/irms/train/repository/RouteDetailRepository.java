package in.gov.irms.train.repository;

import in.gov.irms.train.model.RouteDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteDetailRepository extends JpaRepository<RouteDetail, Long> {

    @Query(value = """
            SELECT rd
            FROM RouteDetail rd
            WHERE rd.trainNumber = :trainNumber
            ORDER BY rd.arrivalSequence ASC
            """
    )
    Page<RouteDetail> findRouteListByTrainNumber(@Param(value = "trainNumber") int trainNumber, Pageable pageable);

    // from each station ~ 100 trains cross every day
    // returns list of routeDetails without pagination ~ maximum 100 records
    @Query(value = """
            SELECT r1.train_number as train_number, r1.station_id as station_id_from,
            r1.departure_time as departure_time, r1.day_offset as journey_start_on,
            r2.station_id as station_id_to, r2.arrival_time as arrival_time,
            r2.day_offset as journey_end_on, r2.distance_covered - r1.distance_covered as distance_covered,
            r1.departs_on_week_days as departs_on_week_days
            FROM trains.t_route_detail r1 JOIN trains.t_route_detail r2
            ON r1.train_number = r2.train_number
            WHERE r1.station_id = :stationIdFrom AND r2.station_id = :stationIdTo AND r1.arrival_sequence < r2.arrival_sequence
            ORDER BY r1.departure_time;
            """, nativeQuery = true
    )
    List<Object[]> findDirectTrainsBetweenStn(
            @Param(value = "stationIdFrom") long stationIdFrom,
            @Param(value = "stationIdTo") long stationIdTo
    );

    @Query(value = """
                SELECT
                    t1.train_number AS t1_train_number,
                    t3.train_number AS t2_train_number,
                    t1.station_id AS start_station_id,
                    t3.station_id AS transfer_station_id,
                    t4.station_id AS end_station_id,
                    t1.departure_time AS t1_departure_from_start_time,
                    t1.day_offset as t1_departure_from_start_offset,
                    t1.departs_on_week_days as t1_running_days_from_start,
                    t2.arrival_time AS t1_arrival_at_transfer_time,
                    t2.day_offset as t1_arrival_at_transfer_offset,
                    t3.departure_time AS t2_departure_from_transfer_time,
                    t3.day_offset as t2_departure_from_transfer_offset,
                    t3.departs_on_week_days as t2_running_days_from_transfer,
                    t4.arrival_time AS t2_arrival_at_end_time,
                    t4.day_offset as t2_arrival_at_end_offset
                FROM trains.t_route_detail t1
                JOIN trains.t_route_detail t2 ON
                    t1.train_number = t2.train_number
                    AND t1.arrival_sequence < t2.arrival_sequence
                    AND t1.station_id = :stationIdFrom
                JOIN trains.t_route_detail t3 ON
                    t2.station_id = t3.station_id
                    and t2.train_number != t3.train_number
                JOIN trains.t_route_detail t4 ON
                    t3.train_number = t4.train_number
                    AND t3.arrival_sequence < t4.arrival_sequence
                    AND t4.station_id = :stationIdTo
                ORDER BY t1.departure_time;
                    """,
            nativeQuery = true
    )
    List<Object[]> findIndirectTrainsBtwStnWithOneTransfer(
            @Param(value = "stationIdFrom") long stationIdFrom,
            @Param(value = "stationIdTo") long stationIdTo
    );
}
