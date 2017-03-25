package nextus.naeilro.utils;


import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import nextus.naeilro.BuildConfig;
import nextus.naeilro.MyApplication;
import nextus.naeilro.model.Blog;
import nextus.naeilro.model.Board;
import nextus.naeilro.model.Location;
import nextus.naeilro.model.Station;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

/**
 * Created by chosw on 2016-08-10.
 */

public interface ContentService {

    @FormUrlEncoded
    @POST ("getLocationData")
    Observable<List<Location>> getLocationData(
            @Field("stationID") int stationID
    );

    @FormUrlEncoded
    @POST ("setLocationRating")
    Call<ResponseBody> setLocationRating(
            @Field("locID") String locID,
            @Field("rating") float rating
    );

    @FormUrlEncoded
    @POST ("/database/db/setCommentCount")
    Call<ResponseBody> setCommentCount(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST ("/database/db/removeComment")
    Call<ResponseBody> removeComment(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("removeLocationRating")
    Call<ResponseBody> removeLocationRating(
            @Field("locID") String locID,
            @Field("rating") float rating
    );


    @FormUrlEncoded
    @POST ("getBlogData")
    Observable<List<Blog>> getBlogData(
            @Field("objectID") int objectID
    );

    @GET ("getStationData")
    Observable<List<Station>> getStationData();

    @GET ("/database/db/getBoardData")
    Observable<List<Board>> getBoardData();

    @GET
    Observable<JsonObject> getTestData(@Url String url);

    @GET
    Observable<JsonObject> getDetailData(@Url String url);

    @FormUrlEncoded
    @POST("/database/db/createBoard")
    Call<ResponseBody> createBoard(
            @Field("uid") String uid,
            @Field("nickname") String nickname,
            @Field("text") String text,
            @Field("date") String date
    );

    @Multipart
    @POST("createBoardData")
    Call<ResponseBody> uploadFileWithPartMap(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);




    /*
    @FormUrlEncoded
    @POST("https://fcm.googleapis.com/fcm/send")
    Call<ResponseBody> sendFCM(JSONObject data);


    @FormUrlEncoded
    @POST("pokemongo/deleteComment.jsp")
    Call<ResponseBody> deleteComment(
            @Field("comment_id") String comment_id,
            @Field("board_id") String board_id
    );



    @FormUrlEncoded
    @POST("pokemongo/updateToken.jsp")
    Call<ResponseBody> updateToken(
            @Field("user_id") String user_id,
            @Field("token") String token) ;

    @FormUrlEncoded
    @POST("pokemongo/getBoardData.jsp")
    Observable<BoardItem> getData(@Field("user_id") String user_id) ;

    @FormUrlEncoded
    @POST("pokemongo/getCommentData.jsp")
    Observable<List<Comment>> getCommentData(@Field("board_id") int board_id);

    @FormUrlEncoded
    @POST("pokemongo/createLike.jsp")
    Call<ResponseBody> createLike(
            @Field("user_id") String user_id,
            @Field("board_id") String board_id
    );

    @FormUrlEncoded
    @POST("pokemongo/addUser.jsp")
    Call<ResponseBody> addUserData(
            @Field("userID") String userID,
            @Field("userNickName") String userNickName,
            @Field("userThumnail") String userThumnail,
            @Field("user_token") String token,
            @Field("user_birthday") String user_birthday,
            @Field("user_phone") String user_phone
    );

    @FormUrlEncoded
    @POST("pokemongo/createComment.jsp")
    Call<ResponseBody> createComment(
            @Field("board_id") String board_id,
            @Field("user_id") String user_id,
            @Field("user_name") String user_name,
            @Field("comment_data") String comment_data,
            @Field("date") String date
    );

    @Multipart
    @POST("pokemongo/multipart_temp.jsp")
    Call<ResponseBody> upload(@Part("description") RequestBody description,
                              @Part MultipartBody.Part file);

    @Multipart
    @POST("pokemongo/multipart_temp.jsp")
    Call<ResponseBody> uploadFileWithPartMap(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);

*/
    /*
    @GET
    Observable<User> userFromUrl(@Url String userUrl);
*/

    class Factory {

        private static final String CACHE_CONTROL = "Cache-Control";

        public static ContentService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://52.78.152.162:3000/")
                    .client(provideOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())  // Json Parser
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())  // RxAndroid 사용을 위한 옵션
                    .build();
            return retrofit.create(ContentService.class);
        }

        public static ContentService createAPIService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://openapi.tago.go.kr/openapi/service/TrainInfoService/")
                    .client(provideOkHttpClient())
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())  // RxAndroid 사용을 위한 옵션
                    .build();
            return retrofit.create(ContentService.class);
        }

        public static ContentService createVisitKoreaAPI() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.visitkorea.or.kr/openapi/service/rest/KorService/")
                    .client(provideOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())  // RxAndroid 사용을 위한 옵션
                    .build();
            return retrofit.create(ContentService.class);
        }

        private static OkHttpClient provideOkHttpClient ()
        {
            return new OkHttpClient.Builder()
                    .addInterceptor( provideHttpLoggingInterceptor() )
                    //.addInterceptor( provideOfflineCacheInterceptor() )
                    //.addNetworkInterceptor( provideCacheInterceptor() )
                    //.cache( provideCache() )
                    .build();
        }

        private static Cache provideCache ()
        {
            Cache cache = null;
            try
            {
                cache = new Cache( new File( MyApplication.getMyapplicationContext().getCacheDir(), "http-cache" ),
                        10 * 1024 * 1024 ); // 10 MB
            }
            catch (Exception e)
            {
                Log.e("Error", "Could not create Cache!" );
            }
            return cache;
        }

        private static HttpLoggingInterceptor provideHttpLoggingInterceptor ()
        {
            HttpLoggingInterceptor httpLoggingInterceptor =
                    new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger()
                    {
                        @Override
                        public void log (String message)
                        {
                            Log.d("", message );
                        }
                    } );
            httpLoggingInterceptor.setLevel( BuildConfig.DEBUG ? HEADERS : NONE );
            return httpLoggingInterceptor;
        }

        public static Interceptor provideCacheInterceptor ()
        {
            return new Interceptor()
            {
                @Override
                public Response intercept (Chain chain) throws IOException
                {
                    Response response = chain.proceed( chain.request() );

                    // re-write response header to force use of cache
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxAge( 1, TimeUnit.MINUTES )
                            .build();

                    return response.newBuilder()
                            .header( CACHE_CONTROL, cacheControl.toString() )
                            .build();
                }
            };
        }

        public static Interceptor provideOfflineCacheInterceptor ()
        {
            return new Interceptor()
            {
                @Override
                public Response intercept (Chain chain) throws IOException
                {
                    Request request = chain.request();

                    if ( !MyApplication.hasNetwork() )
                    {
                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxStale( 7, TimeUnit.DAYS )
                                .build();

                        request = request.newBuilder()
                                .cacheControl( cacheControl )
                                .build();
                    }

                    return chain.proceed( request );
                }
            };
        }
    }
}
