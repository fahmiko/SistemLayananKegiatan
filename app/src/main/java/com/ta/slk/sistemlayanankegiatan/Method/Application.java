package com.ta.slk.sistemlayanankegiatan.Method;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Application extends android.app.Application {

    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }
}
