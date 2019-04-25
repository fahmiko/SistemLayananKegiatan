package com.ta.slk.sistemlayanankegiatan.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ta.slk.sistemlayanankegiatan.Adapter.DoctAdapter;
import com.ta.slk.sistemlayanankegiatan.Method.ClickListenner;
import com.ta.slk.sistemlayanankegiatan.Method.RecyclerTouchListener;
import com.ta.slk.sistemlayanankegiatan.Model.Documentation;
import com.ta.slk.sistemlayanankegiatan.Model.GetDocumentation;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiDocumentation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentationFragment extends Fragment{
    ProgressBar progressBar;
    Bundle bundle;

    RecyclerView recyclerView;
    ApiDocumentation service;
    List<Documentation> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_documentation,container,false);
        recyclerView = view.findViewById(R.id.recycler_content);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        service = ApiClient.getClient().create(ApiDocumentation.class);
        bundle = getArguments();
        loadData();

        return view;
    }

    private void loadData() {
        service.getDocumentation(bundle.get("id_activity").toString()).enqueue(new Callback<GetDocumentation>() {
            @Override
            public void onResponse(Call<GetDocumentation> call, Response<GetDocumentation> response) {
                if(response.code()==200){
                    progressBar.setVisibility(View.GONE);
                    list = response.body().getResult();
                    if(list.size()==0){
                        Snackbar.make(getView(),"DATA TIDAK DITEMUKAN",Snackbar.LENGTH_LONG).show();
                    }else{
                        recyclerView.setAdapter(new DoctAdapter(getContext(),list));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDocumentation> call, Throwable t) {
                Snackbar.make(getView(),"Cek Koneksi Internet ",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        loadData();
                    }
                }).show();
            }
        });
    }
}
