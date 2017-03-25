package nextus.naeilro.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.List;

import nextus.naeilro.MyApplication;
import nextus.naeilro.R;
import nextus.naeilro.model.Board;
import nextus.naeilro.model.Station;
import nextus.naeilro.utils.ContentService;
import nextus.naeilro.view.MainActivity;
import nextus.naeilro.view.StationInfoActivity;
import nextus.naeilro.view.StationListActivity;
import nextus.naeilro.view.WebViewActivity;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by chosw on 2016-09-15.
 */

public class MainVM extends BaseObservable implements ViewModel {

    private Context context;
    private Subscription subscription;
    private Board board;
    private DataListener dataListener;
    private FirebaseUser currentUser;
    private ArrayList<Station> stationList = new ArrayList<>();

    public MainVM(Context context)
    {
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
                    }
                    @Override
                    public void onError(Throwable error)
                    {
                        Log.e(TAG, "Error loading Item ", error);
                    }
                    @Override
                    public void onNext(List<Station> stationList)
                    {
                        setStationList(stationList);
                    }
                });
    }

    public ArrayList<Station> getStationList() { return stationList; }

    public void setStationList(List<Station> list)
    {
        this.stationList.addAll(list);
        notifyChange();
    }
    public void setBoard(Board board)
    {
        this.board = board;
    }

    public String getDate()
    {
        return board.getDate();
    }

    public void setDataListener(DataListener dataListener)
    {
        this.dataListener = dataListener;
    }

    @Override
    public void destroy() {
        this.context = null;
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }

    public interface DataListener
    {
        void stationDataChanged(List<Station> list);
    }

    public void onClick(View view)
    {
        Intent intent;
        switch(view.getId())
        {
            case R.id.best01:
                intent = new Intent(context, StationInfoActivity.class);
                intent.putExtra("Station", stationList.get(0));
                context.startActivity(intent);
                break;
            case R.id.best02:
                intent = new Intent(context, StationInfoActivity.class);
                intent.putExtra("Station", stationList.get(1));
                context.startActivity(intent);
                break;
            case R.id.best03:
                intent = new Intent(context, StationInfoActivity.class);
                intent.putExtra("Station", stationList.get(2));
                context.startActivity(intent);
                break;
            case R.id.best04:
                intent = new Intent(context, StationInfoActivity.class);
                intent.putExtra("Station", stationList.get(3));
                context.startActivity(intent);
                break;
            case R.id.infoNaeilro:
                intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", "http://52.78.152.162:8080/naeilro/info.html");
                context.startActivity(intent);
                ((MainActivity) context).overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_default);
                break;
            case R.id.stationInfo:
                intent = new Intent(context, StationListActivity.class);
                context.startActivity(intent);
                ((MainActivity) context).overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_default);
                break;
            case R.id.test:
                Snackbar.make(view, "준비중입니다.", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        if(stationList.size() == 0)
            return "";
        else
            return stationList.get(1).getS_stationImg();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext().getApplicationContext())
                .load(imageUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }


    public void setCurrentUser(FirebaseUser user)
    {
        currentUser = user;
    }

}
