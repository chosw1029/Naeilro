package nextus.naeilro.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by chosw on 2017-01-10.
 */

@IgnoreExtraProperties
public class Comment{

    private String bid;
    private String uid;
    private String nickname;
    private String comment;
    private String date;

    public Comment()
    {

    }

    public Comment(String bid, String uid, String nickname, String comment, String date) {
        this.bid = bid;
        this.uid = uid;
        this.nickname = nickname;
        this.comment = comment;
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("bid", bid);
        result.put("uid", uid);
        result.put("nickname", nickname);
        result.put("comment", comment);
        result.put("date", date);
        return result;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate()
    {
        String date_string = date;
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
}
