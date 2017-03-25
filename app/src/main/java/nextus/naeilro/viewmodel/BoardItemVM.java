package nextus.naeilro.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nextus.naeilro.R;
import nextus.naeilro.model.Board;
import nextus.naeilro.view.BoardActivity;
import nextus.naeilro.view.BoardItemInfoActivity;
import nextus.naeilro.view.LoginActivity;

/**
 * Created by chosw on 2017-01-10.
 */

public class BoardItemVM extends BaseObservable implements PopupMenu.OnMenuItemClickListener {

    private Board board;
    private Context context;

    public BoardItemVM(Context context)
    {
        this.context = context;
    }

    public void setBoard(Board board) { this.board = board; notifyChange();}

    public boolean getVisible()
    {
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            return FirebaseAuth.getInstance().getCurrentUser().getUid().contentEquals(board.getUid());
        }
        else
            return false;
    }
    public String getCommentCount() { return ""+board.getCommentCount(); }
    public String getNickName() { return board.getNickname(); }
    public String getUid()  {return board.getUid(); }
    public String getText() { return board.getText(); }
    public String getDate()
    {
        String date_string = board.getDate();
        String date = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.KOREA);
        Date current = new Date(System.currentTimeMillis());

        try{
            Date d = format.parse(date_string) ;
            long time_long = current.getTime() - d.getTime();
            int time = (int)time_long/1000;

            if( time < 60 )
            {
                date = "방금";
            }
            else if( time >=60 && time < 3600 )
            {
                time = time/60;
                date = ""+time+"분 전";
            }
            else if( time >=3600 && time < 86400 )
            {
                time = time/3600;
                date = ""+time+"시간 전";
            }
            else if( time >= 86400 && time < 604800 )
            {
                time = time/86400;
                date = ""+time+"일 전";
            }
            else
            {
                date = date_string;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return date;
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.placeHolder:
                if(FirebaseAuth.getInstance().getCurrentUser() == null)
                {
                    Snackbar snackbar = Snackbar.make(view, "로그인이 필요한 기능입니다. 로그인하시겠습니까?", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    snackbar.show();
                }
                else
                {
                    Intent intent = new Intent(context, BoardItemInfoActivity.class);
                    intent.putExtra("boardItem", board);
                    context.startActivity(intent);
                }
                break;

            case R.id.boarditem_settings:
                //Snackbar.make(view, "Settings", Snackbar.LENGTH_SHORT).show();
                PopupMenu popup= new PopupMenu(context, view);
                ((BoardActivity)context).getMenuInflater().inflate(R.menu.board_item_setting, popup.getMenu());
                popup.setOnMenuItemClickListener(this);
                popup.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:
                removeItem();
                break;
            case R.id.edit:
                Snackbar.make(((BoardActivity) context).binding.placeHolder, "준비중입니다.", Snackbar.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    public void removeItem()
    {
        // 삭제 기능
        Snackbar.make(((BoardActivity) context).binding.placeHolder, "준비중입니다.", Snackbar.LENGTH_SHORT).show();
    }
}
