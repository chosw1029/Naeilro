package nextus.naeilro.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import nextus.naeilro.R;
import nextus.naeilro.databinding.ActivityReviewBinding;
import nextus.naeilro.model.Location;
import nextus.naeilro.viewmodel.ReviewVM;

public class ReviewActivity extends AppCompatActivity {

    public ActivityReviewBinding binding;
    ReviewVM reviewVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review);
        reviewVM = new ReviewVM(this, (Location)getIntent().getParcelableExtra("location"));
        binding.setViewModel(reviewVM);
        setUpToolBar(binding.toolbar);

        if(getIntent().getBooleanExtra("isEditing", false)) // 수정이라면
        {
            binding.ratingValue.setRating(getIntent().getFloatExtra("rating", 0f));
            binding.reviewBody.setText(getIntent().getStringExtra("body"));
        }
    }

    public void setUpToolBar(Toolbar toolbar)
    {
        //toolbar.setTitle("TEST");
        setSupportActionBar(toolbar);
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.anim_default, R.anim.anim_slide_out_down);
    }
}
