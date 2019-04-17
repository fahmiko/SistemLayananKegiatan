package com.ta.slk.sistemlayanankegiatan.Adapter;
import com.bumptech.glide.Glide;
import com.ta.slk.sistemlayanankegiatan.Model.*;
import android.content.Context;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder> {
    private Context context;
    private List<Users> myList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView nip,name;
        private CircleImageView image;
        public MyViewHolder(View v) {
            super(v);
            nip = itemView.findViewById(R.id.nip_member);
            name = itemView.findViewById(R.id.name_member);
            image = itemView.findViewById(R.id.img_member);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MembersAdapter(List<Users> myList, Context context) {
        this.myList = myList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_members, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.nip.setText(myList.get(position).getIdEmployee());
        holder.name.setText(myList.get(position).getName());
        if (myList.get(position).getPhotoProfile() != null) {
            Glide.with(holder.itemView.getContext()).load(ApiClient.BASE_URL+"uploads/"+myList.get
                    (position).getPhotoProfile())
                    .into(holder.image);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }
}
