package com.ta.slk.sistemlayanankegiatan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ta.slk.sistemlayanankegiatan.Activity.MyGroups;
import com.ta.slk.sistemlayanankegiatan.Adapter.MembersAdapter;
import com.ta.slk.sistemlayanankegiatan.Method.Application;
import com.ta.slk.sistemlayanankegiatan.Method.Session;
import com.ta.slk.sistemlayanankegiatan.Model.GetUsers;
import com.ta.slk.sistemlayanankegiatan.Model.PostData;
import com.ta.slk.sistemlayanankegiatan.Model.Users;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiGroups;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailGroups extends AppCompatActivity {
    TextView description, title, admin;
    Button action;
    CircleImageView imgGroup;
    CardView cardOption;
    RecyclerView recyclerView;
    ImageButton back;
    Session session;
    ApiGroups apiGroups;
    List<Users> usersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_groups);

        apiGroups = ApiClient.getClient().create(ApiGroups.class);
        title = findViewById(R.id.group_title);
        admin = findViewById(R.id.group_admin);
        imgGroup  = findViewById(R.id.img_group);
        description = findViewById(R.id.text_description);
        action  = findViewById(R.id.action_group);
        back = findViewById(R.id.btn_back);
        cardOption = findViewById(R.id.card_option);
        session = Application.getSession();

        if(session.isAdmin()){
            cardOption.setVisibility(View.GONE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        admin.setText("Admin :"+getIntent().getStringExtra("admin"));
        title.setText(getIntent().getStringExtra("name"));
        description.setText(getIntent().getStringExtra("description"));
        recyclerView = findViewById(R.id.recycler_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Glide.with(getApplicationContext())
                .load(ApiClient.BASE_URL+"uploads/groups/"+getIntent().getStringExtra("picture"))
                .into(imgGroup);
        loadData();
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Peringatan").setMessage("Ingin keluar dari grup "+getIntent().getStringExtra("name")+" ?");
                builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doExit();
                        finish();
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
    }

    private void loadData() {
        Call<GetUsers> call = apiGroups.getMemberGroup(getIntent().getStringExtra("id_group"));
        call.enqueue(new Callback<GetUsers>() {
            @Override
            public void onResponse(Call<GetUsers> call, Response<GetUsers> response) {
                usersList = response.body().getResult();
                RecyclerView.Adapter adapter = new MembersAdapter(usersList,getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<GetUsers> call, Throwable t) {

            }
        });
    }

    private void doExit(){
        Call<PostData> call = apiGroups.exitGroup(getIntent().getStringExtra("id_group"));
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if(response.code() == 200){
                    if(response.body().getStatus().equals("success")){
                        Toast.makeText(getApplicationContext(),"Berhasil Keluar grup",Toast.LENGTH_SHORT).show();
                        finish();
                        MyGroups.myGroups.loadData();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {

            }
        });
    }
}
