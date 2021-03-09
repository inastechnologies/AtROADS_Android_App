package com.inas.atroads.views.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.APIConstants;
import com.inas.atroads.services.CustomApplication;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.util.localData.Constants;
import com.inas.atroads.util.localData.FetchURL;
import com.inas.atroads.util.localData.SharedPrefsData;
import com.inas.atroads.views.Interface.TaskLoadedCallback;
import com.inas.atroads.views.UI.PairedDetailsScreen;
import com.inas.atroads.views.UI.SuggestionsListScreen;
import com.inas.atroads.views.model.CancelRideDetailsRequestModel;
import com.inas.atroads.views.model.CancelRideDetailsResponseModel;
import com.inas.atroads.views.model.DeletePairRequestModel;
import com.inas.atroads.views.model.DeletePairResponseModel;
import com.inas.atroads.views.model.EditUserInfoRequestModel;
import com.inas.atroads.views.model.EditUserInfoResponseModel;
import com.inas.atroads.views.model.FindPairRequestModel;
import com.inas.atroads.views.model.FindPairResponseModel;
import com.inas.atroads.views.model.GetDetailsOfRideRequestModel;
import com.inas.atroads.views.model.GetDetailsOfRideResponseModel;
import com.inas.atroads.views.model.GetUserInfoRequestModel;
import com.inas.atroads.views.model.GetUserInfoResponseModel;
import com.inas.atroads.views.model.PairedUserDetailsRequestModel;
import com.inas.atroads.views.model.PairedUserDetailsResponseModel;
import com.inas.atroads.views.model.SuggestionsListModel;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class PairActivity extends BaseActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, TaskLoadedCallback {
    /**************************START OF DECLARATIONS************************/
    private Context mContext;
    private Toolbar toolbar;
    private EditText pinLocationEdt, dropLocationEdt;
    private String provider, currentLocationstart, currentLocationdrop, str_origin, str_dest;
    Intent intent;
    private Polyline currentPolyline;
    LatLng currentlatLngstart, currentlatLngdrop;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    GoogleApiClient mGoogleApiClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private static final int PLACE_PICKER_REQUEST = 1;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    LocationManager mLocationManager;
    Marker mCurrLocationMarker;
    Address address;
   // public static LatLng pairingcurrentlatLng;
    String currentlatLngstartStr, currentlatLngdropStr;
    String[] latstartStr, longstartStr, latdropStr, longdropstr;
    private static String TAG = "PairActivity";
    Button ridenowBtn,continueBtn;
    int USER_RIDES_ID;
    private static final String DEFAULT = "N/A";
    private String Mobile,Email,Username;
    private Subscription mSubscription;
    private int RideId;
    private TextView bottomTv;
    int UserId;
    private int user_ride_id,PSTATUS;
    private Handler handler;
    private String pinLoc,DropLoc;
    private Dialog DialogWithNoBtns;
    ProgressDialog pd;
    Dialog AutoNoDialog;
    Geocoder geocoder = CustomApplication.geoCoder;
    int PStatus = -1;
    public static List<SuggestionsListModel> suggestionsListModelList = new ArrayList<>();
    /**************************END OF DECLARATIONS************************/

    /**************************START OF ON CREATE************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        //assining layout
        setContentView(R.layout.activity_pair);
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        rippleBackground.startRippleAnimation();
        isNetworkAvailable(PairActivity.this);
        /* intializing and assigning ID's */
        initViews();

        GetSharedPrefs();
        GetRidePrefs();
       // userId = SharedPrefsData.getInt(mContext, "" + Constants.USERID, Constants.PREF_NAME);

        RideId = getIntent().getIntExtra("RideId",0);
        pinLoc = getIntent().getStringExtra("pinLoc");
        DropLoc = getIntent().getStringExtra("DropLoc");
        Log.i(TAG, "RideId: "+RideId);
        Log.i(TAG, "UserId: "+UserId);
        Log.i(TAG, "DropLoc: "+DropLoc);
        Log.i(TAG, "pinLoc: "+pinLoc);
        pinLocationEdt.setText(pinLoc+"");
        dropLocationEdt.setText(DropLoc+"");

        initPlace();
        /*
         * Current Location
         * */
        getCurrentLocation();
        /*
            Finding Pair
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FindPairAPI();
            }
        },15000);
    }

    /**************************END OF ONCREATE************************/

    /**************************START OF INIT VIEWS************************/
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.find_pair));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PairActivity.this, HomeScreen.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                SharedPrefsData.putString(mContext, Constants.STARTLOCATIONPLACE, " ", Constants.PREF_NAME);
//                intent.putExtra("currentadress", address.getAddressLine(0));
//                intent.putExtra("currentlatLng", currentlatLng);
//                intent.putExtra("fromactivity", "pairscreen");
//                intent.putExtra("flag", "3");
//                HomeActivity.isFocusInAutoComplete = "pin";
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        intent = getIntent();
        if (intent != null) {
            currentlatLngstartStr = getIntent().getStringExtra("currentlatLngstart");

            Log.d(TAG, "initViews: " + currentlatLngstartStr);
            String newcurrentlatLngstartStr = currentlatLngstartStr.replaceAll("\\[", "")
                    .replaceAll("\\]", "");
            String[] latlongstart = newcurrentlatLngstartStr.split(",");
            double latitudestart = Double.parseDouble(latlongstart[0]);
            double longitudestart = Double.parseDouble(latlongstart[1]);
            currentlatLngstart = new LatLng(latitudestart, longitudestart);

            currentlatLngdropStr = getIntent().getStringExtra("currentlatLngdrop");
            String newcurrentlatLngdropStr = currentlatLngdropStr.replaceAll("\\[", "")
                    .replaceAll("\\]", "");
            String[] latlongdrop = newcurrentlatLngdropStr.split(",");
            double latitudedrop = Double.parseDouble(latlongdrop[0]);
            double longitudedrop = Double.parseDouble(latlongdrop[1]);
            currentlatLngdrop = new LatLng(latitudedrop, longitudedrop);

        }
        currentLocationstart = SharedPrefsData.getString(mContext, Constants.CURRENTSTART, Constants.PREF_NAME);
        currentLocationdrop = SharedPrefsData.getString(mContext, Constants.CURRENTDROP, Constants.PREF_NAME);

        pinLocationEdt = findViewById(R.id.pinLocationEdt);
        dropLocationEdt = findViewById(R.id.dropLocationEdt);
        pinLocationEdt.setText(currentLocationstart);
        dropLocationEdt.setText(currentLocationdrop);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(PairActivity.this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        SetCancelButton();
        bottomTv = findViewById(R.id.bottomTv);
        bottomTv.setText("Searching for Pair");
        //GetSharedPrefs();
        PSTATUS = getIntent().getIntExtra("PSTATUS",-1);
        ShowDialogForPairedAccordingToPStatus(PSTATUS);
    }

    /**************************END OF INIT VIEWS************************/

    /**************************START OF GET SHARED PREFERENCES************************/
    /*
         GetSharedPrefs
         */
    private void GetSharedPrefs()
    {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginPref", 0);
//        UserId = pref.getInt("user_id", 0);
//        Mobile = pref.getString("mobile_number",DEFAULT);
//        Email =  pref.getString("email_id",DEFAULT);
//        Username = pref.getString("user_name",DEFAULT);
//        // ProfilePic = pref.getString("ProfilePic",DEFAULT);
//        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
        UserId = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Email =  pref.getString("email_id",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);

    }

    private void GetRidePrefs()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("PairedUserPref", 0);
        //  user_ride_id = pref.getInt("user_ride_id", 0);
        //user_ride_id = pref.getInt("user_ride_id", 0);
