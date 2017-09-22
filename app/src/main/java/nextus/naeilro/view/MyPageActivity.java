package nextus.naeilro.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import nextus.naeilro.R;
import nextus.naeilro.databinding.ActivityMyPageBinding;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener{

    private UserProfile userProfile;
    ActivityMyPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_page);


        binding.toolbar.setTitle("마이페이지");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.signOut.setOnClickListener(this);
        binding.accountLayout.setOnClickListener(this);
        binding.nickNameLayout.setOnClickListener(this);
        //binding.nickName.setText(currentUser.getDisplayName());
        requestMe();
    }

    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
                Log.e("onFailure", "failed to get user info : "+errorResult);

                //redirectLoginActivity();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                //redirectLoginActivity();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                MyPageActivity.this.userProfile = userProfile;
                userProfile.saveUserToCache();

                binding.nickName.setText(userProfile.getNickname());
                Glide.with(MyPageActivity.this)
                        .load(userProfile.getProfileImagePath())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.userPhoto);
                //redirectMainActivity();
            }

            @Override
            public void onNotSignedUp() {
                //showSignup();
                Log.e("onNotSigenUp", "onNotSignUp");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.sign_out:
                signOutNotification(v);
                break;
            case R.id.nickNameLayout:
                showNotification(v);
                break;

            case R.id.accountLayout:
                showNotification(v);
                break;
        }
    }

    public void signOutNotification(View view)
    {
        Snackbar snackbar = Snackbar.make(view, "로그아웃 하시겠습니까?", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        snackbar.show();
    }

    public void showNotification(View view)
    {
        Snackbar.make(view, "해당 기능은 준비중입니다.", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        userProfile = UserProfile.loadFromCache();
        //if(userProfile != null)
            //showProfile();
    }
}
