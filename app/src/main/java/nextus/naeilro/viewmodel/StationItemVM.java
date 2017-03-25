package nextus.naeilro.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import nextus.naeilro.model.Station;
import nextus.naeilro.view.StationInfoActivity;

/**
 * Created by chosw on 2016-12-14.
 */

public class StationItemVM extends BaseObservable {

    private Station station;
    private Context context;

    public StationItemVM(Context context, Station station)
    {
        this.context = context;
        this.station = station;
    }

    public void setStation(Station station)
    {
        this.station = station;
        notifyChange();
    }

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        return station.s_stationImg;
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext().getApplicationContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public void onClick(View view)
    {
        Intent intent = new Intent(context, StationInfoActivity.class);
        intent.putExtra("Station", station);
        context.startActivity(intent);
    }

    public String getStationName()
    {
        return station.getS_name();
    }
    public String getStationDo() {
        return station.getS_do();
    }
    public float getStationRating(){
        return Float.parseFloat(station.getS_rating());
    }
    public String getStationRatingCount(){
        return ""+station.getS_ratingCount()+" 명";
    }


}
