package nextus.naeilro.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.kakao.usermgmt.response.model.UserProfile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nextus.naeilro.MyApplication;
import nextus.naeilro.R;
import nextus.naeilro.model.Board;
import nextus.naeilro.view.BoardActivity;
import nextus.naeilro.view.BoardItemInfoActivity;
import nextus.naeilro.view.LoginActivity;

/**
 * Created by chosw on 2017-01-10.
 */

public class BoardItemVM extends BaseObservable implements PopupMenu.OnMenuItemClickListener {

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    private UserProfile userProfile;
    private Board board;
    private Context context;

    public BoardItemVM(Context context)
    {
        this.context = context;
        userProfile = UserProfile.loadFromCache();
        MyApplication.getMyapplicationContext().requestAccessTokenInfo();
    }

    public void setBoard(Board board) { this.board = board; notifyChange();}

    public boolean getVisible()
    {
        if(userProfile != null)
        {
            String uid = ""+userProfile.getId();
            return uid.contentEquals(board.getUid());
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

        long curTime = System.currentTimeMillis();
        long regTime = 0;

        try {
            regTime = format.parse(date_string).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diffTime = (curTime - regTime) / 1000;

        String msg = "";

        if(diffTime < SEC) {
            // sec
            msg = diffTime + "초전";
        } else if ((diffTime /= SEC) < MIN) {
            // min
            System.out.println(diffTime);
            msg = diffTime + "분전";
        } else if ((diffTime /= MIN) < HOUR) {
            // hour
            msg = (diffTime ) + "시간전";
        } else if ((diffTime /= HOUR) < DAY) {
            // day
            if(diffTime < 8 )
                msg = (diffTime ) + "일전";
            else
                msg = date_string;
        } else {
            msg = date_string;
        }

        return msg;

    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.placeHolder:
                if(!MyApplication.getMyapplicationContext().isLogin())
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