//        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("RideidPref", 0); // 0 - for private mode
//        user_ride_id = pref1.getInt("user_ride_id", 0);

    }

    /**************************END OF GET SHARED PREFERENCES************************/

    /**************************START OF INIT PLACE*********************************/

    private void initPlace() {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

// Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);

        placesClient.fetchPlace(new FetchPlaceRequest() {
            @NonNull
            @Override
            public String getPlaceId() {

                return null;
            }

            @NonNull
            @Override
            public List<Place.Field> getPlaceFields() {
                return null;
            }

            @Nullable
            @Override
            public AutocompleteSessionToken getSessionToken() {
                return null;
            }

            @Nullable
            @Override
            public CancellationToken getCancellationToken() {
                return null;
            }
        });
    }

    /**************************END OF INIT PLACE*********************************/

    /**************************START OF ONMAPREADY*********************************/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                setViews();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

    }

    /**************************END OF ONMAPREADY*********************************/

    /**************************START OF buildGoogleApiClient*********************************/

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    /**************************END OF buildGoogleApiClient*********************************/

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    /**************************START OF FETCH LAST LOCATION*********************************/
    private void fetchLastLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
              /*  Toast.makeText(mContext, location.getLatitude() + "Latitude" + " " +
                        location.getLongitude() + "Longitude", Toast.LENGTH_SHORT).show();*/

                if (location != null) {

                    mLastLocation = location;
                    /*Toast.makeText(mContext, mLastLocation.getLatitude() + "Latitude" + " " +
                            mLastLocation.getLongitude() + "Longitude", Toast.LENGTH_SHORT).show();*/
                    mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFrag.getMapAsync(PairActivity.this);

                }
            }
        });
    }

    /**************************END OF FETCH LAST LOCATION*********************************/

    /**************************START OF SET VIEWS*********************************/

    private void setViews()
    {
//        mGoogleMap.addMarker(new MarkerOptions().position(currentlatLngstart).title(currentLocationstart)
//                .icon(bitmapDescriptorFromVector(mContext, R.drawable.transparent_person)));

        mGoogleMap.addMarker(new MarkerOptions().position(currentlatLngstart).title(currentLocationstart)
                .icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(currentlatLngstart));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));

        mGoogleMap.addMarker(new MarkerOptions().position(currentlatLngdrop).title(currentLocationdrop)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//        mGoogleMap.addMarker(new MarkerOptions().position(currentlatLngstart).title(currentLocationstart)
//                .icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(currentlatLngdrop));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));


        new FetchURL(PairActivity.this).execute(getUrl(currentlatLngstart, currentlatLngdrop,
                "driving"), "driving");

    }

    /**************************END OF SET VIEWS*********************************/

    /**************************START OF SET CANCEL BUTTON*********************************/
    private void SetCancelButton()
    {
        ridenowBtn = findViewById(R.id.ridenowBtn);
        ridenowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cancel ride API
                CancelRideAPI();
            }
        });
    }


    /**************************END OF SET VIEWS*********************************/

    /**************************START OF CANCEL RIDE API*********************************/
    /*
     * CancelRideAPI
     * */
    private void CancelRideAPI() {

        JsonObject object = cancelRideDetailsObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.CancelRideResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CancelRideDetailsResponseModel>() {
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
                    public void onNext(CancelRideDetailsResponseModel mRespone) {
                        Log.i(TAG, "CancelRide Response: "+mRespone);
                        if(mRespone.getStatus() == 1)
                        {
                            Intent intent = new Intent(PairActivity.this, HomeMapsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else {

                        }
                        CallDeletePairAPI();
                    }
                });

    }


    /**
     * Json object of ridedetailsObject
     *
     * @return
     */
    private JsonObject cancelRideDetailsObject()
    {
        CancelRideDetailsRequestModel cancelRideDetailsRequestModelObj = new CancelRideDetailsRequestModel();
        cancelRideDetailsRequestModelObj.setUserId(UserId);
        cancelRideDetailsRequestModelObj.setRideId(RideId);
        return new Gson().toJsonTree(cancelRideDetailsRequestModelObj).getAsJsonObject();
    }

    /**************************END OF CANCEL RIDE API*********************************/

    /**************************START OF FIND PAIR API*********************************/
    /*
     * FindPairAPI
     * */
    private void FindPairAPI()
    {
        Log.i(TAG, "FindPairAPI: start of find pair");
        JsonObject object = FindPairObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.FindPairResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FindPairResponseModel>() {
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
                    public void onNext(FindPairResponseModel mResponse) {
                        Log.i(TAG, "FindPairResponse: "+mResponse);
                        if(mResponse.getStatus() == 0)
                        {
                            CustomDialog(PairActivity.this, getString(R.string.Sorry),getString(R.string.UnableToFindPairMsg),
                                    getString(R.string.Change), getString(R.string.Continue), new Runnable() {
                                        @Override
                                        public void run() {
                                            CancelRideAPI();
                                        }
                                    }, new Runnable() {
                                        @Override
                                        public void run() {
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
//                                                    FindPairAPI();
                                                    if(suggestionsListModelList.size()!=0)
                                                    {
                                                        suggestionsListModelList.clear();
                                                    }

                                                    if(mResponse.getResult().size()!=0)
                                                    {

                                                        for (int i = 0; i < mResponse.getResult().get(0).getPairedUser().size(); i++) {
                                                            String name = mResponse.getResult().get(0).getPairedUser().get(i).getName();
                                                            String gender = mResponse.getResult().get(0).getPairedUser().get(i).getGender();
                                                            int pstatus = mResponse.getResult().get(0).getPairedUser().get(i).getPstatus();
                                                            String srcAddress = mResponse.getResult().get(0).getPairedUser().get(i).getUserSourceAddress();
                                                            String DestAddress = mResponse.getResult().get(0).getPairedUser().get(i).getUserDestAddress();
                                                            int PairableUserID = mResponse.getResult().get(0).getPairedUser().get(i).getUserId();
                                                            SuggestionsListModel suggestionsListModel = new SuggestionsListModel(name,gender,pstatus,srcAddress,DestAddress,PairableUserID);
                                                            suggestionsListModelList.add(suggestionsListModel);
                                                        }
                                                        setSuggestionsListModelList(suggestionsListModelList);
                                                        Intent i = new Intent(PairActivity.this, SuggestionsListScreen.class);
                                                        startActivity(i);
                                                    }
                                                    else {
                                                        FindPairAPI();
                                                    }


                                                }
                                            }, 10000);

                                        }
                                    });
                            // DialogWithNoBtns = CustomDialogWithNoButtons(PairActivity.this,"Sorry!",mResponse.getMessage());

                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            Log.i(TAG, "mResponse.getResult().size(): "+mResponse.getResult().size());
                            if(mResponse.getResult().size() == 0) {
                                PairedUserDetailsAPI();
                            }
                            else {
                                int ride_initiater_id = mResponse.getResult().get(0).getRideInitiaterId().getRideInitiaterId();
                                Log.i(TAG, "ride_initiater_id: "+ride_initiater_id);
                                user_ride_id = mResponse.getResult().get(0).getUserRideId();
                                Log.i(TAG, "FindPairResponse: "+user_ride_id);

                                int userOneId = mResponse.getResult().get(0).getPairedUser().get(0).getUserId();
                                int userTwoId = mResponse.getResult().get(0).getPairedUser().get(1).getUserId();
                                Log.i(TAG, "userOne: "+userOneId+ "-->userTwo: "+userTwoId);

                                /**
                                 * saving Paired User one details
                                 */
                                SharedPreferences pref1 = getApplicationContext().getSharedPreferences("PairedUserPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref1.edit();
                                editor.putString("userOneName", mResponse.getResult().get(0).getPairedUser().get(0).getName());
                                editor.putString("userTwoName", mResponse.getResult().get(0).getPairedUser().get(1).getName());
                                editor.putInt("user_ride_id", user_ride_id);
                                editor.putInt("ride_initiater_id", ride_initiater_id);
                                editor.putInt("userOneId", userOneId);
                                editor.putInt("userTwoId", userTwoId);
                                editor.commit();

                                /**
                                 * saving Paired User two details
                                 */
                                SharedPreferences pref2 = getApplicationContext().getSharedPreferences("PairedUserTwoPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor1 = pref2.edit();
                                editor1.putString("userTwoName", mResponse.getResult().get(0).getPairedUser().get(1).getName());
                                editor1.putInt("user_ride_id", user_ride_id);
                                editor1.putInt("ride_initiater_id", ride_initiater_id);
                                editor1.putInt("userTwoId", userTwoId);
                                editor1.commit();

                                /**
                                 *
                                 * @param ride_initiater_id
                                 * @param userOne
                                 * @param userTwo
                                 * @param mResponse
                                 */
                                CheckRideInitializer(ride_initiater_id,userOneId,userTwoId,mResponse);
                            }

                        }else if(mResponse.getStatus() == 2){
                            DialogWithTwoButtons(PairActivity.this, getString(R.string.AcceptOtherGender), getString(R.string.TravelWithOtherGender), getString(R.string.Yes), new Runnable() {
                                @Override
                                public void run() {
                                    CallGetUserInfoAPI();
                                }
                            }, getString(R.string.No), new Runnable() {
                                @Override
                                public void run() {
                                    //CallGetUserInfoAPI();
                                }
                            });


                        }
                    }
                });
        Log.i(TAG, "FindPairAPI: end of find pair");
    }

    /**
     * Json object of FindPairObject
     *
     * @return
     */
    private JsonObject FindPairObject()
    {
        FindPairRequestModel findPairRequestModelObj = new FindPairRequestModel();
        findPairRequestModelObj.setUserId(UserId);
        return new Gson().toJsonTree(findPairRequestModelObj).getAsJsonObject();
    }

    /**
     *
     * @param ride_initiater_id
     * @param userOne
     * @param userTwo
     * @param mResponse
     */
    private void CheckRideInitializer(int ride_initiater_id,int userOne,int userTwo,FindPairResponseModel mResponse)
    {
        String Attention = null;
        String SourceAddress,DestAddress;

        if(UserId == userOne)
        {
            Attention = mResponse.getResult().get(0).getPairedUser().get(0).getAttention();
            PStatus = mResponse.getResult().get(0).getPairedUser().get(0).getPstatus();
            SourceAddress = mResponse.getResult().get(0).getPairedUser().get(0).getUserSourceAddress();
            DestAddress = mResponse.getResult().get(0).getPairedUser().get(0).getUserDestAddress();
            ShowDialogAccordingToPStatus(PStatus,Attention,"Source: "+SourceAddress,"Dest: "+DestAddress);
        }
        else if(UserId == userTwo) {
            Attention = mResponse.getResult().get(0).getPairedUser().get(1).getAttention();
            PStatus = mResponse.getResult().get(0).getPairedUser().get(1).getPstatus();
            SourceAddress = mResponse.getResult().get(0).getPairedUser().get(1).getUserSourceAddress();
            DestAddress = mResponse.getResult().get(0).getPairedUser().get(1).getUserDestAddress();
            ShowDialogAccordingToPStatus(PStatus,Attention,"Source: "+SourceAddress,"Dest: "+DestAddress);
        }
//        if(ride_initiater_id == userOne)
//        {
//            Attention = mResponse.getResult().get(0).getPairedUser().get(0).getAttention();
//            PStatus = mResponse.getResult().get(0).getPairedUser().get(0).getPstatus();
//            SourceAddress = mResponse.getResult().get(0).getPairedUser().get(0).getSource();
//            DestAddress = mResponse.getResult().get(0).getPairedUser().get(0).getDestination();
//            ShowDialogAccordingToPStatus(PStatus,Attention,SourceAddress,DestAddress);
//
//        }
//        else if(ride_initiater_id == userTwo)
//        {
//            Attention = mResponse.getResult().get(1).getPairedUser().get(1).getAttention();
//            PStatus = mResponse.getResult().get(1).getPairedUser().get(1).getPstatus();
//            SourceAddress = mResponse.getResult().get(1).getPairedUser().get(1).getSource();
//            DestAddress = mResponse.getResult().get(1).getPairedUser().get(1).getDestination();
//            ShowDialogAccordingToPStatus(PStatus,Attention,SourceAddress,DestAddress);
//        }
    }

    /**
     *  @param PStatus
     * @param Attention
     * @param Source
     * @param Dest
     */
    private void ShowDialogAccordingToPStatus(int PStatus, String Attention, String Source, String Dest)
    {
        /*0- you are at farthest distance
    1- other user has to initiate ride
    2- other person in farthest distance
    3-you have to initiate
     */
        if(PStatus == 0)
        {
            Intent intent = new Intent(PairActivity.this, PairedDetailsScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user_ride_id",user_ride_id);
            intent.putExtra("Message",Attention+ "\n"+"\n"+ Source+ "\n"+"\n"+ Dest);
            intent.putExtra("PStatus",PStatus);
            startActivity(intent);
            finish();
//            CustomDialog(PairActivity.this, "Attention!", Attention+ "\n"+
//                    Source+ "\n"+ Dest,"Ok","Cancel", new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(PairActivity.this, TravelModeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("user_ride_id",user_ride_id);
//                    startActivity(intent);
//                    finish();
////                    GetDetailsOfRideTimer();
//                }
//            }, new Runnable() {
//                @Override
//                public void run() {
//                    CancelRideAPI();
//                }
//            });
        }
        else if(PStatus == 1)
        {
            Intent intent = new Intent(PairActivity.this, PairedDetailsScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user_ride_id",user_ride_id);
//            intent.putExtra("Message",Attention);
            intent.putExtra("Message",Attention+ "\n"+"\n"+ Source+ "\n"+"\n"+ Dest);
            intent.putExtra("PStatus",PStatus);
            startActivity(intent);
            finish();

//            CustomDialog(PairActivity.this, "Attention!", Attention,"Ok","Cancel", new Runnable() {
//                @Override
//                public void run() {
//                    PleaseWaitDialog();
//                    GetDetailsOfRideTimer();
//                }
//            }, new Runnable() {
//                @Override
//                public void run() {
//                    CancelRideAPI();
//                }
//            });
        }
        else if(PStatus == 2)
        {
            Intent intent = new Intent(PairActivity.this, PairedDetailsScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user_ride_id",user_ride_id);
//            intent.putExtra("Message",Attention);
            intent.putExtra("Message",Attention+ "\n"+"\n"+ Source+ "\n"+"\n"+ Dest);
            intent.putExtra("PStatus",PStatus);
            startActivity(intent);
            finish();
//            CustomDialog(PairActivity.this, "Attention!", Attention,"Ok","Cancel", new Runnable() {
//                @Override
//                public void run() {
//                    PleaseWaitDialog();
//                    GetDetailsOfRideTimer();
//                }
//            }, new Runnable() {
//                @Override
//                public void run() {
//                    CancelRideAPI();
//                }
//            });

//            CustomDialog(PairActivity.this, "Attention!", Attention,"Ok","Cancel", new Runnable() {
//                @Override
//                public void run() {
//                    String AutoNo = "TS123";
////                    String Msg = mResponse.getResult().get(0).getPairedUser().get(1).getFullname()+" has started the Ride. Your Auto Number is "+AutoNo+"\n"+"\n"
////                            +Source+"\n"+Dest;
//                    String Msg = "Other Paired Person has started the Ride. Your Auto Number is "+AutoNo+"\n"+"\n"
//                            +Source+"\n"+Dest;
//                    CustomDialogWithOneBtn(PairActivity.this, "Attention", Msg, "Start Ride", new Runnable() {
//                        @Override
//                        public void run() {
//                            StartRideAPI(AutoNo);
//                        }
//                    });
//                   // GetDetailsOfRideTimer();
////                    Intent intent = new Intent(PairActivity.this, TravelModeActivity.class);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                    startActivity(intent);
////                    finish();
//                }
//            }, new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
        }
        else if(PStatus == 3)
        {
            Intent intent = new Intent(PairActivity.this, PairedDetailsScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user_ride_id",user_ride_id);
//            intent.putExtra("Message",Attention);
            intent.putExtra("Message",Attention+ "\n"+"\n"+ Source+ "\n"+"\n"+ Dest);
            intent.putExtra("PStatus",PStatus);
            startActivity(intent);
            finish();
//
//            CustomDialog(PairActivity.this, "Attention!", Attention,"Ok","Cancel", new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(PairActivity.this, TravelModeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("user_ride_id",user_ride_id);
//                    startActivity(intent);
//                    finish();
//                    //GetDetailsOfRideTimer();
//                }
//            }, new Runnable() {
//                @Override
//                public void run() {
//                    CancelRideAPI();
//                }
//            });
        }
    }

    /**************************END OF FIND PAIR API*********************************/

    /**************************START OF CustomDialog*********************************/
    /**
     *
     * @param context
     * @param Title
     * @param Msg
     * @param buttonNam1
     * @param buttonNam2
     * @param runnable
     * @param runnable1
     */
    public static void CustomDialog(Context context, String Title, String Msg, String buttonNam1,String buttonNam2, Runnable runnable,Runnable runnable1)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
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
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setText(buttonNam2);
        // if decline button is clicked, close the custom dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                runnable1.run();
            }
        });
    }

    /**
     *
     * @param context
     * @param Title
     * @param Msg
     */
    public Dialog CustomDialogWithNoButtons(Context context, String Title, String Msg)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView title = (TextView) dialog.findViewById(R.id.TitleTv);
        title.setText(Title);
        TextView msg = (TextView) dialog.findViewById(R.id.MsgTv);
        msg.setText(Msg);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        okBtn.setVisibility(View.GONE);
        // if decline button is clicked, close the custom dialog
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setVisibility(View.GONE);
        // if decline button is clicked, close the custom dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    /**************************END OF CustomDialog*********************************/

    /**************************START OF GetDetailsOfRideTimer*********************************/
    private void GetDetailsOfRideTimer()
    {
        handler = new Handler();
        handler.postDelayed(mRunnable, 2000);
    }

    private Runnable mRunnable = new Runnable() {

        @SuppressLint("LongLogTag")
        @Override
        public void run() {
            Log.e("CallDetailsOfRideAPI Handlers", "Calls");
            CallDetailsOfRideAPI();
            /** Do something **/
            handler.postDelayed(mRunnable, 3000);
        }
    };
    /**************************END OF GetDetailsOfRideTimer*********************************/

    /**************************START OF CallDetailsOfRideAPI*********************************/

    /*
     * CallDetailsOfRideAPI
     * */
    private void CallDetailsOfRideAPI(){

        JsonObject object = GetDetailsOfRideObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.GetDetailsOfRideResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetDetailsOfRideResponseModel>() {
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
                    public void onNext(GetDetailsOfRideResponseModel mResponse) {
                        if(mResponse.getStatus() == 0)
                        {
                            Toast.makeText(PairActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            if(pd.isShowing())
                            {
                                pd.dismiss();
                            }

                            if(mResponse.getResult().size() == 0)
                            {
                                if(mResponse.getMessage().equals("Please enter auto no"))
                                {
      /*0- you are at farthest distance
    1- other user has to initiate ride
    2- other person in farthest distance
    3-you have to initiate
     */
                                    Log.i(TAG, "onNext: PStatus: "+PStatus);
                                    if(PStatus == 0 || PStatus == 3)
                                    {
                                        Intent intent = new Intent(PairActivity.this, TravelModeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("RideId",RideId);
                                        intent.putExtra("user_ride_id",user_ride_id);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        FindPairAPI();
                                    }

                                }
                            }
                            else {
                                String username = mResponse.getResult().get(0).getFullname();
                                String AutoNo = mResponse.getResult().get(0).getAutoNumber();
                                String source = mResponse.getResult().get(0).getSource();
                                String dest = mResponse.getResult().get(0).getDest();
//                            String Msg = username+" has started the Ride. Your Auto Number is "+AutoNo+"\n"+"\n"
//                                    +source+"\n"+dest;
                                String Msg = "Your Auto Number is "+AutoNo+"\n"+"\n"
                                        +source+"\n"+dest;

                                CustomDialogWithOneBtn(PairActivity.this,"Attention",Msg, "OK", new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(PairActivity.this, PairSuccessScreen.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("RideStatus","StartRide");
                                        intent.putExtra("UserRideId",mResponse.getResult().get(0).getUser_ride_id());
                                        intent.putExtra("AutoNo",AutoNo);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                try {
                                    handler.removeCallbacks(mRunnable);
                                    handler.removeCallbacksAndMessages(null);
                                }catch (Exception e)
                                {

                                }
                            }


                        }
                    }
                });

    }

    /**
     * Json object of GetUserInfoObject
     *
     * @return
     */
    private JsonObject GetDetailsOfRideObject()
    {
        GetDetailsOfRideRequestModel requestModel = new GetDetailsOfRideRequestModel();
        requestModel.setUserRideId(user_ride_id);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /**************************END OF CallDetailsOfRideAPI*********************************/

    /**************************START OF CustomDialogWithOneBtn*********************************/
    /**
     *
     * @param context
     * @param Title
     * @param Msg
     * @param buttonNam1
     * @param runnable
     */
    public void CustomDialogWithOneBtn(Context context,String Title, String Msg,  String buttonNam1, Runnable runnable)
    {
//        if(!AutoNoDialog.isShowing()) {
//        }
            AutoNoDialog = new Dialog(context);
            AutoNoDialog.setContentView(R.layout.dialogwithonebtn);
            AutoNoDialog.setCanceledOnTouchOutside(false);
            AutoNoDialog.show();
            TextView title = (TextView) AutoNoDialog.findViewById(R.id.TitleTv);
            title.setText(Title);
            TextView msg = (TextView) AutoNoDialog.findViewById(R.id.MsgTv);
            msg.setText(Msg);
            Button okBtn = (Button) AutoNoDialog.findViewById(R.id.okBtn);
            okBtn.setText(buttonNam1);
            // if decline button is clicked, close the custom dialog
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AutoNoDialog.dismiss();
                    runnable.run();
                }
            });
//        }
    }
    /**************************END OF CustomDialogWithOneBtn*********************************/

    /**************************START OF bitmapDescriptorFromVector*********************************/
    /**
     *
     * @param context
     * @param vectorResId
     * @return
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId)
    {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(100, 150, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**************************END OF bitmapDescriptorFromVector*********************************/

    /**************************START OF GET URL FOR POLYLINE*********************************/
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
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +
                "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mGoogleMap.addPolyline((PolylineOptions) values[0]);
    }

    /**************************END OF GET URL FOR POLYLINE*********************************/
    @Override
    protected void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }

    /**************************START OF GET CURRENT LOCATION*********************************/
    /*
     * Current Location
     * */
    private void getCurrentLocation() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    mLastLocation = location;
                    mapFrag = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
                    mapFrag.getMapAsync(PairActivity.this);
                    mapFrag.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mGoogleMap = googleMap;
                            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                            mLocationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
                            Criteria c = new Criteria();
                            //if we pass false than
                            //it will check first satellite location than Internet and than Sim Network
                            provider = mLocationManager.getBestProvider(c, false);
                            if ((ActivityCompat.checkSelfPermission(getApplicationContext(),
                                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(),
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                                mLastLocation = mLocationManager.getLastKnownLocation(provider);
                            }
                            if (mLastLocation != null) {
                                double lng = mLastLocation.getLongitude();
                                double lat = mLastLocation.getLatitude();
                                Log.d(TAG, "onCreate: " + "Position actuelle : " + lat + "," + lng);

                               // Geocoder geocoder = new Geocoder(PairActivity.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(
                                            lat, lng,
                                            1);
                                } catch (Exception ioException) {
                                    Log.e("", "Error in getting address for the location");
                                }

                                if (addresses == null || addresses.size() == 0) {
                                    Toast.makeText(mContext, "No address found for the location", Toast.LENGTH_SHORT).show();

                                } else {
                                    address = addresses.get(0);
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

                                    Log.d(TAG, "Latitude" + address.getLatitude() + " Longitude" + address.getLongitude() +
                                            " address" + address.getAddressLine(0));
                                   // pairingcurrentlatLng = new LatLng(address.getLatitude(), address.getLongitude());
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    /**************************END OF GET CURRENT LOCATION*********************************/

    /**************************START OF PairedUserDetails API*********************************/
    /*
     * PairedUserDetailsAPI
     * */
    public void PairedUserDetailsAPI() {

        JsonObject object = PairedUserDetailsObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.PairedUserDetailsResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PairedUserDetailsResponseModel>() {
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
                    public void onNext(PairedUserDetailsResponseModel mResponse) {
                        Log.i(TAG, "PairedUserDetailsResponseModel: "+mResponse);
                        Toast.makeText(mContext, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 1)
                        {
                            user_ride_id = mResponse.getResult().get(0).getUser_ride_id();
                            Log.i(TAG, "PairedUserDetailsResponseModel: "+user_ride_id);
                            String Attention = mResponse.getResult().get(0).getAttention();
                            int PStatus = mResponse.getResult().get(0).getPstatus();
                            String SourceAddress = mResponse.getResult().get(0).getUserSourceAddress();
                            String DestAddress = mResponse.getResult().get(0).getUserDestAddress();
                            ShowDialogAccordingToPStatus(PStatus,Attention,"Source: "+SourceAddress,"Dest: "+DestAddress);
                            /**
                             * saving Paired User one details
                             */
                            SharedPreferences pref1 = getApplicationContext().getSharedPreferences("PairedUserPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref1.edit();
                            editor.putInt("user_ride_id", user_ride_id);
                            editor.commit();
//                           CustomDialogWithOneBtn(PairActivity.this, "Attention!", mRespone.getResult().get(0).getAttention()
//                                   +mRespone.getResult().get(0).getSource()+"\n"+mRespone.getResult().get(0).getDestination()+"\n", "Ok", new Runnable() {
//                               @Override
//                               public void run() {
//                                   Intent intent = new Intent(PairActivity.this, PairSuccessScreen.class);
//                                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                   startActivity(intent);
//                                   finish();
//                               }
//                           });
                        }
                        else {

                        }

                        //   startRideBtn.setText("End Ride");
                    }
                });

    }

    /**
     * Json object of PairedUserDetailsObject
     * @return
     */
    private JsonObject PairedUserDetailsObject()
    {
//        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("RideidPref", 0); // 0 - for private mode
//        user_ride_id = pref1.getInt("user_ride_id", 0);
        PairedUserDetailsRequestModel requestModel = new PairedUserDetailsRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /**************************END OF PairedUserDetailsAPI*********************************/


    @Override
    public void onBackPressed() {

//        Intent intent = new Intent(PairActivity.this, HomeScreen.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        SharedPrefsData.putString(mContext, Constants.STARTLOCATIONPLACE, " ", Constants.PREF_NAME);
////        intent.putExtra("currentadress", address.getAddressLine(0));
////        intent.putExtra("currentlatLng", currentlatLng);
////        intent.putExtra("fromactivity", "pairscreen");
////        intent.putExtra("currentpin", " ");
////        intent.putExtra("flag", "3");
////        HomeActivity.isFocusInAutoComplete = "pin";
//        startActivity(intent);
//        finish();
        super.onBackPressed();
    }


    private void PleaseWaitDialog()
    {
        pd = new ProgressDialog(PairActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please Wait.. While the Other User Starts the Ride..");
        pd.show();
    }



    /**
     *  @param PStatus
     */
    private void ShowDialogForPairedAccordingToPStatus(int PStatus)
    {
        /*0- wait
    1- initiate
    2- initiate
    3-wait
     */
        if(PStatus == 0)
        {
            PleaseWaitDialog();
            GetDetailsOfRideTimer();
        }
        else if(PStatus == 1)
        {
            CustomDialog(PairActivity.this, "Attention!", "Please Start the Ride..","Ok","Cancel", new Runnable() {
                @Override
                public void run() {
//                    PleaseWaitDialog();
//                    GetDetailsOfRideTimer();
                    Intent intent = new Intent(PairActivity.this, TravelModeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("user_ride_id",RideId);
                    startActivity(intent);
                    finish();
                }
            }, new Runnable() {
                @Override
                public void run() {
                    CancelRideAPI();
                }
            });
        }
        else if(PStatus == 2)
        {
            CustomDialog(PairActivity.this, "Attention!", "Please Start the Ride..","Ok","Cancel", new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PairActivity.this, TravelModeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("user_ride_id",RideId);
                    startActivity(intent);
                    finish();

                }
            }, new Runnable() {
                @Override
                public void run() {
                    CancelRideAPI();
                }
            });

        }
        else if(PStatus == 3)
        {
            PleaseWaitDialog();
            GetDetailsOfRideTimer();
        }
    }



    /********************************START OF CallDeletePairAPI*******************************/
    /*
     * CallDeletePairAPI
     * */
    public void CallDeletePairAPI(){

        JsonObject object = DeletePairObject();
        AtroadsService service = ServiceFactory.createRetrofitService(PairActivity.this, AtroadsService.class);
        mSubscription = service.DeletePairResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeletePairResponseModel>() {
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
                    public void onNext(DeletePairResponseModel mResponse) {
                        Log.i(TAG, "DeletePairResponseModel: "+mResponse);
                        // Toast.makeText(BillingDetailsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            //Toast.makeText(PairSuccessScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {

                        }
                    }
                });

    }

    /**
     * Json object of DeletePairObject
     *
     * @return
     */
    private JsonObject DeletePairObject()
    {
        DeletePairRequestModel requestModel = new DeletePairRequestModel();
        requestModel.setUserRideId(user_ride_id);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF CallDeletePairAPI *******************************/

    public List<SuggestionsListModel> getSuggestionsListModelList() {
        return suggestionsListModelList;
    }

    public void setSuggestionsListModelList(List<SuggestionsListModel> suggestionsListModelList) {
        this.suggestionsListModelList = suggestionsListModelList;
    }


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
                dialog.dismiss();
                runnable.run();

            }
        });

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setText(secondButtonName);
        // if decline button is clicked, close the custom dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                secondRunnable.run();
            }
        });
    }

    /*
     * CallEditUserInfoAPI
     * */
    private void CallEditUserInfoAPI(String ProfilePic,String name,String email,String selectedRadio){

        JsonObject object = EditUserInfoObject(ProfilePic, name, email, selectedRadio);
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.EditUserInfoResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EditUserInfoResponseModel>() {
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
                    public void onNext(EditUserInfoResponseModel mResponse) {
                        Log.i(TAG, "EditUserInfoResponseModel: "+mResponse);

                        // Toast.makeText(PairActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            Toast.makeText(PairActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            Toast.makeText(PairActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                            //  LoadImageFromUrl(EditProfileScreen.this, APIConstants.IMAGE_URL+pflPic,profile_image);
                        }
                    }
                });

    }


    private JsonObject EditUserInfoObject(String ProfilePic,String name,String email,String selectedRadio)
    {
        EditUserInfoRequestModel requestModel = new EditUserInfoRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setProfilePic(ProfilePic);
        requestModel.setName(name);
        requestModel.setEmailId(email);
        requestModel.setAcceptingOtherGender(selectedRadio);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    private JsonObject GetUserInfoObject()
    {
        GetUserInfoRequestModel requestModel = new GetUserInfoRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }
    private void CallGetUserInfoAPI(){

        JsonObject object = GetUserInfoObject();
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
                            Toast.makeText(PairActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            String username = mResponse.getResult().get(0).getName();
                            String mobileNo = mResponse.getResult().get(0).getMobileNumber();
                            String emailId = mResponse.getResult().get(0).getEmailId();
                            String pflPic = mResponse.getResult().get(0).getProfilePic();
                            String refferalCode= mResponse.getResult().get(0).getreferralcode();
                            int coins= mResponse.getResult().get(0).getCoins();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("coins", coins);
                            editor.putString("refferalCode", refferalCode);
                            editor.commit();
                            Log.i(TAG, "pflpic url: "+ APIConstants.IMAGE_URL+pflPic);
                            String accepting_other_gender = mResponse.getResult().get(0).getAcceptingOtherGender();

                            SharedPreferences pref1 = getApplicationContext().getSharedPreferences("USERnMAE", 0); // 0 - for private mode
                            SharedPreferences.Editor editor1 = pref1.edit();
                            editor1.putString("USER_HEAD_NAME", username);
                            editor1.commit();


                            SharedPreferences prefq = getApplicationContext().getSharedPreferences("USERnMAE", 0); // 0 - for private mode
                            String headNmae = prefq.getString("USER_HEAD_NAME","");

                            if(headNmae.equals("")){
                                toolbar.setTitle("Hey ");
                            }else {
                                toolbar.setTitle("Hey " + headNmae+" !");
                            }
                            if(accepting_other_gender.equals(""))
                            {
                                DialogWithTwoButtons(PairActivity.this, getString(R.string.AcceptOtherGender), getString(R.string.TravelWithOtherGender), getString(R.string.Yes), new Runnable() {
                                    @Override
                                    public void run() {
                                        CallEditUserInfoAPI(pflPic,username,emailId,"yes");
                                    }
                                }, getString(R.string.No), new Runnable() {
                                    @Override
                                    public void run() {
                                        CallEditUserInfoAPI(pflPic,username,emailId,"no");
                                    }
                                });
                            }
                            Log.i(TAG, "onNext: "+username + "->"+mobileNo+"=>"+Email);
                        }
                    }
                });

    }

}
