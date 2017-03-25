package nextus.naeilro.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import nextus.naeilro.MyApplication;
import nextus.naeilro.R;
import nextus.naeilro.model.Board;
import nextus.naeilro.model.Comment;
import nextus.naeilro.model.User;
import nextus.naeilro.view.BoardItemInfoActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chosw on 2017-01-10.
 */

public class BoardItemInfoVM {

    private Context context;
    private Board boardItem;
    private ObservableField<String> comment = new ObservableField<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private ProgressDialog progressDialog;

    public BoardItemInfoVM(Context context, Board boardItem)
    {
        this.context = context;
        this.boardItem = boardItem;
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

    public String getCommentCount() { return ""+boardItem.getCommentCount(); }
    public String getUid()  {return boardItem.getUid(); }
    public String getNickName() { return boardItem.getNickname(); }
    public String getText() { return boardItem.getText(); }
    public String getDate()
    {
        String date_string = boardItem.getDate();
        String date = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.KOREA);
        Date current = new Date(System.currentTimeMillis());

        try{
            Date d = format.parse(date_string) ;
            long time_long = current.getTime() - d.getTime();
            int time = (int)time_long/1000;

            if( time < 60 )
            {
                date = "방금";
            }
            else if( time >=60 && time < 3600 )
            {
                time = time/60;
                date = ""+time+"분 전";
            }
            else if( time >=3600 && time < 86400 )
            {
                time = time/3600;
                date = ""+time+"시간 전";
            }
            else if( time >= 86400 && time < 604800 )
            {
                time = time/86400;
                date = ""+time+"일 전";
            }
            else
            {
                date = date_string;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return date;
    }


    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.create_comment:
                if(comment.get() == null)
                {
                    Snackbar.make(view, "댓글을 입력하세요.", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog = ProgressDialog.show(context, "", "작성중입니다.", true);
                    insertData();
                }
                break;

            case R.id.boarditem_settings:
                Snackbar.make(view, "준비중입니다.", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    public void insertData()
    {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                User user = dataSnapshot.getValue(User.class);

                // [START_EXCLUDE]
                if (user == null) {
                    // User is null, error out
                    Log.e("BoardItemInfoVM", "User " + userId + " is unexpectedly null");
                    Toast.makeText(context,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Write new post
                    writeReview(userId, user.username);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //sendServer();
    }

    private void writeReview(String userId, String username)
    {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(System.currentTimeMillis()));

        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = databaseReference.child("reviews").push().getKey();
        Comment commentItem = new Comment(""+boardItem.getId(), userId, username, comment.get(), date);
        Map<String, Object> postValues = commentItem.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/comments/" + key, postValues);
        childUpdates.put("/user-comments/" + userId + "/" + key, postValues);
        //childUpdates.put("/ratings/"+key, )

        databaseReference.updateChildren(childUpdates);

        ((BoardItemInfoActivity) context).binding.commentInput.setText(null);
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(((BoardItemInfoActivity) context).binding.commentInput.getWindowToken(), 0);

        Call<ResponseBody> call = MyApplication.getMyapplicationContext().getContentService().setCommentCount(""+boardItem.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Toast.makeText(context, "작성 완료!!", Toast.LENGTH_SHORT).show();
                /*
                Intent intent = new Intent();
                MyApplication.getMyapplicationContext().getLocation().setLocRcount(location.getLocRcount()+1);
                MyApplication.getMyapplicationContext().getLocation().setLocRvalue(newValue);
                intent.putExtra("location", location);
                ((ReviewActivity) context).setResult(1, intent);
                ((ReviewActivity) context).finish();
                */
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

    }
}
