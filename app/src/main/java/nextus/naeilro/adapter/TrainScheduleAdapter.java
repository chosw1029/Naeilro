package nextus.naeilro.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.databinding.TrainscheduleItemBinding;
import nextus.naeilro.model.TrainSchedule;
import nextus.naeilro.viewmodel.TrainScheduleItemVM;


/**
 * Created by chosw on 2016-08-11.
 */

public class TrainScheduleAdapter extends RecyclerView.Adapter<TrainScheduleAdapter.ScheduleViewHolder> {

    private List<TrainSchedule> trainScheduleList;
    private Context context;

    public TrainScheduleAdapter(Context context) {
        this.context = context;
        this.trainScheduleList = Collections.emptyList();
    }

    public TrainScheduleAdapter(List<TrainSchedule> trainScheduleList) {
        this.trainScheduleList = trainScheduleList;
    }

    public void setTrainScheduleList(List<TrainSchedule> trainScheduleList) {
        this.trainScheduleList = trainScheduleList;
        notifyDataSetChanged();
    }

    public List<TrainSchedule> getTrainScheduleList() { return trainScheduleList; }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TrainscheduleItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.trainschedule_item, parent, false);

        return new ScheduleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        holder.bindItem(trainScheduleList.get(position));
      //  Log.e("IMAGE_TEXT", imageList.get(position).getStationImg());
    }

    @Override
    public int getItemCount() {
        if(trainScheduleList.size() != 0)
            return trainScheduleList.size();
        else
            return 0;
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        final TrainscheduleItemBinding binding;

        public ScheduleViewHolder(TrainscheduleItemBinding binding) {
            super(binding.placeHolder);
            this.binding = binding;
        }

        void bindItem(TrainSchedule trainSchedule) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new TrainScheduleItemVM());
                binding.getViewModel().setTrainSchedule(trainSchedule);
            } else {
                binding.getViewModel().setTrainSchedule(trainSchedule);
            }
        }

    }
}
