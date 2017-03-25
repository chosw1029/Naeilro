package nextus.naeilro.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import nextus.naeilro.model.Location;
import nextus.naeilro.view.ObjectInfoActivity;

/**
 * Created by chosw on 2016-12-16.
 */

public class LocationItemVM extends BaseObservable{

    private Location location;
    private Context context;

    public LocationItemVM(Context context, Location location)
    {
        this.context = context;
        this.location = location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
        notifyChange();
    }

    public String getName() { return location.getLocName(); }
    public String getRcount() { return ""+location.getLocRcount(); }
    public float getRvalue() { return location.getLocRvalue(); }
    public String getRavlueString() { return ""+location.getLocRvalue(); }

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        //return imageUrl.get();
        return location.getLocImage();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext().getApplicationContext())
                .load(imageUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public void onClick(View view)
    {
        Intent intent = new Intent(context.getApplicationContext(), ObjectInfoActivity.class);
        intent.putExtra("Object", location);
        context.startActivity(intent);
        //Snackbar.make(view, ""+location.getLocName(), Snackbar.LENGTH_SHORT).show();
    }

}
