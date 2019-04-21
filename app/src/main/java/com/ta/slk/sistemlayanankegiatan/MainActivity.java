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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getMenu();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:YY | HH:mm");
//        String currentDateandTime = sdf.format(new Date());
//        Toast.makeText(getApplicationContext(),"DateTime "+currentDateandTime,Toast.LENGTH_LONG).show();

        SharedPreferences sf = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        if(sf.getString("id_member","").equals("")){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

//        final Context mContext = getApplicationContext();
//        mRecyclerView = findViewById(R.id.recycler_activities);
//        mLayoutManager = new LinearLayoutManager(mContext);
//        mRecyclerView.setLayoutManager(mLayoutManager);
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
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 0;
    private void internet_permission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_CONTACTS_PERMISSIONS_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    // Callback yang membawa hasil dari pemanggilan requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
