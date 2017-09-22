package nextus.naeilro.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import nextus.naeilro.R;
import nextus.naeilro.databinding.ActivityLoginBinding;
import nextus.naeilro.model.User;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chosw on 2016-12-10.
 */

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private SessionCallback callback;

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MyPageActivity.class);
            startActivity(intent);
            finish();
        }

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = (firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
           if(user !=null)
           {
               Intent intent = new Intent(LoginActivity.this, MyPageActivity.class);
               startActivity(intent);
               finish();
           }
        });

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(binding.rootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void find()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(User.class) == null)
                {
                    startActivity(new Intent(LoginActivity.this, UserInfoSettingActivity.class));
                    finish();
                }
                else
                   finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                startActivity(new Intent(LoginActivity.this, UserInfoSettingActivity.class));
                finish();
            }
        });
    }

    protected void redirectSignupActivity() {
        SharedPreferences prefs = getSharedPreferences("Access", MODE_PRIVATE);
        //SharedPreferences.Editor editor = prefs.edit();

        if(!prefs.getBoolean("Access",false)) // Access 성공한 적이 없는 경우
        {
            final Intent intent = new Intent(this, UserInfoSettingActivity.class);
            startActivity(intent);
            finish();
        }
        else // 성공한 경우
        {
            final Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }
}
