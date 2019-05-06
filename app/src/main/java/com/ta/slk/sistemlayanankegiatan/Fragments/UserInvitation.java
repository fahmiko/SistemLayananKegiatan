package com.ta.slk.sistemlayanankegiatan.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ta.slk.sistemlayanankegiatan.Adapter.InvitationAdapter;
import com.ta.slk.sistemlayanankegiatan.Method.ClickListenner;
import com.ta.slk.sistemlayanankegiatan.Method.RecyclerTouchListener;
import com.ta.slk.sistemlayanankegiatan.Model.GetInvtActivities;
import com.ta.slk.sistemlayanankegiatan.Model.InvtActivities;
import com.ta.slk.sistemlayanankegiatan.Model.PostData;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiInterface;

import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInvitation extends Fragment {
    SwipeRefreshLayout refreshLayout;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<InvtActivities> activitiesList;

    public UserInvitation(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_invitation,container,false);

        recyclerView = view.findViewById(R.id.recycler_content);
        refreshLayout = view.findViewById(R.id.swipe_refresh);
        progressBar = view.findViewById(R.id.progress_bar);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(view);
            }
        });

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        refreshData(view);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(view.getContext(), recyclerView, new ClickListenner() {
            @Override
            public void onClick(View v, int position) {
                showDialog(activitiesList.get(position).getIdActivity(),activitiesList.get(position).getNameActivities());
            }

            @Override
            public void onLongClick(View v, int position) {

            }
        }));
        return view;
    }

    private void refreshData(final View view){
        SharedPreferences sf = view.getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        ApiInterface apiClient = ApiClient.getClient().create(ApiInterface.class);
        Call<GetInvtActivities> call = apiClient.getActivityStatus(sf.getString("id_member",""),"pending");
        call.enqueue(new Callback<GetInvtActivities>() {
            @Override
            public void onResponse(Call<GetInvtActivities> call, Response<GetInvtActivities> response) {
                if(response.code()==200){
                    activitiesList = response.body().getResult();
                    adapter = new InvitationAdapter(activitiesList,view.getContext());
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<GetInvtActivities> call, Throwable t) {
                Snackbar.make(view,"Cek Koneksi Internet",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(final String id_activity, String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Terima undangan "+name+" ?")
                .setPositiveButton("Terima", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getMessageDialog("join",id_activity);
                    }
                })
                .setNegativeButton("Tolak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getMessageDialog("rejected",id_activity);
//                        confirmInvitation(view,"rejected",id_activity,"");y
                    }
                });
        // Create the AlertDialog object and return it
            builder.show();
        }
    private void confirmInvitation(String action, String id_activity, String message){
        SharedPreferences sf = getContext().getSharedPreferences("login",Context.MODE_PRIVATE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PostData> call = apiInterface.putInvitationStatus(id_activity,sf.getString("id_member",""),action, message);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                refreshData(getView());
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Snackbar.make(getView(), "Cek Koneksi Internet",Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    private void getMessageDialog(final String action, final String id_activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Kirim Pesan");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmInvitation(action,id_activity,input.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
