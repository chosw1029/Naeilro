package nextus.naeilro.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.databinding.BoardItemLayoutBinding;
import nextus.naeilro.model.Board;
import nextus.naeilro.viewmodel.BoardItemVM;

/**
 * Created by chosw on 2016-08-11.
 */

public class BoardListAdapter extends RecyclerView.Adapter<BoardListAdapter.BoardItemViewHolder> {

    private List<Board> boardList;
    private Context context;

    public BoardListAdapter(Context context) {
        this.context = context;
        this.boardList = Collections.emptyList();
    }

    public BoardListAdapter(List<Board> boardList) {
        this.boardList = boardList;
    }

    public void setBoardList(List<Board> boardList) {
        this.boardList = boardList;
    }

    public List<Board> getBoardList() { return boardList; }

    @Override
    public BoardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BoardItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.board_item_layout, parent, false);

        return new BoardItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BoardItemViewHolder holder, int position) {
        holder.bindItem(boardList.get(position));


    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public static class BoardItemViewHolder extends RecyclerView.ViewHolder {
        final BoardItemLayoutBinding binding;

        public BoardItemViewHolder(BoardItemLayoutBinding binding) {
            super(binding.placeHolder);
            this.binding = binding;
        }

        void bindItem(Board board) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new BoardItemVM(itemView.getContext()));
                binding.getViewModel().setBoard(board);
            } else {
                binding.getViewModel().setBoard(board);
            }
        }

    }
}
