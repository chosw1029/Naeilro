package nextus.naeilro.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import nextus.naeilro.R;
import nextus.naeilro.utils.OpenGraphData;
import nextus.naeilro.view.ObjectInfoActivity;
import nextus.naeilro.view.WebViewActivity;

/**
 * Created by chosw on 2016-12-20.
 */

public class BlogItemVM extends BaseObservable {

    private OpenGraphData blogData;
    private Context context;

    public BlogItemVM(Context context, OpenGraphData blogData)
    {
        this.context = context;
        this.blogData = blogData;
    }

    public void setBlogData(OpenGraphData blogData)
    {
        this.blogData = blogData;
        notifyChange();
    }

    public String getBlogTitle() { return blogData.getTitle(); }
    public String getBlogDes() { return blogData.getDescription(); }
    public String getBlogUrl() { return blogData.getUrl(); }

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        //return location.getLocImage();

        return blogData.getImage();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Log.e("LoadImage", "LoadImage");
        Glide.with(view.getContext().getApplicationContext())
                .load(imageUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public void onClick(View view)
    {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", blogData.getUrl());
        context.startActivity(intent);
        ((ObjectInfoActivity) context).overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_default);
    }

}
