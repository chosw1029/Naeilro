package nextus.naeilro.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import nextus.naeilro.MyApplication;
import nextus.naeilro.R;
import nextus.naeilro.adapter.BlogItemAdapter;
import nextus.naeilro.adapter.ReviewItemAdapter;
import nextus.naeilro.databinding.ActivityObjectInfoBinding;
import nextus.naeilro.model.Location;
import nextus.naeilro.model.Review;
import nextus.naeilro.utils.OpenGraphData;
import nextus.naeilro.viewholder.ReviewViewHolder;
import nextus.naeilro.viewmodel.ObjectInfoVM;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chosw on 2016-11-30.
 */

public class ObjectInfoActivity extends BaseActivity implements ObjectInfoVM.DataListener {

    ObjectInfoVM objectInfoVM;
    public ActivityObjectInfoBinding binding;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Review, ReviewViewHolder> mAdapter;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    Location location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = getIntent().getParcelableExtra("Object");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_object_info);
        objectInfoVM = new ObjectInfoVM(this);
        objectInfoVM.settingLocation(location);
        MyApplication.getMyapplicationContext().setLocation(location);
        objectInfoVM.getBlogData();
        objectInfoVM.settingMap();
        objectInfoVM.setDataListener(this);
        binding.setViewModel(objectInfoVM);
        binding.objectInfo.objectDes.setHtml(location.getLocDes());
        setUpLayout();
        setUpRecycler();
    }

    public void setUpLayout()
    {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    public void setUpRecycler()
    {
        binding.objectInfo.blogRecycler.setAdapter(new BlogItemAdapter());
        binding.objectInfo.blogRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        //binding.objectInfo.blogRecycler.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider), false, false));

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        binding.objectInfo.reviewRecycler.setLayoutManager(mManager);


        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = mDatabase.child("reviews").orderByChild("oid").equalTo(""+location.getLocID()).limitToFirst(3);

        mAdapter = new FirebaseRecyclerAdapter<Review, ReviewViewHolder>(Review.class, R.layout.item_review,
                ReviewViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final ReviewViewHolder viewHolder, final Review review, final int position) {
                if(getItemCount() > 0)
                    binding.objectInfo.notReview.setVisibility(View.INVISIBLE);
                else
                    binding.objectInfo.notReview.setVisibility(View.VISIBLE);

                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();

                if( getUid() != null && review.getUid().contentEquals(getUid()) )
                {
                    viewHolder.deleteView.setVisibility(View.VISIBLE);
                    binding.objectInfo.reviewButton.setText("리뷰 수정");
                    binding.objectInfo.reviewButton.setTag(100);
                    objectInfoVM.setReview(review);
                }
                else
                {
                    viewHolder.deleteView.setVisibility(View.INVISIBLE);
                    binding.objectInfo.reviewButton.setTag(0);
                }

                viewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getUid().equals(review.getUid()))
                            onRemove(v, postKey, review.getStarCount());
                        else
                            Snackbar.make(v, "권한이 없습니다.", Snackbar.LENGTH_SHORT).show();
                    }
                });
                /*
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }*/


                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToReview(review);
            }
        };

        binding.objectInfo.reviewRecycler.setAdapter(mAdapter);

    }

    public void onRemove(View view, final String key, final float rating)
    {
        Snackbar snackbar = Snackbar.make(view, "정말 삭제하시겠습니까?", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("reviews").child(key).removeValue();
                removeData(rating);
            }
        });
        snackbar.show();
    }

    public void removeData(final float rating)
    {
        int count = location.getLocRcount();
        float newValue;
        if( count == 1 ) newValue = 0f;
        else
            newValue = (location.getLocRvalue()*count - rating)/(count - 1);

        location.setLocRcount(count-1);
        location.setLocRvalue(newValue);
        Call<ResponseBody> call = MyApplication.getMyapplicationContext().getContentService().removeLocationRating(""+location.getLocID(), newValue);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.objectInfo.reviewButton.setText("리뷰 쓰기");
                objectInfoVM.notifyChange();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    @Override
    public void dataChanged(List<OpenGraphData> blog) {
        BlogItemAdapter adapter = (BlogItemAdapter) binding.objectInfo.blogRecycler.getAdapter();
        adapter.setBlogList(blog);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void reviewDataChanged(List<Review> reviews) {
        ReviewItemAdapter adapter = (ReviewItemAdapter) binding.objectInfo.reviewRecycler.getAdapter();
        adapter.setReviews(reviews);
        adapter.notifyDataSetChanged();
    }

    public String getUid() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        else
            return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                objectInfoVM.settingLocation((Location) data.getParcelableExtra("location"));
                break;

            default:
                break;
        }
    }

}
