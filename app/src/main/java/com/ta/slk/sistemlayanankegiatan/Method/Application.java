package com.ta.slk.sistemlayanankegiatan.Method;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class Application extends android.app.Application {
    private static Session session;
    @Override
    public void onCreate() {
        super.onCreate();
        session = new Session(getApplicationContext());
    }

    public static Session getSession(){
        return session;
    }

    public void showMessageSuccess(){
        Toast.makeText(this.getApplicationContext(),"Sukses",Toast.LENGTH_SHORT).show();
    }


}
