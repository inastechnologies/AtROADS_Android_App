package com.inas.atroads.views.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.inas.atroads.R;
import com.inas.atroads.notifications.FCMService;
import com.inas.atroads.services.AnswerCallReceiver;
import com.inas.atroads.services.CallBgService;
import com.inas.atroads.views.Activities.ChatActivity;
import com.inas.atroads.views.Activities.TrackerService;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class VoiceCallActivity extends AppCompatActivity {
    Button CallBtn,endBtn,receive;
    TextView callState,receiverName;
    public static SinchClient sinchClient;
    ImageView callIV;
    TextView receivecallTv;
    private static final String TAG = "VoiceCallActivity";
    private static final String APP_KEY = "77fae56c-dfc7-48a4-92eb-b26933751bcf";
    private static final String APP_SECRET = "D593d6dgu0OVT7nqswBC9w==";
    private static final String ENVIRONMENT = "clientapi.sinch.com";
    private Call call;
    MediaPlayer mpIncoming;
    MediaPlayer mpRinging;
    Chronometer cmTimer;
    Boolean resume = false;
    long elapsedTime;
    private String ReciverName,UserName,OtherProfilePic;
    private int UserId,OtheruserId;
    private String Caller,Receiver;
    private String AudioSavePathInDevice;
    private MediaRecorder mediaRecorder;
    private AudioManager audioManager;
    private KeyguardManager keyguardManager;
    private boolean isSpeakerOn = true;

    public static final int RequestPermissionCode = 1;
    private ImageView speakerIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);
        SetViews();

        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        GetPermissions();
    }

    /**
     * SetViews()
     */
    private void SetViews()
    {
        CallBtn = (Button) findViewById(R.id.CallBtn);
        endBtn = (Button) findViewById(R.id.endBtn);
        receive = (Button) findViewById(R.id.receive);
        endBtn.setVisibility(View.INVISIBLE);
        receive.setVisibility(View.INVISIBLE);
        speakerIv =findViewById(R.id.speakerIv);
        speakerIv.setVisibility(View.INVISIBLE);
        receivecallTv = findViewById(R.id.receivecallTv);
        callIV = (ImageView) findViewById(R.id.callIV);
        callState = (TextView) findViewById(R.id.callState);
        cmTimer = (Chronometer) findViewById(R.id.cmTimer);
        cmTimer.setVisibility(View.INVISIBLE);
        receiverName = (TextView) findViewById(R.id.receiverName);
//        ReciverName = getIntent().getStringExtra("ReciverName");
        UserName = getIntent().getStringExtra("username");
        UserId = getIntent().getIntExtra("UserId",0);
//        OtheruserId = getIntent().getIntExtra("OtheruserId",0);
//        Log.i(TAG, "onCreate: "+ReciverName+"-->"+UserName);
//        Log.i(TAG, "onCreate: UserId "+UserId+"-->"+OtheruserId);
        receiverName = (TextView) findViewById(R.id.receiverName);
//        receiverName.setText(ReciverName);
        Caller = UserName+UserId;
//        Receiver = Receiver+OtheruserId;
//        Log.i(TAG, "SetViews: Caller:"+Caller+"-->"+Receiver);
        GetOtherUserPrefs();
        GetSharedPrefs();
        SetSinchClient();
    }


    private void GetOtherUserPrefs()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("OtherUserCreds", 0); // 0 - for private mode
        ReciverName = pref.getString("OtherUsername","N/A");
        OtherProfilePic = pref.getString("OtherProfilePic","N/A");
        OtheruserId = pref.getInt("OtheruserId",0);
        Log.i(TAG, "GetOtherUserPrefs: "+ReciverName+"-->"+OtheruserId+"-->"+OtherProfilePic);
        receiverName.setText(ReciverName);
        Caller = UserName+UserId;
        Receiver = ReciverName+OtheruserId;
        Log.i(TAG, "GetOtherUserPrefs: Caller:"+Caller+"-->"+Receiver);
    }


    /*
   GetSharedPrefs
   */
    private void GetSharedPrefs()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
        UserId = pref.getInt("user_id", 0);
        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
    }

    /**
     * SetSinchClient
     */
    private void SetSinchClient() {
        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(Caller)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.setSupportManagedPush(true);
        sinchClient.setSupportActiveConnectionInBackground(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
        SetCallBtn();

    }

    /**
     * SetCallBtn
     */
    private void SetCallBtn()
    {
        CallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (call == null) {
                    // Calling Receiver
                    CallClient callClient = sinchClient.getCallClient();
                    call = callClient.callUser(Receiver);
                    call.addCallListener(new SinchCallListener());
                    CallBtn.setText("Hang Up");
                } else {
                    call.hangup();
                }
            }
        });
    }



    /**
     * SinchCallListener
     */
    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            SinchError a = endedCall.getDetails().getError();
            CallBtn.setText("Call");
            CallBtn.setVisibility(View.VISIBLE);
            callState.setText("");
            receivecallTv.setVisibility(View.INVISIBLE);
            cmTimer.setVisibility(View.INVISIBLE);
            if(cmTimer!=null)
            {
                cmTimer.stop();
                cmTimer.setText("");
                resume = true;
//                cmTimer.stop();
//                cmTimer.setText("00:00");
                resume = false;
            }
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            HideBtns();
            speakerIv = findViewById(R.id.speakerIv);
            speakerIv.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            callState.setText("connected");
            cmTimer.setVisibility(View.VISIBLE);
            receivecallTv.setVisibility(View.INVISIBLE);
            StartTimer();
            if (!resume) {
                cmTimer.setBase(SystemClock.elapsedRealtime());
                cmTimer.start();
            } else {
                cmTimer.start();
            }
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            stopPlaying(mpIncoming);
            stopPlaying(mpRinging);
            HideBtns();
            CallBtn.setText("Hang Up");
            CallBtn.setVisibility(View.VISIBLE);
            SetSpeakerBtn();
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            callState.setText("ringing");
            StartRingingSound();
            HideBtns();
            CallBtn.setVisibility(View.VISIBLE);
            speakerIv = findViewById(R.id.speakerIv);
            speakerIv.setVisibility(View.VISIBLE);
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {

        }
    }


    /**
     * SinchCallClientListener
     */
    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            Toast.makeText(VoiceCallActivity.this, "incoming call", Toast.LENGTH_SHORT).show();
            receivecallTv.setVisibility(View.VISIBLE);
            StartIncomingSound();
