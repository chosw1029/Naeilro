package nextus.naeilro.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by chosw on 2017-01-13.
 */

public class Item implements Parcelable{

    @SerializedName("addr1")
    @Expose
    private String addr1;
    @SerializedName("addr2")
    @Expose
    private String addr2;
    @SerializedName("areacode")
    @Expose
    private String areacode;
    @SerializedName("cat1")
    @Expose
    private String cat1;
    @SerializedName("cat2")
    @Expose
    private String cat2;
    @SerializedName("cat3")
    @Expose
    private String cat3;
    @SerializedName("contentid")
    @Expose
    private String contentid;
    @SerializedName("contenttypeid")
    @Expose
    private String contenttypeid;
    @SerializedName("createdtime")
    @Expose
    private String createdtime;
    @SerializedName("firstimage")
    @Expose
    private String firstimage;
    @SerializedName("firstimage2")
    @Expose
    private String firstimage2;
    @SerializedName("mapx")
    @Expose
    private String mapx;
    @SerializedName("mapy")
    @Expose
    private String mapy;
    @SerializedName("mlevel")
    @Expose
    private String mlevel;
    @SerializedName("modifiedtime")
    @Expose
    private String modifiedtime;
    @SerializedName("readcount")
    @Expose
    private String readcount;
    @SerializedName("sigungucode")
    @Expose
    private String sigungucode;
    @SerializedName("tel")
    @Expose
    private String tel;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;

    public String getAddr1() {
        return addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public String getAreacode() {
        return areacode;
    }

    public String getCat1() {
        return cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public String getContentid() {
        return contentid;
    }

    public String getContenttypeid() {
        return contenttypeid;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public String getFirstimage() {
        return firstimage;
    }

    public String getFirstimage2() {
        return firstimage2;
    }

    public String getMapx() {
        return mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public String getMlevel() {
        return mlevel;
    }

    public String getModifiedtime() {
        return modifiedtime;
    }

    public String getReadcount() {
        return readcount;
    }

    public String getSigungucode() {
        return sigungucode;
    }

    public String getTel() {
        return tel;
    }

    public String getTitle() {
        return title;
    }

    public String getZipcode() {
        return zipcode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    private Item(Parcel in)
    {
        this.addr1 = in.readString();
        this.addr2 = in.readString();
        this.areacode = in.readString();
        this.cat1 = in.readString();
        this.cat2 = in.readString();
        this.cat3 = in.readString();
        this.contentid = in.readString();
        this.contenttypeid = in.readString();
        this.createdtime = in.readString();
        this.firstimage = in.readString();
        this.firstimage2 = in.readString();
        this.mapx = in.readString();
        this.mapy = in.readString();
        this.mlevel = in.readString();
        this.modifiedtime = in.readString();
        this.readcount = in.readString();
        this.sigungucode = in.readString();
        this.title = in.readString();
        this.zipcode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.addr1);
        parcel.writeString(this.addr2);
        parcel.writeString(this.areacode);
        parcel.writeString(this.cat1);
        parcel.writeString(this.cat2);
        parcel.writeString(this.cat3);
        parcel.writeString(this.contentid);
        parcel.writeString(this.contenttypeid);
        parcel.writeString(this.createdtime);
        parcel.writeString(this.firstimage);
        parcel.writeString(this.firstimage2);
        parcel.writeString(this.mapx);
        parcel.writeString(this.mapy);
        parcel.writeString(this.mlevel);
        parcel.writeString(this.modifiedtime);
        parcel.writeString(this.readcount);
        parcel.writeString(this.sigungucode);
        parcel.writeString(this.title);
        parcel.writeString(this.zipcode);
    }
}
