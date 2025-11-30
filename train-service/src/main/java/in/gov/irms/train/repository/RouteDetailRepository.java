package in.gov.irms.train.repository;

import in.gov.irms.train.model.RouteDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteDetailRepository extends JpaRepository<RouteDetail, Long> {

    @Query("SELECT rd FROM " +
            "RouteDetail rd " +
            "WHERE rd.trainNumber = :trainNumber " +
            "ORDER BY rd.arrivalSequence ASC")
    Page<RouteDetail> findRouteListByTrainNumber(@Param(value = "trainNumber") int trainNumber, Pageable pageable);
}
