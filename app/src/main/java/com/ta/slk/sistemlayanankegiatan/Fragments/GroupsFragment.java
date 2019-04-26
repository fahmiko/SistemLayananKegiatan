package com.ta.slk.sistemlayanankegiatan.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.ValueIterator;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ta.slk.sistemlayanankegiatan.Adapter.GroupsAdapter;
import com.ta.slk.sistemlayanankegiatan.DetailGroups;
import com.ta.slk.sistemlayanankegiatan.Method.ClickListenner;
import com.ta.slk.sistemlayanankegiatan.Method.RecyclerTouchListener;
import com.ta.slk.sistemlayanankegiatan.Model.GetGroups;
import com.ta.slk.sistemlayanankegiatan.Model.Groups;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiInterface;

import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    ProgressBar progressBar;
    RecyclerView.Adapter adapter;
    List<Groups> groupsList;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups,container,false);
        recyclerView = view.findViewById(R.id.recycler_content);
        refreshLayout = view.findViewById(R.id.swipe_refresh);
        progressBar = view.findViewById(R.id.progress_bar);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        loadData();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListenner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), DetailGroups.class);
                intent.putExtra("id_group",groupsList.get(position).getIdGroup());
                intent.putExtra("name",groupsList.get(position).getName());
                intent.putExtra("description",groupsList.get(position).getDescription());
                intent.putExtra("picture",groupsList.get(position).getPhotoGroup());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View v, int position) {

            }
        }));
        return view;
    }

    private void loadData(){
        progressBar.setVisibility(View.VISIBLE);
        refreshLayout.setRefreshing(true);
        ApiInterface apiClient = ApiClient.getClient().create(ApiInterface.class);
        Call<GetGroups> call = apiClient.getGroups();
        call.enqueue(new Callback<GetGroups>() {
            @Override
            public void onResponse(Call<GetGroups> call, Response<GetGroups> response) {
                if(response.code()==200){
                    groupsList = response.body().getResult();
                    adapter = new GroupsAdapter(groupsList,getContext());
                    recyclerView.scheduleLayoutAnimation();
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(getView(),"Cek koneksi Internet",Snackbar.LENGTH_LONG).setAction("retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.VISIBLE);
                            loadData();
                        }
                    }).show();
                }
            }

            @Override
            public void onFailure(Call<GetGroups> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                Snackbar.make(getView(), "Cek Koneksi Internet ",Snackbar.LENGTH_LONG).setAction("retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadData();
                    }
                }).show();
            }
        });
    }



}
