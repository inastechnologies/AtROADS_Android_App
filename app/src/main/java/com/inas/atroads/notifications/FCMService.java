package com.inas.atroads.notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inas.atroads.R;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.Activities.StartUpActivity;
import com.inas.atroads.views.UI.SchedulingRideScreen;
import com.inas.atroads.views.model.PayloadModel;

import java.util.ArrayList;

public class FCMService extends FirebaseMessagingService{
    String TAG="FCMService";
    public static String Token;
    public static String DeviceTokenURL = "http://35.224.241.203:9000/fitzcube/push_notifications";
    private int mStatusCode = 0;
   // VoiceCallActivity voiceCallActivity = new VoiceCallActivity();
    private Notification.Builder notification;
    private Bitmap remote_picture;
    private String CallerName = "";
    public static final String CALL_ID = "CALL_ID";
   // PayloadModel payloadModel= new PayloadModel();
   // String user_id ="";
   // private Call IncomingCallFromOtherUser;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
               /* Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("pairing"));
                //Log.d(TAG, "Message data payload: " + remoteMessage.getNotification().get());
                user_id = remoteMessage.getData().get("home");
                    payloadModel.setPairing(Integer.parseInt(remoteMessage.getData().get("pairing")));
                    payloadModel.setOffice(remoteMessage.getData().get("office"));
                    payloadModel.setWhenYouWantToNotify(Integer.parseInt(remoteMessage.getData().get("When_you_want_to_notify")));
                    payloadModel.setToTime(remoteMessage.getData().get("to_time"));
                    payloadModel.setType(remoteMessage.getData().get("type"));
                    payloadModel.setFromTime(remoteMessage.getData().get("from_time"));
                    payloadModel.setDate(remoteMessage.getData().get("Date"));
                    payloadModel.setHome(remoteMessage.getData().get("home"));*/
               // }

            }
            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.ic_launcher);
                sendNotification(icon,remoteMessage.getNotification().getBody());
            }
    }


    @Override
    public void onNewToken(String s) {
        Log.d(TAG, "Refreshed token: " + s);
        int maxLogSize = 1000;
        for(int i = 0; i <= s.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > s.length() ? s.length() : end;
            Log.v(TAG+"token", s.substring(start, end));
        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Token", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Dev_Token", s);
        editor.apply();
        editor.commit();
        sendRegistrationToServer(s);
        Token=s;
    }

    /**
     *
     * @param token
     */
    public void sendRegistrationToServer(String token)
    {
        //SendDeviceTokenToServer(token);
    }

    private void sendNotification(Bitmap bitmap, String message){



        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Intent intent = new Intent(getApplicationContext(), HomeMapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
           // intent.putExtra("user_id", user_id);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "101";

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

                //Configure Notification Channel
                notificationChannel.setDescription("Game Notifications");
                notificationChannel.enableLights(true);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);

                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Atroads")
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setStyle(style)
                    // .setLargeIcon(bitmap)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_MAX);


            notificationManager.notify(1, notificationBuilder.build());



    }

}
