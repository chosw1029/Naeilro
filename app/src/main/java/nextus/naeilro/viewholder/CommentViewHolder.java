package nextus.naeilro.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import nextus.naeilro.R;
import nextus.naeilro.model.Comment;

/**
 * Created by chosw on 2016-12-25.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder  {

    public TextView commentView;
    public TextView nickNameView;
    public TextView dateView;
    public FrameLayout deleteLayout;


    public CommentViewHolder(View itemView)
    {
        super(itemView);
        dateView = (TextView) itemView.findViewById(R.id.comment_date);
        commentView = (TextView) itemView.findViewById(R.id.comment);
        nickNameView = (TextView) itemView.findViewById(R.id.comment_nickname);
        deleteLayout = (FrameLayout) itemView.findViewById(R.id.comment_delete);
    }

    public void bindToReview(Comment comment)
    {
        commentView.setText(comment.getComment());
        dateView.setText(comment.getDate());
        nickNameView.setText(comment.getNickname());
    }
}
