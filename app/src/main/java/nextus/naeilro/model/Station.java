package nextus.naeilro.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

/**
 * Created by chosw on 2016-09-14.
 */

// Parcelable : Activity 간 intent로 데이터를 전달해야 하는 경우가 있는데 Class 객체는 intent로 전달할 수 없다. 이를 해결하기 위해 Parcelable 클래스를 이용해야 한다.
public class Station implements Parcelable, SortedListAdapter.ViewModel{

    public int s_id;
    public String s_name;
    public String s_rating;
    public int s_ratingCount;
    public String s_stationImg;
    public String s_do;
    public String s_mapx;
    public String s_mapy;
    public String s_line;
    public String s_des;
    public int s_show;

    public Station(Parcel in)
    {
        this.s_id = in.readInt();
        this.s_name = in.readString();
        this.s_rating = in.readString();
        this.s_ratingCount = in.readInt();
        this.s_stationImg = in.readString();
        this.s_do = in.readString();
        this.s_mapx = in.readString();
        this.s_mapy = in.readString();
        this.s_line = in.readString();
        this.s_des = in.readString();
        this.s_show = in.readInt();
    }

    public String getS_mapx() {
        return s_mapx;
    }

    public String getS_mapy() {
        return s_mapy;
    }

    public int getS_id() {
        return s_id;
    }

    public String getS_name() {
        return s_name;
    }

    public String getS_rating() {
        return s_rating;
    }

    public int getS_ratingCount() {
        return s_ratingCount;
    }

    public String getS_stationImg() {
        return s_stationImg;
    }

    public String getS_do() {
        return s_do;
    }

    public String getS_line() {
        return s_line;
    }

    public String getS_des() {
        return s_des;
    }

    public int getS_show() {
        return s_show;
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        public Station createFromParcel(Parcel source) {
            return new Station(source);
        }

        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.s_id);
        parcel.writeString(this.s_name);
        parcel.writeString(this.s_rating);
        parcel.writeInt(this.s_ratingCount);
        parcel.writeString(this.s_stationImg);
        parcel.writeString(this.s_do);
        parcel.writeString(this.s_mapx);
        parcel.writeString(this.s_mapy);
        parcel.writeString(this.s_line);
        parcel.writeString(this.s_des);
        parcel.writeInt(this.s_show);
    }
}
