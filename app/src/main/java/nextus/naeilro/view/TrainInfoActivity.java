package nextus.naeilro.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.adapter.TrainScheduleAdapter;
import nextus.naeilro.databinding.ActivityTrainInfoBinding;
import nextus.naeilro.model.TrainSchedule;
import nextus.naeilro.utils.DividerItemDecoration;
import nextus.naeilro.viewmodel.TrainScheduleVM;

public class TrainInfoActivity extends BaseActivity implements TrainScheduleVM.DataListener {

    public ActivityTrainInfoBinding binding;
    TrainScheduleVM trainScheduleVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_train_info);
        trainScheduleVM = new TrainScheduleVM(this);
        trainScheduleVM.setDataListener(this);
        binding.setViewModel(trainScheduleVM);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setUpRecycler();

    }

    public void setUpRecycler()
    {
        binding.contentTrainschedule.schedulRecycler.setAdapter(new TrainScheduleAdapter(this));
        binding.contentTrainschedule.schedulRecycler.setLayoutManager(new LinearLayoutManager(this, 1, false));
        binding.contentTrainschedule.schedulRecycler.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider), false, false));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode != 0)  // 출발역
        {
            trainScheduleVM.setDepName(data.getStringExtra("name"));
            trainScheduleVM.setDepId(data.getStringExtra("id"));
        }
        else if(requestCode !=0 && resultCode != 0) // 도착역
        {
            trainScheduleVM.setArrName(data.getStringExtra("name"));
            trainScheduleVM.setArrId(data.getStringExtra("id"));
        }
    }

    @Override
    public void dataChangedListener(List<TrainSchedule> list) {
        ((TrainScheduleAdapter)binding.contentTrainschedule.schedulRecycler.getAdapter()).setTrainScheduleList(list);
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.anim_default, R.anim.anim_slide_out_right);
    }
}
