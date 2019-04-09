package com.ta.slk.sistemlayanankegiatan;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.ta.slk.sistemlayanankegiatan.Model.*;
import com.ta.slk.sistemlayanankegiatan.Rest.*;
import com.ta.slk.sistemlayanankegiatan.Adapter.*;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
        final Context mContext = getApplicationContext();
//        mRecyclerView = findViewById(R.id.recycler_activities);
//        mLayoutManager = new LinearLayoutManager(mContext);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        LoadData();
        internet_permission();
    }



    private void LoadData(){
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetActivities> mActivitiesCall = mApiInterface.getActivities();
        mActivitiesCall.enqueue(new Callback<GetActivities>() {
            @Override
            public void onResponse(Call<GetActivities> call, Response<GetActivities> response) {
                Log.d("Get Server",response.body().getStatus());
                listActivities = response.body().getResult();
                mAdapter = new ActivitiesAdapter(listActivities,getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
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
