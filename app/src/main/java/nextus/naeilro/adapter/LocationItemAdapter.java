package nextus.naeilro.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.databinding.LocationItemLayoutBinding;
import nextus.naeilro.model.Location;
import nextus.naeilro.viewmodel.LocationItemVM;


/**
 * Created by chosw on 2016-08-11.
 */

public class LocationItemAdapter extends RecyclerView.Adapter<LocationItemAdapter.AddedImageViewHolder> {

    private List<Location> locationList;

    public LocationItemAdapter() {
        this.locationList = Collections.emptyList();
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    @Override
    public AddedImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LocationItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.location_item_layout, parent, false);

        return new AddedImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AddedImageViewHolder holder, int position) {
        holder.bindItem(locationList.get(position));
    }

    @Override
    public int getItemCount() {
        if( locationList.size() != 0 ) return locationList.size();
        else
            return 0;
    }

    public static class AddedImageViewHolder extends RecyclerView.ViewHolder {
        final LocationItemLayoutBinding binding;

        public AddedImageViewHolder(LocationItemLayoutBinding binding) {
            super(binding.placeHolder);
            this.binding = binding;
        }

        void bindItem(Location location) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new LocationItemVM(itemView.getContext(), location));
            } else {
                binding.getViewModel().setLocation(location);
            }
        }
    }
}
