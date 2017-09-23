package nextus.naeilro.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.kakao.usermgmt.response.model.UserProfile;

import java.text.SimpleDateFormat;
import java.util.Date;

import nextus.naeilro.utils.ContentService;
import nextus.naeilro.view.CreateBoardActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chosw on 2016-09-23.
 */

public class CreateBoardVM extends BaseObservable{

    private UserProfile userProfile;
    private Context context;
    public ObservableField<String> comment = new ObservableField<>();

    public CreateBoardVM(Context context)
    {
        this.context = context;
        userProfile = UserProfile.loadFromCache();
    }

    public TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (comment.get()==null || !comment.get().contentEquals(editable.toString())) {
                comment.set(editable.toString());
            }
        }
    };

    public String getNickName() { return userProfile.getNickname(); }

    public void sendData(View view)
    {
        String  date = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(System.currentTimeMillis()));
        String nickname = userProfile.getNickname();
        String uid = ""+userProfile.getId();

        Log.e("UserProfile", "UID : "+uid+" nickname : "+nickname);

        // finally, execute the request
        Call<ResponseBody> call = ContentService.Factory.create().createBoard(uid, nickname, comment.get(), date);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                ((CreateBoardActivity)context).finish();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}
