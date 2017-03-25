package nextus.naeilro.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.adapter.StationListAdapter;
import nextus.naeilro.databinding.ActivityStationListBinding;
import nextus.naeilro.model.Station;
import nextus.naeilro.viewmodel.StationListVM;
import nextus.naeilro.viewmodel.ViewModel;

public class StationListActivity extends BaseActivity implements StationListVM.DataListener, SearchView.OnQueryTextListener, ViewModel{


    public ActivityStationListBinding binding;
    StationListVM stationListVM;
    StationListAdapter stationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_station_list);
        setUpToolBar(binding.toolbar);
        stationListVM = new StationListVM(this);
        stationListVM.setDataListener(this);
        stationListVM.loadStationData();
        binding.setViewModel(stationListVM);
        setUpRecyclerView(binding.stationListRecycler);
    }

    public void setUpToolBar(Toolbar toolbar)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setUpRecyclerView(RecyclerView recyclerView)
    {
        stationListAdapter = new StationListAdapter(this, ALPHABETICAL_COMPARATOR, new StationListAdapter.Listener() {
            @Override
            public void onExampleModelClicked(Station model) {
                final String message = getString(0, model.getS_name());
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(stationListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, 1, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void stationDataChanged(List<Station> list) {
        stationListAdapter = (StationListAdapter) binding.stationListRecycler.getAdapter();
        stationListAdapter.edit().replaceAll(list).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.station_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement the filter logic
        final List<Station> filteredModelList = filter(stationListVM.getStationList(), query);
        stationListAdapter.edit()
                .replaceAll(filteredModelList)
                .commit();
        binding.stationListRecycler.scrollToPosition(0);
        return true;
    }

    private static List<Station> filter(List<Station> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Station> filteredModelList = new ArrayList<>();
        for (Station model : models) {
            final String text = model.getS_name().toLowerCase();
            final String text2 = model.getS_do().toLowerCase();
            if (text.contains(lowerCaseQuery) | text2.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private static final Comparator<Station> ALPHABETICAL_COMPARATOR = new Comparator<Station>() {
        @Override
        public int compare(Station a, Station b) {
            return a.getS_name().compareTo(b.getS_name());
        }
    };

    @Override
    public void destroy() {
        super.onDestroy();
        stationListVM.destroy();
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.anim_default, R.anim.anim_slide_out_down);
    }
}
