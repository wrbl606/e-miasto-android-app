package pl.marcinwroblewski.e_miasto.Events;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import pl.marcinwroblewski.e_miasto.Activities.EventDetailsActivity;
import pl.marcinwroblewski.e_miasto.R;

/**
 * Created by Marcin Wróblewski on 16.10.2016.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Event> eventsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView mainImage;
        public AppCompatButton share, show;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.event_title);
            mainImage = (ImageView) view.findViewById(R.id.event_main_image);
            share = (AppCompatButton) view.findViewById(R.id.event_share);
            show = (AppCompatButton) view.findViewById(R.id.event_show);
        }
    }


    public EventsAdapter(Context mContext, List<Event> eventsList) {
        this.mContext = mContext;
        this.eventsList = eventsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Event event = eventsList.get(position);
        holder.title.setText(event.getName());

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(event.getImage(), holder.mainImage);

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Opcja w przygotowaniu", Toast.LENGTH_SHORT).show();
            }
        });

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventDetail = new Intent(mContext, EventDetailsActivity.class);
                eventDetail.putExtra("eventId", eventsList.get(position).getId());
                mContext.startActivity(eventDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
