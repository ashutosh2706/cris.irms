package in.gov.irms.train.repository;

import in.gov.irms.train.model.TrainMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainMasterRepository extends JpaRepository<TrainMaster, Long> {

}
