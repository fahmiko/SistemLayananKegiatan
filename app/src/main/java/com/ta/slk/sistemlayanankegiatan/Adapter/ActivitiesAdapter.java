package com.ta.slk.sistemlayanankegiatan.Adapter;
import com.bumptech.glide.Glide;
import com.ta.slk.sistemlayanankegiatan.Model.*;
import android.content.Context;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyViewHolder> {
    private Context context;
    private List<Activities> myActivity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView title,admin,date,location;
        private ImageView image;
        public MyViewHolder(View v) {
            super(v);
            title = itemView.findViewById(R.id.main_title);
            admin = itemView.findViewById(R.id.main_admin);
            date = itemView.findViewById(R.id.main_tgl);
            image = itemView.findViewById(R.id.main_img_card);
            location = itemView.findViewById(R.id.main_location);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ActivitiesAdapter(List<Activities> myActivity, Context context) {
        this.myActivity = myActivity;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_activities, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(myActivity.get(position).getNameActivities());
        holder.admin.setText(myActivity.get(position).getCreatedBy());
        holder.date.setText(myActivity.get(position).getDate());
        holder.location.setText(myActivity.get(position).getPlace());
        if (myActivity.get(position).getPicture() != null) {
            Glide.with(holder.itemView.getContext()).load(ApiClient.BASE_URL+"uploads/"+myActivity.get
                    (position).getPicture())
                    .into(holder.image);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myActivity.size();
    }
}