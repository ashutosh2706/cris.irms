package in.gov.irms.train.model;

import in.gov.irms.train.enums.JourneyDirection;
import in.gov.irms.train.enums.TrainType;
import jakarta.persistence.*;

@Entity
@Table(name = "t_train_master")
public class TrainMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "train_master_seq")
    @SequenceGenerator(name = "train_master_seq", sequenceName = "t_train_master_seq", allocationSize = 5)
    private long id;
    @Column(nullable = false, unique = true, updatable = false)
    private int trainNumber;
    @Column(nullable = false)
    private String trainName;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainType trainType;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JourneyDirection journeyDirection;
    private Integer journeyTotalDuration;   // minutes
    private Long journeyTotalDistance;     // meters
    @OneToOne(mappedBy = "trainMaster")
    private CoachInfo coachInfo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public TrainType getTrainType() {
        return trainType;
    }

    public void setTrainType(TrainType trainType) {
        this.trainType = trainType;
    }

    public JourneyDirection getDirection() {
        return journeyDirection;
    }

    public void setDirection(JourneyDirection direction) {
        this.journeyDirection = direction;
    }

    public int getDuration() {
        return journeyTotalDuration;
    }

    public void setDuration(Integer duration) {
        this.journeyTotalDuration = duration;
    }

    public Long getDistance() {
        return journeyTotalDistance;
    }

    public void setDistance(Long distance) {
        this.journeyTotalDistance = distance;
    }

    public TrainMaster(
            long id,
            int trainNumber,
            String trainName,
            TrainType trainType,
            JourneyDirection direction,
            Integer duration,
            Long distance
    ) {
        this.id = id;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.trainType = trainType;
        this.journeyDirection = direction;
        this.journeyTotalDuration = duration;
        this.journeyTotalDistance = distance;
    }

    public TrainMaster() {}
}
