package com.ta.slk.sistemlayanankegiatan.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ta.slk.sistemlayanankegiatan.Adapter.GroupsAdapter;
import com.ta.slk.sistemlayanankegiatan.Model.GetGroups;
import com.ta.slk.sistemlayanankegiatan.Model.Groups;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiInterface;

import java.security.acl.Group;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyGroups extends AppCompatActivity{
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;
    ApiInterface service;
    List<Groups> groupsList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_groups);
        refreshLayout = findViewById(R.id.swipe_refresh);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_content);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        service = ApiClient.getClient().create(ApiInterface.class);
        loadData();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        initComponents();
    }

    private void initComponents() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().show();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        Call<GetGroups> call = service.getGroupsById("","groups");
        call.enqueue(new Callback<GetGroups>() {
            @Override
            public void onResponse(Call<GetGroups> call, Response<GetGroups> response) {
                if(response.code()==200){
                    if(response.body().getResult().size()!=0){
                        refreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        groupsList = response.body().getResult();
                        adapter = new GroupsAdapter(groupsList,getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        refreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "NO DATA",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetGroups> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(),"Cek Koneksi Internet",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
