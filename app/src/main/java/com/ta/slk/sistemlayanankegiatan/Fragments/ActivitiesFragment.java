package com.ta.slk.sistemlayanankegiatan.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.ta.slk.sistemlayanankegiatan.Adapter.*;
import com.ta.slk.sistemlayanankegiatan.AdminContent;
import com.ta.slk.sistemlayanankegiatan.DetailActivity;
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
public class ActivitiesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout refreshLayout;
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    MultiSelectDialog multiSelectDialog;
    View v;
    private String id_activity;

    List<Groups> listGroups;
    List<Activities> listActivities;
    List<Users> listUsers;

    public ActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_activities, container, false);
        this.v = view;
        mRecyclerView = view.findViewById(R.id.recycler_content);
        progressBar = view.findViewById(R.id.progress_bar);
        refreshLayout = view.findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(this);
        
//        refreshLayout.setColorSchemeResources(R.color.colorAccent,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
//        mLayoutManager = new LinearLayoutManager(view.getContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);

        loadData();
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, new ClickListenner() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                bundle.putString("id_activity",listActivities.get(position).getIdActivity());
                bundle.putString("comment_key",listActivities.get(position).getCommentKey());
                bundle.putString("name",listActivities.get(position).getNameActivities());
                bundle.putString("contact",listActivities.get(position).getContactPerson());
                bundle.putString("date",listActivities.get(position).getDate());
                bundle.putString("picture",listActivities.get(position).getPicture());
                bundle.putString("place",listActivities.get(position).getPlace());
                bundle.putString("description",listActivities.get(position).getDescription());
                intent.putExtra("activity",bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View v, final int position) {
                id_activity = listActivities.get(position).getIdActivity();
                final CharSequence[] dialogitem = {"Buka","Kirim Grup","Kirim Pribadi","Edit","Hapus"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Pilih Menu");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 1:
                                MultiSelectDialog dialog1 = multiSelectShow("groups");
                                dialog1.show(getFragmentManager(),"Testing");
                                break;
                            case 2:
                                MultiSelectDialog dialog2 = multiSelectShow("members");
                                dialog2.show(getFragmentManager(),"Testing");
                                break;
                            case 4:
                                deleteActivity(listActivities.get(position).getIdActivity());
                                break;
                        }
                    }
                }).create().show();
            }
        }));
        return view;
    }

    private void deleteActivity(final String idActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Konfirmasi").setMessage("Hapus kegiatan akan menghapus semua rekam jejak kegiatan");
        builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doDelete(idActivity);
            }
        }).show();
    }

    private void doDelete(String idActivity) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<PostData> call = service.deleteActivities(idActivity);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if(response.code()==200){
                    if(response.body().getStatus().equals("success")){
                        Toast.makeText(getContext(),"Berhasil di hapus",Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {

            }
        });
    }

    private void loadData(){
        ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetActivities> mGetActivity = mApiInterface.getActivities();
        mGetActivity.enqueue(new Callback<GetActivities>() {
            @Override
            public void onResponse(Call<GetActivities> call, Response<GetActivities> response) {
                if(response.code()==200){
                    listActivities = response.body().getResult();
                    mAdapter = new ActivitiesAdapter(listActivities, getContext());
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.scheduleLayoutAnimation();
                    progressBar.setVisibility(View.GONE);
                    getDataGroups();
                    getDataUsers();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(getView(),"NO DATA",Snackbar.LENGTH_LONG).setAction("retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.VISIBLE);
                            loadData();
                        }
                    }).show();
                }
            }

            @Override
            public void onFailure(Call<GetActivities> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(v,"Cek koneksi Internet",Snackbar.LENGTH_LONG).setAction("retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        loadData();
                    }
                }).show();
            }
        });
    }

    private MultiSelectDialog multiSelectShow(final String table){
        ArrayList<MultiSelectModel> listOfSelect= new ArrayList<>();
        if(table.equals("groups")){
            for(int i = 0; i < listGroups.size(); i++){
                int idGroups = Integer.parseInt(listGroups.get(i).getIdGroup());
                String nameGroups = listGroups.get(i).getName();
                listOfSelect.add(new MultiSelectModel(idGroups,nameGroups));
            }
        }else if (table.equals("members")){
            for(int i=0 ;i < listUsers.size();i++){
                int idEmployee = Integer.parseInt(listUsers.get(i).getIdMember());
                String nameEmployee = listUsers.get(i).getName();
                listOfSelect.add(new MultiSelectModel(idEmployee,nameEmployee));
            }
        }

        multiSelectDialog = new MultiSelectDialog()
                .title("Pilih") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .setMaxSelectionLimit(listOfSelect.size()) //you can set maximum checkbox selection limit (Optional)
                .multiSelectList(listOfSelect) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> arrayList, ArrayList<String> arrayList1, String s) {
                        if(table.equals("groups")){
                            sendGroup(arrayList);
                        }else{
                            sendInvitation(arrayList,"groups");
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
        return multiSelectDialog;
    }

    private void getDataGroups(){
        ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetGroups> mGetGroups = mApiInterface.getGroups();
        mGetGroups.enqueue(new Callback<GetGroups>() {
            @Override
            public void onResponse(Call<GetGroups> call, Response<GetGroups> response) {
                if(response.code()==200){
                    listGroups = response.body().getResult();
                }
            }

            @Override
            public void onFailure(Call<GetGroups> call, Throwable t) {

            }
        });
    }

    private void getDataUsers(){
        ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetUsers> mGetUser = mApiInterface.getUsers();
        mGetUser.enqueue(new Callback<GetUsers>() {
            @Override
            public void onResponse(Call<GetUsers> call, Response<GetUsers> response) {
                if(response.code()==200){
                    listUsers = response.body().getResult();
                }
            }

            @Override
            public void onFailure(Call<GetUsers> call, Throwable t) {

            }
        });
    }

    private void sendInvitation(ArrayList<Integer> list, String action){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PostData> postDataCall = apiInterface.sendInvitation(list,id_activity,action);
        postDataCall.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if(response.code()==200){
                    if(response.body().getStatus().equals("success")){
                        Toast.makeText(getContext(),"Undangan Berhasil dikirimkan",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(getContext(),"Cek koneksi interner",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendGroup(ArrayList<Integer> list){
        ApiGroups apiGroups = ApiClient.getClient().create(ApiGroups.class);
        Call<PostData> call = apiGroups.sendGroup(list,id_activity);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if(response.code()==200){
                    if(response.body().getStatus().equals("success")){
                        Toast.makeText(getContext(),"Undangan Berhasil dikirimkan",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(getContext(),"Cek koneksi internet",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
        refreshLayout.setRefreshing(false);
    }
}
