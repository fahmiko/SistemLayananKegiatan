package com.ta.slk.sistemlayanankegiatan.Method;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.preference.Preference;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.Random;

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

    public static int getColorRandom(){
        Random rnd = new Random();
        int color = Color.argb(200, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }
}
