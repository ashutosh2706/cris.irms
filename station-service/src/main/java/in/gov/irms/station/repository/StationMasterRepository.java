package in.gov.irms.station.repository;

import in.gov.irms.station.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationMasterRepository extends JpaRepository<Station, Long> {
    @Query("SELECT s FROM Station s where s.stationCode = :stationCode")
    Optional<Station> findByStationCode(@Param(value = "stationCode") String stationCode);

    @Query("SELECT s FROM Station s where s.id = :id")
    Optional<Station> findByStationId(@Param(value = "id") long id);

    @Query("SELECT s FROM Station s where s.stateName = :stateName")
    List<Station> findAllStationByStateName(@Param(value = "stateName") String stateName);

}
