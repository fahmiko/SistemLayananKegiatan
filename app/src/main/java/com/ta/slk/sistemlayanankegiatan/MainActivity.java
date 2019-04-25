package com.ta.slk.sistemlayanankegiatan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ta.slk.sistemlayanankegiatan.Method.Preferences;
import com.ta.slk.sistemlayanankegiatan.Model.*;
import com.ta.slk.sistemlayanankegiatan.Rest.*;
import com.ta.slk.sistemlayanankegiatan.Adapter.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Menu{
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private List<Activities> listActivities;
    ApiInterface mApiInterface;
    private static final int READ_STORAGE_PERMISSIONS_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getMenu();
        /*
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:YY | HH:mm");
        String currentDateandTime = sdf.format(new Date());
        Toast.makeText(getApplicationContext(),"DateTime "+currentDateandTime,Toast.LENGTH_LONG).show();
        */

        SharedPreferences sf = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        if(sf.getString("id_member","").equals("")){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        galleryPermition();

        LoadData();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private void LoadData(){
        SharedPreferences sf = this.getSharedPreferences("login",MODE_PRIVATE);
        mApiInterface = ApiClient.getServer(sf.getString("token","")).create(ApiInterface.class);
        Call<GetActivities> mActivitiesCall = mApiInterface.getActivities();
        mActivitiesCall.enqueue(new Callback<GetActivities>() {
            @Override
            public void onResponse(Call<GetActivities> call, Response<GetActivities> response) {
                Log.d("msg", "onResponse: "+response.message());
                if(response.message().equals("Unauthorized")){
                        finish();
                        Preferences preferences = new Preferences(getApplicationContext());
                        preferences.logout(0);
                }
            }

            @Override
            public void onFailure(Call<GetActivities> call, Throwable t) {
                Log.e("Get Server",t.getMessage());
            }
        });
    }

    private void galleryPermition(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_STORAGE_PERMISSIONS_REQUEST);
            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_STORAGE_PERMISSIONS_REQUEST: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }

        }
    }

}
