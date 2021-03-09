package com.inas.atroads.views.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.inas.atroads.R;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.views.UI.MobileNumberRegisterScreen;
import com.inas.atroads.views.adapter.StartUpVPAdapter;

import java.util.Timer;

public class StartUpActivity extends BaseActivity {

    ViewPager viewpager_view;
    int images[] = {R.drawable.intro_1,R.drawable.auto_ride, R.drawable.intro_3};
    String text[] = {"","Ride With Us!", "Book Now!"};
    StartUpVPAdapter myCustomPagerAdapter;
    Timer timer;
    private int currentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        viewpager_view = findViewById(R.id.viewpager_view);
        ImageView forward= findViewById(R.id.forward);

        /*if(viewpager_view.getCurrentItem()==2){
            forward.setVisibility(View.GONE);
        }else{
            forward.setVisibility(View.VISIBLE);
        }*/

        myCustomPagerAdapter = new StartUpVPAdapter(StartUpActivity.this, images,text);
        viewpager_view.setAdapter(myCustomPagerAdapter);

        viewpager_view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("page", "page selected " + position);
                currentPage = position;
                if(currentPage==2){
                    forward.setVisibility(View.GONE);
                }else{
                    forward.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewpager_view.getCurrentItem()==2){
                    Intent i = new Intent(StartUpActivity.this, MobileNumberRegisterScreen.class);
                    startActivity(i);
                    finish();
                }else {

                    viewpager_view.setCurrentItem((viewpager_view.getCurrentItem() + 1) % images.length);
                }
            }
        });
        /*TimerTask timerTask = new TimerTask() {
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
        timer.schedule(timerTask, 1500, 1500);*/

        SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isStartUp", true); // Storing string
        editor.commit();
    }
}
