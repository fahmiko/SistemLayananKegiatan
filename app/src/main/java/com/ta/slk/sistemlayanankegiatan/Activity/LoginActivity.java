package com.ta.slk.sistemlayanankegiatan.Activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ta.slk.sistemlayanankegiatan.AdminContent;
import com.ta.slk.sistemlayanankegiatan.MainActivity;
import com.ta.slk.sistemlayanankegiatan.Method.Application;
import com.ta.slk.sistemlayanankegiatan.Method.Session;
import com.ta.slk.sistemlayanankegiatan.Model.GetUsers;
import com.ta.slk.sistemlayanankegiatan.Model.Users;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiInterface;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity{
    TextInputEditText username,password;
    Button btn_login,btn_register;
    private String TAG = "Pesan Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String msg = getIntent().getStringExtra("message");
        if(msg != null){
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCheck();
            }
        });
    }

    private void registerCheck(){
        final ApiInterface service = ApiClient.getAuth().create(ApiInterface.class);
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.popup_register,null);
        final TextInputEditText input = dialog.findViewById(R.id.register_nip);
        builder.setView(dialog).setTitle("Daftar Anggota").setMessage("Masukan Identitas");
        builder.setPositiveButton("cek", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<GetUsers> call = service.getLoginNip(input.getText().toString());
                call.enqueue(new Callback<GetUsers>() {
                    @Override
                    public void onResponse(Call<GetUsers> call, Response<GetUsers> response) {
                        if(response.code()==200){
                            if(response.body().getStatus().equals("success")){
                                Toast.makeText(getApplicationContext(),"DATA TIDAK DITEMUKAN",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),response.body().getResult().get(0).getName(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetUsers> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"CEK KONEKSI",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void Login(){
        ApiInterface mApiInterface = ApiClient.getAuth().create(ApiInterface.class);
        final SharedPreferences sf = getSharedPreferences("device_token",MODE_PRIVATE);
//        Log.d(TAG, "Login: Login berjalan");
        Call<GetUsers> mLoginCall = mApiInterface.getUser(username.getText().toString(),
                password.getText().toString(),
                sf.getString("device_token",null));

        mLoginCall.enqueue(new Callback<GetUsers>() {
            @Override
            public void onResponse(Call<GetUsers> call, retrofit2.Response<GetUsers> response) {
                Toast.makeText(getApplicationContext(),response.body().getToken(),Toast.LENGTH_SHORT).show();
                if(response.code()==200){
                    if(response.body().getStatus().equals("success")){
                        Session session = Application.getSession();
                        String id_user = response.body().getResult().get(0).getIdUser();
                        String username = response.body().getResult().get(0).getUsername();
                        String name = response.body().getResult().get(0).getName();
                        String photo = response.body().getResult().get(0).getPhotoProfile();
                        String id_member = response.body().getResult().get(0).getIdMember();
                        String token = response.body().getToken();
                        session.saveCredentials(id_user,name,username,photo, id_member, token);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }else{
                        username.setError("invalid");
                        password.setError("invalid");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUsers> call, Throwable t) {
                Log.e("error",t.toString());
            }
        });


    }
}
