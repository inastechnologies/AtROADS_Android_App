package com.inas.atroads.services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;

public class AnswerCallReceiver extends BroadcastReceiver implements CallClientListener {
    String TAG = "AnswerCallReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationID = intent.getIntExtra("notificationID", 0);
//        Toast.makeText(context, Integer.toString(notificationID), Toast.LENGTH_LONG).show();
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
               .notify();
    }

    @Override
    public void onIncomingCall(CallClient callClient, Call call) {
        Log.i(TAG, "onIncomingCall: "+call);

    }
}
