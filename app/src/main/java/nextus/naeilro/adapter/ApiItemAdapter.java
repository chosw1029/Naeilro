package nextus.naeilro.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.databinding.ApiItemLayoutBinding;
import nextus.naeilro.model.Item;
import nextus.naeilro.viewmodel.APIItemVM;


/**
 * Created by chosw on 2016-08-11.
 */

public class ApiItemAdapter extends RecyclerView.Adapter<ApiItemAdapter.AddedImageViewHolder> {

    private List<Item> itemList;

    public ApiItemAdapter() {
        this.itemList = Collections.emptyList();
    }

    public void setLocationList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public AddedImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ApiItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.api_item_layout, parent, false);
        return new AddedImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AddedImageViewHolder holder, int position) {
        holder.bindItem(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        if( itemList.size() != 0 ) return itemList.size();
        else
            return 0;
    }

    public static class AddedImageViewHolder extends RecyclerView.ViewHolder {
        final ApiItemLayoutBinding binding;

        public AddedImageViewHolder(ApiItemLayoutBinding binding) {
            super(binding.placeHolder);
            this.binding = binding;
        }

        void bindItem(Item item) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new APIItemVM(itemView.getContext(), item));
            } else {
                binding.getViewModel().setItem(item);
            }
        }
    }
}
