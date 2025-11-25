package in.gov.irms.train.repository;

import in.gov.irms.train.model.RouteDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteDetailRepository extends JpaRepository<RouteDetail, Long> {
}
