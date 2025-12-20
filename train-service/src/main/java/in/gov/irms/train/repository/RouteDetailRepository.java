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
    List<Object[]> findDirectTrainsBetweenTwoStn(
            @Param(value = "stationIdFrom") long stationIdFrom,
            @Param(value = "stationIdTo") long stationIdTo
    );
}
