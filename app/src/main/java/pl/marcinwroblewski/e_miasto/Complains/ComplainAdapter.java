package pl.marcinwroblewski.e_miasto.Complains;

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

import pl.marcinwroblewski.e_miasto.Activities.ComplainDetailsActivity;
import pl.marcinwroblewski.e_miasto.R;

/**
 * Created by Marcin Wróblewski on 19.10.16.
 */

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.MyViewHolder> {

    private Context mContext;
    private List<Complain> complainList;

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


    public ComplainAdapter(Context mContext, List<Complain> complainList) {
        this.mContext = mContext;
        this.complainList = complainList;
    }

    @Override
    public ComplainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);

        return new ComplainAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ComplainAdapter.MyViewHolder holder, final int position) {
        Complain complain = complainList.get(position);
        holder.title.setText(complain.getTitle());

        // loading album cover using Glide library
        //Glide.with(mContext).load(complain.getPhoto()).into(holder.mainImage);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(complain.getPhoto(), holder.mainImage);


        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Opcja w przygotowaniu", Toast.LENGTH_SHORT).show();
            }
        });

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventDetail = new Intent(mContext, ComplainDetailsActivity.class);
                eventDetail.putExtra("complainId", complainList.get(position).getId());
                mContext.startActivity(eventDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return complainList.size();
    }
}