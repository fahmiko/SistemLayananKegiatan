package com.ta.slk.sistemlayanankegiatan.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ta.slk.sistemlayanankegiatan.Adapter.MembersAdapter;
import com.ta.slk.sistemlayanankegiatan.Model.GetUsers;
import com.ta.slk.sistemlayanankegiatan.Model.PostData;
import com.ta.slk.sistemlayanankegiatan.Model.Users;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiGroups;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiMembers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {
    TextView detail;
    Bundle bundle;
    RecyclerView recyclerView,recyclerView2;
    RecyclerView.Adapter adapter,adapter2;
    List<Users> usersList,usersList2;
    ApiMembers service;
    public DetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view =  inflater.inflate(R.layout.fragment_detail,container,false);
        detail = view.findViewById(R.id.dt_detail);
        recyclerView = view.findViewById(R.id.recycler_member);
        recyclerView2 = view.findViewById(R.id.recycler_pending);
        bundle = getArguments();
        detail.setText(bundle.getString("description"));
        service = ApiClient.getClient().create(ApiMembers.class);

        loadDataMembers();
        loadDataPending();
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
}
