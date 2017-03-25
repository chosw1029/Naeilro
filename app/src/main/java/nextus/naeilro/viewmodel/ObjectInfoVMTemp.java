package nextus.naeilro.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import nextus.naeilro.MyApplication;
import nextus.naeilro.R;
import nextus.naeilro.model.Blog;
import nextus.naeilro.model.Item;
import nextus.naeilro.model.ItemDetail;
import nextus.naeilro.model.Location;
import nextus.naeilro.model.Review;
import nextus.naeilro.utils.ContentService;
import nextus.naeilro.utils.OpenGraph;
import nextus.naeilro.utils.OpenGraphData;
import nextus.naeilro.view.LoginActivity;
import nextus.naeilro.view.ObjectInfoActivity;
import nextus.naeilro.view.ObjectInfoActivityTemp;
import nextus.naeilro.view.ReviewActivity;
import nextus.naeilro.view.StationInfoActivity;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by chosw on 2016-11-30.
 */

public class ObjectInfoVMTemp extends BaseObservable implements MapView.MapViewEventListener{

    private Subscription subscription;
    private Context context;
    private ItemDetail itemDetail;
    private Item item;
    private MapView mapView;
    private DataListener dataListener;
    private List<OpenGraphData> blogData = new ArrayList<>();
    private List<Blog> blogList = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private Review myreview;
    private String url;
    private String contentID;

    public ObjectInfoVMTemp(Context context, String contentID, Item item)
    {
        this.context = context;
        this.contentID = contentID;
        this.item = item;
        loadDetailData();
        mapView = new MapView(context);

    }

    public void setReview(Review review)
    {
        myreview = review;
    }

    public void settingMap()
    {
        mapView.setDaumMapApiKey(context.getString(R.string.daum_map_api));
        ((ObjectInfoActivityTemp)context).binding.objectInfo.mapViewCon.addView(mapView);
        mapView.setMapViewEventListener(this);
    }

    public void loadDetailData()
    {
        url = "detailCommon?ServiceKey=AYfA0yQD4QNdFo%2FGK9SBlBh%2Fvb7pAk3hWEWZOYspVpY0mfbJbsGjqpZ1wjalXRHbhLqyBNxHVybiDJ%2FrdC5lHw%3D%3D" +
                "&contentTypeId=12&contentId="+contentID+"" +
                "&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y&_type=json";

        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

        ContentService contentService = MyApplication.getMyapplicationContext().getVisitKoreaAPI();

        subscription = contentService.getDetailData(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(MyApplication.getMyapplicationContext().defaultSubscribeScheduler())
                .subscribe(new Subscriber<JsonObject>(){
                    @Override
                    public void onCompleted(){
                        notifyChange();

                        if(dataListener != null)
                            dataListener.setSite(itemDetail.getHomepage());

                    }
                    @Override
                    public void onError(Throwable error)
                    {
                        Log.e(TAG, "Error loading Item ", error);
                    }
                    @Override
                    public void onNext(JsonObject apiTempList)
                    {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonElement rootObejct = parser.parse(apiTempList.toString())
                                .getAsJsonObject().get("response")
                                .getAsJsonObject().get("body")
                                .getAsJsonObject().get("items")
                                .getAsJsonObject().get("item");
                        Type listType = new TypeToken<ItemDetail>() {}.getType();
                        itemDetail = gson.fromJson(rootObejct, listType);
                    }
                });


    }

    public void setDataListener(DataListener dataListener)
    {
        this.dataListener = dataListener;
    }

    public interface DataListener
    {
        void setSite(String url);
        void dataChanged(List<OpenGraphData> blog);
        void reviewDataChanged(List<Review> reviews);
    }

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        return item.getFirstimage();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext().getApplicationContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public String getLocationName() { return item.getTitle(); }
    public String getLocationValueString() { return ""; }
    public float getLocationValueDouble() { return 0; }

    public String getLocationRcountString() { return "0명";}
    public String getLocationDes() { return itemDetail.getOverview(); }
    public String getLocationSite()
    {
        if(itemDetail != null)
            return itemDetail.getHomepage();
        else
            return "";
    }
    public String getLocationAddress() { return item.getAddr1(); }
    public String getLocationPhone()
    {
        if(itemDetail != null)
            return itemDetail.getTel();
        else
            return "";
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

        MapPoint CUSTOM_MARKER_POINT = MapPoint.mapPointWithGeoCoord(Double.parseDouble(item.getMapy()), Double.parseDouble(item.getMapx()));

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(item.getTitle());
        marker.setDraggable(false);
        marker.setTag(0);
        marker.setMapPoint(CUSTOM_MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
        mapView.setMapCenterPointAndZoomLevel(CUSTOM_MARKER_POINT, 3, false);

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    public void onDestroy()
    {
        //mapView.fin
    }
}
