package nextus.naeilro.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import nextus.naeilro.model.Item;
import nextus.naeilro.view.ObjectInfoActivity;
import nextus.naeilro.view.ObjectInfoActivityTemp;

/**
 * Created by chosw on 2017-01-17.
 */

public class APIItemVM extends BaseObservable {

    private Context context;
    private Item item;

    public APIItemVM(Context context, Item item)
    {
        this.context = context;
        this.item = item;
    }

    public void setItem(Item item)
    {
        this.item = item;
        notifyChange();
    }

    public String getName() { return item.getTitle(); }
    public String getRvalueStrig() { return "";}
    public float getRvalue() { return 0f;}
    public String getRcount() { return "0"; }

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        return item.getFirstimage();
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
        Intent intent = new Intent(context.getApplicationContext(), ObjectInfoActivityTemp.class);
        intent.putExtra("Item", item);
        context.startActivity(intent);
        //Snackbar.make(view, ""+location.getLocName(), Snackbar.LENGTH_SHORT).show();
    }
}
