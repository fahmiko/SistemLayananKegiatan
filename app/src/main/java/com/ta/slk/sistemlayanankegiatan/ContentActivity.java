package com.ta.slk.sistemlayanankegiatan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.ta.slk.sistemlayanankegiatan.Adapter.*;
import com.ta.slk.sistemlayanankegiatan.Model.*;
import com.ta.slk.sistemlayanankegiatan.Rest.*;
import com.ta.slk.sistemlayanankegiatan.Method.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ContentActivity extends AppCompatActivity{
    // Variabel untuk recyclerview
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout refreshLayout;
    FloatingActionButton floatingActionButton;
    MultiSelectDialog multiSelectDialog;
    // Variabel untuk menampung hasil intent
    String user_level;
    String request;
    // List variabel
    List<Groups> listGroups;
    List<InvtActivities> listInvitation;
    List<Activities> listActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        user_level = getIntent().getStringExtra("user_level");
        request = getIntent().getStringExtra("action");

        instanceComponents();
        loadData(request);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(request);
            }
        });
    }



    private void loadData(String action) {
        if (action.equals("groups")) {
            showDataGroupsById();
        }else if(action.equals("invitation")){
            showDataInvitationById();
        }else if(action.equals("my_activities")){
            showDataActivitiesById();
        }
    }

    private void instanceComponents(){
        refreshLayout = findViewById(R.id.swipe_refresh);

//        floatingActionButton.setVisibility(View.INVISIBLE);

        getSupportActionBar().setTitle(request);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().show();

        mRecyclerView = findViewById(R.id.recycler_content);
        if(this.request.equals("groups")){
            mLayoutManager = new GridLayoutManager(this,3);
        }else{
            mLayoutManager = new LinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void showDataInvitationById(){
        ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetInvtActivities> minvitCall =  mApiInterface.getInvitationActivity("1","invitation");

        minvitCall.enqueue(new Callback<GetInvtActivities>() {
            @Override
            public void onResponse(Call<GetInvtActivities> call, Response<GetInvtActivities> response) {
                listInvitation = response.body().getResult();
                mAdapter = new InvitationAdapter(listInvitation,getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetInvtActivities> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Cek koneksi Internet",Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showDataActivitiesById(){
        ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetActivities> mCall =  mApiInterface.getActiviesById("1","my_activity");

        mCall.enqueue(new Callback<GetActivities>() {
            @Override
            public void onResponse(Call<GetActivities> call, Response<GetActivities> response) {
                listActivities = response.body().getResult();
                mAdapter = new ActivitiesAdapter(listActivities, getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetActivities> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Cek koneksi Internet",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDataGroupsById(){
        ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetGroups> mGroupCall =  mApiInterface.getGroupsById("1","groups");

        mGroupCall.enqueue(new Callback<GetGroups>() {
            @Override
            public void onResponse(Call<GetGroups> call, Response<GetGroups> response) {
                listGroups = response.body().getResult();
                mAdapter = new GroupsAdapter(listGroups, getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetGroups> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Cek koneksi Internet",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
