package nextus.naeilro.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import nextus.naeilro.model.EventAPI;

/**
 * Created by chosw on 2017-01-03.
 */

public class EventPageVM extends BaseObservable {

    private Context context;
    private EventAPI event;

    public EventPageVM(EventAPI event, Context context)
    {
        this.context = context;
        this.event = event;
    }

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        return event.getImgUrl();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext().getApplicationContext())
                .load(imageUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public String getTitle() { return event.getTitle(); }
    public String getAddr() { return event.getAddr(); }
    public String getTel() { return event.getTel(); }
}
