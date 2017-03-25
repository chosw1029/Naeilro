package nextus.naeilro.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import nextus.naeilro.MyApplication;
import nextus.naeilro.model.Location;
import nextus.naeilro.model.Station;
import nextus.naeilro.utils.ContentService;
import nextus.naeilro.view.StationInfoActivity;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by chosw on 2016-09-30.
 */

public class StationInfoVM extends BaseObservable implements ViewModel{

    private Subscription subscription;
    private DataListener dataListener;
    private Context context;
    private Station station;

    private ObservableField<List<Location>> locationList = new ObservableField<>();
    private List<Location> sightList = new ArrayList<>();
    private List<Location> foodList = new ArrayList<>();
    private List<Location> stayList = new ArrayList<>();

    public StationInfoVM(Context context, Parcelable station)
    {
        this.context = context;
        this.station = (Station) station;
        loadLocationData();
    }

    public void loadLocationData()
    {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

        ContentService contentService = MyApplication.getMyapplicationContext().getContentService();

        subscription = contentService.getLocationData(station.getS_id())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(MyApplication.getMyapplicationContext().defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Location>>(){
                    @Override
                    public void onCompleted(){
                         if (dataListener != null)
                         {
                             sightList.clear();
                             stayList.clear();
                             foodList.clear();

                             for(int i=0; i<locationList.get().size(); i++)
                             {
                                 switch(locationList.get().get(i).getLocType())
                                 {
                                     case 10:
                                         sightList.add(locationList.get().get(i));
                                         break;
                                     case 20:
                                         stayList.add(locationList.get().get(i));
                                         break;
                                     case 30:
                                         foodList.add(locationList.get().get(i));
                                         break;
                                 }
                             }
                             dataListener.sightDataChanged(sightList);
                             dataListener.foodDataChanged(foodList);
                             dataListener.stayDataChanged(stayList);
                         }

                        ((StationInfoActivity) context).binding.stationInfo.stayProgress.setVisibility(View.GONE);
                        ((StationInfoActivity) context).binding.stationInfo.stayRecycler.setVisibility(View.VISIBLE);
                        ((StationInfoActivity) context).binding.stationInfo.sightProgress.setVisibility(View.GONE);
                        ((StationInfoActivity) context).binding.stationInfo.sightRecycler.setVisibility(View.VISIBLE);

                        notifyChange();
                    }
                    @Override
                    public void onError(Throwable error)
                    {
                        Log.e(TAG, "Error loading Item ", error);
                    }
                    @Override
                    public void onNext(List<Location> list)
                    {
                        locationList.set(list);
                    }
                });
    }

    public interface DataListener
    {
        void sightDataChanged(List<Location> sightList);
        void foodDataChanged(List<Location> foodList);
        void stayDataChanged(List<Location> stayList);
    }

    public void setDataListener(DataListener dataListener)
    {
        this.dataListener = dataListener;
        notifyChange();
    }

    public String getStationDes() { return station.getS_des(); }
    public String getStationName() { return station.getS_name(); }
    public String getStationDo() { return station.getS_do(); }
    public String getStationRvalueString() { return station.getS_rating(); }
    public float getStationRvalue() { return Float.parseFloat(station.getS_rating()); }
    public String getStationRcount() { return ""+station.getS_ratingCount()+" reviews"; }

    @Override
    public void destroy() {
        this.context = null;
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }
}
