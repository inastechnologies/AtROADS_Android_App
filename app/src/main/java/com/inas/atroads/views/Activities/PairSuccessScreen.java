package com.inas.atroads.views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.APIConstants;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.CustomApplication;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.util.Utilities;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.util.localData.FetchURL;
import com.inas.atroads.views.Interface.TaskLoadedCallback;
import com.inas.atroads.views.UI.EnterUPIDetailsActivity;
import com.inas.atroads.views.UI.MobileNumberRegisterScreen;
import com.inas.atroads.views.UI.PairedDetailsScreen;
import com.inas.atroads.views.UI.SOSActivity;
import com.inas.atroads.views.UI.SchedulingRideScreen;
import com.inas.atroads.views.UI.UploadQRActivity;
import com.inas.atroads.views.model.EndRideRequestModel;
import com.inas.atroads.views.model.EndRideResponseModel;
import com.inas.atroads.views.model.GetUserInfoRequestModel;
import com.inas.atroads.views.model.GetUserInfoResponseModel;
import com.inas.atroads.views.model.OnGoingRideRequestModel;
import com.inas.atroads.views.model.OnGoingRidesResponseModel;
import com.inas.atroads.views.model.RouteSourceDestRequestModel;
import com.inas.atroads.views.model.RouteSourceDestResponseModel;
import com.inas.atroads.views.model.ScheduleRideNotifiyRequestModel;
import com.inas.atroads.views.model.ScheduleRideNotifyResponseModel;
import com.inas.atroads.views.model.StartRideForPairedUserRequestModel;
import com.inas.atroads.views.model.StartRideForPairedUsersResponseModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class PairSuccessScreen extends BaseActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener,
        TaskLoadedCallback {


    private static final String TAG = "PairSuccessScreen";
    private static final String DEFAULT = "N/A";
    private static final int BOUND_PADDING = 100;
    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private Subscription mSubscription;
    String Username, Email, Mobile;
    int UserId;
    CircleImageView profilePicInSideBar;
    private TextView UserNameTv, MobileNoTv, EmailTv, VehicleNoTv,auto_number,tv_ride,pairUserName;
    private AppBarConfiguration mAppBarConfiguration;
    private Button rideButton;
    private String RideStatus;
    private int UserRideId;
    private String AutoNo;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final float ZOOM_LEVEL = 12;
    private double CurrentLatitude, CurrentLongitude;
    private Polyline currentPolyline;
    private String MypflPic;
    private String str_origin, str_dest;
    private Button emergency_Btn,callBtn;
    LinearLayout lin_shareLoc,lin_endride,lin_chat,lin_call;
    ImageView iv_ride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_success_screen);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle(getString(R.string.PairedSuccessfully));
        // toolbar.setSubtitle(getString(R.string.PairedSuccessfullyMsg));
        setSupportActionBar(toolbar);
        SetNavigationDrawer();
        isNetworkAvailable(PairSuccessScreen.this);
        RideStatus = getIntent().getStringExtra("RideStatus");
        UserRideId = getIntent().getIntExtra("UserRideId", 0);
        AutoNo = getIntent().getStringExtra("AutoNo");
        VehicleNoTv = findViewById(R.id.VehicleNoTv);
        auto_number = findViewById(R.id.auto_number);
        if (AutoNo.equals("")) {
            VehicleNoTv.setVisibility(View.INVISIBLE);
        } else {
            VehicleNoTv.setVisibility(View.INVISIBLE);
            VehicleNoTv.setText("Your Auto Number is " + AutoNo);
            auto_number.setText("" + AutoNo);
        }

        callBtn= findViewById(R.id.callBtn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogWithOneBtn(PairSuccessScreen.this,"Request for CALL","You will get call from a number to connect with paired user!!", "Call Request",new Runnable() {
                    @Override
                    public void run() {
                        showProgressDialog();
                        callingAPI();
                    }
                });
            }
        });

        lin_call= findViewById(R.id.lin_call);
        lin_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogWithOneBtn(PairSuccessScreen.this,"Request for CALL","You will get call from a number to connect with paired user!!", "Call Request",new Runnable() {
                    @Override
                    public void run() {
                        showProgressDialog();
                        callingAPI();
                    }
                });
            }
        });
        pairUserName = findViewById(R.id.pairUserName);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("PairedUserTwoPref", 0); // 0 - for private mode
        String pairedUserName = pref.getString("userTwoName", "");

        String sourceString = "Ride with- "+"<b>" + pairedUserName + "</b> ";
        pairUserName.setText(Html.fromHtml(sourceString));
        //pairUserName.setText("Ride with- "+pairedUserName);



