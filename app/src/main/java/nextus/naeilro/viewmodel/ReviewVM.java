package nextus.naeilro.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import nextus.naeilro.BR;
import nextus.naeilro.MyApplication;
import nextus.naeilro.model.Location;
import nextus.naeilro.model.Review;
import nextus.naeilro.model.User;
import nextus.naeilro.view.ReviewActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chosw on 2016-12-21.
 */

public class ReviewVM extends BaseObservable {

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";

    private Context context;
    private Location location;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    public final ObservableField<String> text_lengh = new ObservableField<>();
    private ObservableField<String> text = new ObservableField<>();
    private float rating;
    private int result=0;

    public ReviewVM(Context context, Location location)
    {
        this.context = context;
        this.location = location;
    }

    public TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!Objects.equals(text.get(), editable.toString())) {
                if(getTextLength().length() <= 150)
                {
                    text.set(editable.toString());
                    text_lengh.set("("+text.get().length()+"/150)");
                    notifyPropertyChanged(BR.textLength);
                }

            }
        }
    };

    @Bindable
    public String getTextLength()
    {
        if( text_lengh.get() == null )
            return "(0/150)";
        else
            return text_lengh.get();
    }

    public void onClick(View view)
    {
        insertData();
    }

    public int getResult() { return result; }

    public void insertData()
    {/*
        Review review = new Review(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString(),
                text.get(),
                DateFormat.getDateTimeInstance().format(new Date()),
                ((ReviewActivity) context).binding.ratingValue.getRating());*/



        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                User user = dataSnapshot.getValue(User.class);

                // [START_EXCLUDE]
                if (user == null) {
                    // User is null, error out
                    Log.e(TAG, "User " + userId + " is unexpectedly null");
                    Toast.makeText(context,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Write new post
                    writeReview(userId, user.username);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //sendServer();
    }

    public void writeReview(String userId, String username)
    {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(System.currentTimeMillis()));

        rating = ((ReviewActivity) context).binding.ratingValue.getRating();
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = databaseReference.child("reviews").push().getKey();
        Review review = new Review(userId, username, "", text.get(), date, rating, ""+location.getLocID(), location.getLocName());
        Map<String, Object> postValues = review.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/reviews/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
        //childUpdates.put("/ratings/"+key, )

        databaseReference.updateChildren(childUpdates);

        int count = location.getLocRcount();
        float ratingValue = location.getLocRvalue();
        final float newValue = (ratingValue*count+rating)/(count+1);
        // 평가 카운트 1 증가 및 rating 값 증가
        Call<ResponseBody> call = MyApplication.getMyapplicationContext().getContentService().setLocationRating(""+location.getLocID(), newValue);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Intent intent = new Intent();
                MyApplication.getMyapplicationContext().getLocation().setLocRcount(location.getLocRcount()+1);
                MyApplication.getMyapplicationContext().getLocation().setLocRvalue(newValue);
                intent.putExtra("location", location);
                ((ReviewActivity) context).setResult(1, intent);
                ((ReviewActivity) context).finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}
