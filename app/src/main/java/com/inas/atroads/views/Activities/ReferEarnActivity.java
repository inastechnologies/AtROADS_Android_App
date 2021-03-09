package com.inas.atroads.views.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    LinearLayout shareBtn,btn_whatsapp_share;
    ImageView iv_copy;
    TextView txt_invite_code,tv_totalcoins;
    String refferalCode;
    int coins;

    ClipboardManager myClipboard;
    ClipData myClip;

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
        shareBtn = findViewById(R.id.btn_others_share);
        btn_whatsapp_share= findViewById(R.id.btn_whatsapp_share);
        iv_copy= findViewById(R.id.iv_copy);
        txt_invite_code= findViewById(R.id.txt_invite_code);
        tv_totalcoins= findViewById(R.id.tv_totalcoins);

        GetSharedPrefs();
        txt_invite_code.setText(refferalCode.toString());
        tv_totalcoins.setText(""+coins +" Coins");

        myClipboard =  (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String content = "Hey..! Im using ATROADS App. You can download this By the following link.\n https://play.google.com/store/apps/details?id=com.inas.atroads";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ATROADS");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        btn_whatsapp_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // val imgUri = Uri.parse(pictureFile.getAbsolutePath())
                Intent whatsappIntent = new Intent(android.content.Intent.ACTION_SEND);
                whatsappIntent.setType ("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                String content = "Hey..! Im using ATROADS App. You can download this By the following link \n https://play.google.com/store/apps/details?id=com.inas.atroads \n and your refferal code is: "+refferalCode;
                whatsappIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        content);
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(whatsappIntent);
                } catch (Exception e) {
                    Toast.makeText(ReferEarnActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
                }
            }
        });

        iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClip = ClipData.newPlainText("text", txt_invite_code.getText().toString());
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(ReferEarnActivity.this, "Text Copied", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetSharedPrefs()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
        refferalCode = pref.getString("refferalCode","");
        coins= pref.getInt("coins",0);
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
