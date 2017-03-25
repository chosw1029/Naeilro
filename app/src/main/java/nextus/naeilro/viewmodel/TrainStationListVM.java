package nextus.naeilro.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nextus.naeilro.R;
import nextus.naeilro.model.TrainStation;
import nextus.naeilro.view.TrainStationListActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by chosw on 2017-01-05.
 */

public class TrainStationListVM extends BaseObservable{

    private DataListener dataListener;
    private Context context;
    private List<TrainStation> trainStationList = new ArrayList<>();
    private TrainStation trainStation;
    private ObservableField<String> text = new ObservableField<>();

    public TrainStationListVM(Context context)
    {
        this.context = context;
    }

    public String getStationName() { return trainStation.getNodeName(); }

    public void setTrainStation(TrainStation trainStation) { this.trainStation = trainStation; notifyChange(); }

    private List<TrainStation> filter(List<TrainStation> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<TrainStation> filteredModelList = new ArrayList<>();
        for (TrainStation model : models) {
            final String text = model.getNodeName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void setDataListener(DataListener dataListener)
    {
        this.dataListener = dataListener;
    }

    public interface DataListener
    {
        void dataChangedListener(List<TrainStation> list);
    }

    public TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!Objects.equals(text.get(), editable.toString())) {
                text.set(editable.toString());
                List<TrainStation> filteredModelList = filter(trainStationList, text.get());
                dataListener.dataChangedListener(filteredModelList);
            }
        }
    };

    public void loadStationList()
    {
        Observable<List<TrainStation>> simpleObservable =
                Observable.create(new Observable.OnSubscribe<List<TrainStation>>() {
                    @Override
                    public void call(Subscriber<? super List<TrainStation>> subscriber) {
                        subscriber.onNext(readStationList());
                        subscriber.onCompleted();
                    }
                });

        simpleObservable.subscribe(new Subscriber<List<TrainStation>>() {
            @Override
            public void onCompleted() {
                if(dataListener != null) {
                    dataListener.dataChangedListener(trainStationList);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<TrainStation> trainStations) {
                trainStationList = trainStations;
            }
        });
    }

    private List<TrainStation> readStationList()
    {
        List<TrainStation> trainStations = new ArrayList<>();

        try{

            XmlResourceParser xrp = context.getResources().getXml(R.xml.station);
            int eventType = xrp.getEventType();

            TrainStation trainStation = null;


            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        String startTag = xrp.getName();
                        if(startTag.equals("item"))
                        {
                            trainStation = new TrainStation();
                        }
                        else if(startTag.equals("nodeid"))
                        {
                            trainStation.setNodeID(xrp.nextText());
                        }
                        else if(startTag.equals("nodename"))
                        {
                            trainStation.setNodeName(xrp.nextText());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = xrp.getName();
                        if(endTag.equals("item")) {
                            trainStations.add(trainStation);
                        }
                        break;
                }
                eventType = xrp.next();
            }

        } catch(XmlPullParserException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trainStations;
    }

    public void onClick(View view)
    {
        Intent intent = new Intent();
        intent.putExtra("name", trainStation.getNodeName());
        intent.putExtra("id", trainStation.getNodeID());
        ((TrainStationListActivity) context).flag_number = 1;
        ((TrainStationListActivity) context).setResult(1, intent);
        ((TrainStationListActivity) context).finish();
    }

}
