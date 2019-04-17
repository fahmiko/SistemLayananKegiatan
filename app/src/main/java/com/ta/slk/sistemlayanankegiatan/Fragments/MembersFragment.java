package com.ta.slk.sistemlayanankegiatan.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.ta.slk.sistemlayanankegiatan.Adapter.*;
import com.ta.slk.sistemlayanankegiatan.ContentActivity;
import com.ta.slk.sistemlayanankegiatan.MainActivity;
import com.ta.slk.sistemlayanankegiatan.Model.*;
import com.ta.slk.sistemlayanankegiatan.Rest.*;
import com.ta.slk.sistemlayanankegiatan.Method.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import com.ta.slk.sistemlayanankegiatan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersFragment extends Fragment {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
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
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadData(view);

        return view;
    }

    private void loadData(final View view){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetUsers> usersCall = apiInterface.getUsers();
        usersCall.enqueue(new Callback<GetUsers>() {
            @Override
            public void onResponse(Call<GetUsers> call, Response<GetUsers> response) {
                listUsers = response.body().getResult();
                mAdapter =  new MembersAdapter(listUsers, view.getContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetUsers> call, Throwable t) {

            }
        });
    }
}
