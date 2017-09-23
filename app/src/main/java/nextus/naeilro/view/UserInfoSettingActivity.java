package nextus.naeilro.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import java.util.HashMap;
import java.util.Map;

import nextus.naeilro.R;


public class UserInfoSettingActivity extends AppCompatActivity {

    private UserProfile userProfile;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_setting);

        requestMe();

        editText = findViewById(R.id.userNickname);
        Button button = findViewById(R.id.okButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });
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
                UserInfoSettingActivity.this.userProfile = userProfile;
                userProfile.saveUserToCache();

                editText.setText(userProfile.getNickname());
            }

            @Override
            public void onNotSignedUp() {
                //showSignup();
                Log.e("onNotSigenUp", "onNotSignUp");
            }
        });
    }

    /**
     * 사용자의 정보를 변경 저장하는 API를 호출한다.
     */
    private void onClickUpdateProfile() {
        final Map<String, String> properties = new HashMap<>();
        properties.put("nickname", editText.getText().toString());

        UserManagement.requestUpdateProfile(new UsermgmtResponseCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                userProfile.updateUserProfile(properties);
                if (userProfile != null) {
                    userProfile.saveUserToCache();
                }
                Toast.makeText(UserInfoSettingActivity.this, "성공적으로 프로필이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                Logger.d("succeeded to update user profile" + userProfile);

                startActivity(new Intent(UserInfoSettingActivity.this, MainActivity.class));
            }

        }, properties);
    }


    private abstract class UsermgmtResponseCallback<T> extends ApiResponseCallback<T> {
        @Override
        public void onNotSignedUp() {
            //redirectSignupActivity();
        }

        @Override
        public void onFailure(ErrorResult errorResult) {
            String message = "failed to get user info. msg=" + errorResult;
            Logger.e(message);
            //KakaoToast.makeToast(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            //redirectLoginActivity();
        }
    }

}
