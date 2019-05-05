package com.ta.slk.sistemlayanankegiatan;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ta.slk.sistemlayanankegiatan.Activity.LoginActivity;
import com.ta.slk.sistemlayanankegiatan.Method.Application;
import com.ta.slk.sistemlayanankegiatan.Method.Session;
import com.ta.slk.sistemlayanankegiatan.Model.*;
import com.ta.slk.sistemlayanankegiatan.Rest.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Menu{
    Session session;
    ApiMembers service;
    TextView invitation, activities, documentation,groups;
    String[] dataServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getMenu();
        initComponents();
        getData();
    }

    private void getData() {
        Call<PostData> call = service.description();
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if(response.code()==200){
                    if(response.body().getStatus().equals("success")){
                        dataServer = response.body().getMessage().split("_");
                        invitation.setText(dataServer[0]+" Undangan belum diterima");
                        activities.setText(dataServer[1]+" Kegiatan");
                        documentation.setText(dataServer[2]+" upload dokumentasi");
                        groups.setText(dataServer[3]+" Group");
                    }
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {

            }
        });
    }

    private void initComponents() {
        invitation = findViewById(R.id.main_inv);
        activities = findViewById(R.id.main_act);
        groups     = findViewById(R.id.main_groups);
        documentation =findViewById(R.id.main_docs);
        service = ApiClient.getClient().create(ApiMembers.class);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
