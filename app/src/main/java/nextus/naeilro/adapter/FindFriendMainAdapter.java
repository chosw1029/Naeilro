package nextus.naeilro.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.databinding.FindfriendListSimpleLayoutBinding;
import nextus.naeilro.model.Board;
import nextus.naeilro.viewmodel.MainVM;


/**
 * Created by chosw on 2016-08-11.
 */

public class FindFriendMainAdapter extends RecyclerView.Adapter<FindFriendMainAdapter.BoardItemViewHolder> {

    private List<Board> boardList;
    public static Context context;

    public FindFriendMainAdapter(Context context) {
        this.context = context;
        this.boardList = Collections.emptyList();
    }

    public FindFriendMainAdapter(List<Board> boardList) {
        this.boardList = boardList;
    }

    public void setBoardList(List<Board> boardList) {
        this.boardList = boardList;
        notifyDataSetChanged();
    }

    public List<Board> getBoardList() { return boardList; }

    @Override
    public BoardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FindfriendListSimpleLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.findfriend_list_simple_layout, parent, false);

        return new BoardItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BoardItemViewHolder holder, int position) {
       holder.bindItem(boardList.get(position));
    }

    @Override
    public int getItemCount() {
        if(boardList.size() > 5)
            return 5;
        else
            return boardList.size();
    }

    public static class BoardItemViewHolder extends RecyclerView.ViewHolder {
        final FindfriendListSimpleLayoutBinding binding;

        public BoardItemViewHolder(FindfriendListSimpleLayoutBinding binding) {
            super(binding.placeHolder);
            this.binding = binding;
        }

        void bindItem(Board board) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new MainVM(context));
                binding.getViewModel().setBoard(board);
            } else {
                binding.getViewModel().setBoard(board);
            }
        }

    }
}
