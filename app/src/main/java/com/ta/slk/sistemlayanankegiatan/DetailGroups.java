package com.ta.slk.sistemlayanankegiatan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ta.slk.sistemlayanankegiatan.Adapter.MembersAdapter;
import com.ta.slk.sistemlayanankegiatan.Model.GetUsers;
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
    CircleImageView imgGroup;
    RecyclerView recyclerView;
    ImageButton back;
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
        back = findViewById(R.id.btn_back);
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
}
