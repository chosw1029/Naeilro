package nextus.naeilro.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.util.Log;

import java.util.List;

import nextus.naeilro.MyApplication;
import nextus.naeilro.model.Board;
import nextus.naeilro.utils.ContentService;
import nextus.naeilro.view.BoardActivity;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by chosw on 2016-09-20.
 */

public class FindFriendVM extends BaseObservable {

    private Subscription subscription;
    private DataListener dataListener;
    private final ObservableField<List<Board>> boardList = new ObservableField<>();
    private Board board;
    private Context context;

    public FindFriendVM()
    {
        loadBoardData();
    }

    public FindFriendVM(Context context)
    {
        this.context = context;
        loadBoardData();
    }

    public void loadBoardData()
    {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

        ContentService contentService = MyApplication.getMyapplicationContext().getContentService();

        subscription = contentService.getBoardData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(MyApplication.getMyapplicationContext().defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Board>>(){
                    @Override
                    public void onCompleted(){
                        if (dataListener != null) dataListener.boardDataChanged(boardList.get());
                        ((BoardActivity) context).dataLoadFinish();
                    }
                    @Override
                    public void onError(Throwable error)
                    {
                        Log.e(TAG, "Error loading Item ", error);
                    }
                    @Override
                    public void onNext(List<Board> boardList)
                    {
                        setBoardList(boardList);
                    }
                });
    }

    public void setDataListener(DataListener dataListener)
    {
        this.dataListener = dataListener;
    }

    public interface DataListener
    {
        void boardDataChanged(List<Board> list);
    }

    public void setBoardList(List<Board> list)
    {
        this.boardList.set(list);
        notifyChange();
    }

    public void setBoard(Board board)
    {
        this.board = board;
        notifyChange();
    }


    public String getDate() { return board.getDate(); }
}
