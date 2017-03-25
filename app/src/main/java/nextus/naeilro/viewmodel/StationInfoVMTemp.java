package nextus.naeilro.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import nextus.naeilro.MyApplication;
import nextus.naeilro.model.Item;
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

public class StationInfoVMTemp extends BaseObservable implements ViewModel {

    private Subscription subscription;
    private DataListener dataListener;
    private Context context;
    private Station station;

    private ObservableField<List<Location>> locationList = new ObservableField<>();
    private List<Location> sightList = new ArrayList<>();
    private List<Location> foodList = new ArrayList<>();
    private List<Location> stayList = new ArrayList<>();
    private ObservableField<List<Item>> apiItems = new ObservableField<>();
    private ObservableField<List<Item>> foodItems = new ObservableField<>();
    private ObservableField<List<Item>> sightItems = new ObservableField<>();
    private String url;

    public StationInfoVMTemp(Context context, Parcelable station)
    {
        this.context = context;
        this.station = (Station) station;
        //loadLocationData();
        loadTest();

        //Log.e("Station Map", "x = "+this.station.getS_mapx()+" | y = "+this.station.getS_mapy());
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
                          //   dataListener.sightDataChanged(sightList);
                           //  dataListener.foodDataChanged(foodList);
                          //   dataListener.stayDataChanged(stayList);
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

    public void loadSight()
    {
        url = "locationBasedList?ServiceKey=AYfA0yQD4QNdFo%2FGK9SBlBh%2Fvb7pAk3hWEWZOYspVpY0mfbJbsGjqpZ1wjalXRHbhLqyBNxHVybiDJ%2FrdC5lHw%3D%3D&contentTypeId=12&mapX="+station.getS_mapx()+"&mapY="+station.getS_mapy()
                +"&radius=2000&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=Q&numOfRows=12&pageNo=1&_type=json";

        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

        ContentService contentService = MyApplication.getMyapplicationContext().getVisitKoreaAPI();

        subscription = contentService.getTestData(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(MyApplication.getMyapplicationContext().defaultSubscribeScheduler())
                .subscribe(new Subscriber<JsonObject>(){
                    @Override
                    public void onCompleted(){
                        if (dataListener != null) {
                            dataListener.sightDataChanged(sightItems.get());
                            ((StationInfoActivity) context).binding.stationInfo.sightProgress.setVisibility(View.GONE);
                            ((StationInfoActivity) context).binding.stationInfo.sightRecycler.setVisibility(View.VISIBLE);
                        }
                        loadFood();
                    }
                    @Override
                    public void onError(Throwable error)
                    {
                        Log.e(TAG, "Error loading Item ", error);
                    }
                    @Override
                    public void onNext(JsonObject apiTempList)
                    {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonElement rootObejct = parser.parse(apiTempList.toString())
                                .getAsJsonObject().get("response")
                                .getAsJsonObject().get("body")
                                .getAsJsonObject().get("items")
                                .getAsJsonObject().get("item");
                        Type listType = new TypeToken<List<Item>>() {}.getType();
                        sightItems.set(gson.fromJson(rootObejct, listType));

                    }
                });


    }

    public void loadFood()
    {
        url = "locationBasedList?ServiceKey=AYfA0yQD4QNdFo%2FGK9SBlBh%2Fvb7pAk3hWEWZOYspVpY0mfbJbsGjqpZ1wjalXRHbhLqyBNxHVybiDJ%2FrdC5lHw%3D%3D&contentTypeId=39&mapX="+station.getS_mapx()+"&mapY="+station.getS_mapy()
                +"&radius=2000&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=Q&numOfRows=12&pageNo=1&_type=json";

        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

        ContentService contentService = MyApplication.getMyapplicationContext().getVisitKoreaAPI();


        subscription = contentService.getTestData(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(MyApplication.getMyapplicationContext().defaultSubscribeScheduler())
                .subscribe(new Subscriber<JsonObject>(){
                    @Override
                    public void onCompleted(){
                        if (dataListener != null) {
                            dataListener.foodDataChanged(foodItems.get());
                            //((StationInfoActivity) context).binding.stationInfo.stayProgress.setVisibility(View.GONE);
                            ((StationInfoActivity) context).binding.stationInfo.foodRecycler.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onError(Throwable error)
                    {
                        Log.e(TAG, "Error loading Item ", error);
                    }
                    @Override
                    public void onNext(JsonObject apiTempList)
                    {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonElement rootObejct = parser.parse(apiTempList.toString())
                                .getAsJsonObject().get("response")
                                .getAsJsonObject().get("body")
                                .getAsJsonObject().get("items")
                                .getAsJsonObject().get("item");
                        Type listType = new TypeToken<List<Item>>() {}.getType();
                        foodItems.set(gson.fromJson(rootObejct, listType));
                    }
                });


    }

    public void loadTest()
    {
        url = "locationBasedList?ServiceKey=AYfA0yQD4QNdFo%2FGK9SBlBh%2Fvb7pAk3hWEWZOYspVpY0mfbJbsGjqpZ1wjalXRHbhLqyBNxHVybiDJ%2FrdC5lHw%3D%3D&contentTypeId=32&mapX="+station.getS_mapx()+"&mapY="+station.getS_mapy()
        +"&radius=2000&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=Q&numOfRows=12&pageNo=1&_type=json";

        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

        ContentService contentService = MyApplication.getMyapplicationContext().getVisitKoreaAPI();

        Log.e("TEST_-------------", url);

        subscription = contentService.getTestData(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(MyApplication.getMyapplicationContext().defaultSubscribeScheduler())
                .subscribe(new Subscriber<JsonObject>(){
                    @Override
                    public void onCompleted(){
                        if (dataListener != null) {
                            dataListener.stayDataChanged(apiItems.get());
                            ((StationInfoActivity) context).binding.stationInfo.stayProgress.setVisibility(View.GONE);
                            ((StationInfoActivity) context).binding.stationInfo.stayRecycler.setVisibility(View.VISIBLE);
                        }
                        loadSight();
                    }
                    @Override
                    public void onError(Throwable error)
                    {
                        Log.e(TAG, "Error loading Item ", error);
                    }
                    @Override
                    public void onNext(JsonObject apiTempList)
                    {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonElement rootObejct = parser.parse(apiTempList.toString())
                                .getAsJsonObject().get("response")
                                .getAsJsonObject().get("body")
                                .getAsJsonObject().get("items")
                                .getAsJsonObject().get("item");
                        Type listType = new TypeToken<List<Item>>() {}.getType();
                        apiItems.set(gson.fromJson(rootObejct, listType));

                    }
                });


    }

    public interface DataListener
    {
        void sightDataChanged(List<Item> sightList);
        void foodDataChanged(List<Item> foodList);
        void stayDataChanged(List<Item> apiDataList);
        void apitDataChanged(List<Item> apiDataList);
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
