package nextus.naeilro.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import nextus.naeilro.R;
import nextus.naeilro.databinding.ActivityEventPageBinding;
import nextus.naeilro.model.EventAPI;
import nextus.naeilro.viewmodel.EventPageVM;

public class EventPageActivity extends BaseActivity {

    ActivityEventPageBinding binding;
    EventPageVM eventPageVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_page);
        EventAPI eventAPI = getIntent().getParcelableExtra("eventapi");
        eventPageVM = new EventPageVM(eventAPI, this);
        binding.setViewModel(eventPageVM);
        binding.objectInfo.eventDes.setHtml(eventAPI.getOverview());

        if(eventAPI.getSite() != null)
            binding.objectInfo.eventSite.setHtml(eventAPI.getSite());
        setUpLayout(eventAPI);
    }

    public void setUpLayout(EventAPI eventAPI)
    {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }
}
