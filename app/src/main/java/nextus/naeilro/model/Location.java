package nextus.naeilro.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

/**
 * Created by chosw on 2016-09-14.
 */

public class Location extends BaseObservable implements Parcelable, SortedListAdapter.ViewModel{

    private int locID;
    private int locType;
    private int locStation;
    private String locName;
    private String locDes;
    private String locAddr;
    private Double locGpsx;
    private Double locGpsy;
    private String locPhone;
    private String locSite;
    private int locRcount;
    private float locRvalue;
    private String locImage;

    private Location(Parcel in)
    {
        this.locID = in.readInt();
        this.locType = in.readInt();
        this.locStation = in.readInt();
        this.locName = in.readString();
        this.locDes = in.readString();
        this.locAddr = in.readString();
        this.locGpsx = in.readDouble();
        this.locGpsy = in.readDouble();
        this.locPhone = in.readString();
        this.locSite = in.readString();
        this.locRcount = in.readInt();
        this.locRvalue = in.readFloat();
        this.locImage = in.readString();
    }

    public void setLocID(int locID) {
        this.locID = locID;
    }

    public void setLocType(int locType) {
        this.locType = locType;
    }

    public void setLocStation(int locStation) {
        this.locStation = locStation;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public void setLocDes(String locDes) {
        this.locDes = locDes;
    }

    public void setLocAddr(String locAddr) {
        this.locAddr = locAddr;
    }

    public void setLocGpsx(Double locGpsx) {
        this.locGpsx = locGpsx;
    }

    public void setLocGpsy(Double locGpsy) {
        this.locGpsy = locGpsy;
    }

    public void setLocPhone(String locPhone) {
        this.locPhone = locPhone;
    }

    public void setLocSite(String locSite) {
        this.locSite = locSite;
    }

    public void setLocRcount(int locRcount) {
        this.locRcount = locRcount;
        notifyPropertyChanged(BR.locRcount);
    }

    public void setLocRvalue(float locRvalue) {
        this.locRvalue = locRvalue;
        notifyPropertyChanged(BR.locRcount);
    }

    public void setLocImage(String locImage) {
        this.locImage = locImage;
    }

    public int getLocID() {
        return locID;
    }

    public int getLocType() {
        return locType;
    }

    public int getLocStation() {
        return locStation;
    }

    public String getLocName() {
        return locName;
    }

    public String getLocDes() {
        return locDes;
    }

    public String getLocAddr() {
        return locAddr;
    }

    public Double getLocGpsx() {
        return locGpsx;
    }

    public Double getLocGpsy() {
        return locGpsy;
    }

    public String getLocPhone() {
        return locPhone;
    }

    public String getLocSite() {
        return locSite;
    }

    @Bindable
    public int getLocRcount() {
        return locRcount;
    }

    @Bindable
    public float getLocRvalue() {
        return locRvalue;
    }

    public String getLocImage() {
        return locImage;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.locID);
        parcel.writeInt(this.locType);
        parcel.writeInt(this.locStation);
        parcel.writeString(this.locName);
        parcel.writeString(this.locDes);
        parcel.writeString(this.locAddr);
        parcel.writeDouble(this.locGpsx);
        parcel.writeDouble(this.locGpsy);
        parcel.writeString(this.locPhone);
        parcel.writeString(this.locSite);
        parcel.writeInt(this.locRcount);
        parcel.writeFloat(this.locRvalue);
        parcel.writeString(this.locImage);

    }

}
