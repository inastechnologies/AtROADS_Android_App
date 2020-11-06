package com.inas.atroads.views.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.inas.atroads.R;
import com.inas.atroads.services.MyServiceToCheckIsAppClosed;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.Activities.StartUpActivity;
import com.sendbird.android.SendBird;

import static android.provider.UserDictionary.Words.APP_ID;
import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class SpalshScreen extends AppCompatActivity
{
    private String DEFAULT = "N/A";
    private  int user_id;
    private String TAG = SpalshScreen.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);
        isNetworkAvailable(SpalshScreen.this);

        SendBird.init("6CD9131B-E2D7-4F4B-AF9D-117518AC8E70", this);
        MoveToRegister();

       // startService(new Intent(this, MyServiceToCheckIsAppClosed.class));
    }
    /*
      This method is used to move from Splash screen to Register screen
    */
    private void MoveToRegister() {
        Log.i(TAG, "onCreate: Start Of MoveToRegister");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0);
                user_id = pref.getInt("user_id", 0);
                Log.i("user_id:", String.valueOf(user_id));
                if(user_id == 0) {

                    boolean landFlag = pref.getBoolean("isStartUp",false);

                    if(landFlag) {
                        Intent i = new Intent(SpalshScreen.this, MobileNumberRegisterScreen.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(SpalshScreen.this, StartUpActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent i = new Intent(SpalshScreen.this, HomeMapsActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        },1000);
        Log.i(TAG, "onCreate: End Of MoveToRegister");
    }



}
