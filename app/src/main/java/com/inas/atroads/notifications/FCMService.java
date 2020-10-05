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
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;

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
    private Call IncomingCallFromOtherUser;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // .....
       /* if (SinchHelpers.isSinchPushPayload(remoteMessage.getData()))
        {
            Log.i(TAG, "onMessageReceived: SinchHelpers"+remoteMessage.getNotification());
            Log.i(TAG, "onMessageReceived: SinchHelpers"+remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            data.get("sinch");
            Log.i(TAG, "onMessageReceived: "+data.get("sinch"));
            String json = data.get("sinch");
            JSONObject obj = null;
            try {
                obj = new JSONObject(json);
                String user_id = obj.getString("user_id");
                Log.i(TAG, "onMessageReceived: user_id"+user_id);
                CallerName = user_id.replaceAll("[0-9]","");
                showNotification(CallerName,"Hyd",Math.abs(new Random().nextInt()));
                Log.i(TAG, "onMessageReceived: CallerName: "+CallerName);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {*/
            // it's NOT Sinch message - process yourself
            // TODO(developer): Handle FCM messages here.
            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
            Log.d(TAG, "From: " + remoteMessage.getFrom());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                if (/* Check if data needs to be processed by long running job */ true) {
                    // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
                    Log.d(TAG, "onMessageReceived: "+remoteMessage.getData());
                } else {
                    // Handle message within 10 seconds
//                handleNow();
                    Log.d(TAG, "handle "+remoteMessage.getData());
                }

            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.ic_launcher);
                sendNotification(icon,remoteMessage.getNotification().getBody());
            }

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.



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
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

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


    private void SendDeviceTokenToServer(String token)
    {
        NotificationRequestModel requestModel = new NotificationRequestModel();
        requestModel.setRegistrationId(token);
        //createRequestToServerWithVolley(requestModel);
    }
//
//    /**
//     * Input:ServerObject & ServerMethodNames
//     * Description:This method is used to create the request to server and call appropriate fire response to it.
//     */
//    public void createRequestToServerWithVolley(ServerInput serverInputObj)
//    {
//        Log.d(TAG, "createRequestToServer : Start of Function");
//        final String jsonRequest = serverInputObj.ToJson();
//        Log.i(TAG+"jsonRequest",jsonRequest);
//        RequestQueue mRequestQueue;
//        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, DeviceTokenURL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i(TAG+"server response", response);
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG+"onErrorResponse exception", String.valueOf(error));
//                        if (mStatusCode == 0) {
////                            m_serverEvents.fire_ErrorResponse(error, mStatusCode);
//                        }
//                        try {
//                            mStatusCode = error.networkResponse.statusCode;
//                        } catch (Exception e) {
////                            m_serverEvents.fire_ErrorResponse(error, mStatusCode);
//                        }
//
//                        Log.i(TAG+"Statuscode error", String.valueOf(mStatusCode));
////                        m_serverEvents.fire_ErrorResponse(error, mStatusCode);
//                        if (error instanceof NetworkError) {
//                        } else if (error instanceof ServerError) {
//                        } else if (error instanceof AuthFailureError) {
//                        } else if (error instanceof ParseError) {
//                        } else if (error instanceof NoConnectionError) {
//                        } else if (error instanceof TimeoutError) {
//                        }
//                        Log.d("Error", "Error: " + error.getMessage());
//                    }
//                }) {
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                if (response != null) {
//                    mStatusCode = response.statusCode;
//                    Log.i("Statuscode:", String.valueOf(mStatusCode));
//                } else {
//                    Log.i("Statuscodenull:", String.valueOf(mStatusCode));
//                }
//                return super.parseNetworkResponse(response);
//            }
//
//            @Override
//            public byte[] getBody() throws com.android.volley.AuthFailureError {
//                String str = jsonRequest;
//                return str.getBytes();
//            };
//
//            public String getBodyContentType()
//            {
//                return "application/json; charset=utf-8";
//            }
//
//
//        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        mRequestQueue.add(stringRequest);
//        Log.d(TAG, "createRequestToServer : End of Function");
//    }


    private void startCallService() {

//        startService(new Intent(this, CallBgService.class).putExtra("Caller",Caller).putExtra("call", (Serializable) call));
       // startService(new Intent(this, CallBgService.class).putExtra("Caller","Sahasra"));
    }


}
