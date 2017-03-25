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

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import nextus.naeilro.MyApplication;
import nextus.naeilro.R;
import nextus.naeilro.model.Blog;
import nextus.naeilro.model.Location;
import nextus.naeilro.model.Review;
import nextus.naeilro.utils.ContentService;
import nextus.naeilro.utils.OpenGraph;
import nextus.naeilro.utils.OpenGraphData;
import nextus.naeilro.view.LoginActivity;
import nextus.naeilro.view.ObjectInfoActivity;
import nextus.naeilro.view.ReviewActivity;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by chosw on 2016-11-30.
 */

public class ObjectInfoVM extends BaseObservable implements MapView.MapViewEventListener{

    private Subscription subscription;
    private Context context;
    private Location location;
    private MapView mapView;
    private DataListener dataListener;
    private List<OpenGraphData> blogData = new ArrayList<>();
    private List<Blog> blogList = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private Review myreview;

    public ObjectInfoVM(Context context)
    {
        this.context = context;
        mapView = new MapView(context);
    }

    public void setReview(Review review)
    {
        myreview = review;
    }

    public void settingLocation(Location location)
    {
        this.location = location;
        notifyChange();
    }

    public void settingMap()
    {
        mapView.setDaumMapApiKey(context.getString(R.string.daum_map_api));
        ((ObjectInfoActivity)context).binding.objectInfo.mapViewCon.addView(mapView);
        mapView.setMapViewEventListener(this);
    }

    public void getBlogData()
    {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

        ContentService contentService = MyApplication.getMyapplicationContext().getContentService();

        subscription = contentService.getBlogData(location.getLocID())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(MyApplication.getMyapplicationContext().defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Blog>>(){
                    @Override
                    public void onCompleted(){
                        getOgTag();
                    }
                    @Override
                    public void onError(Throwable error)
                    {
                        Log.e(TAG, "Error loading Item ", error);
                    }
                    @Override
                    public void onNext(List<Blog> blogList)
                    {
                        setBlogData(blogList);
                    }
                });
    }


    public void setBlogData(List<Blog> blogList)
    {
        this.blogList = blogList;
    }

    public void getOgTag()
    {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                for(int i=0; i<blogList.size(); i++)
                {
                    OpenGraph openGraph = new OpenGraph.Builder(blogList.get(i).getSite()).build();
                    try
                    {
                        blogData.add(openGraph.getOpenGraph());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(dataListener != null)
                    dataListener.dataChanged(blogData);
            }
        }.execute();
    }

    public Location getLocation() { return location; }


    public void setDataListener(DataListener dataListener)
    {
        this.dataListener = dataListener;
    }

    public interface DataListener
    {
        void dataChanged(List<OpenGraphData> blog);
        void reviewDataChanged(List<Review> reviews);
    }

    public void onClick(View view)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((ObjectInfoActivity)context).overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_default);
        }
        else
        {
            Intent intent = new Intent(context, ReviewActivity.class);
            intent.putExtra("location", location);


            if(view.getTag()!=null && (int)view.getTag() == 100) // 이미 쓴 글 있음 수정창으로
            {
                intent.putExtra("isEditing", true);
                intent.putExtra("rating", myreview.getStarCount());
                intent.putExtra("body", myreview.getBody());
                ((ObjectInfoActivity)context).startActivityForResult(intent, 1);
                ((ObjectInfoActivity)context).overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_default);
            }
            else
            {
                intent.putExtra("isEditing", false);
                ((ObjectInfoActivity)context).startActivityForResult(intent, 0);
                ((ObjectInfoActivity)context).overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_default);
            }
        }
    }


    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        return location.getLocImage();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext().getApplicationContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public String getLocationName() { return location.getLocName(); }
    public String getLocationValueString() { return ""+MyApplication.getMyapplicationContext().getLocation().getLocRvalue(); }
    public float getLocationValueDouble() { return MyApplication.getMyapplicationContext().getLocation().getLocRvalue(); }

    public String getLocationRcountString() { return ""+MyApplication.getMyapplicationContext().getLocation().getLocRcount()+"명";}
    public String getLocationDes() { return location.getLocDes(); }
    public String getLocationSite() { return location.getLocSite(); }
    public String getLocationAddress() { return location.getLocAddr(); }
    public String getLocationPhone() { return location.getLocPhone(); }

    @Override
    public void onMapViewInitialized(MapView mapView) {

        MapPoint CUSTOM_MARKER_POINT = MapPoint.mapPointWithGeoCoord(location.getLocGpsx(), location.getLocGpsy());

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(location.getLocName());
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

    }
}
