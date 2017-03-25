package nextus.naeilro.model;

/**
 * Created by chosw on 2017-01-05.
 */

public class TrainSchedule {

    private String adultcharge;
    private String arrPlaceName;
    private String depPlaceName;
    private String arrTime;
    private String depTime;
    private String trainType;

    public TrainSchedule()
    {

    }

    public String getAdultcharge() {
        return adultcharge;
    }

    public void setAdultcharge(String adultcharge) {
        this.adultcharge = adultcharge;
    }

    public String getArrPlaceName() {
        return arrPlaceName;
    }

    public void setArrPlaceName(String arrPlaceName) {
        this.arrPlaceName = arrPlaceName;
    }

    public String getDepPlaceName() {
        return depPlaceName;
    }

    public void setDepPlaceName(String depPlaceName) {
        this.depPlaceName = depPlaceName;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }
}
