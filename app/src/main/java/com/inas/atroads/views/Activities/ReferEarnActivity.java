package com.inas.atroads.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.inas.atroads.R;
import com.inas.atroads.util.localData.BaseActivity;

import java.util.List;
import java.util.Locale;

import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class ReferEarnActivity extends BaseActivity {

    private Context mContext;
    private Toolbar toolbar;
    Address address;
    LatLng currentlatLng;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    LocationManager mLocationManager;
    private String provider;
    private static String TAG = "MAP LOCATION";
    FusedLocationProviderClient fusedLocationProviderClient;
    Button shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        //assining layout
        setContentView(R.layout.activity_refer_earn);

        isNetworkAvailable(ReferEarnActivity.this);
        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
        setViews();

    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.refer_nav));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });
    }

    private void setViews() {
        shareBtn = findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String content = "Hey..! Im using ATROADS App. You can download this By the following link.";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ATROADS");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }


    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(ReferEarnActivity.this, HomeScreen.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
        super.onBackPressed();
    }
}
