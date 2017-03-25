package nextus.naeilro.view;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import nextus.naeilro.R;
import nextus.naeilro.adapter.PagerAdapter;
import nextus.naeilro.adapter.ReviewItemAdapter;
import nextus.naeilro.databinding.ContentMainActivityBinding;
import nextus.naeilro.model.EventAPI;
import nextus.naeilro.model.Review;
import nextus.naeilro.model.Station;
import nextus.naeilro.utils.EventDataXmlParser;
import nextus.naeilro.viewholder.ReviewViewHolder;
import nextus.naeilro.viewmodel.MainVM;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment implements  MainVM.DataListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ContentMainActivityBinding binding;


    ReviewItemAdapter adapter;
    MainVM viewModel;


    private FirebaseRecyclerAdapter<Review, ReviewViewHolder> mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_main_activity, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = ContentMainActivityBinding.bind(getView());
        viewModel = new MainVM(getContext());
        viewModel.setDataListener(this);
        binding.setViewModel(viewModel);
        MobileAds.initialize(getContext(), getString(R.string.banner_ad_unit_id));

        //setUpToolBar(binding.toolbar, binding.drawerLayout);
        setUpViewPager(binding.pager, binding.indicator);
        //binding.navView.setNavigationItemSelectedListener(this);
        //binding.navView.getMenu().getItem(0).setChecked(true);
        setUpRecyclerView(binding.reviewRecycler);

        testHtml();
        setUpAds();
    }

    public void testHtml()
    {
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, List<EventAPI>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<EventAPI> doInBackground(Void... params) {
            EventDataXmlParser eventDataXmlParser = new EventDataXmlParser();
            eventDataXmlParser.getContentID();
            return eventDataXmlParser.getEventList();
        }

        @Override
        protected void onPostExecute(List<EventAPI> result) {
            //textviewHtmlDocument.setText(htmlContentInStringFormat);
            ((PagerAdapter)binding.pager.getAdapter()).setImgUrl(result);
            handler();
        }
    }

    public void handler()
    {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            int currentPage = 0;
            public void run() {
                if (binding.pager.getCurrentItem() == 11) {
                    currentPage = 0;
                }
                binding.pager.setCurrentItem(currentPage++, true);

            }
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 5000);
    }

    public void setUpAds()
    {
        AdRequest request = new AdRequest.Builder()
                .build();
        binding.adView.loadAd(request);
    }

    public void setUpViewPager(ViewPager viewPager, CirclePageIndicator mIndicator)
    {
        // Pass results to ViewPagerAdapter Class
        PagerAdapter adapter = new PagerAdapter(getContext());
        // Binds the Adapter to the ViewPager
        viewPager.setAdapter(adapter);
        // ViewPager Indicator
        mIndicator.setViewPager(viewPager);
    }

    public void setUpRecyclerView(RecyclerView recyclerView)
    {
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getContext());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        Query postsQuery = mDatabase.child("reviews").limitToLast(5);
        mAdapter = new FirebaseRecyclerAdapter<Review, ReviewViewHolder>(Review.class, R.layout.item_review,
                ReviewViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final ReviewViewHolder viewHolder, final Review review, final int position) {
                final DatabaseReference postRef = getRef(position);
                // Set click listener for the whole post view
                final String postKey = postRef.getKey();

                review.setDate(getDate(review.getDate()));
                /*
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }*/

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToReview(review);
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    public String getDate(String dateString)
    {
        String date_string = dateString;
        String date = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.KOREA);
        Date current = new Date(System.currentTimeMillis());

        try{
            Date d = format.parse(date_string) ;
            long time_long = current.getTime() - d.getTime();
            int time = (int)time_long/1000;

            if( time < 60 )
            {
                date = "방금";
            }
            else if( time >=60 && time < 3600 )
            {
                time = time/60;
                date = ""+time+"분 전";
            }
            else if( time >=3600 && time < 86400 )
            {
                time = time/3600;
                date = ""+time+"시간 전";
            }
            else if( time >= 86400 && time < 604800 )
            {
                time = time/86400;
                date = ""+time+"일 전";
            }
            else
            {
                date = date_string;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return date;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void stationDataChanged(List<Station> list) {
        Glide.with(this)
                .load(list.get(0).getS_stationImg())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.best01);
        binding.best01Name.setText("#"+list.get(0).getS_name());

        Glide.with(this)
                .load(list.get(1).getS_stationImg())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.best02);
        binding.best02Name.setText("#"+list.get(1).getS_name());

        Glide.with(this)
                .load(list.get(2).getS_stationImg())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.best03);
        binding.best03Name.setText("#"+list.get(2).getS_name());

        Glide.with(this)
                .load(list.get(3).getS_stationImg())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.best04);
        binding.best04Name.setText("#"+list.get(3).getS_name());
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
