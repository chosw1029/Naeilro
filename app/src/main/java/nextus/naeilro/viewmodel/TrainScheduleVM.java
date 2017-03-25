package nextus.naeilro.viewmodel;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.DatePicker;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nextus.naeilro.BR;
import nextus.naeilro.MyApplication;
import nextus.naeilro.R;
import nextus.naeilro.model.TrainSchedule;
import nextus.naeilro.view.TrainInfoActivity;
import nextus.naeilro.view.TrainStationListActivity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by chosw on 2016-09-15.
 */

public class TrainScheduleVM extends BaseObservable{

    private Subscription subscription;
    private DataListener dataListener;
    private Context context;
    private String depName = "출발역을 선택하세요";
    private String arrName = "도착역을 선택하세요";
    private String depTime = "날짜를 선택하세요.";
    private boolean hasDepName = false;
    private boolean hasArrName = false;
    private boolean hasDepTime = false;
    private String depId;
    private String arrId;
    private String key = "AYfA0yQD4QNdFo%2FGK9SBlBh%2Fvb7pAk3hWEWZOYspVpY0mfbJbsGjqpZ1wjalXRHbhLqyBNxHVybiDJ%2FrdC5lHw%3D%3D";
    private List<TrainSchedule> trainScheduleList = new ArrayList<>();


    public TrainScheduleVM(Context context)
    {
        this.context = context;
    }

    @Bindable
    public String getDepName() { return depName; }

    @Bindable
    public String getArrName() { return arrName; }

    @Bindable
    public String getDepTime() { return depTime; }

    public void setDepName(String depName) { this.depName = depName; setHasDepName(true); notifyPropertyChanged(BR.depName);}
    public void setArrName(String arrName) { this.arrName = arrName; setHasArrName(true); notifyPropertyChanged(BR.arrName);}
    public void setDepTime(String depTime) { this.depTime = depTime; setHasDepTime(true); notifyPropertyChanged(BR.depTime);}
    public void setDepId(String depId) { this.depId = depId; notifyChange(); }
    public void setArrId(String arrId) { this.arrId = arrId; notifyChange(); }
    public void setHasDepName(Boolean hasDepName) { this.hasDepName = hasDepName; notifyChange(); }
    public void setHasArrName(Boolean hasArrName) { this.hasArrName = hasArrName; notifyChange(); }
    public void setHasDepTime(Boolean hasDepTime) { this.hasDepTime = hasDepTime; notifyChange(); }

    public interface DataListener
    {
        void dataChangedListener(List<TrainSchedule> list);
    }

    public void setDataListener(DataListener dataListener)
    {
        this.dataListener = dataListener;
    }

    public void onClick(View view)
    {
        Intent intent;
        switch(view.getId())
        {
            case R.id.depLayout:
                intent = new Intent(context, TrainStationListActivity.class);
                ((TrainInfoActivity) context).startActivityForResult(intent, 0);
                break;
            case R.id.arrLayout:
                intent = new Intent(context, TrainStationListActivity.class);
                ((TrainInfoActivity) context).startActivityForResult(intent, 1);
                break;
            case R.id.search:
                if(!hasArrName||!hasDepName||!hasDepTime)
                {
                    Snackbar.make(view, "항목을 입력해주세요", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    ((TrainInfoActivity)context).binding.contentTrainschedule.searchProgress.setVisibility(View.VISIBLE);
                    ((TrainInfoActivity)context).binding.contentTrainschedule.loadingText.setVisibility(View.INVISIBLE);
                    ((TrainInfoActivity)context).binding.contentTrainschedule.schedulRecycler.setVisibility(View.INVISIBLE);
                    searchData();
                }
                break;
            case R.id.dateLayout:
               showDialog();
                break;
        }
    }

    private void showDialog()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //Snackbar.make(view, year + "-" + monthOfYear + "-" + dayOfMonth, Snackbar.LENGTH_SHORT).show();
            String month ="";
            String day = "";
            if(monthOfYear < 9)
                month = "0"+(monthOfYear+1);
            else
                month = ""+(monthOfYear+1);

            if(dayOfMonth < 10)
                day = "0"+dayOfMonth;
            else
                day = ""+dayOfMonth;

            String date = ""+year+""+month+""+day;

            setDepTime(date);
            //Log.e("DataSet", ""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
        }
    };

    private void searchData()
    {
        Observable<List<TrainSchedule>> simpleObservable =
                Observable.create(new Observable.OnSubscribe<List<TrainSchedule>>() {
                    @Override
                    public void call(Subscriber<? super List<TrainSchedule>> subscriber) {
                        subscriber.onNext(trainScheduleParsing());
                        subscriber.onCompleted();
                    }
                });

        subscription = simpleObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(MyApplication.getMyapplicationContext().defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<TrainSchedule>>() {
            @Override
            public void onCompleted() {
                if(dataListener != null) {
                    dataListener.dataChangedListener(trainScheduleList);
                    ((TrainInfoActivity)context).binding.contentTrainschedule.searchProgress.setVisibility(View.INVISIBLE);
                    ((TrainInfoActivity)context).binding.contentTrainschedule.schedulRecycler.setVisibility(View.VISIBLE);
               }
            }

            @Override
            public void onError(Throwable e) {
                //Log.e("TrainSchedule", e.);
                e.printStackTrace();
            }

            @Override
            public void onNext(List<TrainSchedule> trainStations) {
                if(trainStations.size() == 0)
                {
                    ((TrainInfoActivity)context).binding.contentTrainschedule.loadingText.setVisibility(View.VISIBLE);
                    ((TrainInfoActivity)context).binding.contentTrainschedule.loadingText.setText("결과가 없습니다.");
                }
                trainScheduleList = trainStations;
            }
        });
    }

    private List<TrainSchedule> trainScheduleParsing()
    {
        List<TrainSchedule> trainSchedules = new ArrayList<>();
        try {

            URL url = new URL("http://openapi.tago.go.kr/openapi/service/TrainInfoService/getStrtpntAlocFndTrainInfo?serviceKey=" +
                    key+"&depPlaceId="+depId+"&arrPlaceId="+arrId+"&depPlandTime="+depTime+"&numOfRows=100"); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));

            int eventType = parser.getEventType();

            TrainSchedule trainSchedule = null;
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("item"))
                        {
                            trainSchedule = new TrainSchedule();
                        }
                        else if(startTag.equals("adultcharge"))
                        {
                            trainSchedule.setAdultcharge(parser.nextText());
                        }
                        else if(startTag.equals("arrplacename"))
                        {
                            trainSchedule.setArrPlaceName(parser.nextText());
                        }
                        else if(startTag.equals("arrplandtime"))
                        {
                            trainSchedule.setArrTime(parser.nextText());
                        }
                        else if(startTag.equals("depplacename"))
                        {
                            trainSchedule.setDepPlaceName(parser.nextText());
                        }
                        else if(startTag.equals("depplandtime"))
                        {
                            trainSchedule.setDepTime(parser.nextText());
                        }
                        else if(startTag.equals("traingradename"))
                        {
                            trainSchedule.setTrainType(parser.nextText());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("item")) {
                            trainSchedules.add(trainSchedule);
                        }
                        break;
                }
                eventType = parser.next();
            }

        }catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return trainSchedules;
    }


}
