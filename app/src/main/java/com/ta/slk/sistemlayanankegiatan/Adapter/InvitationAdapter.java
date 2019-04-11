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
public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.MyViewHolder> {
    private Context context;
    private List<InvtActivities> myList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView title,admin,date;
        private ImageView image;
        public MyViewHolder(View v) {
            super(v);
            title = itemView.findViewById(R.id.inv_title);
            admin = itemView.findViewById(R.id.inv_admin);
            date = itemView.findViewById(R.id.inv_tgl);
            image = itemView.findViewById(R.id.inv_img_card);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public InvitationAdapter(List<InvtActivities> myList, Context context) {
        this.myList = myList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_invitation, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(myList.get(position).getNameActivities());
        holder.admin.setText(myList.get(position).getCreatedBy());
        holder.date.setText(myList.get(position).getDate());
        if (myList.get(position).getPicture() != null) {
            Glide.with(holder.itemView.getContext()).load(ApiClient.BASE_URL+"uploads/"+myList.get
                    (position).getPicture())
                    .into(holder.image);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }
}