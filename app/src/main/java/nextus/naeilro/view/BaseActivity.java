package nextus.naeilro.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import nextus.naeilro.MyApplication;

/**
 * @author leoshin, created at 15. 7. 20..
 */
public class BaseActivity extends AppCompatActivity {

    protected static Activity self;

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.setCurrentActivity(this);
        self = BaseActivity.this;
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences() {
        Activity currActivity = MyApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            MyApplication.setCurrentActivity(null);
        }
    }

}
