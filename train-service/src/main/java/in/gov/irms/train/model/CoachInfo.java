package in.gov.irms.train.model;

import jakarta.persistence.*;

@Entity
@Table(name = "t_train_coach_info")
public class CoachInfo {

    @Id
    @Column(nullable = false)
    private int trainNumber;
    @Column(nullable = false)
    private int firstAC;
    @Column(nullable = false)
    private int secondAC;
    @Column(nullable = false)
    private int thirdAC;
    @Column(nullable = false)
    private int economyAC;
    @Column(nullable = false)
    private int secondSeater;
    @Column(nullable = false)
    private int sleeper;
    @Column(nullable = false)
    private int general;
    @Column(nullable = false)
    private int chairCarAC;
    @Column(nullable = false)
    private int pantryCar;
    @Column(nullable = false)
    private int parcelVan = 1;

    /*
    CoachInfo is the owner of relation
    name = CoachInfo train_number column
    referenced = TrainMaster train_number column
     */
    @OneToOne
    @JoinColumn(name = "trainNumber", referencedColumnName = "trainNumber", updatable = false)
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

    public int getPantryCar() {
        return pantryCar;
    }

    public void setPantryCar(int pantryCar) {
        this.pantryCar = pantryCar;
    }

    public int getParcelVan() {
        return parcelVan;
    }

    public void setParcelVan(int parcelVan) {
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
            int pantryCar,
            int parcelVan
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
