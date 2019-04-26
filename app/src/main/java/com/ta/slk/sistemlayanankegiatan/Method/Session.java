package com.ta.slk.sistemlayanankegiatan.Method;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.ta.slk.sistemlayanankegiatan.LoginActivity;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class Session {
    private Context context;

    public Session(Context context) {
        this.context = context;
    }

    public String getToken(){
        SharedPreferences preferences = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        return preferences.getString("token",null);
    }


    public void saveDeviceToken(String token){
        SharedPreferences sf = context.getSharedPreferences("device_token",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("device_token", token);
        editor.apply();
    }

    public void saveCredentials(String id, String name, String username, String password, String photo, String id_member, String token){
        SharedPreferences sf = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("id_user",id);
        editor.putString("name",name);
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putString("photo",ApiClient.BASE_URL+"/uploads/members/"+photo);
        editor.putString("id_member",id_member);
        editor.putString("token",token);
        editor.apply();
    }

    public void logout(int condition){
        SharedPreferences sf = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.remove("id_user");
        editor.remove("id_member");
        editor.remove("name");
        editor.remove("username");
        editor.remove("password");
        editor.remove("photo");
        editor.remove("token");
        editor.apply();
        Intent intent = new Intent(context, LoginActivity.class);
        if (condition == 1){
            intent.putExtra("message","Berhasil Logout");
        }else{
            intent.putExtra("message","SESI ANDA SUDAH HABIS");
        }
        context.startActivity(intent);

    }
    public boolean checkSavedCredetential(){
        SharedPreferences sf = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        String id_user = sf.getString("id_user","");
        if (id_user.equals("")){
            return false;
        }else {
            return true;
        }
    }
}
