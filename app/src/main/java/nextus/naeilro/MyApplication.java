package nextus.naeilro;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kakao.auth.KakaoSDK;

import nextus.naeilro.adapter.KakaoSDKAdapter;
import nextus.naeilro.model.Location;
import nextus.naeilro.utils.ContentService;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by chosw on 2016-09-10.
 */

public class MyApplication extends Application {

    private static volatile Activity currentActivity = null;
    private static volatile MyApplication instance = null;
    private ContentService mContentService;
    private ContentService mContentServiceAPI;
    private ContentService mContentVisitKoreaAPI;
    private Scheduler defaultSubscribeScheduler;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAnalytics analytics;
    private static Location location;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        //AppEventsLogger.activateApp(this);
        KakaoSDK.init(new KakaoSDKAdapter());
        TypefaceProvider.registerDefaultIconSets();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://naeilroproject-99b0c.appspot.com");
        analytics = FirebaseAnalytics.getInstance(this);
    }

    public Location getLocation() { return location; }

    public void setLocation(Location lc)
    {
        location = lc;
    }

    public StorageReference getStorageReference() { return storageReference; }

    public static Activity getCurrentActivity() {
     //   Logger.d("++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        MyApplication.currentActivity = currentActivity;
    }

    public static MyApplication getMyapplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    public ContentService getContentService() {
        if (mContentService == null) {
            mContentService = ContentService.Factory.create();
        }
        return mContentService;
    }

    public ContentService getVisitKoreaAPI() {
        if(mContentVisitKoreaAPI == null)
            mContentVisitKoreaAPI = ContentService.Factory.createVisitKoreaAPI();

        return mContentVisitKoreaAPI;
    }

    public ContentService getContentServiceAPI() {
        if (mContentServiceAPI == null) {
            mContentServiceAPI = ContentService.Factory.createAPIService();
        }
        return mContentServiceAPI;
    }

    //For setting mocks during testing
    public void setContentService(ContentService mContentService) {
        this.mContentService = mContentService;
    }

    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }

    //User to change scheduler from tests
    public void setDefaultSubscribeScheduler(Scheduler scheduler) {
        this.defaultSubscribeScheduler = scheduler;
    }

    public static boolean hasNetwork ()
    {
        return instance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        //super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }
}
