package in.gov.irms.train.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "t_route_detail")
public class RouteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "train_route_seq")
    @SequenceGenerator(name = "train_route_seq", sequenceName = "t_train_route_seq", allocationSize = 5)
    private long id;
    @Column(nullable = false, updatable = false)
    private int trainNumber;
    @Column(nullable = false)
    private long stationId;
    @Column(nullable = false)
    private int arrivalSequence = 0;
    @Column(nullable = false)
    private LocalTime arrivalTime = LocalTime.MIDNIGHT;
    @Column(nullable = false)
    private LocalTime departureTime = LocalTime.MIDNIGHT;
    @Column(nullable = false, name = "departs_on_week_days")
    private int departsOnWeekDays = 127;
    @Column(nullable = false)
    private int dayOffset = 0;
    private Long distanceCovered = 0L;

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

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public int getArrivalSequence() {
        return arrivalSequence;
    }

    public void setArrivalSequence(int arrivalSequence) {
        this.arrivalSequence = arrivalSequence;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getDepartsOnWeekDays() {
        return departsOnWeekDays;
    }

    public void setDepartsOnWeekDays(int departsOnWeekDays) {
        this.departsOnWeekDays = departsOnWeekDays;
    }

    public int getDayOffset() {
        return dayOffset;
    }

    public void setDayOffset(int dayOffset) {
        this.dayOffset = dayOffset;
    }

    public Long getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(Long distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public RouteDetail(
            long id,
            int trainNumber,
            long stationId,
            int arrivalSequence,
            LocalTime arrivalTime,
            LocalTime departureTime,
            int departsOnWeekDays,
            int dayOffset,
            Long distanceCovered
    ) {
        this.id = id;
        this.trainNumber = trainNumber;
        this.stationId = stationId;
        this.arrivalSequence = arrivalSequence;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.departsOnWeekDays = departsOnWeekDays;
        this.dayOffset = dayOffset;
        this.distanceCovered = distanceCovered;
    }

    public RouteDetail() {}
}
