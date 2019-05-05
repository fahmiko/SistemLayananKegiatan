package com.ta.slk.sistemlayanankegiatan.Adapter;
import com.bumptech.glide.Glide;
import com.ta.slk.sistemlayanankegiatan.Activity.UserActivity;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder> implements Filterable{
    private Context context;
    private List<Users> myList;
    private List<Users> myListFiltered;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView nip,name;

        //        private ImageView image;
        public MyViewHolder(View v) {
            super(v);
            nip = itemView.findViewById(R.id.phone);
            name = itemView.findViewById(R.id.name);
//            image = itemView.findViewById(R.id.thumbnail);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    listener.onMemberSelected(myListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public MembersAdapter(List<Users> myList, Context context) {
        this.myList = myList;
        this.context = context;
        this.myListFiltered = myList;
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
        final Users users = myListFiltered.get(position);
        holder.nip.setText(users.getIdEmployee());
        holder.name.setText(users.getName());
//        if (myList.get(position).getPhotoProfile() != null) {
//            Glide.with(holder.itemView.getContext()).load(ApiClient.BASE_URL+"uploads/"+myList.get
//                    (position).getPhotoProfile())
//                    .into(holder.image);
//        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()){
                    myListFiltered = myList;
                }else{
                    List<Users> filteredList = new ArrayList<>();
                    for (Users row : myList){
                        if(row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getIdEmployee().contains(charString)){
                            filteredList.add(row);
                        }
                    }
                    myListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = myListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                myListFiltered = (ArrayList<Users>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<Users> getUsersFiltered(){
        return myListFiltered;
    }
}
