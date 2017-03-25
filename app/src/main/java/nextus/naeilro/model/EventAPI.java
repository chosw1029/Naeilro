package nextus.naeilro.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chosw on 2017-01-03.
 */

public class EventAPI implements Parcelable{

    private String addr;
    private String imgUrl;
    private String site;
    private String mapX;
    private String mapY;
    private String mapLevel;
    private String title;
    private String tel;
    private String overview;
    private String contentid;
    private String startDate;
    private String endDate;
    private String playTime;

    public EventAPI()
    {

    }

    public EventAPI(Parcel in)
    {
        this.addr = in.readString();
        this.imgUrl = in.readString();
        this.site = in.readString();
        this.mapX = in.readString();
        this.mapY = in.readString();
        this.mapLevel = in.readString();
        this.title = in.readString();
        this.tel = in.readString();
        this.overview = in.readString();
        this.contentid = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.playTime = in.readString();
    }

    public static final Creator<EventAPI> CREATOR = new Creator<EventAPI>() {
        public EventAPI createFromParcel(Parcel source) {
            return new EventAPI(source);
        }

        public EventAPI[] newArray(int size) {
            return new EventAPI[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addr);
        dest.writeString(this.imgUrl);
        dest.writeString(this.site);
        dest.writeString(this.mapX);
        dest.writeString(this.mapY);
        dest.writeString(this.mapLevel);
        dest.writeString(this.title);
        dest.writeString(this.tel);
        dest.writeString(this.overview);
        dest.writeString(this.contentid);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.playTime);
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getMapX() {
        return mapX;
    }

    public void setMapX(String mapX) {
        this.mapX = mapX;
    }

    public String getMapY() {
        return mapY;
    }

    public void setMapY(String mapY) {
        this.mapY = mapY;
    }

    public String getMapLevel() {
        return mapLevel;
    }

    public void setMapLevel(String mapLevel) {
        this.mapLevel = mapLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }
}
