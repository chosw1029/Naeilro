package nextus.naeilro.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chosw on 2016-12-20.
 */

public class Blog implements Parcelable{

    private int id;
    private int objectID;
    private String site;
    private String date;

    public Blog(Parcel in)
    {
        this.id = in.readInt();
        this.objectID = in.readInt();
        this.site = in.readString();
        this.date = in.readString();
    }

    public int getId() {
        return id;
    }

    public int getObjectID() {
        return objectID;
    }

    public String getSite() {
        return site;
    }

    public String getDate() {
        return date;
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        public Blog createFromParcel(Parcel source) {
            return new Blog(source);
        }

        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.objectID);
        dest.writeString(this.site);
        dest.writeString(this.date);
    }
}
