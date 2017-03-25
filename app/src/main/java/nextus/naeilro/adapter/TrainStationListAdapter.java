package nextus.naeilro.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.Comparator;

import nextus.naeilro.R;
import nextus.naeilro.databinding.TrainstationItemBinding;
import nextus.naeilro.model.TrainStation;
import nextus.naeilro.viewmodel.TrainStationListVM;

/**
 * Created by chosw on 2016-10-02.
 */

public class TrainStationListAdapter extends SortedListAdapter<TrainStation> {

    private Context context;

    public TrainStationListAdapter(Context context, Comparator<TrainStation> comparator ) {
        super(context, TrainStation.class, comparator);
        this.context = context;
    }

    @Override
    protected ViewHolder<? extends TrainStation> onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        TrainstationItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.trainstation_item, parent, false);

        return new TrainStationInfoViewHolder(binding);
    }

    @Override
    protected boolean areItemsTheSame(TrainStation item1, TrainStation item2) {
        return item1.getNodeName() == item2.getNodeName();
    }

    @Override
    protected boolean areItemContentsTheSame(TrainStation oldItem, TrainStation newItem) {
        return oldItem.equals(newItem);
    }

    /**
     *  ViewHolder Class
     *
     */
    public static class TrainStationInfoViewHolder extends SortedListAdapter.ViewHolder<TrainStation>
    {
        final TrainstationItemBinding mBinding;

        public TrainStationInfoViewHolder(TrainstationItemBinding binding) {
            super(binding.placeHolder);
            mBinding = binding;
        }

        @Override
        protected void performBind(TrainStation trainStation) {
            if (mBinding.getViewModel() == null) {
                mBinding.setViewModel(new TrainStationListVM(itemView.getContext()));
                mBinding.getViewModel().setTrainStation(trainStation);
            } else {
                mBinding.getViewModel().setTrainStation(trainStation);
            }
        }
    }
}
