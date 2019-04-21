package com.ta.slk.sistemlayanankegiatan.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ta.slk.sistemlayanankegiatan.Adapter.InvitationAdapter;
import com.ta.slk.sistemlayanankegiatan.Method.ClickListenner;
import com.ta.slk.sistemlayanankegiatan.Method.RecyclerTouchListener;
import com.ta.slk.sistemlayanankegiatan.Model.GetInvtActivities;
import com.ta.slk.sistemlayanankegiatan.Model.InvtActivities;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInvitationAccept extends Fragment {
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<InvtActivities> activitiesList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_invitation,container,false);
        recyclerView = view.findViewById(R.id.recycler_content);
        progressBar = view.findViewById(R.id.progress_bar);
        refreshLayout = view.findViewById(R.id.swipe_refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(view);
            }
        });


        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        refreshData(view);
        return view;
    }

    private void refreshData(final View view){
        SharedPreferences sf = view.getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        ApiInterface apiClient = ApiClient.getClient().create(ApiInterface.class);
        Call<GetInvtActivities> call = apiClient.getActivityStatus(sf.getString("id_member",""),"join");
        call.enqueue(new Callback<GetInvtActivities>() {
            @Override
            public void onResponse(Call<GetInvtActivities> call, Response<GetInvtActivities> response) {
                activitiesList = response.body().getResult();
                adapter = new InvitationAdapter(activitiesList,view.getContext());
                recyclerView.setAdapter(adapter);
                refreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetInvtActivities> call, Throwable t) {
                Snackbar.make(view,"Cek Koneksi Internet",Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
