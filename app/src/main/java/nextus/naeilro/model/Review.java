package nextus.naeilro.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chosw on 2016-12-21.
 */

@IgnoreExtraProperties
public class Review {

    private String uid;
    private String author;
    private String icon;
    private String oid;
    private String body;
    private String date;
    private String oName;
    private float starCount;


    public Review()
    {
    }

    public Review(String uid, String author, String icon, String body, String date, float starCount, String oid, String oName) {
        this.uid = uid;
        this.author = author;
        this.icon = icon;
        this.body = body;
        this.date = date;
        this.starCount = starCount;
        this.oid = oid;
        this.oName = oName;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("icon", icon);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("oid", oid);
        result.put("date", date);
        result.put("oName", oName);
        return result;
    }

    public String getUid() {
        return uid;
    }

    public String getAuthor() {
        return author;
    }

    public String getIcon() {
        return icon;
    }

    public String getOid() {
        return oid;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }

    public float getStarCount() {
        return starCount;
    }

    public String getoName() {
        return oName;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setoName(String oName) {
        this.oName = oName;
    }

    public void setStarCount(float starCount) {
        this.starCount = starCount;
    }
}
