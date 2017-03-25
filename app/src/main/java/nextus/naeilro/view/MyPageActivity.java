package nextus.naeilro.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nextus.naeilro.R;
import nextus.naeilro.databinding.ActivityMyPageBinding;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityMyPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_page);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        binding.toolbar.setTitle("마이페이지");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.signOut.setOnClickListener(this);
        binding.accountLayout.setOnClickListener(this);
        binding.nickNameLayout.setOnClickListener(this);
        binding.nickName.setText(currentUser.getDisplayName());
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
}
