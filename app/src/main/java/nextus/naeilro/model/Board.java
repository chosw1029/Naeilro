package nextus.naeilro.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chosw on 2016-09-19.
 */

public class Board implements Parcelable {

    private int id;
    private String uid;
    private String nickname;
    private String text;
    private String date;
    private int commentCount;

    public Board(Parcel in)
    {
        this.id = in.readInt();
        this.uid = in.readString();
        this.nickname = in.readString();
        this.text = in.readString();
        this.date = in.readString();
        this.commentCount = in.readInt();
    }

    public int getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public int getCommentCount() { return commentCount; }

    public static final Creator<Board> CREATOR = new Creator<Board>() {
        public Board createFromParcel(Parcel source) {
            return new Board(source);
        }

        public Board[] newArray(int size) {
            return new Board[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.uid);
        parcel.writeString(this.nickname);
        parcel.writeString(this.text);
        parcel.writeString(this.date);
        parcel.writeInt(this.commentCount);
    }
}
