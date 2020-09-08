package com.inas.atroads.services;

import android.content.Context;
import android.util.Log;

import java.util.Date;

public class CallReceiver extends PhonecallReceiver{
    private String TAG = "CallReceiver";
    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        Log.i(TAG, "onIncomingCallReceived: "+number);
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        Log.i(TAG, "onIncomingCallAnswered: "+number);
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {

    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {

    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {

    }
}
