package com.inas.atroads.views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.inas.atroads.R;
import com.inas.atroads.util.localData.AppUtils;

import java.util.List;
import java.util.Locale;

public class SelectCurrentLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, android.location.LocationListener {

    private Context mContext;
    Button currentLocationBtn;
    Marker mCurrLocationMarker;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    GoogleApiClient mGoogleApiClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    android.location.Location mLastLocation;
    LocationRequest mLocationRequest;
    LocationManager mLocationManager;
    public static LatLng latLng, mCenterLatLong;
    private static String TAG = "MAP LOCATION";
    private AddressResultReceiver mResultReceiver;
    protected String currentLocationstart, mStateOutput, mCityOutput, mAreaOutput, provider, mAddressOutput;
    private TextView locationTv;
    private String Description, Location;
    private String EventName, StartDate, EndDate,start_time,end_time,location,eventDetails;
    private String ImageStr = "";
    private String FROMACTIVITY = "";
    private Double Latitude,Longitude;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //assining layout
        setContentView(R.layout.activity_select_current_location);
        mContext = getApplicationContext();
        currentLocationBtn = findViewById(R.id.currentLocationBtn);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
       // showProgressDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
            }
        },1000);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }
        fetchLastLocation();

        setViews();
       // Intent intent = new Intent(SelectCurrentLocationActivity.this, TrackerActivity.class);
        //startActivity(intent);

    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in ImageVideoActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));

            }
        }
    }

    /**
     * Updates the address in the UI.
     */
    protected void displayAddressOutput() {
        //  mLocationAddressTextView.setText(mAddressOutput);
        try {
            if (mAreaOutput != null) {
                // mLocationAddress.setText(mAddressOutput);
                Toast.makeText(mContext, mAreaOutput, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mGoogleMap = googleMap;

        if (mLastLocation != null) {
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            googleMap.addMarker(markerOptions);
            // Toast.makeText(mContext, "" + latLng, Toast.LENGTH_SHORT).show();
        }

        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;

                mGoogleMap.clear();

                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    startIntentService(mLocation);

                    Geocoder geocoder = new Geocoder(SelectCurrentLocationActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(
                                mCenterLatLong.latitude,
                                mCenterLatLong.longitude,
                                1);
                    } catch (Exception ioException) {
                        Log.e("", "Error in getting address for the location");
                    }

                    if (addresses == null || addresses.size() == 0) {
//                        Toast.makeText(mContext, "No address found for the location", Toast.LENGTH_SHORT).show();

                    } else {
                        Address address = addresses.get(0);
                        StringBuffer addressDetails = new StringBuffer();

                        addressDetails.append(address.getFeatureName());
                        addressDetails.append("\n");

                        addressDetails.append(address.getThoroughfare());
                        addressDetails.append("\n");

                        addressDetails.append("Locality: ");
                        addressDetails.append(address.getLocality());
                        addressDetails.append("\n");

                        addressDetails.append("County: ");
                        addressDetails.append(address.getSubAdminArea());
                        addressDetails.append("\n");

                        addressDetails.append("State: ");
                        addressDetails.append(address.getAdminArea());
                        addressDetails.append("\n");

                        addressDetails.append("Country: ");
                        addressDetails.append(address.getCountryName());
                        addressDetails.append("\n");

                        addressDetails.append("Postal Code: ");
                        addressDetails.append(address.getPostalCode());
                        addressDetails.append("\n");
                        latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        Latitude = address.getLatitude();
                        Longitude = address.getLongitude();

                        currentLocationstart = address.getAddressLine(0);
                        locationTv = findViewById(R.id.locationTv);
                        locationTv.setText(""+currentLocationstart);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                        googleMap.addMarker(markerOptions);

                        //  Toast.makeText(mContext, "" + currentLocationstart, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    mLastLocation = location;

                    mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFrag.getMapAsync(SelectCurrentLocationActivity.this);

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //fetchLastLocation();
                }
                break;
        }
    }

    private void setViews() {
        currentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(SelectCurrentLocationActivity.this, ChatActivity.class);
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("FROMACTIVITY","SelectCurrentLocationActivity");
                intent.putExtra("CurrentLocation", currentLocationstart);
                intent.putExtra("latitude",Latitude);
                intent.putExtra("longitude",Longitude);
                setResult(101,intent);
                finish();
                //startActivity(intent);
            }
        });
    }

    public String getCurrentLocationstart() {
        return currentLocationstart;
    }

    public void setCurrentLocationstart(String currentLocationstart) {
        this.currentLocationstart = currentLocationstart;
    }




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
