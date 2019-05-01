package com.ta.slk.sistemlayanankegiatan.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import com.ta.slk.sistemlayanankegiatan.Adapter.*;
import com.ta.slk.sistemlayanankegiatan.AdminContent;
import com.ta.slk.sistemlayanankegiatan.Method.ClickListenner;
import com.ta.slk.sistemlayanankegiatan.Method.RecyclerTouchListener;
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
public class MembersFragment extends Fragment{
    SwipeRefreshLayout refreshLayout;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ProgressBar progressBar;
    Fragment fragment;
    List<Users> listUsers;

    private SearchView searchView;
    String s = null;

    public MembersFragment(){
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
        fragment = this;

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(view);
                refreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, new ClickListenner() {
            @Override
            public void onClick(View v, final int position) {
                CharSequence[] sequence = {"Ganti Role","Hapus Member"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Opsi Member");
                builder.setItems(sequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                if(!listUsers.get(position).getLevel().isEmpty()){
                                    showSwitch(listUsers.get(position).getIdMember(),listUsers.get(position).getLevel());
                                }else{
                                    Toast.makeText(getContext(),"User Belum Mendaftar",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                doDelete(listUsers.get(position).getIdMember());
                                break;

                        }
                    }
                }).create().show();
            }

            @Override
            public void onLongClick(View v, int position) {

            }
        }));

        loadData(view);
        return view;
    }

    private void showSwitch(final String idMember, final String level) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final Switch aSwitch = new Switch(getContext());
        aSwitch.setTextOn("1");
        aSwitch.setTextOff("2");
        if(level.equals("1")){
            aSwitch.setChecked(true);
        }
        builder.setTitle("Ganti Status ").setMessage("Akun Admin");
        builder.setView(aSwitch);
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String level = null;
                if(aSwitch.isChecked()){
                    level = "1";
                }else{
                    level = "2";
                }
                ApiMembers members = ApiClient.getClient().create(ApiMembers.class);
                Call<PostData> call = members.changeRule(idMember,level);
                call.enqueue(new Callback<PostData>() {
                    @Override
                    public void onResponse(Call<PostData> call, Response<PostData> response) {
                        if(response.code()==200){
                            if(response.body().getStatus().equals("success")){
                                Toast.makeText(getContext(),"sukes update role",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PostData> call, Throwable t) {
                        Toast.makeText(getContext(),"Cek koneksi Internet",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.create().show();
    }

    private void doDelete(String idUser) {
        ApiMembers members = ApiClient.getClient().create(ApiMembers.class);
        Call<PostData> call = members.deleteNip(idUser);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if(response.code()==200){
                    if(response.body().getStatus().equals("success")){
                        Toast.makeText(getContext(),"Sukses hapus member",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(getContext(),"Cek koneksi Internet",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData(final View view){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetUsers> usersCall = apiInterface.getUsers();
        usersCall.enqueue(new Callback<GetUsers>() {
            @Override
            public void onResponse(Call<GetUsers> call, Response<GetUsers> response) {
                if(response.code() == 200){
                    listUsers = response.body().getResult();
                    mRecyclerView.setAdapter(new MembersAdapter(listUsers,getContext()));
                    mRecyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL,36));
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
