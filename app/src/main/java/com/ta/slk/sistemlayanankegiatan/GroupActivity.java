package com.ta.slk.sistemlayanankegiatan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ta.slk.sistemlayanankegiatan.Adapter.*;
import com.ta.slk.sistemlayanankegiatan.Model.*;
import com.ta.slk.sistemlayanankegiatan.Rest.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class GroupActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private List<Groups> listGroups;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        getSupportActionBar().setTitle("Grup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().show();

        mRecyclerView = findViewById(R.id.recycler_groups);
        mLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        LoadGroup();
    }

    private void LoadGroup(){
        ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetGroups> mGroupCall =  mApiInterface.getGroupsById("1","groups");

        mGroupCall.enqueue(new Callback<GetGroups>() {
            @Override
            public void onResponse(Call<GetGroups> call, Response<GetGroups> response) {
                listGroups = response.body().getResult();
                mAdapter = new GroupsAdapter(listGroups,getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetGroups> call, Throwable t) {

            }
        });
    }
}
