package nextus.naeilro.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;

import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.adapter.ApiItemAdapter;
import nextus.naeilro.adapter.LocationItemAdapter;
import nextus.naeilro.databinding.ActivityStationInfoBinding;
import nextus.naeilro.model.Item;
import nextus.naeilro.model.Location;
import nextus.naeilro.model.Station;
import nextus.naeilro.viewmodel.StationInfoVM;
import nextus.naeilro.viewmodel.StationInfoVMTemp;

public class StationInfoActivity extends BaseActivity implements StationInfoVMTemp.DataListener {

    private static final String TAG = "StationInfoActivity";
    public ActivityStationInfoBinding binding;
    StationInfoVMTemp stationInfoVM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_station_info);
        stationInfoVM = new StationInfoVMTemp(this, getIntent().getParcelableExtra("Station"));
        stationInfoVM.setDataListener(this);
        binding.setViewModel(stationInfoVM);
        binding.stationInfo.stationDes.setHtml(stationInfoVM.getStationDes(), new HtmlHttpImageGetter(binding.stationInfo.stationDes));
        setUpLayout();
        setUpRecycler();
    }

    public void setUpLayout()
    {
        Intent i = getIntent();
        Station station = i.getParcelableExtra("Station");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.collapsingToolbar.setTitle(station.getS_name());
        binding.collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
        Glide.with(this).load(station.getS_stationImg()).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.stationImg);

    }

    public void setUpRecycler()
    {
        binding.stationInfo.stayRecycler.setNestedScrollingEnabled(false);
        binding.stationInfo.sightRecycler.setNestedScrollingEnabled(false);
        binding.stationInfo.foodRecycler.setNestedScrollingEnabled(false);

        binding.stationInfo.stayRecycler.setAdapter(new ApiItemAdapter());
        binding.stationInfo.sightRecycler.setAdapter(new ApiItemAdapter());
        binding.stationInfo.foodRecycler.setAdapter(new ApiItemAdapter());

        binding.stationInfo.stayRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.stationInfo.sightRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.stationInfo.foodRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        binding.stationInfo.sightRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 20;
            }
        });

        binding.stationInfo.foodRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 20;
            }
        });

        binding.stationInfo.stayRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 20;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_station_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    public void sightDataChanged(List<Location> sightList) {
        LocationItemAdapter adapter = (LocationItemAdapter) binding.stationInfo.sightRecycler.getAdapter();
        adapter.setLocationList(sightList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void foodDataChanged(List<Location> foodList) {
        LocationItemAdapter adapter = (LocationItemAdapter) binding.stationInfo.foodRecycler.getAdapter();
        adapter.setLocationList(foodList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void stayDataChanged(List<Location> stayList) {
        LocationItemAdapter adapter = (LocationItemAdapter) binding.stationInfo.stayRecycler.getAdapter();
        adapter.setLocationList(stayList);
        adapter.notifyDataSetChanged();
    }
*/
    @Override
    public void onResume()
    {
        super.onResume();
        //stationInfoVM.loadLocationData();
    }

    @Override
    public void sightDataChanged(List<Item> sightList) {
        ApiItemAdapter adapter = (ApiItemAdapter) binding.stationInfo.sightRecycler.getAdapter();
        adapter.setLocationList(sightList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void foodDataChanged(List<Item> foodList) {
        ApiItemAdapter adapter = (ApiItemAdapter) binding.stationInfo.stayRecycler.getAdapter();
        adapter.setLocationList(foodList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void stayDataChanged(List<Item> apiDataList) {
        ApiItemAdapter adapter = (ApiItemAdapter) binding.stationInfo.foodRecycler.getAdapter();
        adapter.setLocationList(apiDataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void apitDataChanged(List<Item> apiDataList) {
    }
}
