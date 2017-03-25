package nextus.naeilro.viewmodel;

import android.databinding.BaseObservable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nextus.naeilro.model.TrainSchedule;

/**
 * Created by chosw on 2017-01-05.
 */

public class TrainScheduleItemVM extends BaseObservable{

    private TrainSchedule trainSchedule;

    public TrainScheduleItemVM(){}

    public void setTrainSchedule(TrainSchedule trainSchedule)
    {
        this.trainSchedule = trainSchedule;
        notifyChange();
    }

    public String getTrainType() { return trainSchedule.getTrainType(); }
    public String getDepTime() { return getDate(trainSchedule.getDepTime()); }
    public String getArrTime() { return getDate(trainSchedule.getArrTime()); }
    public String getDepName() { return trainSchedule.getDepPlaceName(); }
    public String getArrName() { return trainSchedule.getArrPlaceName(); }
    public String getPay() { return trainSchedule.getAdultcharge(); }

    public String getDate(String dateString)
    {
        String date="";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);

        try{
            Date d = format.parse(dateString);
            date = ""+d.getHours()+"시 "+d.getMinutes()+"분";
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return date;
    }
}
