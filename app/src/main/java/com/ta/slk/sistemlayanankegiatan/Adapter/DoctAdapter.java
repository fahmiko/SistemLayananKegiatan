package com.ta.slk.sistemlayanankegiatan.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ta.slk.sistemlayanankegiatan.Fragments.DocumentationFragment;
import com.ta.slk.sistemlayanankegiatan.Model.Documentation;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;

import java.util.List;

public class DoctAdapter extends RecyclerView.Adapter<DoctAdapter.MyViewHolder> {
    private Context context;
    private List<Documentation> documentationList;

    public DoctAdapter(Context context, List<Documentation> documentationList) {
        this.context =context;
        this.documentationList = documentationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_documentation, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (documentationList.get(position).getPicture() != "") {
            Glide.with(holder.itemView.getContext()).load(ApiClient.BASE_URL+"uploads/documentation/"+documentationList.get
                    (position).getPicture())
                    .into(holder.imageDoc);
        }

        holder.imageDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(context,R.style.ZoomImageDialog);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.zoom_image);
                ImageView imageView = dialog.findViewById(R.id.zoom_image);
                try {
                    Glide.with(v.getContext()).load(ApiClient.BASE_URL+"uploads/documentation/"+documentationList.get
                            (position).getPicture())
                            .into(imageView);
                }catch (Exception e){

                }

                dialog.setCancelable(true);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageDoc;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageDoc = itemView.findViewById(R.id.image_doc);
        }
    }
}
