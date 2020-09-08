package com.inas.atroads.views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.inas.atroads.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inas.atroads.services.CustomApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TrackerDisplayActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = TrackerDisplayActivity.class.getSimpleName();
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private GoogleMap mMap;
    ArrayList<String> al = new ArrayList<>();
    private int UserId;
    private String Mobile,Email;
    private String DEFAULT = "N/A";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_display);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        showProgressDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
            }
        },2000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Authenticate with Firebase when the Google map is loaded
        mMap = googleMap;
        mMap.setMaxZoomPreference(16);
        loginToFirebase();
    }

    private void loginToFirebase() {
//        String email = getString(R.string.firebase_email);
//        String password = getString(R.string.firebase_password);
        // Authenticate with Firebase and subscribe to updates
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginCreds", 0); // 0 - for private mode
//        String email = pref.getString("email","N/A");
//        String password = pref.getString("password","N/A");


        SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
        UserId = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Email =  pref.getString("email_id",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId +"-->"+Mobile);
        String email = Email;
        String password = Mobile;
        Log.i(TAG, "loginToFirebase: "+ email +"-->"+ password);
        subscribeToUpdates();
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    private void subscribeToUpdates() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getString(R.string.firebase_path));
        Log.i(TAG, "subscribeToUpdates: ");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.i(TAG, "onChildAdded: ");
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
                Log.i(TAG, "onChildChanged: ");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.i(TAG, "onChildMoved: ");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onChildRemoved: ");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setMarker(DataSnapshot dataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        String key = dataSnapshot.getKey();
        Log.i(TAG, "setMarker: key"+key);
        al.add(key);
        Log.i(TAG, "setMarker: number"+al);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("OtherCreds", 0); // 0 - for private mode
        int OtheruserId = pref.getInt("OtheruserId",0);
        Log.i(TAG, "setMarker: OtheruserId  "+OtheruserId);
        String OtherID = String.valueOf(OtheruserId);

        int Otherid = getIntent().getIntExtra("OtherID",0);
        String other = String.valueOf(Otherid);
        if(key.contains(other))
        {
            if(mMap!=null)
            {
                mMap.clear();
            }
            HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
            double lat = Double.parseDouble(value.get("latitude").toString());
            double lng = Double.parseDouble(value.get("longitude").toString());
            LatLng location = new LatLng(lat, lng);
            TextView locationTv = findViewById(R.id.locationTv);
            String locationName = GetAddressFromLatLng(lat,lng);
            locationTv.setText(locationName+"");
            MarkerOptions markerOptions = new MarkerOptions().position(location);
            mMap.addMarker(markerOptions).setTitle(locationName);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 20f));
            // mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }

//        if (!mMarkers.containsKey(key)) {
//            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)));
//        } else {
//            mMarkers.get(key).setPosition(location);
//        }
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (Marker marker : mMarkers.values()) {
//            Log.i(TAG, "setMarker: mMarkers.values()"+mMarkers.values());
//            builder.include(marker.getPosition());
//        }
        //mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),500));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),200));

    }


    /****************************START OF GetAddressFromLatLng**************************/
    /**
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public String GetAddressFromLatLng(Double latitude,Double longitude)
    {
        //Geocoder geocoder = null;
        List<Address> addresses;
        String address = "";
        //Geocoder geocoder = new Geocoder(HomeMapsActivity.this, Locale.getDefault());

//        Geocoder geocoder = CustomApplication.geoCoder;
        try {
            addresses = CustomApplication.geoCoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "GetAddressFromLatLng: "+address);

        return address;
    }


    /****************************END OF GetAddressFromLatLng**************************/



    // to intialize the Progress Dialog
    private void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }


    // to start the Progress Dialog
    public void showProgressDialog() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null)
                        initProgressDialog();
                    if (!mProgressDialog.isShowing())
                        mProgressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // to hide the Progress Dialog
    public void hideProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mProgressDialog = null;
                }
            }
        });
    }


}