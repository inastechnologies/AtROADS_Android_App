package com.inas.atroads.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.inas.atroads.R;
import com.inas.atroads.views.Activities.TrackerService;
import com.inas.atroads.views.UI.VoiceCallActivity;

import java.io.Serializable;
import java.util.Random;

public class CallBgService extends Service {

    private static final String TAG = CallBgService.class.getSimpleName();
    String Caller;
    private Serializable call;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showNotification("SUSH","Hyd",Math.abs(new Random().nextInt()));
////
//////Add this code to onCreate or some onclick Buttton
//        NotificationManager manager1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
//        long when = System.currentTimeMillis();
//        builder.setSmallIcon(R.drawable.call_black);
//        Intent notificationIntent = new Intent(getApplicationContext(), VoiceCallActivity.class).putExtra("notification", "1");
//        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);
//        Notification notification1 = builder.getNotification();
//        notification1.when = when;
//
//        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_view);
//        remoteViews.setTextViewText(R.id.tvName, "New Name");
//        listener(remoteViews,getApplicationContext());
//
//
//        notification1.contentView = remoteViews;
//        notification1.flags |= Notification.FLAG_AUTO_CANCEL;
//        manager1.notify(1, notification1);
//

//        buildNotification();
//        loginToFirebase();
    }


    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.blue_pointer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
        //  startForeground(1, builder.build());
    }


    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            Caller = intent.getStringExtra("Caller");
            call = intent.getStringExtra("call");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };


    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.inas.atroads";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Intent notificationIntent = new Intent(getApplicationContext(), VoiceCallActivity.class).putExtra("notification", "1");

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.atroads_logo)
                .setContentTitle("Call From "+Caller)
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setOngoing(true)
                .setCategory(Notification.CATEGORY_CALL)
                .setContentIntent(contentIntent)
                .addAction(R.drawable.receive_call_icon,"Answer",contentIntent)
                .addAction(R.drawable.end_call_icon,"Hangup",contentIntent)
                .build();

//        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_view);
//        remoteViews.setTextViewText(R.id.tvName, Caller );
//        listener(remoteViews,getApplicationContext());
//
//
//        notification.contentView = remoteViews;
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        manager.notify(1, notification);
//

        startForeground(2, notification);

    }


    public void listener(RemoteViews remoteViews, Context context) {
        // you have to make intetns for each action (your Buttons)
        Intent intent = new Intent("Accept");
        Intent intent2 = new Intent("Reject");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,1,intent,0);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,1,intent2,0);

        // add actions here !
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Accept");
        intentFilter.addAction("Reject");


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("Accept")){
                    Toast.makeText(context, "Accepted !!", Toast.LENGTH_SHORT).show();
                    Intent i  = new Intent(getApplicationContext(),VoiceCallActivity.class);
                    i.putExtra("call",call);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else if(intent.getAction().equals("Reject")) {
                    Toast.makeText(context, "Rejected !!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        context.registerReceiver(receiver,intentFilter);
        remoteViews.setOnClickPendingIntent(R.id.ivRequest,pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.ivReject,pendingIntent2);

    }

    public void sendNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.journaldev.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Notifications Title");
        builder.setContentText("Your notification content here.");
        builder.setSubText("Tap to view the website.");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
    }





    private void showNotification(String name, String address, int notification_id)
    {
        Intent contentIntent = new Intent(this, VoiceCallActivity.class);
        Intent answerIntent = new Intent(this, VoiceCallActivity.class);
        answerIntent.putExtra("notificationID", notification_id);
        answerIntent.putExtra("FromCallIntent","Answer");
        answerIntent.putExtra("Call",call);
        Intent declineIntent = new Intent(this, DeclineCallReceiver.class);
        declineIntent.putExtra("notificationID", notification_id);
        declineIntent.putExtra("FromCallIntent","Decline");

        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, contentIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent answerPendingIntent = PendingIntent.getActivity(this, 0, answerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent declinePendingIntent = PendingIntent.getBroadcast(this, 0, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, getResources().getString(R.string.call_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.atroads_logo))
                .setContentTitle(name)
//                .setContentText(address)
                .setColor(Color.YELLOW)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
//                .setOngoing(true)
                .addAction(R.drawable.receive_call_icon, "ANSWER", answerPendingIntent)
                .addAction(R.drawable.end_call_icon, "DECLINE", declinePendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notification_id, notification);
    }



}

