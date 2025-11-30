package in.gov.irms.train.repository;

import in.gov.irms.train.model.TrainMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainMasterRepository extends JpaRepository<TrainMaster, Long> {

    @Query("SELECT tm FROM TrainMaster tm JOIN FETCH tm.coachInfo WHERE tm.trainNumber = :trainNumber")
    Optional<TrainMaster> findByTrainNumber(@Param(value = "trainNumber") int trainNumber);
}
