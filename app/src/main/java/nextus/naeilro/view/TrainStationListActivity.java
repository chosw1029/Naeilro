package nextus.naeilro.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.util.Comparator;
import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.adapter.TrainStationListAdapter;
import nextus.naeilro.databinding.ActivityTrainStationListBinding;
import nextus.naeilro.model.TrainStation;
import nextus.naeilro.utils.DividerItemDecoration;
import nextus.naeilro.viewmodel.TrainStationListVM;
import nextus.naeilro.viewmodel.ViewModel;

public class TrainStationListActivity extends BaseActivity implements ViewModel, TrainStationListVM.DataListener {

    ActivityTrainStationListBinding binding;
    TrainStationListVM trainStationListVM;
    TrainStationListAdapter trainStationListAdapter;
    public int flag_number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_train_station_list);
        setUpDisplay();
        trainStationListVM = new TrainStationListVM(this);
        trainStationListVM.setDataListener(this);
        binding.setViewModel(trainStationListVM);
        setUpRecyclerView(binding.trainStationRecycler);
        trainStationListVM.loadStationList();
    }

    public void setUpDisplay()
    {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width =  (int)(display.getWidth() * 0.95); //Display 사이즈의 70%
        int height = (int)(display.getHeight() * 0.95);  //Display 사이즈의 90%

        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
    }

    public void setUpRecyclerView(RecyclerView recyclerView)
    {
        trainStationListAdapter = new TrainStationListAdapter(this, ALPHABETICAL_COMPARATOR);
        recyclerView.setAdapter(trainStationListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, 1, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider), false, false));
    }

    private static final Comparator<TrainStation> ALPHABETICAL_COMPARATOR = new Comparator<TrainStation>() {
        @Override
        public int compare(TrainStation a, TrainStation b) {
            return a.getNodeName().compareTo(b.getNodeName());
        }
    };

    @Override
    public void destroy() {
        super.onDestroy();
    }

    @Override
    public void dataChangedListener(List<TrainStation> list) {
        trainStationListAdapter = (TrainStationListAdapter) binding.trainStationRecycler.getAdapter();
        trainStationListAdapter.edit().replaceAll(list).commit();
    }

    @Override
    public void finish()
    {
        super.finish();
        if(flag_number == 0)
            setResult(0);
    }

}
