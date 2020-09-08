package com.inas.atroads.views.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.CustomApplication;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.util.Utilities;
import com.inas.atroads.util.localData.FetchURL;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.Activities.PairSuccessScreen;
import com.inas.atroads.views.Interface.TaskLoadedCallback;
import com.inas.atroads.views.model.OnGoingRideRequestModel;
import com.inas.atroads.views.model.OnGoingRidesResponseModel;
import com.inas.atroads.views.model.RidesHistoryRequestModel;
import com.inas.atroads.views.model.RidesHistoryResponseModel;

import java.io.IOException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.WriteLog;
import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class OnGoingRidesFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback
{
    private static final String DEFAULT = "N/A" ;
    private View view;
    private Subscription mSubscription;
    private static String TAG = "OnGoingRidesFragment";
    String Username,Email,Mobile;
    int UserId;
    TextView dateandtime,number,pickup,droplocation;
    private GoogleMap map;
    GoogleApiClient mGoogleApiClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment mapFragment;
    CardView ongoing_ride_cardview;
    private String str_origin,str_dest;
    private Polyline currentPolyline;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ongoing_fragment, container, false);
        SetViews();
        SetMap();
        GetSharedPrefs();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /********************************START OF GetSharedPrefs*******************************/
    /*
      GetSharedPrefs
      */
    private void GetSharedPrefs()
    {
//        SharedPreferences pref = getActivity().getSharedPreferences("LoginPref", 0);
//        UserId = pref.getInt("user_id", 0);
//        Mobile = pref.getString("mobile_number",DEFAULT);
//        Email =  pref.getString("email_id",DEFAULT);
//        Username = pref.getString("user_name",DEFAULT);
//        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);

        SharedPreferences pref = getActivity().getSharedPreferences("RegPref", 0); // 0 - for private mode
        UserId = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Email =  pref.getString("email_id",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
        CallOnGoingRideAPI();
    }

    /********************************END OF GetSharedPrefs*******************************/

    private void SetViews()
    {
        ongoing_ride_cardview = view.findViewById(R.id.ongoing_ride_cardview);
        number = view.findViewById(R.id.number);
        dateandtime = view.findViewById(R.id.dateandtime);
        pickup = view.findViewById(R.id.pickup);
        droplocation = view.findViewById(R.id.droplocation);

    }

    private void SetMap()
    {
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    /****************************START OF GetLastKnownLocation**************************/
    private void GetLastKnownLocation()
    {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null)
                        {
                            // Logic to handle location object
                            SetMarkerOnMap(location.getLatitude(),location.getLongitude(), BitmapDescriptorFactory.HUE_MAGENTA);
                        }
                    }
                });
    }
    /****************************END OF GetLastKnownLocation**************************/

    /****************************START OF SetMarkerOnMap**************************/
    /**
     *
     * @param latitude
     * @param longitude
     */
    private void SetMarkerOnMap(Double latitude,Double longitude,float color)
    {
        //Place location marker
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        String addressOfLocation = GetAddressFromLatLng(latitude, longitude);
        markerOptions.title(addressOfLocation+"");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color));
        map.addMarker(markerOptions);
        //move map camera
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(12));

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



    /********************************START OF CallOnGoingRideAPI*******************************/
    /*
     * CallOnGoingRideAPI
     * */
    private void CallOnGoingRideAPI(){

        JsonObject object = OnGoingRideObject();
        AtroadsService service = ServiceFactory.createRetrofitService(getActivity(), AtroadsService.class);
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
                        Toast.makeText(getActivity(), mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            //Toast.makeText(PairSuccessScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            if(mResponse.getResult().size() == 0)
                            {
                                ongoing_ride_cardview.setVisibility(View.INVISIBLE);
                            }
                            else {
                                ongoing_ride_cardview.setVisibility(View.VISIBLE);
                                number.setText(" : "+mResponse.getResult().get(0).getAutoNumber());
                                dateandtime.setText(""+Utilities.GetCurrentDateTime());
                                pickup.setText(""+mResponse.getResult().get(0).getUserSourceAddress());
                                droplocation.setText(""+mResponse.getResult().get(0).getUserDestAddress());
                                List<Double> srcList = mResponse.getResult().get(0).getUserSourceLatLong();
                                List<Double> destList = mResponse.getResult().get(0).getUserDestLatLong();
                                LatLng srcLatLng = new LatLng(srcList.get(0),srcList.get(1));
                                LatLng destLatLng = new LatLng(destList.get(0),destList.get(1));
//                                new FetchURL(getActivity()).execute(getUrl(srcLatLng, destLatLng,
//                                        "driving"), "driving");


//                                RideStatus = getIntent().getStringExtra("RideStatus");
//                                UserRideId = getIntent().getIntExtra("UserRideId",0);
//                                AutoNo = getIntent().getStringExtra("AutoNo");
                                ongoing_ride_cardview.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SharedPreferences pref1 = getActivity().getSharedPreferences("PairedUserPref", 0); // 0 - for private mode
                                       int user_ride_id = pref1.getInt("user_ride_id",0);

                                        Intent i = new Intent(getActivity(), PairSuccessScreen.class);
                                        i.putExtra("RideStatus","RideStarted");
                                        i.putExtra("UserRideId",user_ride_id);
                                        i.putExtra("AutoNo",mResponse.getResult().get(0).getAutoNumber());
                                        startActivity(i);
                                    }
                                });


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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        GetLastKnownLocation();
    }

    @Override
    public void onTaskDone(Object... values) {
//        if (currentPolyline != null)
//            currentPolyline.remove();
//        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }

    /*************************************END OF CallOnGoingRideAPI*******************************/
}
