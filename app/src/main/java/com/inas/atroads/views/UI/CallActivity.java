package com.inas.atroads.views.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agik.AGIKSwipeButton.Controller.OnSwipeCompleteListener;
import com.agik.AGIKSwipeButton.View.Swipe_Button_View;
import com.inas.atroads.R;
import com.inas.atroads.services.MyServiceToCheckIsAppClosed;
import com.inas.atroads.views.Activities.ChatActivity;
import com.inas.atroads.views.Activities.UserDetails;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CallActivity extends AppCompatActivity implements CallListener, CallClientListener {
    private static final String TAG = "CallActivity";
    private Call call;
    Button CallBtn,endBtn;
    TextView callState,receiverName;
    SinchClient sinchClient;
    public static final int RequestPermissionCode = 1;
    MediaRecorder mediaRecorder = new MediaRecorder();
    private String AudioSavePathInDevice;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    private static final String APP_KEY = "77fae56c-dfc7-48a4-92eb-b26933751bcf";
    private static final String APP_SECRET = "D593d6dgu0OVT7nqswBC9w==";
//    private static final String ENVIRONMENT = "sandbox.sinch.com";
    private static final String ENVIRONMENT = "clientapi.sinch.com";
    private int soundID;
    ImageView callIV;
    TextView receivecallTv;
    MediaPlayer mpIncoming = new MediaPlayer();
    MediaPlayer mpRinging = new MediaPlayer();
    Chronometer cmTimer;
    Boolean resume = false;
    long elapsedTime;
    com.stfalcon.swipeablebutton.SwipeableButton swipeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
         CallBtn = (Button) findViewById(R.id.CallBtn);
        endBtn = (Button) findViewById(R.id.endBtn);
        receivecallTv = findViewById(R.id.receivecallTv);
        callIV = (ImageView) findViewById(R.id.callIV);
        callState = (TextView) findViewById(R.id.callState);
        receiverName = (TextView) findViewById(R.id.receiverName);
        String ReciverName = getIntent().getStringExtra("ReciverName");
        String UserName = getIntent().getStringExtra("username");
        Log.i(TAG, "onCreate: "+ReciverName+"-->"+UserName);
        receiverName = (TextView) findViewById(R.id.receiverName);
        receiverName.setText(ReciverName);

        sinchClient = Sinch.getSinchClientBuilder()
                .context(CallActivity.this)
                .userId(UserName)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
        CallBtn = (Button) findViewById(R.id.CallBtn);
        callState = (TextView) findViewById(R.id.callState);

        CallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (call == null) {
                    call = sinchClient.getCallClient().callUser(ReciverName);
                    call.addCallListener(new SinchCallListener());
                    CallBtn.setText("Hang Up");
                } else {
                    call.hangup();
                }

            }
        });

        if(checkPermission()) {
            // create instance of Random class
            Random rand = new Random();

            // Generate random integers in range 0 to 999
            int rand_int1 = rand.nextInt(1000);

            // Print random integers
            System.out.println("Random Integers: "+rand_int1);
            AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";
//            AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
//                    rand_int1 + "AudioRecording.3gp";

            MediaRecorderReady();

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            CallBtn.setEnabled(false);
            endBtn.setEnabled(true);

            Toast.makeText(CallActivity.this, "Recording started",
                    Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }


    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        String randomStr = UUID.randomUUID().toString().substring(0,5);
//        StringBuilder stringBuilder = new StringBuilder( string );
//        int i = 0 ;
//        while(i < string ) {
//            stringBuilder.append(RandomAudioFileName.
//                    charAt(random.nextInt(RandomAudioFileName.length())));
//
//            i++ ;
//        }
        return randomStr;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(CallActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(CallActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CallActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onIncomingCall(CallClient callClient, Call call) {
        Toast.makeText(CallActivity.this, "on IncomingCall", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCallProgressing(Call call) {

    }

    @Override
    public void onCallEstablished(Call call) {

    }

    @Override
    public void onCallEnded(Call call) {

    }

    @Override
    public void onShouldSendPushNotification(Call call, List<PushPair> list) {

    }


    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            SinchError a = endedCall.getDetails().getError();
            CallBtn.setText("Call");
            callState.setText("");
            receivecallTv.setVisibility(View.INVISIBLE);
            if(cmTimer!=null)
            {
                cmTimer.stop();
                cmTimer.setText("");
                resume = true;
                cmTimer.stop();
//                cmTimer.setText("00:00");
                resume = false;
            }
//            if(mpIncoming!=null && mpIncoming.isPlaying())
//            {
//                mpIncoming.stop();
//                mpIncoming.release();
//            }
//            if(mpRinging!=null && mpRinging.isPlaying())
//            {
//                mpRinging.stop();
//                mpRinging.release();
//            }
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            callState.setText("connected");
            receivecallTv.setVisibility(View.INVISIBLE);
            StartTimer();
            if (!resume) {
                cmTimer.setBase(SystemClock.elapsedRealtime());
                cmTimer.start();
            } else {
                cmTimer.start();
            }
//            if(mpIncoming!=null && mpIncoming.isPlaying())
//            {
//                mpIncoming.stop();
//                mpIncoming.release();
//            }
//            if(mpRinging!=null && mpRinging.isPlaying())
//            {
//                mpRinging.stop();
//                mpRinging.release();
//            }
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            callState.setText("ringing");
            StartRingingSound();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            StartIncomingSound();

            receivecallTv.setVisibility(View.VISIBLE);
            Toast.makeText(CallActivity.this, "incoming call", Toast.LENGTH_SHORT).show();
//            Animation animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
//                    R.anim.bounce);
//            callIV.startAnimation(animBlink);
            callIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call.answer();
                    call.addCallListener(new SinchCallListener());
                    CallBtn.setText("Hang Up");
                    callIV.clearAnimation();
                }
            });
        }
    }

    
    private void StartIncomingSound()
    {
        mpIncoming = MediaPlayer.create(this, R.raw.incoming_call);
        mpIncoming.start();
        mpIncoming.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                //code
                mp.release();
            }
        });
    }

    private void StartRingingSound()
    {
        mpRinging = MediaPlayer.create(this, R.raw.phone_ringing);
        mpRinging.start();
        mpRinging.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                //code
                mp.release();
            }
        });
    }



    private void StartTimer()
    {
        cmTimer = (Chronometer) findViewById(R.id.cmTimer);
        // example setOnChronometerTickListener
        cmTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer arg0) {
                if (!resume) {
                    long minutes = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) / 60;
                    long seconds = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) % 60;
                    elapsedTime = SystemClock.elapsedRealtime();
                    Log.d(TAG, "onChronometerTick: " + minutes + " : " + seconds);
                } else {
                    long minutes = ((elapsedTime - cmTimer.getBase())/1000) / 60;
                    long seconds = ((elapsedTime - cmTimer.getBase())/1000) % 60;
                    elapsedTime = elapsedTime + 1000;
                    Log.d(TAG, "onChronometerTick: " + minutes + " : " + seconds);
                }
            }
        });
    }
    
}