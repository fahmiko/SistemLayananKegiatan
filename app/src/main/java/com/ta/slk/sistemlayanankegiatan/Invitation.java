package com.ta.slk.sistemlayanankegiatan;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ta.slk.sistemlayanankegiatan.Model.*;
import com.ta.slk.sistemlayanankegiatan.Rest.*;
import com.ta.slk.sistemlayanankegiatan.Adapter.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class Invitation extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private List<InvtActivities> listInvitation;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        getSupportActionBar().setTitle("Invitation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().show();

        mRecyclerView = findViewById(R.id.recycler_invitation);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        LoadInvitation();
    }

    private void LoadInvitation(){
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

            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        return true;
    }
}
