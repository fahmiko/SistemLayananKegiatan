package com.ta.slk.sistemlayanankegiatan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ta.slk.sistemlayanankegiatan.Adapter.MembersAdapter;
import com.ta.slk.sistemlayanankegiatan.Model.GetUsers;
import com.ta.slk.sistemlayanankegiatan.Model.Users;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiGroups;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailGroups extends AppCompatActivity {
    TextView description, title;
    CollapsingToolbarLayout toolbarLayout;
    RecyclerView recyclerView;
    ApiGroups apiGroups;
    List<Users> usersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        getSupportActionBar().show();

        apiGroups = ApiClient.getClient().create(ApiGroups.class);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        description = findViewById(R.id.text_description);
        description.setText(getIntent().getStringExtra("description"));
        recyclerView = findViewById(R.id.recycler_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void loadData() {
        Call<GetUsers> call = apiGroups.getMemberGroup(getIntent().getStringExtra("id_group"));
        call.enqueue(new Callback<GetUsers>() {
            @Override
            public void onResponse(Call<GetUsers> call, Response<GetUsers> response) {
                usersList = response.body().getResult();
                RecyclerView.Adapter adapter = new MembersAdapter(usersList,getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<GetUsers> call, Throwable t) {

            }
        });
    }
}
