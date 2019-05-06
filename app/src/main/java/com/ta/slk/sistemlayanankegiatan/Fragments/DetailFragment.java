package com.ta.slk.sistemlayanankegiatan.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ta.slk.sistemlayanankegiatan.Activity.UserActivity;
import com.ta.slk.sistemlayanankegiatan.Adapter.MembersAdapter;
import com.ta.slk.sistemlayanankegiatan.DetailActivity;
import com.ta.slk.sistemlayanankegiatan.Method.Application;
import com.ta.slk.sistemlayanankegiatan.Method.ClickListenner;
import com.ta.slk.sistemlayanankegiatan.Method.RecyclerTouchListener;
import com.ta.slk.sistemlayanankegiatan.Method.Session;
import com.ta.slk.sistemlayanankegiatan.Model.GetUsers;
import com.ta.slk.sistemlayanankegiatan.Model.PostData;
import com.ta.slk.sistemlayanankegiatan.Model.Users;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiGroups;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiInterface;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiMembers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {
    TextView detail;
    Bundle bundle;
    ImageButton place;
    CardView cardOption;
    Button exit;
    Session session;
    RecyclerView recyclerView,recyclerView2;
    RecyclerView.Adapter adapter,adapter2;
    List<Users> usersList,usersList2;
    ApiMembers service;
    public DetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detail,container,false);
        detail = view.findViewById(R.id.dt_detail);
        place = view.findViewById(R.id.btn_place);
        recyclerView = view.findViewById(R.id.recycler_member);
        recyclerView2 = view.findViewById(R.id.recycler_pending);
        cardOption = view.findViewById(R.id.card_option);
        exit = view.findViewById(R.id.action_activity);
        session = Application.getSession();
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        bundle = getArguments();
        detail.setText(bundle.getString("description"));
        service = ApiClient.getClient().create(ApiMembers.class);
        loadDataMembers();
        loadDataPending();
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+bundle.getString("place"));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListenner() {
            @Override
            public void onClick(View v, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Pesan").setMessage(usersList.get(position).getMessage());
                builder.setIcon(R.drawable.round_announcement_black_18dp).create().show();
            }

            @Override
            public void onLongClick(View v, int position) {

            }
        }));
        if(session.isAdmin()){
            cardOption.setVisibility(View.GONE);
        }
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Peringatan").setMessage("Yaking ingin keluar dari kegiatan ? ").setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getMessageDialog("rejected",bundle.getString("id_activity"), v);
                        dialog.dismiss();
                    }
                }).setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        return view;
    }

    private void loadDataPending() {
        Call<GetUsers> call = service.getUserActivities(bundle.getString("id_activity", null),"pending");
        call.enqueue(new Callback<GetUsers>() {
            @Override
            public void onResponse(Call<GetUsers> call, Response<GetUsers> response) {
                if(response.code()==200){
                    if(response.body().getResult().size()!=0){
                        usersList2 = response.body().getResult();
                        adapter2 = new MembersAdapter(usersList2,getContext());
                        recyclerView2.setAdapter(adapter2);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUsers> call, Throwable t) {

            }
        });
    }

    private void loadDataMembers() {
        Call<GetUsers> call = service.getUserActivities(bundle.getString("id_activity",null),"join");
        call.enqueue(new Callback<GetUsers>() {
            @Override
            public void onResponse(Call<GetUsers> call, Response<GetUsers> response) {
                if(response.code()==200){
                    if(response.body().getStatus().equals("success")){
                        usersList = response.body().getResult();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter = new MembersAdapter(usersList,getContext());
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUsers> call, Throwable t) {

            }
        });
    }
    private void confirmInvitation(String action, String id_activity, String message){
        SharedPreferences sf = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PostData> call = apiInterface.putInvitationStatus(id_activity,sf.getString("id_member",""),action, message);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if(response.code()==200){
                    DetailActivity.detail.finish();
                    UserActivity.userActivity.loadData();
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Snackbar.make(getView(), "Cek Koneksi Internet",Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    private void getMessageDialog(final String action, final String id_activity,final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Kirim Pesan");
        final EditText input = new EditText(view.getContext());
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
