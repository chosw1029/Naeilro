package nextus.naeilro.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import nextus.naeilro.MyApplication;
import nextus.naeilro.R;
import nextus.naeilro.databinding.ActivityBoardItemInfoBinding;
import nextus.naeilro.model.Board;
import nextus.naeilro.model.Comment;
import nextus.naeilro.viewholder.CommentViewHolder;
import nextus.naeilro.viewmodel.BoardItemInfoVM;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardItemInfoActivity extends BaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    public ActivityBoardItemInfoBinding binding;
    BoardItemInfoVM boardItemInfoVM;
    LinearLayoutManager mManager;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseRecyclerAdapter<Comment, CommentViewHolder> mAdapter;
    private Board boardItem;
    public String key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_item_info);
        boardItem = getIntent().getParcelableExtra("boardItem");
        boardItemInfoVM = new BoardItemInfoVM(this, boardItem);
        binding.setViewModel(boardItemInfoVM);
        setSupportActionBar(binding.toolbar);
        binding.backButton.setOnClickListener(this);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null && boardItem.getUid().contentEquals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            binding.boarditemSettings.setVisibility(View.VISIBLE);
        }

        setUpRecycler();
    }

    public void setUpRecycler()
    {
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(false);
        mManager.setStackFromEnd(true);
        binding.commentRecycler.setLayoutManager(mManager);

        Query postsQuery = mDatabase.child("comments").orderByChild("bid").equalTo(""+boardItem.getId());

        mAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(Comment.class, R.layout.item_comment,
                CommentViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final CommentViewHolder viewHolder, final Comment comment, final int position) {
                if(getItemCount() > 0)
                    binding.notComment.setVisibility(View.INVISIBLE);
                else
                    binding.notComment.setVisibility(View.VISIBLE);

                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();

                if(!getUid().contentEquals("") && getUid().contentEquals(comment.getUid()))
                    viewHolder.deleteLayout.setVisibility(View.VISIBLE);
                else
                    viewHolder.deleteLayout.setVisibility(View.GONE);

                viewHolder.deleteLayout.setOnClickListener(v -> {
                    //Snackbar.make(v, "준비중입니다..", Snackbar.LENGTH_SHORT).show();
                    showMenu(v, postKey);
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
                viewHolder.bindToReview(comment);
            }
        };

        binding.commentRecycler.setAdapter(mAdapter);
    }

    public void showMenu(View view, String postKey)
    {
        key = postKey;
        PopupMenu popup= new PopupMenu(this, view);
        getMenuInflater().inflate(R.menu.board_item_setting, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    public void removeComment()
    {
        Snackbar snackbar = Snackbar.make(binding.placeHolder, "정말 삭제하시겠습니까?", Snackbar.LENGTH_LONG).setAction("OK", v -> {
            mDatabase.child("comments").child(key).removeValue();
            updateServer();
        });
        snackbar.show();
    }

    public void updateServer()
    {
        Call<ResponseBody> call = MyApplication.getMyapplicationContext().getContentService().removeComment(""+boardItem.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Snackbar.make(binding.placeHolder, "삭제되었습니다.", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:
                removeComment();
                break;
            case R.id.edit:
                Snackbar.make(binding.placeHolder, "준비중입니다.", Snackbar.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    public String getUid() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        else
            return "";
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backButton)
        {
            super.onBackPressed();
        }
    }
}
