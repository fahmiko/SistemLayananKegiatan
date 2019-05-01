package com.ta.slk.sistemlayanankegiatan.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ta.slk.sistemlayanankegiatan.Adapter.ActivitiesAdapter;
import com.ta.slk.sistemlayanankegiatan.Model.Activities;
import com.ta.slk.sistemlayanankegiatan.Model.GetActivities;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {
    ApiInterface service;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Activities> activitiesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activities);
        recyclerView = findViewById(R.id.recycler_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        service = ApiClient.getClient().create(ApiInterface.class);
        loadData();
    }

    private void loadData() {
        Call<GetActivities> call = service.getActiviesById("0","my_activity");
        call.enqueue(new Callback<GetActivities>() {
            @Override
            public void onResponse(Call<GetActivities> call, Response<GetActivities> response) {
                if(response.code()==200){
                    if(response.body().getStatus().equals("success")){
                        activitiesList = response.body().getResult();
                        adapter = new ActivitiesAdapter(activitiesList,getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetActivities> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Cek Koneksi Interner",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