//        SetRideButton();
        CallOnGoingRideAPI();
        SetRideButton();
        RouteSourceDestDetailsAPI();
        setChatBtn();
    }

    private void setChatBtn() {
        lin_chat = findViewById(R.id.lin_chat);
        lin_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(PairSuccessScreen.this, UsersActivity.class));
            }
        });
    }

    private void SetShareBtn(String userSourceAddress, String userDestAddress) {
        emergency_Btn = findViewById(R.id.emergency_Btn);
        emergency_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sosintent = new Intent(PairSuccessScreen.this, SOSActivity.class);
                sosintent.putExtra("FROMACTIVITY", "HomeScreen");
                startActivity(sosintent);
            }
        });

        lin_shareLoc= findViewById(R.id.lin_shareLoc);
        lin_shareLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String content = "This is My Auto Number: " + AutoNo + "\n" + "Source: " + userSourceAddress + "\n" + "Dest: " + userDestAddress;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Ride Details");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        GetLastKnownLocation();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
        mMap.setOnMarkerClickListener(this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /****************************START OF GetLastKnownLocation**************************/
    private void GetLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            String addressOfLocation = GetAddressFromLatLng(location.getLatitude(), location.getLongitude());
                            CurrentLatitude = location.getLatitude();
                            CurrentLongitude = location.getLongitude();
//                            Bitmap bitmap = Utilities.DrawableToBitmap(PairSuccessScreen.this,R.drawable.person_30px);
//                            SetMarkerOnMap(location.getLatitude(),location.getLongitude(),bitmap);
                        }
                    }
                });
    }
    /****************************END OF GetLastKnownLocation**************************/

    /*
     * Custom bitmap Conversion for Marker
     * */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(100, 150, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    /****************************START OF SetMarkerOnMap**************************/
    /**
     *
     * @param latitude
     * @param longitude
     */
    private void SetMarkerOnMap(Double latitude,Double longitude,Bitmap bitmap)
    {
        //Place location marker
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        String addressOfLocation = GetAddressFromLatLng(latitude, longitude);
        markerOptions.title(addressOfLocation+"").snippet(addressOfLocation).visible(true);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
      //  markerOptions.icon(bitmapDescriptorFromVector(PairSuccessScreen.this,vectorResId));
        mMap.addMarker(markerOptions);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));

    }
    /****************************END OF SetMarkerOnMap**************************/


    /****************************START OF GetAddressFromLatLng**************************/
    /**
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public String GetAddressFromLatLng(Double latitude,Double longitude)
    {
        List<Address> addresses;
        String address = "";
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

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.person_30px));
//        mCurrLocationMarker = mMap.addMarker(markerOptions);
//
//        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /**********************************START OF SET RIDE BUTTON************************/
    private void SetRideButton()
    {
        lin_endride = findViewById(R.id.lin_endride);
        tv_ride= findViewById(R.id.tv_ride);
        iv_ride= findViewById(R.id.iv_ride);
        //rideButton.setTextColor(Color.WHITE);
        if(RideStatus.equals("StartRide"))
        {
            tv_ride.setText("Start Ride");
            iv_ride.setImageResource(R.drawable.yourride);
        }else {
            tv_ride.setText("End Ride");
            iv_ride.setImageResource(R.drawable.ic_close_black_24dp);
            //rideButton.setBackgroundResource(R.drawable.round_rect_red);
        }
        lin_endride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_ride.getText().toString().equals("Start Ride"))
                {
                    StartRideForPairedUserAPI(AutoNo);
                }else {
                    DialogWithTwoButtons(PairSuccessScreen.this, "Attention!!", getString(R.string.AreYouSureToEnd), getString(R.string.Yes), new Runnable() {
                        @Override
                        public void run() {
                            CallEndRideAPI();
                        }
                    }, getString(R.string.No), new Runnable() {
                        @Override
                        public void run() {

                        }
                    });


                }
            }
        });
    }



    /************************************START OF NAVIGATION DRAWER***************************/
    /*
          Nav Drawer
        */
    private void SetNavigationDrawer()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_gmaps_screen);
        profilePicInSideBar = (CircleImageView)hView.findViewById(R.id.ProfilePicImg);
        profilePicInSideBar.setImageResource(R.drawable.profile);
        profilePicInSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PairSuccessScreen.this, EditProfileScreen.class);
                intent.putExtra("FROMACTIVITY","PairSuccessScreen");
                startActivity(intent);
            }
        });
        UserNameTv = (TextView)hView.findViewById(R.id.NameTv);
        UserNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PairSuccessScreen.this, EditProfileScreen.class);
                intent.putExtra("FROMACTIVITY","PairSuccessScreen");
                startActivity(intent);
            }
        });

        MobileNoTv = (TextView)hView.findViewById(R.id.phnNumber);
        MobileNoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeScreen.this, ProfileScreen.class);
