package com.inas.atroads.views.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.inas.atroads.R;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.views.adapter.StartUpVPAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class StartUpActivity extends BaseActivity {

    ViewPager viewpager_view;
    int images[] = {R.drawable.intro_1,R.drawable.auto_ride, R.drawable.upi_onboarding_pic5};
    String text[] = {"","Ride With Us!", "We care for your security!"};
    StartUpVPAdapter myCustomPagerAdapter;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        viewpager_view = findViewById(R.id.viewpager_view);


        myCustomPagerAdapter = new StartUpVPAdapter(StartUpActivity.this, images,text);
        viewpager_view.setAdapter(myCustomPagerAdapter);


        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewpager_view.post(new Runnable(){
                    @Override
                    public void run() {
                        if(viewpager_view.getCurrentItem()==2){
                            timer.cancel();
                        }else {
                            viewpager_view.setCurrentItem((viewpager_view.getCurrentItem() + 1) % images.length);
                        }
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1500, 1500);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isStartUp", true); // Storing string
        editor.commit();
    }
}
