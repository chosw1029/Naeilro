package nextus.naeilro.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import nextus.naeilro.model.Review;

/**
 * Created by chosw on 2016-12-21.
 */

@IgnoreExtraProperties
public class ReviewItemVM extends BaseObservable{

    private Context context;
    private Review review;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    public ReviewItemVM(Context context, Review review)
    {
        this.context = context;
        this.review = review;
    }

    public void setReview(Review review)
    {
        this.review = review;
        notifyChange();
    }

    public String getUserName() { return review.getAuthor(); }
    public String getMsg() { return review.getBody(); }
    public String getDate() { return review.getDate(); }
    public float getValue() { return review.getStarCount(); }
    public String getID() { return review.getUid(); }
    public String getObjectID() { return review.getOid(); }
    public boolean isAmI() {
        if( FirebaseAuth.getInstance().getCurrentUser() != null )
            return review.getUid().contentEquals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ? true : false;
        else
            return false;
    }

    public void onRemove(View view)
    {
        final Snackbar snackbar = Snackbar.make(view, "정말 삭제하시겠습니까?", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("reviews").child("author").equalTo(review.getUid()).getRef().removeValue();
            }
        });
        snackbar.show();
    }

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        //return location.getLocImage();

        return review.getIcon();
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

}
