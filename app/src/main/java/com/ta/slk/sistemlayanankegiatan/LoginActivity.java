package com.ta.slk.sistemlayanankegiatan;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ta.slk.sistemlayanankegiatan.Method.Preferences;
import com.ta.slk.sistemlayanankegiatan.Model.GetUsers;
import com.ta.slk.sistemlayanankegiatan.Model.Users;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity{
    EditText username,password;
    Button btn_login,btn_login_other;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void Login(){
        ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        SharedPreferences sf = getSharedPreferences("device_token",MODE_PRIVATE);

        Call<GetUsers> mLoginCall = mApiInterface.getLoginNip(username.getText().toString(),sf.getString("device_token",""));
        mLoginCall.enqueue(new Callback<GetUsers>() {
            @Override
            public void onResponse(Call<GetUsers> call, retrofit2.Response<GetUsers> response) {
                if(response.body().getStatus().equals("success")){
                    String id_user = response.body().getResult().get(0).getIdUser();
                    String username = response.body().getResult().get(0).getUsername();
                    String password = response.body().getResult().get(0).getPassword();
                    String name = response.body().getResult().get(0).getName();
                    String photo = response.body().getResult().get(0).getPhotoProfile();
                    Preferences pr = new Preferences(getApplicationContext());
                    pr.saveCredentials(id_user,name,username,password,photo);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<GetUsers> call, Throwable t) {
                Log.e("error",t.toString());
            }
        });


    }
}
