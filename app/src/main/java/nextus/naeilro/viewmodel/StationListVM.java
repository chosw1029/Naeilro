package nextus.naeilro.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import java.util.List;

import nextus.naeilro.MyApplication;
import nextus.naeilro.model.Location;
import nextus.naeilro.model.Station;
import nextus.naeilro.utils.ContentService;
import nextus.naeilro.view.StationListActivity;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by chosw on 2016-09-14.
 */

public class StationListVM extends BaseObservable implements ViewModel {

    private Subscription subscription;
    private DataListener dataListener;
    private List<Station> stationList;
    private final ObservableField<List<Location>> locationList = new ObservableField<>();
    private Station station;
    private Context context;

    public StationListVM(Context context){
        this.context = context;
        loadStationData();
    }

    public void loadStationData()
    {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

        ContentService contentService = MyApplication.getMyapplicationContext().getContentService();

        subscription = contentService.getStationData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(MyApplication.getMyapplicationContext().defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Station>>(){
                    @Override
                    public void onCompleted(){
                        if (dataListener != null) dataListener.stationDataChanged(stationList);
                        ((StationListActivity) context).binding.stationListProgress.setVisibility(View.GONE);
                        ((StationListActivity) context).binding.stationListRecycler.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onError(Throwable error)
                    {
                        Log.e(TAG, "Error loading Item ", error);
                    }
                    @Override
                    public void onNext(List<Station> stationList)
                    {
                        StationListVM.this.stationList = stationList;
                    }
                });
    }
/*
    public void loadLocationData()
    {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

        ContentService contentService = MyApplication.getMyapplicationContext().getContentService();

        subscription = contentService.getLocationData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(MyApplication.getMyapplicationContext().defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Location>>(){
                    @Override
                    public void onCompleted(){
                       // if (dataListener != null) dataListener.boardDataChanged(boardList.get());
                        Log.e(TAG, "Complete");
                    }
                    @Override
                    public void onError(Throwable error)
                    {
                        Log.e(TAG, "Error loading Item ", error);
                    }
                    @Override
                    public void onNext(List<Location> boardList)
                    {
                        //setBoardList(boardList);
                        locationList.set(boardList);
                        Log.e("TEST", ""+boardList.get(0).getLocName());
                    }
                });
    }*/

    public void setDataListener(DataListener dataListener)
    {
        this.dataListener = dataListener;
    }

    public interface DataListener
    {
        void stationDataChanged(List<Station> list);
    }

    public List<Station> getStationList() { return stationList; }

    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
        context = null;
        dataListener = null;
    }
}
