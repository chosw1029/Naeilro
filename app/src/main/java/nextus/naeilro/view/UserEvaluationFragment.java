package nextus.naeilro.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;

import nextus.naeilro.R;
import nextus.naeilro.utils.OpenGraph;
import nextus.naeilro.utils.OpenGraphData;

/**
 * Created by chosw on 2016-09-30.
 */

public class UserEvaluationFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public UserEvaluationFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static UserEvaluationFragment newInstance(int sectionNumber) {
        UserEvaluationFragment fragment = new UserEvaluationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_station_info, container, false);
        /*
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.stayRecycler);
        LocationItemAdapter adapter = new LocationItemAdapter(getContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        */
       // addOGTypeMemo();
        return rootView;
    }


    private void addOGTypeMemo() {

        // 입력받은 url에 해당하는 html을 요청한다.
        final String url = "";
        final ArrayList<String> url_list = new ArrayList<>();
        url_list.add("http://redwine1004.tistory.com/263");
        url_list.add("http://blog.naver.com/sunyen97/220818434099");

        new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i=0; i< url_list.size(); i++)
                    {
                        OpenGraph openGraph = new OpenGraph.Builder(url_list.get(i)).build();
                        OpenGraphData data = openGraph.getOpenGraph();
                        Log.e("TEST", ""+data.getTitle());
                        Log.e("TEST", ""+data.getImage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
