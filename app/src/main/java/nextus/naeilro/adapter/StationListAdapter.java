package nextus.naeilro.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.Comparator;

import nextus.naeilro.R;
import nextus.naeilro.databinding.StationListLayoutBinding;
import nextus.naeilro.model.Station;
import nextus.naeilro.viewmodel.StationItemVM;

/**
 * Created by chosw on 2016-10-02.
 */

public class StationListAdapter extends SortedListAdapter<Station> {

    private Context context;

    public interface Listener {
        void onExampleModelClicked(Station model);
    }

    private final Listener mListener;

    public StationListAdapter(Context context, Comparator<Station> comparator, Listener listener) {
        super(context, Station.class, comparator);
        mListener = listener;
        this.context = context;
    }

    @Override
    protected ViewHolder<? extends Station> onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        StationListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.station_list_layout, parent, false);
        //return new StationInfoViewHolder(binding, mListener);
        return new StationInfoViewHolder(binding, mListener);
    }

    @Override
    protected boolean areItemsTheSame(Station item1, Station item2) {
        return item1.getS_name() == item2.getS_name();
    }

    @Override
    protected boolean areItemContentsTheSame(Station oldItem, Station newItem) {
        return oldItem.equals(newItem);
    }

    /**
     *  ViewHolder Class
     *
     */
    public static class StationInfoViewHolder extends SortedListAdapter.ViewHolder<Station>
    {
        final StationListLayoutBinding mBinding;

        public StationInfoViewHolder(StationListLayoutBinding binding, StationListAdapter.Listener listener) {
            super(binding.placeHolder);
            binding.setListener(listener);
            mBinding = binding;
        }

        @Override
        protected void performBind(Station station) {
            if (mBinding.getViewModel() == null) {
                mBinding.setViewModel(new StationItemVM(itemView.getContext(), station));
            } else {
                mBinding.getViewModel().setStation(station);
            }
        }
    }
}
