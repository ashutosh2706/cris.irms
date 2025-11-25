package in.gov.irms.train.repository;

import in.gov.irms.train.model.CoachInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoachInfoRepository extends JpaRepository<CoachInfo, Integer> {

    @Query("SELECT c FROM CoachInfo c where c.trainNumber = :trainNumber")
    Optional<CoachInfo> findCoachInfoByTrainNumber(@Param(value = "trainNumber") int trainNumber);
}
