package com.ta.slk.sistemlayanankegiatan.Method;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ta.slk.sistemlayanankegiatan.Activity.LoginActivity;
import com.ta.slk.sistemlayanankegiatan.Model.Users;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;

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

    public void saveGuide(){
        SharedPreferences sf = context.getSharedPreferences("guide",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("user_guide","false");
        editor.apply();
    }

    public boolean isAdmin(){
        SharedPreferences sf = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        String admin = sf.getString("level",null);
        return (admin.equals("1"));
    }

    public void saveCredentials(String id, String name, String username, String photo, String id_member,String email, String telp, String level,String token){
        SharedPreferences sf = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("id_user",id);
        editor.putString("name",name);
        editor.putString("username",username);
        editor.putString("photo",photo);
        editor.putString("email",email);
        editor.putString("telp",telp);
        editor.putString("id_member",id_member);
        editor.putString("level",level);
        editor.putString("token",token);
        editor.apply();
    }

    public void logout(int condition){
        SharedPreferences sf = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.clear().apply();
        Intent intent = new Intent(context, LoginActivity.class);
        if (condition == 1){
//            intent.putExtra("message","Berhasil Logout");
        }else{
//            intent.putExtra("message","SESI ANDA SUDAH HABIS");
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
