package nextus.naeilro.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nextus.naeilro.R;
import nextus.naeilro.model.User;

public class UserInfoSettingActivity extends AppCompatActivity {


    EditText editText;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_setting);
        editText = (EditText)findViewById(R.id.userNickname);

        Button button = (Button) findViewById(R.id.okButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeUser(editText.getText().toString());
            }
        });
    }

    public void writeUser(String nickname)
    {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        User users = new User(nickname, FirebaseAuth.getInstance().getCurrentUser().getEmail(), "");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nickname)
                .setPhotoUri(Uri.parse(""))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("UserInfoActivity", "User profile updated.");
                        }
                    }
                });

        mDatabase.child("users").child(userId).setValue(users);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
