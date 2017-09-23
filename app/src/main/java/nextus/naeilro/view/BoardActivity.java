package nextus.naeilro.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import java.util.List;

import nextus.naeilro.MyApplication;
import nextus.naeilro.R;
import nextus.naeilro.adapter.BoardListAdapter;
import nextus.naeilro.databinding.ActivityFindFriendBinding;
import nextus.naeilro.model.Board;
import nextus.naeilro.viewmodel.FindFriendVM;

public class BoardActivity extends BaseActivity implements FindFriendVM.DataListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    public ActivityFindFriendBinding binding;
    BoardListAdapter adapter;
    FindFriendVM findFriendVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_friend);
        findFriendVM = new FindFriendVM(this);
        findFriendVM.setDataListener(this);
        findFriendVM.loadBoardData();

        MyApplication.getMyapplicationContext().requestAccessTokenInfo();

        setSupportActionBar(binding.toolbar);
        setUpRecyclerView(binding.findfriendRecycler);
        binding.sendButton.setOnClickListener(this);
        binding.backButton.setOnClickListener(this);
        binding.swipeRefresh.setOnRefreshListener(this);
    }

    public void setUpRecyclerView(RecyclerView recyclerView)
    {
        adapter = new BoardListAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, 1, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void boardDataChanged(List<Board> list) {
        adapter = (BoardListAdapter) binding.findfriendRecycler.getAdapter();
        adapter.setBoardList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.sendButton:
                if(MyApplication.getMyapplicationContext().isLogin())
                {
                    startActivity(new Intent(BoardActivity.this, CreateBoardActivity.class));
                }
                else
                {
                    Log.e("onFailure", "failed to get user info : not login");

                    Snackbar snackbar = Snackbar.make(view, "로그인이 필요한 기능입니다. 로그인하시겠습니까?", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BoardActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_default);
                        }
                    });
                    snackbar.show();

                }

                break;
            case R.id.backButton:
                super.onBackPressed();
                break;
        }
    }


    @Override
    public void onResume()
    {
        super.onResume();
        findFriendVM.loadBoardData();
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.anim_default, R.anim.anim_slide_out_right);
    }

    @Override
    public void onRefresh() {
        findFriendVM.loadBoardData();
    }

    public void dataLoadFinish()
    {
        if( binding.swipeRefresh.isRefreshing() )
            binding.swipeRefresh.setRefreshing(false);
    }
}
