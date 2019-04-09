package com.ta.slk.sistemlayanankegiatan.Method;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private Context context;

    public void saveCredentials(String id, String name,String username, String password, String photo, String token){
        SharedPreferences sf = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("id_user",id);
        editor.putString("name",name);
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putString("photo",photo);
        editor.putString("token",token);

        editor.apply();
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

    public String getToken(){
        return "1234";
    }


}
