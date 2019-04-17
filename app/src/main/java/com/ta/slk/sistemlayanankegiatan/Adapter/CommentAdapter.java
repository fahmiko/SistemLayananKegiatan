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


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private Context context;
    private List<Comment> myList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView name,comment,date;
        private CircleImageView image;
        public MyViewHolder(View v) {
            super(v);
            comment = itemView.findViewById(R.id.comment_member);
            date = itemView.findViewById(R.id.date_time);
            name = itemView.findViewById(R.id.name_member);
            image = itemView.findViewById(R.id.img_member);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentAdapter(List<Comment> myList, Context context) {
        this.myList = myList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_comment, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.MyViewHolder holder, final int position) {
        holder.comment.setText(myList.get(position).getComment());
        holder.date.setText(myList.get(position).getDate());
        holder.name.setText(myList.get(position).getName());
        if (myList.get(position).getPhoto() != "") {
            Glide.with(holder.itemView.getContext()).load(ApiClient.BASE_URL+"uploads/"+myList.get
                    (position).getPhoto())
                    .into(holder.image);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }
}
