package nextus.naeilro.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.databinding.ReviewItemLayoutBinding;
import nextus.naeilro.model.Review;
import nextus.naeilro.viewmodel.ReviewItemVM;


/**
 * Created by chosw on 2016-08-11.
 */

public class ReviewItemAdapter extends RecyclerView.Adapter<ReviewItemAdapter.AddedImageViewHolder> {

    private List<Review> reviews;

    public ReviewItemAdapter() {
        this.reviews = Collections.emptyList();
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public AddedImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReviewItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.review_item_layout, parent, false);

        return new AddedImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AddedImageViewHolder holder, int position) {
        holder.bindItem(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        if( reviews.size() != 0 ) return reviews.size();
        else
            return 0;
    }

    public static class AddedImageViewHolder extends RecyclerView.ViewHolder {
        final ReviewItemLayoutBinding binding;

        public AddedImageViewHolder(ReviewItemLayoutBinding binding) {
            super(binding.placeHolder);
            this.binding = binding;
        }

        void bindItem(Review review) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new ReviewItemVM(itemView.getContext(), review));
            } else {
                binding.getViewModel().setReview(review);
            }
        }
    }
}
