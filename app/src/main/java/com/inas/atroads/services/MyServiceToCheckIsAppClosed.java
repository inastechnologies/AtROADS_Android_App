package com.inas.atroads.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.inas.atroads.views.Activities.BillingDetailsActivity;

public class MyServiceToCheckIsAppClosed  extends Service {
    public static String TAG = "MyServiceToCheckIsAppClosed";
//    @Override
//    public void onStartService() {
//        //your code
//    }
    @Override
    public int onStartCommand(final Intent intent,
                              final int flags,
                              final int startId) {

        //your code
        Log.i(TAG, "onStartCommand: "+startId);
        return START_STICKY;
    }

    public MyServiceToCheckIsAppClosed() {
        super();
        Log.i(TAG, "MyServiceToCheckIsAppClosed: ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "onTaskRemoved: "+rootIntent);
        super.onTaskRemoved(rootIntent);
        //do something you want
        //stop service
        Log.i(TAG, "run: onTaskRemoved"+"Start of delete pair API");
        BillingDetailsActivity detailsActivity = new BillingDetailsActivity();
        detailsActivity.CallDelayDeletePairAPI();
        Log.i(TAG, "run: onTaskRemoved"+"end of delete pair API");

        this.stopSelf();
    }
}