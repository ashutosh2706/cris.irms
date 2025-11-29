package in.gov.irms.station.model;

import in.gov.irms.station.enums.StationType;
import jakarta.persistence.*;

@Entity
@Table(name = "t_station_model")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "station_seq")
    @SequenceGenerator(name = "station_seq", sequenceName = "t_station_model_seq", allocationSize = 1)
    private long id;

    @Column(nullable = false, unique = true)
    private String stationCode;

    @Column(nullable = false)
    private String stationName;

    private String stateName;

    private String division;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StationType stationType;

    private Integer elevationFromSea;

    @Column(nullable = false)
    private int platformCount;

    private Boolean boardingDisabled = false;

    public Station(
            long id,
            String stationCode,
            String stationName,
            String stateName,
            String division,
            StationType stationType,
            Integer elevationFromSea,
            int platformCount,
            Boolean boardingDisabled
    ) {
        this.id = id;
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.stateName = stateName;
        this.division = division;
        this.stationType = stationType;
        this.elevationFromSea = elevationFromSea;
        this.platformCount = platformCount;
        this.boardingDisabled = boardingDisabled;
    }

    public Station() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public StationType getStationType() {
        return stationType;
    }

    public void setStationType(StationType stationType) {
        this.stationType = stationType;
    }

    public Integer getElevationFromSea() {
        return elevationFromSea;
    }

    public void setElevationFromSea(Integer elevationFromSea) {
        this.elevationFromSea = elevationFromSea;
    }

    public int getPlatformCount() {
        return platformCount;
    }

    public void setPlatformCount(int platformCount) {
        this.platformCount = platformCount;
    }

    public Boolean isBoardingDisabled() {
        return boardingDisabled;
    }

    public void setBoardingDisabled(Boolean boardingDisabled) {
        this.boardingDisabled = boardingDisabled;
    }
}
