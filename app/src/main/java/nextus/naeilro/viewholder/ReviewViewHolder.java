package nextus.naeilro.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import nextus.naeilro.R;
import nextus.naeilro.model.Review;

/**
 * Created by chosw on 2016-12-25.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder  {

    public TextView bodyView;
    public TextView authorView;
    public TextView dateView;
    public RatingBar ratingBar;
    public TextView deleteView;
    public TextView oNameView;


    public ReviewViewHolder(View itemView)
    {
        super(itemView);
        dateView = (TextView) itemView.findViewById(R.id.post_date);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        ratingBar = (RatingBar) itemView.findViewById(R.id.review_rating);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
        deleteView = (TextView) itemView.findViewById(R.id.delete);
        oNameView = (TextView) itemView.findViewById(R.id.oName);
    }

    public void bindToReview(Review review)
    {
        bodyView.setText(review.getBody());
        authorView.setText(review.getAuthor());
        ratingBar.setRating(review.getStarCount());
        dateView.setText(review.getDate());
        oNameView.setText(review.getoName());
    }
}
