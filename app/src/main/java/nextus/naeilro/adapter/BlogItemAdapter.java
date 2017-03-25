package nextus.naeilro.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.databinding.BlogItemLayoutBinding;
import nextus.naeilro.utils.OpenGraphData;
import nextus.naeilro.viewmodel.BlogItemVM;


/**
 * Created by chosw on 2016-08-11.
 */

public class BlogItemAdapter extends RecyclerView.Adapter<BlogItemAdapter.AddedImageViewHolder> {

    private List<OpenGraphData> blogList;

    public BlogItemAdapter() {
        this.blogList = Collections.emptyList();
    }

    public void setBlogList(List<OpenGraphData> blogList) {
        this.blogList = blogList;
    }

    @Override
    public AddedImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BlogItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.blog_item_layout, parent, false);

        return new AddedImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AddedImageViewHolder holder, int position) {
        holder.bindItem(blogList.get(position));
    }

    @Override
    public int getItemCount() {
        if( blogList.size() != 0 ) return blogList.size();
        else
            return 0;
    }

    public static class AddedImageViewHolder extends RecyclerView.ViewHolder {
        final BlogItemLayoutBinding binding;

        public AddedImageViewHolder(BlogItemLayoutBinding binding) {
            super(binding.placeHolder);
            this.binding = binding;
        }

        void bindItem(OpenGraphData blog) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new BlogItemVM(itemView.getContext(), blog));
            } else {
                binding.getViewModel().setBlogData(blog);
            }
        }
    }
}
