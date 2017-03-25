package nextus.naeilro.model;

/**
 * Created by chosw on 2017-01-02.
 */

public class Event {
    private String title;
    private String date;
    private String address;
    private String imgSrc;
    private String href;

    public Event(String title, String date, String address, String imgSrc, String href) {
        this.title = title;
        this.date = date;
        this.address = address;
        this.imgSrc = imgSrc;
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getHref() {
        return href;
    }
}