//                intent.putExtra("FROMACTIVITY","GMapsScreen");
//                startActivity(intent);
            }
        });
        EmailTv = (TextView)hView.findViewById(R.id.email);
        EmailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(GMapsScreen.this, ProfileScreen.class);
//                intent.putExtra("FROMACTIVITY","GMapsScreen");
//                startActivity(intent);
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        Menu menuNav=navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.scheduling_ride);
        nav_item2.setEnabled(false);
        nav_item2.setVisible(false);
        //Menu nav_Menu = navigationView.getMenu();
        //nav_Menu.findItem(R.id.scheduling_ride).setVisible(Gone);

        Button nav_button = findViewById(R.id.nav_button);
        nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        GetSharedPrefs();
    }

    /************************************END OF NAVIGATION DRAWER***************************/



    /**********************************START OF SHARED PREFERENCES**************/

     /*
        GetSharedPrefs
        */
    private void GetSharedPrefs()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
        UserId = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Email =  pref.getString("email_id",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
        CallGetUserInfoAPI(UserId);
    }

    /**********************************END OF SHARED PREFERENCES**************/




/********************************START OF ONNAVIGATIONITEMSELECTED******************/
    /**
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.Covid_nav:
                Intent covidIntent = new Intent(PairSuccessScreen.this, WebViewActivity.class);
                covidIntent.putExtra("titile", "Covid 19");
                covidIntent.putExtra("url", "www.atroads.com/covid19/");
                startActivity(covidIntent);
                break;

            case R.id.Emergency_nav:
                Intent sosintent = new Intent(PairSuccessScreen.this, SOSActivity.class);
                sosintent.putExtra("FROMACTIVITY", "HomeScreen");
                startActivity(sosintent);
                break;

            case R.id.scheduling_ride:
                menuItem.setVisible(false);
                break;

            case R.id.youRide_nav:

                Intent i = new Intent(PairSuccessScreen.this, YourRidesActivity.class);
                i.putExtra("FROMACTIVITY", "HomeScreen");
                startActivity(i);
                break;

            case R.id.wallet_nav:
//                Intent intent = new Intent(PairSuccessScreen.this, WalletActivity.class);
//                intent.putExtra("FROMACTIVITY", "HomeScreen");
//                startActivity(intent);
                break;

            case R.id.refer_nav:
                Intent pricingIntent = new Intent(PairSuccessScreen.this, ReferEarnActivity.class);
                // pricingIntent.putExtra("Url", Constants.SubscriptionURL);
                startActivity(pricingIntent);
                break;

            case R.id.help_nav:
                Intent termsIntent = new Intent(PairSuccessScreen.this, HelpActivity.class);
//                termsIntent.putExtra("Url", Constants.TermsAndConditionsURL);
                startActivity(termsIntent);
                break;

            case R.id.howitworks_nav:
                Intent workIntent = new Intent(PairSuccessScreen.this, WebViewActivity.class);
                workIntent.putExtra("titile", "How It Work?");
                workIntent.putExtra("url", "http://atroads.com/faq/");
                startActivity(workIntent);
                break;


            case R.id.settings_nav:
                Intent privacyIntent = new Intent(PairSuccessScreen.this, SettingsActivity.class);
                //privacyIntent.putExtra("Url", Constants.PrivacyAgreementURL);
                startActivity(privacyIntent);
                break;

            case R.id.terms_con_nav:
                Intent feedIntent = new Intent(PairSuccessScreen.this, WebViewActivity.class);
                feedIntent.putExtra("titile", "Terms & Condition");
                feedIntent.putExtra("url", "http://atroads.com/terms-conditions/");
                startActivity(feedIntent);
                break;

            case R.id.privacy_policy_nav:
                Intent Acintent = new Intent(PairSuccessScreen.this, WebViewActivity.class);
                Acintent.putExtra("titile", "Privacy Policy");
                Acintent.putExtra("url", "http://atroads.com/privacy-policy/");
                startActivity(Acintent);
                break;


            case R.id.aboutus_nav:
                Intent aboutIntent = new Intent(PairSuccessScreen.this, WebViewActivity.class);
                aboutIntent.putExtra("titile", "About Us");
                aboutIntent.putExtra("url", "http://atroads.com/ourself/");
                startActivity(aboutIntent);
                break;


            case R.id.Show_qr:
                Intent qrIntent = new Intent(PairSuccessScreen.this, UploadQRActivity.class);
                //privacyIntent.putExtra("Url", Constants.PrivacyAgreementURL);
                startActivity(qrIntent);
                break;

            case R.id.logout_nav:
                DialogWithTwoButtons(PairSuccessScreen.this, getString(R.string.Logout), getString(R.string.AreYouSure), getString(R.string.Yes), new Runnable() {
                    @Override
                    public void run() {
                        Intent intent1 = new Intent(PairSuccessScreen.this, MobileNumberRegisterScreen.class);
                        startActivity(intent1);
                        SharedPreferences settings = getApplicationContext().getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
                        settings.edit().remove("user_id").commit();
                        finish();
                    }
                }, getString(R.string.No), new Runnable() {
                    @Override
                    public void run() {

                    }
                });

                break;
        }

        return false;
    }
    /********************************END OF ONNAVIGATIONITEMSELECTED******************/

    /**
     *
     * @param context
     * @param Title
     * @param Msg
     * @param ButtonName
     * @param runnable
     * @param secondButtonName
     * @param secondRunnable
     */
    public static void DialogWithTwoButtons(Context context, String Title, String Msg, String ButtonName, final Runnable runnable,String secondButtonName, final Runnable secondRunnable)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_with_2buttons);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        TextView title = (TextView) dialog.findViewById(R.id.TimeTv);
        title.setText(Title);
        if(title.getText().toString().equals(""))
        {
            title.setBackgroundColor(Color.WHITE);
        }
        TextView msg = (TextView) dialog.findViewById(R.id.DescTv);
        msg.setText(Msg);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        okBtn.setText(ButtonName);
        // if ok button is clicked, close the custom dialog
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.run();
                dialog.dismiss();
            }
        });

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setText(secondButtonName);
        // if decline button is clicked, close the custom dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondRunnable.run();
                dialog.dismiss();
            }
        });
    }

    /********************************START OF GET USER INFO*******************************/
    /*
     * CallGetUserInfoAPI
     * */
    private void CallGetUserInfoAPI(int UserId){

        JsonObject object = GetUserInfoObject(UserId);
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.GetUserInfoResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUserInfoResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(GetUserInfoResponseModel mResponse) {
                        Log.i(TAG, "GetUserInfoResponse: "+mResponse);

                        // Toast.makeText(PairActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            Toast.makeText(PairSuccessScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            String username = mResponse.getResult().get(0).getName();
                            String mobileNo = mResponse.getResult().get(0).getMobileNumber();
                            MypflPic = mResponse.getResult().get(0).getProfilePic();
                            Log.i(TAG, "pflpic url: "+ APIConstants.IMAGE_URL+MypflPic);
                            UserNameTv.setText(username+"");
                            MobileNoTv.setText(mobileNo+"");
                            LoadImageFromUrl(PairSuccessScreen.this, APIConstants.IMAGE_URL+MypflPic,profilePicInSideBar);
                        }
                    }
                });

    }

    /**
     * Json object of GetUserInfoObject
     *
     * @return
     */
    private JsonObject GetUserInfoObject(int UserId)
    {
        GetUserInfoRequestModel requestModel = new GetUserInfoRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    /**
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    private void LoadImageFromUrl(Context context, String imageUrl, ImageView imageView)
    {
        Picasso.with(context).load(imageUrl).error(R.drawable.profile).into(imageView);
    }

    /********************************END OF GET USER INFO*******************************/


    /********************************START OF END RIDE API*******************************/
    /*
     * CallEndRideAPI
     * */
    private void CallEndRideAPI(){

        JsonObject object = EndRideObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.EndRideResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EndRideResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                                Log.i(TAG, "onError: "+  ((HttpException) e).response().errorBody().string());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(EndRideResponseModel mResponse) {
                        Log.i(TAG, "EndRideResponseModel: "+mResponse);

                         Toast.makeText(PairSuccessScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            //Toast.makeText(PairSuccessScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            Intent i = new Intent(PairSuccessScreen.this,BillingDetailsActivity.class);
                            i.putExtra("UserId",UserId);
                            i.putExtra("UserRideId",UserRideId);
                            i.putExtra("AutoNumber",mResponse.getResult().get(0).getAutoNumber());
                            i.putExtra("FareType",mResponse.getResult().get(0).getType());
                            startActivity(i);
                        }
                    }
                });

    }

    /**
     * Json object of EndRideObject
     *
     * @return
     */
    private JsonObject EndRideObject()
    {
//        LatLng currentLatLng = new LatLng(CurrentLatitude,CurrentLongitude);
//        String cLatLng = String.valueOf(currentLatLng);
//        String currentlatLngEndStr=cLatLng.replace("lat/lng:","");
//        String newcurrentlatLngStr=currentlatLngEndStr.replaceAll("\\(","[")
//                .replaceAll("\\)","]");
        EndRideRequestModel requestModel = new EndRideRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setUserRideId(UserRideId);
       // requestModel.setEnd_lat_long(newcurrentlatLngStr);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF END RIDE *******************************/

    /**************************START OF START RIDE API*********************************/
    /*
     * StartRideForPairedUserAPI
     * */
    public void StartRideForPairedUserAPI(String AutoNo) {

        JsonObject object = StartRideForPairedUserDetailsObject(AutoNo);
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.StartRideForPairedUsersResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StartRideForPairedUsersResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(StartRideForPairedUsersResponseModel mRespone) {
                        Log.i(TAG, "StartRideForPairedUsersResponseModel: "+mRespone);
                        Toast.makeText(PairSuccessScreen.this, "Ride Started", Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            UserId = mRespone.getResult().get(0).getUserId();
                            UserRideId = mRespone.getResult().get(0).getUserRideId();
                            tv_ride.setText("End Ride");
                            iv_ride.setImageResource(R.drawable.ic_close_black_24dp);
                            //rideButton.setText("End Ride");
                           // rideButton.setBackgroundResource(R.drawable.round_rect_red);
                            //rideButton.setBackgroundColor(Color.RED);
                            RideStatus = "RideStarted";
                            CustomDialogWithOneBtn(PairSuccessScreen.this,"Success",mRespone.getMessage(),"Ok", new Runnable() {
                                @Override
                                public void run() {
//                                    Intent intent = new Intent(PairSuccessScreen.this, PairSuccessScreen.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                    finish();
                                }
                            });
                        }
                        else {

                        }

                        //   startRideBtn.setText("End Ride");
                    }
                });

    }

    /**
     * Json object of StartRideForPairedUserDetailsObject
     * @return
     */
    private JsonObject StartRideForPairedUserDetailsObject(String AutoNo)
    {
        Log.i(TAG, "StartRideForPairedUserDetailsObject: "+UserRideId+"-->AutoNo: "+AutoNo);
//        LatLng currentLatLng = new LatLng(CurrentLatitude,CurrentLongitude);
//        String cLatLng = String.valueOf(currentLatLng);
//        String currentlatLngStartStr=cLatLng.replace("lat/lng:","");
//        String newcurrentlatLngStr=currentlatLngStartStr.replaceAll("\\(","[")
//                .replaceAll("\\)","]");
        StartRideForPairedUserRequestModel requestModel = new StartRideForPairedUserRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setUserRideId(UserRideId);
      //  requestModel.setP_start_lat_long(newcurrentlatLngStr);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /**************************END OF START RIDE API*********************************/

/**************************START OF CustomDialogWithOneBtn*********************************/
    /**
     *
     * @param context
     * @param Title
     * @param Msg
     * @param buttonNam1
     * @param runnable
     */
    public void CustomDialogWithOneBtn(Context context, String Title, String Msg, String buttonNam1, Runnable runnable)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogwithonebtn);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView title = (TextView) dialog.findViewById(R.id.TitleTv);
        title.setText(Title);
        TextView msg = (TextView) dialog.findViewById(R.id.MsgTv);
        msg.setText(Msg);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        okBtn.setText(buttonNam1);
        // if decline button is clicked, close the custom dialog
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                runnable.run();
            }
        });

    }
    /**************************END OF CustomDialogWithOneBtn*********************************/


    /**************************START OF RouteSourceDestDetailsAPI*********************************/
    /*
     * RouteSourceDestDetailsAPI
     * */
    public void RouteSourceDestDetailsAPI() {

        JsonObject object = RouteSourceDestDetailsObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.RouteSourceDestDetailsResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RouteSourceDestResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(RouteSourceDestResponseModel mRespone) {
                        Log.i(TAG, "RouteSourceDestResponseModel: "+mRespone);
                       // Toast.makeText(PairSuccessScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            Bitmap bmp = null;
                            List<LatLng> polyList = new ArrayList<>();
//                            UserId = mRespone.getResult().get(0).getUserId();
                            for (int i = 0; i < mRespone.getResult().size(); i++)
                            {
                                List<Double> user_dest_lat_long = mRespone.getResult().get(i).getUserDestLatLong();
                                List<Double> user_source_lat_long = mRespone.getResult().get(i).getUserSourceLatLong();
                                LatLng PinlatLng = new LatLng(user_source_lat_long.get(0),user_source_lat_long.get(1));
                                LatLng droplatLng = new LatLng(user_dest_lat_long.get(0),user_dest_lat_long.get(1));
                                polyList.add(PinlatLng);
                                polyList.add(droplatLng);
                                new FetchURL(PairSuccessScreen.this).execute(getUrl(PinlatLng, droplatLng,
                                        "driving"), "driving");
                                String UserPflPic = mRespone.getResult().get(i).getProfile_pic();
//                                if(UserPflPic.equals(""))
//                                {
//                                     bmp = DrawableToBitmap(PairSuccessScreen.this,R.drawable.person_30px);
//                                }
//                                else {
//                                     bmp = ConvertURLImageToBitmap(APIConstants.IMAGE_URL+UserPflPic);
//                                }

//                                bmp = ConvertURLImageToBitmap(APIConstants.IMAGE_URL+UserPflPic);
//                                Bitmap bitmap = Bitmap.createScaledBitmap(bmp, 60, 60, false);
                                Bitmap pinbitmap = Utilities.DrawableToBitmap(PairSuccessScreen.this,R.drawable.person_30px);
                                Bitmap dropbitmap = Utilities.DrawableToBitmap(PairSuccessScreen.this,R.drawable.red_person30px);
                                SetMarkerOnMap(PinlatLng.latitude,PinlatLng.longitude,pinbitmap);
                                SetMarkerOnMap(droplatLng.latitude,droplatLng.longitude,dropbitmap);
                                SetShareBtn(mRespone.getResult().get(0).getUserSourceAddress(),mRespone.getResult().get(0).getUserDestAddress());
//                                try {
//                                URL url = new URL(APIConstants.IMAGE_URL+UserPflPic);
//                                HttpURLConnection conn = null;
//                                    conn = (HttpURLConnection) url.openConnection();
//                                    conn.setDoInput(true);
//                                    conn.connect();
//                                    InputStream is = conn.getInputStream();
//                                    Bitmap bmImg = BitmapFactory.decodeStream(is);
//                                    Bitmap bitmapResized = Bitmap.createScaledBitmap(bmImg, 30, 30, false);
//                                    SetMarkerOnMap(PinlatLng.latitude,PinlatLng.longitude,bitmapResized);
//                                    SetMarkerOnMap(droplatLng.latitude,droplatLng.longitude,bitmapResized);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }


                               // DrawPolyLineOnMap(polyList);

                            }
                         //   drawPolyLineOnMap(polyList);

                        }
                        else {

                        }
                    }
                });

    }

    /**
     * Json object of RouteSourceDestDetailsObject
     * @return
     */
    private JsonObject RouteSourceDestDetailsObject()
    {
        Log.i(TAG, "RouteSourceDestDetailsObject: "+UserRideId);

        RouteSourceDestRequestModel requestModel = new RouteSourceDestRequestModel();
        requestModel.setUserRideId(UserRideId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    /********************************START OF CallOnGoingRideAPI*******************************/
    /*
     * CallOnGoingRideAPI
     * */
    private void CallOnGoingRideAPI(){

        JsonObject object = OnGoingRideObject();
        AtroadsService service = ServiceFactory.createRetrofitService(PairSuccessScreen.this, AtroadsService.class);
        mSubscription = service.OnGoingRidesResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OnGoingRidesResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(OnGoingRidesResponseModel mResponse) {
                        Log.i(TAG, "OnGoingRidesResponseModel: "+mResponse);
                       // Toast.makeText(getActivity(), mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            RideStatus = "StartRide";
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            if(mResponse.getResult().size() == 0)
                            {
                               RideStatus = "StartRide";
                            }
                            else {
                               RideStatus = "RideStarted";
                            }

                        }
                    }
                });

    }

    /**
     * Json object of RidesHistoryObject
     *
     * @return
     */
    private JsonObject OnGoingRideObject()
    {
        OnGoingRideRequestModel requestModel = new OnGoingRideRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    /********************************START OF GET URL*************************/
    /*
     * latitude and longitude
     * */
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }


    /********************************END OF GET URL*************************/


    /**
     *
     * @param polyList
     */
    private void DrawPolyLineOnMap(List<LatLng> polyList)
    {
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int z = 0; z < polyList.size(); z++) {
            LatLng point = polyList.get(z);
            options.add(point);
        }
        currentPolyline = mMap.addPolyline(options);
    }
    /**************************END OF START RIDE API*********************************/

    // Draw polyline on map
    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.BLACK);
        polyOptions.width(5);
        polyOptions.addAll(list);

        mMap.clear();
        mMap.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }

        final LatLngBounds bounds = builder.build();

        //BOUND_PADDING is an int to specify padding of bound.. try 100.
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, BOUND_PADDING);
        mMap.animateCamera(cu);
    }



    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PairSuccessScreen.this, HomeMapsActivity.class);
        startActivity(i);
    }

    private void callingAPI(){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("PairedUserPref", 0); // 0 - for private mode
        UserRideId = pref.getInt("user_ride_id",0);

        JsonObject object = callingObject();
        AtroadsService service = ServiceFactory.createRetrofitService(PairSuccessScreen.this, AtroadsService.class);
        mSubscription = service.callingApi(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ScheduleRideNotifyResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(ScheduleRideNotifyResponseModel mResponse) {
                        Log.i(TAG, "ScheduleRideNotifyResponseModel: "+mResponse);
//                        Toast.makeText(YourBillScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0) {
                            hideProgressDialog();
                            finish();
                        }
                        else if(mResponse.getStatus() == 1) {
                            hideProgressDialog();
                            Toast.makeText(PairSuccessScreen.this,"We are connecting for you!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private JsonObject callingObject()
    {
        ScheduleRideNotifiyRequestModel requestModel = new ScheduleRideNotifiyRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setuser_ride_id(UserRideId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }
}
