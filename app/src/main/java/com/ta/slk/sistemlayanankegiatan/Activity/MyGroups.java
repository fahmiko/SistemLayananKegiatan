package com.ta.slk.sistemlayanankegiatan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ta.slk.sistemlayanankegiatan.Adapter.GroupsAdapter;
import com.ta.slk.sistemlayanankegiatan.DetailGroups;
import com.ta.slk.sistemlayanankegiatan.Method.ClickListenner;
import com.ta.slk.sistemlayanankegiatan.Method.RecyclerTouchListener;
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
    Toolbar toolbar;
    ApiInterface service;
    List<Groups> groupsList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_group);
        toolbar = findViewById(R.id.user_toolbar);
        refreshLayout = findViewById(R.id.swipe_refresh);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().show();

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
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListenner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailGroups.class);
                intent.putExtra("id_group",groupsList.get(position).getIdGroup());
                intent.putExtra("admin",groupsList.get(position).getCreatedBy());
                intent.putExtra("name",groupsList.get(position).getName());
                intent.putExtra("description",groupsList.get(position).getDescription());
                intent.putExtra("picture",groupsList.get(position).getPhotoGroup());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View v, int position) {

            }
        }));

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
