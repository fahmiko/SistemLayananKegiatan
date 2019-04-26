package com.ta.slk.sistemlayanankegiatan.Fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.ta.slk.sistemlayanankegiatan.Adapter.*;
import com.ta.slk.sistemlayanankegiatan.Model.*;
import com.ta.slk.sistemlayanankegiatan.Rest.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import com.ta.slk.sistemlayanankegiatan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersFragment extends Fragment {
    SwipeRefreshLayout refreshLayout;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ProgressBar progressBar;
    List<Users> listUsers;

    public MembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_members, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_content);
        progressBar = view.findViewById(R.id.progress_bar);
        refreshLayout = view.findViewById(R.id.swipe_refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(view);
                refreshLayout.setRefreshing(false);
            }
        });


        loadData(view);

        return view;
    }

    private void loadData(final View view){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetUsers> usersCall = apiInterface.getUsers();
        usersCall.enqueue(new Callback<GetUsers>() {
            @Override
            public void onResponse(Call<GetUsers> call, Response<GetUsers> response) {
                if(response.code() == 200){
                    listUsers = response.body().getResult();
                    mAdapter =  new MembersAdapter(listUsers, view.getContext());
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.scheduleLayoutAnimation();
                    progressBar.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(getView(),"NO DATA",Snackbar.LENGTH_LONG).setAction("retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.VISIBLE);
                            loadData(view);
                        }
                    }).show();
                }
            }

            @Override
            public void onFailure(Call<GetUsers> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(getView(),"Cek koneksi Internet",Snackbar.LENGTH_LONG).setAction("retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        loadData(view);
                    }
                }).show();
            }
        });
    }
}
