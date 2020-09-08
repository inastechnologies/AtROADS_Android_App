package com.inas.atroads.services;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Build;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.FirebaseApp;
import com.inas.atroads.R;

import java.util.Locale;

public class CustomApplication extends Application
{
    private static CustomApplication sInstance;
    public static Geocoder geoCoder;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        geoCoder = new Geocoder(getAppContext(), Locale.getDefault());
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleListener());
        FirebaseApp.initializeApp(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel callChannel = new NotificationChannel(getResources().getString(R.string.call_channel_id),
                    getResources().getString(R.string.cal_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            callChannel.setDescription(getResources().getString(R.string.call_channel_description));
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(callChannel);
        }
    }

    public static CustomApplication getsInstance()
    {
        return sInstance;
    }

    public static Context getAppContext()
    {
        return sInstance.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }



}
