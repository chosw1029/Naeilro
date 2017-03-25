package nextus.naeilro.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import nextus.naeilro.R;
import nextus.naeilro.model.EventAPI;
import nextus.naeilro.view.EventPageActivity;
import nextus.naeilro.view.MainActivity;

/**
 * Created by chosw on 2016-09-14.
 */

public class PagerAdapter extends android.support.v4.view.PagerAdapter{
    // Declare Variables
    private Context context;
    private LayoutInflater inflater;
    private List<EventAPI> eventList = new ArrayList<>();

    public PagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    public void setImgUrl(List<EventAPI> eventList)
    {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // layout inflator
        View itemView = inflater.inflate(R.layout.pager_layout, container, false);
        ImageView imageView = (ImageView)itemView.findViewById(R.id.event);
        TextView title = (TextView) itemView.findViewById(R.id.event_title);
        TextView date = (TextView) itemView.findViewById(R.id.event_date);
        TextView address = (TextView) itemView.findViewById(R.id.event_address);
        FrameLayout placeHolder = (FrameLayout) itemView.findViewById(R.id.placeHolder);

        title.setText(eventList.get(position).getTitle());
        date.setText(eventList.get(position).getStartDate()+" ~ "+eventList.get(position).getEndDate());
        address.setText(eventList.get(position).getAddr());

        Glide.with(context)
                .load(eventList.get(position).getImgUrl())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        container.addView(itemView);

        placeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventPageActivity.class);
                intent.putExtra("eventapi", eventList.get(position));
                context.startActivity(intent);
                ((MainActivity) context).overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_default);
            }
        });

        return itemView;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((FrameLayout)object);

    }
}
