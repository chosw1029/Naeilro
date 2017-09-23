package nextus.naeilro.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.kakao.auth.Session;
import com.roughike.bottombar.OnTabSelectListener;

import nextus.naeilro.R;
import nextus.naeilro.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity implements BlankFragment.OnFragmentInteractionListener, OnTabSelectListener {

    FragmentTransaction transaction;
    ActivityMainBinding binding;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, BlankFragment.newInstance("fragment","main"));
        transaction.commit();
        binding.bottomBar.setOnTabSelectListener(this);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId)
        {
            case R.id.tab_01:
                break;
            case R.id.tab_02:
                startActivity(new Intent(MainActivity.this, StationListActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_default);
                break;
            case R.id.tab_03:
                startActivity(new Intent(MainActivity.this, TrainInfoActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_default);
                break;
            case R.id.tab_04:
                startActivity(new Intent(MainActivity.this, BoardActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_default);
                break;
            case R.id.tab_05: // 더보기 버튼 -> 카카오 로그
                //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                //overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_default);

                // 만약 세션이 유효하지 않은경우
                if (!Session.getCurrentSession().checkAndImplicitOpen()) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_default);
                } else {
                    Intent intent = new Intent(MainActivity.this, MyPageActivity.class); //
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_default);
                }

                break;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        binding.bottomBar.selectTabAtPosition(0);
    }


    public void alertDialog(View view)
    {
        AlertDialog.Builder buider= new AlertDialog.Builder(this); //AlertDialog.Builder 객체 생성
        buider.setView(view);
        buider.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog = buider.create();
    }

    @Override
    public void onBackPressed()
    {
        dialog.show();
    }
}
