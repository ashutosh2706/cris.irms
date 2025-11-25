package in.gov.irms.train.model;

import jakarta.persistence.*;

@Entity
@Table(name = "t_coach_info")
public class CoachInfo {

    @Id
    @Column(nullable = false)
    private int trainNumber;
    @Column(nullable = false, name = "first_ac")
    private int firstAC = 0;
    @Column(nullable = false, name = "second_ac")
    private int secondAC = 0;
    @Column(nullable = false, name = "third_ac")
    private int thirdAC = 0;
    @Column(nullable = false, name = "economy_ac")
    private int economyAC = 0;
    @Column(nullable = false)
    private int secondSeater = 0;
    @Column(nullable = false)
    private int sleeper = 0;
    @Column(nullable = false)
    private int general = 0;
    @Column(nullable = false, name = "chair_car_ac")
    private int chairCarAC = 0;
    @Column(nullable = false)
    private boolean pantryCar=false;
    @Column(nullable = false)
    private boolean parcelVan = true;

    /*
    CoachInfo is the owner of relation
    name = CoachInfo train_number column
    referenced = TrainMaster train_number column
     */
    @OneToOne
    @JoinColumn(name = "trainNumber", referencedColumnName = "trainNumber")
    private TrainMaster trainMaster;

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public int getFirstAC() {
        return firstAC;
    }

    public void setFirstAC(int firstAC) {
        this.firstAC = firstAC;
    }

    public int getSecondAC() {
        return secondAC;
    }

    public void setSecondAC(int secondAC) {
        this.secondAC = secondAC;
    }

    public int getThirdAC() {
        return thirdAC;
    }

    public void setThirdAC(int thirdAC) {
        this.thirdAC = thirdAC;
    }

    public int getEconomyAC() {
        return economyAC;
    }

    public void setEconomyAC(int economyAC) {
        this.economyAC = economyAC;
    }

    public int getSecondSeater() {
        return secondSeater;
    }

    public void setSecondSeater(int secondSeater) {
        this.secondSeater = secondSeater;
    }

    public int getSleeper() {
        return sleeper;
    }

    public void setSleeper(int sleeper) {
        this.sleeper = sleeper;
    }

    public int getGeneral() {
        return general;
    }

    public void setGeneral(int general) {
        this.general = general;
    }

    public int getChairCarAC() {
        return chairCarAC;
    }

    public void setChairCarAC(int chairCarAC) {
        this.chairCarAC = chairCarAC;
    }

    public boolean getPantryCar() {
        return pantryCar;
    }

    public void setPantryCar(boolean pantryCar) {
        this.pantryCar = pantryCar;
    }

    public boolean getParcelVan() {
        return parcelVan;
    }

    public void setParcelVan(boolean parcelVan) {
        this.parcelVan = parcelVan;
    }

    public CoachInfo(
            int trainNumber,
            int firstAC,
            int secondAC,
            int thirdAC,
            int economyAC,
            int secondSeater,
            int sleeper,
            int general,
            int chairCarAC,
            boolean pantryCar,
            boolean parcelVan
    ) {
        this.trainNumber = trainNumber;
        this.firstAC = firstAC;
        this.secondAC = secondAC;
        this.thirdAC = thirdAC;
        this.economyAC = economyAC;
        this.secondSeater = secondSeater;
        this.sleeper = sleeper;
        this.general = general;
        this.chairCarAC = chairCarAC;
        this.pantryCar = pantryCar;
        this.parcelVan = parcelVan;
    }

    public CoachInfo() {}
}