//            SetCallIvToPickCall();
            EnableDisableBtns();
//            startCallService(incomingCall);
        }
    }


    private void startCallService(Call incomingCall) {
        startService(new Intent(this, CallBgService.class).putExtra("Caller",Caller));
        finish();
    }


    private void SetSpeakerBtn()
    {
        speakerIv.setVisibility(View.VISIBLE);
        speakerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSpeakerOn == false)
                {
                    isSpeakerOn = true;
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    if (!audioManager.isSpeakerphoneOn()){
                        audioManager.setSpeakerphoneOn(true);
                        audioManager.setWiredHeadsetOn(false);
                        speakerIv.setImageResource(R.drawable.sound_on);
                    }
                    else {
                        audioManager.setSpeakerphoneOn(false);
                        audioManager.setWiredHeadsetOn(true);
                        speakerIv.setImageResource(R.drawable.volume_off);
                    }
                }
                else {
                    isSpeakerOn = false;
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    if (!audioManager.isSpeakerphoneOn()){
                        audioManager.setSpeakerphoneOn(true);
                        speakerIv.setImageResource(R.drawable.sound_on);
                    }
                    else {
                        audioManager.setSpeakerphoneOn(false);
                        speakerIv.setImageResource(R.drawable.volume_off);
                    }
                }
            }
        });
    }


    private void EnableDisableBtns()
    {
        SetReceiveBtn();
        SetEndBtn();
        CallBtn.setVisibility(View.INVISIBLE);
    }


    private void SetReceiveBtn()
    {
        receive = findViewById(R.id.receive);
        receive.setVisibility(View.VISIBLE);
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(call!=null)
                {
                    call.answer();
                    call.addCallListener(new SinchCallListener());
                }

            }
        });
    }

    private void SetEndBtn()
    {
        endBtn = findViewById(R.id.endBtn);
        endBtn.setVisibility(View.VISIBLE);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(call!=null) {
                    call.hangup();
                }
                VoiceCallActivity.super.onBackPressed();
            }
        });
    }


    private void HideBtns()
    {
        receive = findViewById(R.id.receive);
        receive.setVisibility(View.INVISIBLE);
        endBtn = findViewById(R.id.endBtn);
        endBtn.setVisibility(View.INVISIBLE);
    }



    /**
     * StartIncomingSound
     */
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

    /**
     * StartRingingSound
     */
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


    private void stopPlaying(MediaPlayer mp) {
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
                mp = null;
            }catch (Exception e)
            {
                Log.i(TAG, "stopPlaying: "+e.getLocalizedMessage());
            }

        }

    }


    /**
     * StartTimer
     */
    private void StartTimer()
    {
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


    /**
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                callIV.setAlpha(0.5f);
                receive.setAlpha(0.5f);
                endBtn.setAlpha(0.5f);
                break;
            }
            case MotionEvent.ACTION_UP: {
                callIV.setAlpha(1.0f);
                receive.setAlpha(1.0f);
                endBtn.setAlpha(1.0f);
                break;
            }
        }
        return false;
    }


    private void GetPermissions()
    {
        if(checkPermission()) {
            // create instance of Random class
            Random rand = new Random();

            // Generate random integers in range 0 to 999
            int rand_int1 = rand.nextInt(1000);

            // Print random integers
            System.out.println("Random Integers: "+rand_int1);
            AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                    CreateRandomAudioFileName(5) + "AudioRecording."+ MediaRecorder.OutputFormat.MPEG_4;
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

            Toast.makeText(VoiceCallActivity.this, "Recording started",
                    Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(VoiceCallActivity.this, new
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
                        Toast.makeText(VoiceCallActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(VoiceCallActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
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


    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);

    }

    public String CreateRandomAudioFileName(int string){
        String randomStr = UUID.randomUUID().toString().substring(0,5);
        Log.i(TAG, "CreateRandomAudioFileName: "+randomStr);
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
}