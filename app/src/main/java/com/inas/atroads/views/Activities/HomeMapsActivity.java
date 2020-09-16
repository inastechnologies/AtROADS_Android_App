package com.inas.atroads.views.Activities;

import android.Manifest;
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
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.APIConstants;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.CustomApplication;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.util.Utilities;
import com.inas.atroads.util.localData.FetchURL;
import com.inas.atroads.views.Interface.TaskLoadedCallback;
import com.inas.atroads.views.UI.MobileNumberRegisterScreen;
import com.inas.atroads.views.UI.PairedDetailsScreen;
import com.inas.atroads.views.UI.SOSActivity;
import com.inas.atroads.views.UI.SchedulingRideScreen;
import com.inas.atroads.views.UI.UploadQRActivity;
import com.inas.atroads.views.model.AddressComponentsResponse;
import com.inas.atroads.views.model.CancelRideDetailsRequestModel;
import com.inas.atroads.views.model.CancelRideDetailsResponseModel;
import com.inas.atroads.views.model.EditUserInfoRequestModel;
import com.inas.atroads.views.model.EditUserInfoResponseModel;
import com.inas.atroads.views.model.GetDetailsOfRideRequestModel;
import com.inas.atroads.views.model.GetDetailsOfRideResponseModel;
import com.inas.atroads.views.model.GetRideDetailsRequestModel;
import com.inas.atroads.views.model.GetRideDetailsResponseModel;
import com.inas.atroads.views.model.GetRideDetailsUpdateRequestModel;
import com.inas.atroads.views.model.GetRideDetailsUpdateResponseModel;
import com.inas.atroads.views.model.GetUserInfoRequestModel;
import com.inas.atroads.views.model.GetUserInfoResponseModel;
import com.inas.atroads.views.model.OnGoingRideRequestModel;
import com.inas.atroads.views.model.OnGoingRidesResponseModel;
import com.inas.atroads.views.model.PairedDetailsRequestModel;
import com.inas.atroads.views.model.PairedDetailsResponseModel;
import com.inas.atroads.views.model.PairedUserDetailsRequestModel;
import com.inas.atroads.views.model.PairedUserDetailsResponseModel;
import com.inas.atroads.views.model.SearchedPlaceDetails;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.WriteLog;
import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class HomeMapsActivity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener,
		TaskLoadedCallback,GoogleMap.OnMarkerClickListener,
		LocationListener, GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener {
	/*****************************START OF DECLARATIONS**********************************/
	private static final int AUTOCOMPLETE_REQUEST_CODE = 2 ;
	private AppBarConfiguration mAppBarConfiguration;
	private static final String TAG = "HomeMapsActivity";
	private static final int TAG_CODE_PERMISSION_LOCATION = 1;
	private static final float ZOOM_LEVEL = 12 ;
	private GoogleMap mMap;
	GoogleApiClient mGoogleApiClient;
	FusedLocationProviderClient fusedLocationProviderClient;
	//private FusedLocationProviderClient fusedLocationClient;
	private LinearLayout auto_lty,bike_lty,car_lty;
	private Spinner spinnerShare;
	private ArrayAdapter sharearray;
	private static String shareTxt;
	private TextView autoText;
	private Button ridenowBtn;
	ImageView imageMarker, imageMarkerDrop, autoImage;
	private EditText PinEditText,DropEditText;
	private String FromEditText = "PinEditText";
	private Subscription mSubscription;
	String Username,Email,Mobile;
	int UserId;
	private static final String DEFAULT = "N/A";
	CircleImageView profilePicInSideBar;
	private TextView UserNameTv,MobileNoTv,EmailTv;
	private Double DropLatitude,DropLongitude;
	public String currentdatetime;
	private Polyline currentPolyline;
	private String str_origin = "", str_dest = "";
	private int RideId = 0;
	LocationManager mLocationManager;
	SupportMapFragment mapFragment;
	private String UserSourceAddress,UserDestAddress;
	boolean isRangeAlertShown = false;
	public static Double PinLatitude,PinLongitude;
	Geocoder geocoder = CustomApplication.geoCoder;
	private ProgressDialog mProgressDialog;
	int user_ride_id = 0;
	private Button sosBtn;
	int PStatus = -1;
	/*****************************END OF DECLARATIONS**********************************/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_maps);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		isNetworkAvailable(HomeMapsActivity.this);
		WriteLog(TAG);
		mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.homeMap);
		mapFragment.getMapAsync(this);
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
		if(isRangeAlertShown == false)
		{
			isRangeAlertShown = true;
			//RangeAlertDialog(HomeMapsActivity.this,"Distance Range",getString(R.string.Permissions))
		}
		else {

		}
		initGAPIClient();
		AskPermission();
		SetNavigationDrawer();
		PairedUserDetailsAPI();
		//        FloatingActionButton fab = findViewById(R.id.fab);
		//        fab.setOnClickListener(new View.OnClickListener() {
		//            @Override
		//            public void onClick(View view) {
		//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
		//                        .setAction("Action", null).show();
		//            }
		//        });
		//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
		//        NavigationView navigationView = findViewById(R.id.nav_view);
		//        // Passing each menu ID as a set of Ids because each
		//        // menu should be considered as top level destinations.
		//        mAppBarConfiguration = new AppBarConfiguration.Builder(
		//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
		//                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
		//                .setDrawerLayout(drawer)
		//                .build();
		//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		//        NavigationUI.setupWithNavController(navigationView, navController);
	}

	/*****************************START OF INIT GAPICLIENT********************************/
	/**
	 * initGAPIClient()
	 */
	private void initGAPIClient()
	{
		mGoogleApiClient = new GoogleApiClient.Builder(HomeMapsActivity.this)
				.addConnectionCallbacks(HomeMapsActivity.this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();

		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeMapsActivity.this);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		CallPlacesAPIClient();
		PlacesSelection();
	}

	/*****************************END OF INIT GAPICLIENT********************************/

	/*****************************START OF CallPlacesAPIClient********************************/
	/**
	 * CallPlacesAPIClient
	 */
	private void CallPlacesAPIClient()
	{
		if (!Places.isInitialized()) {
			Places.initialize(HomeMapsActivity.this, getString(R.string.google_maps_key));
			//            Places.initialize(HomeMapsActivity.this, "AIzaSyCl6GKb2QSL6jL0tAumlzbRJaYfzSLhFgs");
			//            Places.initialize(HomeMapsActivity.this, "AIzaSyBKKbYBBBaKBwLRxEzcb9DyGaMYwQlYb0I");
		}

		// Create a new Places client instance
		PlacesClient placesClient = Places.createClient(HomeMapsActivity.this);

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
		//PlacesSelection();
	}

	/*****************************END OF CallPlacesAPIClient********************************/

	/*****************************START OF PlacesSelection********************************/
    /*
      PlacesSelection
     */
	private void PlacesSelection()
	{
		AutocompleteSupportFragment autocomplete_fragmentpin = (AutocompleteSupportFragment)
				getSupportFragmentManager().findFragmentById(R.id.Pin_autocomplete_fragment);
		autocomplete_fragmentpin.getView().findViewById(R.id.Pin_autocomplete_fragment).setVisibility(View.GONE);
		// Specify the types of place data to return.
		autocomplete_fragmentpin.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

		// Set up a PlaceSelectionListener to handle the response.
		autocomplete_fragmentpin.setOnPlaceSelectedListener(new PlaceSelectionListener() {
			@Override
			public void onPlaceSelected(Place place) {
				// TODO: Get info about the selected place.
				Log.i("GMapsScreen", "Place: " + place.getName() + ", " + place.getId());
				Log.i("GMapsScreen", "Place: "+ place.getLatLng()+" ,"+ place.getAddress()+","+place.getViewport()+"\n"+ place.getAddressComponents());
			}

			@Override
			public void onError(Status status) {
				// TODO: Handle the error.
				Log.i("GMapsScreen", "An error occurred: " + status);
			}
		});

		AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
				getSupportFragmentManager().findFragmentById(R.id.Drop_autocomplete_fragment);
		autocompleteFragment.getView().findViewById(R.id.Drop_autocomplete_fragment).setVisibility(View.GONE);
		// Specify the types of place data to return.
		autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

		// Set up a PlaceSelectionListener to handle the response.
		autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
			@Override
			public void onPlaceSelected(Place place) {
				// TODO: Get info about the selected place.
				Log.i("GMapsScreen", "Place: " + place.getName() + ", " + place.getId());
				Log.i("GMapsScreen", "Place: "+ place.getLatLng()+" ,"+ place.getAddress()+","+place.getViewport()+"\n"+ place.getAddressComponents());
			}

			@Override
			public void onError(Status status) {
				// TODO: Handle the error.
				Log.i("GMapsScreen", "An error occurred: " + status);
			}
		});
	}


	/**********************************END OF PlacesSelection********************************/

	/************************************START OF NAVIGATION DRAWER***************************/
    /*
          Nav Drawer
        */
	private void SetNavigationDrawer()
	{
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setItemIconTintList(null);
		View hView =  navigationView.inflateHeaderView(R.layout.nav_header_gmaps_screen);
		profilePicInSideBar = (CircleImageView)hView.findViewById(R.id.ProfilePicImg);
		profilePicInSideBar.setImageResource(R.drawable.profile);
		profilePicInSideBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeMapsActivity.this, EditProfileScreen.class);
				intent.putExtra("FROMACTIVITY","GMapsScreen");
				startActivity(intent);
			}
		});
		UserNameTv = (TextView)hView.findViewById(R.id.NameTv);
		UserNameTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeMapsActivity.this, EditProfileScreen.class);
				intent.putExtra("FROMACTIVITY","GMapsScreen");
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
		GetSharedPrefs();
		SetViews();
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
		CallGetUserInfoAPI();

		Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);

	}

	/**********************************END OF SHARED PREFERENCES**************/


	/**********************************START OF SET VIEWS*******************************/
	/**
	 * SetViews
	 */
	private void SetViews()
	{
		auto_lty = findViewById(R.id.auto_lty);
		car_lty = findViewById(R.id.car_lty);
		bike_lty = findViewById(R.id.bike_lty);
		autoImage = findViewById(R.id.autoImage);
		autoText = findViewById(R.id.autoText);
		imageMarker = findViewById(R.id.imageMarker);
		imageMarkerDrop = findViewById(R.id.imageMarkerDrop);
		ridenowBtn = findViewById(R.id.ridenowBtn);
		PinEditText = findViewById(R.id.PinEditText);
		DropEditText = findViewById(R.id.DropEditText);
		PinEditText.setInputType(InputType.TYPE_NULL);
		DropEditText.setInputType(InputType.TYPE_NULL);
		PinEditText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FromEditText = "PinEditText";
				AutoCompleteIntent();
			}
		});
		DropEditText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FromEditText = "DropEditText";
				AutoCompleteIntent();
			}
		});
		//        GetSharedPrefs();
		SetShareSpinner();
		AutoLayoutClick();
		car_lty.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(HomeMapsActivity.this,"Coming Soon...",Toast.LENGTH_LONG).show();
			}
		});

		bike_lty.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(HomeMapsActivity.this,"Coming Soon...",Toast.LENGTH_LONG).show();
			}
		});

	}

	/*******************************START OF SET SPINNER*****************************/
	/**
	 SetShareSpinner
	 */
	private void SetShareSpinner()
	{
		spinnerShare = findViewById(R.id.spinnerShare);
		spinnerShare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				shareTxt = spinnerShare.getSelectedItem().toString();
				Log.i(TAG, "onItemSelected: "+shareTxt);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		String[] sharecab = getResources().getStringArray(R.array.share);
		sharearray = new ArrayAdapter(HomeMapsActivity.this, android.R.layout.simple_list_item_1, sharecab);
		spinnerShare.setAdapter(sharearray);

	}
	/*******************************END OF SET SPINNER*****************************/

	/********************************START OF AUTO LAYOUT CLICK***********************/

	/**
	 * set onclick
	 */
	private void AutoLayoutClick()
	{
		auto_lty.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Glide.with(HomeMapsActivity.this).load(R.drawable.auto_yellow)
						.fitCenter()
						.error(R.drawable.auto_grey)
						.into(autoImage);
				autoText.setTextColor(getColor(R.color.colorPrimary));

				if (PinEditText.getText().toString().equals("")) {
					//                    ridenowBtn.setBackgroundColor(getColor(R.color.grey));
					ridenowBtn.setBackgroundResource(R.drawable.round_rect_white_button_bg);
					ridenowBtn.setEnabled(false);
					Glide.with(HomeMapsActivity.this).load(R.drawable.auto_grey)
							.fitCenter()
							.error(R.drawable.auto_grey)
							.into(autoImage);
					autoText.setTextColor(getColor(R.color.black));
				} else if (DropEditText.getText().toString().equals("")) {
					// ridenowBtn.setBackgroundColor(getColor(R.color.grey));
					ridenowBtn.setBackgroundResource(R.drawable.round_rect_white_button_bg);
					ridenowBtn.setEnabled(false);
					Glide.with(HomeMapsActivity.this).load(R.drawable.auto_grey)
							.fitCenter()
							.error(R.drawable.auto_grey)
							.into(autoImage);
					autoText.setTextColor(getColor(R.color.black));
				} else if (!PinEditText.getText().toString().equals("")) {
					Glide.with(HomeMapsActivity.this).load(R.drawable.auto_yellow)
							.fitCenter()
							.error(R.drawable.auto_grey)
							.into(autoImage);
					autoText.setTextColor(getColor(R.color.colorPrimary));
					//ridenowBtn.setBackgroundColor(getColor(R.color.colorPrimary));
					ridenowBtn.setBackgroundResource(R.drawable.round_rect_button_bg);
					ridenowBtn.setTextColor(Color.parseColor("#ffffff"));
					ridenowBtn.setEnabled(true);
				} else if (!DropEditText.getText().toString().equals("")) {
					Glide.with(HomeMapsActivity.this).load(R.drawable.auto_yellow)
							.fitCenter()
							.error(R.drawable.auto_grey)
							.into(autoImage);
					autoText.setTextColor(getColor(R.color.colorPrimary));
					//                    ridenowBtn.setBackgroundColor(getColor(R.color.colorPrimary));
					ridenowBtn.setBackgroundResource(R.drawable.round_rect_button_bg);
					ridenowBtn.setTextColor(Color.parseColor("#ffffff"));
					ridenowBtn.setEnabled(true);
				}

			}
		});

	}
	/********************************END OF AUTO LAYOUT CLICK***********************/



	/*********************************START OF AutoCompleteIntent*********************/

     /*
     AutoCompleteIntent
     */
	private void AutoCompleteIntent()
	{
		// Set the fields to specify which types of place data to
		// return after the user has made a selection.
		List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

		// Start the autocomplete intent.
		Intent intent = new Autocomplete.IntentBuilder(
				AutocompleteActivityMode.FULLSCREEN, fields)
				.build(HomeMapsActivity.this);
		startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
	}


	/**
	 *
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AUTOCOMPLETE_REQUEST_CODE)
		{
			LatLng PinlatLng = null,DropLatLng = null;
			if (resultCode == RESULT_OK)
			{

				Place place = Autocomplete.getPlaceFromIntent(data);
				Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
				Log.i(TAG, "PlaceAddress: " + place.getAddress());
				Log.i(TAG, "PlaceAddressComponents(): " + place.getAddressComponents());
				Log.i(TAG, "place Viewport: " + place.getViewport() + place.getAttributions());
				if(FromEditText.equals("PinEditText"))
				{
					PinEditText.setText(place.getName());
					PinlatLng = place.getLatLng();
					Log.i(TAG, "onActivityResult:PinlatLng "+PinlatLng);
					//                    if(latLng!=null)
					//                    {
					//                        SetMarkerOnMap(PinlatLng.latitude,PinlatLng.longitude);
					//                    }
				}else if(FromEditText.equals("DropEditText"))
				{
					DropEditText.setText(place.getName());
					DropLatLng = place.getLatLng();
					Log.i(TAG, "onActivityResult:DropLatLng "+DropLatLng);
					//                    if(latLng!=null)
					//                    {
					//                        SetMarkerOnMap(DropLatLng.latitude,DropLatLng.longitude);
					//                    }
				}

				SearchedPlaceDetails details = new SearchedPlaceDetails();
				details.setPlaceId(place.getId());
				details.setKey(getString(R.string.google_maps_key));
				//GetLocationFromPlaceId(place.getId(),getString(R.string.google_maps_key));
				getAddressComponentsAPI(place.getId());
			} else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
				// TODO: Handle the error.
				Status status = Autocomplete.getStatusFromIntent(data);
				Log.i(TAG, status.getStatusMessage());
			} else if (resultCode == RESULT_CANCELED) {
				// The user canceled the operation.
			}
		}
	}

	/*********************************END OF AutoCompleteIntent*********************/


	/********************************START OF getAddressComponentsAPI******************/
	/**
	 *
	 * @param PlaceId
	 */
	private void getAddressComponentsAPI(String PlaceId) {

		AtroadsService service = ServiceFactory.createRetrofitService(HomeMapsActivity.this, AtroadsService.class);
		mSubscription = service.GetAddressComponents(APIConstants.getAddressComponentsURL + PlaceId)
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<AddressComponentsResponse>() {
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
					public void onNext(AddressComponentsResponse mResponse) {
						Log.i(TAG, "AddressComponentsResponse: "+mResponse.getResults().get(0).getFormattedAddress());
						String formattedAddress = mResponse.getResults().get(0).getFormattedAddress();
						if(mResponse.getStatus().equals("OK"))
						{
							if(FromEditText.equals("PinEditText"))
							{
								PinEditText.setText(formattedAddress);
								PinLatitude = mResponse.getResults().get(0).getGeometry().getLocation().getLat();
								PinLongitude = mResponse.getResults().get(0).getGeometry().getLocation().getLng();
								Log.i(TAG, "onNext:pinLatLng: "+PinLatitude+"-->"+PinLongitude);
								mMap.clear();
								SetMarkerOnMap(PinLatitude,PinLongitude,BitmapDescriptorFactory.HUE_AZURE);
							}else if(FromEditText.equals("DropEditText"))
							{
								DropEditText.setText(formattedAddress);
								DropLatitude = mResponse.getResults().get(0).getGeometry().getLocation().getLat();
								DropLongitude = mResponse.getResults().get(0).getGeometry().getLocation().getLng();
								Log.i(TAG, "onNext:DropLatLng: "+DropLatitude+"-->"+DropLongitude);
								mMap.clear();
								if(PinLatitude!=null)
								{
									SetMarkerOnMap(PinLatitude,PinLongitude,BitmapDescriptorFactory.HUE_AZURE);
								}
								if(DropLongitude!=null)
								{
									SetMarkerOnMap(DropLatitude,DropLongitude,BitmapDescriptorFactory.HUE_RED);
								}

							}
							if(PinLatitude!=null && DropLatitude!=null)
							{
								SetMarkerOnMap(PinLatitude,PinLongitude,BitmapDescriptorFactory.HUE_AZURE);
								SetMarkerOnMap(DropLatitude,DropLongitude,BitmapDescriptorFactory.HUE_RED);
								String PinAddress = GetAddressFromLatLng(PinLatitude,PinLongitude);
								String DropAddress = GetAddressFromLatLng(DropLatitude,DropLongitude);
								Log.i(TAG, "PinAddress: "+PinAddress + "-->"+DropAddress);
								LatLng PinlatLng = new LatLng(PinLatitude,PinLongitude);
								LatLng DropLatLng = new LatLng(DropLatitude,DropLongitude);
								//draw polyline
								new FetchURL(HomeMapsActivity.this).execute(getUrl(PinlatLng, DropLatLng,
										"driving"), "driving");
								SetRideNowOnclick(PinlatLng,DropLatLng,PinAddress,DropAddress);
							}

						}
						else {

						}
					}
				});
	}


	/********************************END OF getAddressComponentsAPI******************/


	/********************************START OF SET RIDE NOW BUTTON***********************/
	/**
	 *
	 * @param currentlatLngstart
	 * @param currentlatLngdrop
	 * @param currentLocationstart
	 * @param currentLocationdrop
	 */
	private void SetRideNowOnclick(LatLng currentlatLngstart,LatLng currentlatLngdrop,String currentLocationstart,String currentLocationdrop)
	{
		ridenowBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				mMap.clear();
				//                mMap.addMarker(new MarkerOptions().position(currentlatLngstart).title(currentLocationstart)
				//                        .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.startpointsmall)));

				mMap.addMarker(new MarkerOptions().position(currentlatLngstart).title(currentLocationstart)
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

				mMap.addMarker(new MarkerOptions().position(currentlatLngdrop).title(currentLocationdrop)
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

				getCurrentDateTime();
				new FetchURL(HomeMapsActivity.this).execute(getUrl(currentlatLngstart, currentlatLngdrop,
						"driving"), "driving");

				//                HomeFragment.HomeFragInterface homeFragInterface = new HomeFragment.HomeFragInterface() {
				//                    @Override
				//                    public void fetchPolyline(LatLng currentlatLngstart, LatLng currentlatLngdrop) {
				//
				//                    }
				//                };
				//                homeFragInterface.fetchPolyline(currentlatLngstart, currentlatLngdrop);
				//                // For Polyline
				////                new FetchURL(getActivity()).execute(getUrl(currentlatLngstart, currentlatLngdrop,
				////                        "driving"), "driving");
				try {
					LatLng PinLatLong = getLocationFromAddress(HomeMapsActivity.this,PinEditText.getText().toString());
					LatLng DropLatLong = getLocationFromAddress(HomeMapsActivity.this,DropEditText.getText().toString());
					showProgressDialog();
					GetRideDetailsAPI(PinEditText.getText().toString(), DropEditText.getText().toString(),PinLatLong,DropLatLong);
				}
				catch (Exception e)
				{

				}
				//GetRideDetailsAPI(currentLocationstart, currentLocationdrop,PinLatLong,DropLatLong);

			}
		});
	}

	/********************************END OF SET RIDE NOW BUTTON***********************/


	/********************************START OF GET LOCATION FROM ADDRESS*************************/
	/**
	 *
	 * @param context
	 * @param strAddress
	 * @return
	 */
	public LatLng getLocationFromAddress(Context context,String strAddress) {

		Geocoder coder = new Geocoder(context);
		List<Address> address;
		LatLng p1 = null;

		try {
			// May throw an IOException
			address = CustomApplication.geoCoder.getFromLocationName(strAddress, 5);
			// address = coder.getFromLocationName(strAddress, 5);
			if (address == null) {
				return null;
			}

			Address location = address.get(0);
			p1 = new LatLng(location.getLatitude(), location.getLongitude() );

		} catch (IOException ex) {

			ex.printStackTrace();
		}

		return p1;
	}


	/********************************END OF GET LOCATION FROM ADDRESS*************************/

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

	/********************************START OF GET CURRENT TIME*************************/
	/*
	 * getetCurrentDateTime
	 * */
	private void getCurrentDateTime() {

		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		currentdatetime = sdf.format(date);
		Log.d("currentdatetime", "getetCurrentDateTime: " + currentdatetime);
	}
	/********************************END OF GET CURRENT TIME*************************/

	/****************************START OF  RUNTIME PERMISSIONS**************************/
	/**
	 * Runtime permissions for location
	 */
	public void AskPermission()
	{
		// Here, thisActivity is the current activity
		if (ContextCompat.checkSelfPermission(HomeMapsActivity.this,
				Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED)
		{
			// Permission is not granted
			// Should we show an explanation?
			Log.i(TAG, "onRequestPermissionsResult: "+"PermissionGrantedExpla");
			if (ActivityCompat.shouldShowRequestPermissionRationale(HomeMapsActivity.this,
					Manifest.permission.ACCESS_FINE_LOCATION))
			{
				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.
                /*PermissionDialog(HomeMapsActivity.this, getString(R.string.Permissions), getString(R.string.PermissionMsg), "Allow", "Cancel", new Runnable() {
                    @Override
                    public void run() {
                */        ActivityCompat.requestPermissions(HomeMapsActivity.this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
					TAG_CODE_PERMISSION_LOCATION);
                   /* }
                });
                Log.i(TAG, "onRequestPermissionsResult: "+"PermissionGranted89");*/
			}
			else {
				// No explanation needed; request the permission
				ActivityCompat.requestPermissions(HomeMapsActivity.this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						TAG_CODE_PERMISSION_LOCATION);
				Log.i(TAG, "onRequestPermissionsResult: "+"PermissionGranted656");
				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		} else {
			// Permission has already been granted
			CheckLocationEnabledOrNot();
			Log.i(TAG, "onRequestPermissionsResult: "+"PermissionGranted......");
		}
	}


	/* public void PermissionDialog(Context context, String Title, String Msg, String buttonNam1, String buttonNam2, Runnable runnable)
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

			 }
		 });
	 }
 */
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == TAG_CODE_PERMISSION_LOCATION)
		{
			// for each permission check if the user granted/denied them
			// you may want to group the rationale in a single dialog,
			// this is just an example
			for (int i = 0, len = permissions.length; i < len; i++) {
				String permission = permissions[i];
				if (grantResults[i] == PackageManager.PERMISSION_DENIED)
				{
					// user rejected the permission
					boolean showRationale = shouldShowRequestPermissionRationale( permission );
					if (! showRationale) {
						// user also CHECKED "never ask again"
						// you can either enable some fall back,
						// disable features of your app
						// or open another dialog explaining
						// again the permission and directing to
						// the app setting
                        /*PermissionDialog(HomeMapsActivity.this, getString(R.string.Permissions), getString(R.string.PermissionSettings), "Allow", "Cancel", new Runnable() {
                            @Override
                            public void run() {*/
						Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						Uri uri = Uri.fromParts("package", getPackageName(), null);
						intent.setData(uri);
						startActivityForResult(intent, TAG_CODE_PERMISSION_LOCATION);
                            /*}
                        });*/

						Log.i(TAG, "onRequestPermissionsResult: "+"PermissionGranted3");
					} else if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {

						// user did NOT check "never ask again"
						// this is a good place to explain the user
						// why you need the permission and ask if he wants
						// to accept it (the rationale)
                        /*PermissionDialog(HomeMapsActivity.this, getString(R.string.Permissions), getString(R.string.PermissionMsg), "Allow", "Cancel", new Runnable() {
                            @Override
                            public void run() {*/
						ActivityCompat.requestPermissions(HomeMapsActivity.this,
								new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
								TAG_CODE_PERMISSION_LOCATION);
                           /* }
                        });*/
						Log.i(TAG, "onRequestPermissionsResult: "+"PermissionGranted1");
					}

				}
				else {
					// Allowed
					Log.i(TAG, "onRequestPermissionsResult: "+"PermissionGranted2");
					GetLastKnownLocation();
					CheckLocationEnabledOrNot();

					// AskPermission();
				}
			}


		}
	}


	/**
	 * CheckLocationEnabledOrNot
	 */
	private void CheckLocationEnabledOrNot()
	{
		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		boolean gps_enabled = false;
		boolean network_enabled = false;

		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch(Exception ex) {}

		try {
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch(Exception ex) {}

		if(!gps_enabled && !network_enabled)
		{
			// notify user
            /*PermissionDialog(HomeMapsActivity.this, "Enable Location", getString(R.string.gps_network_not_enabled),
                    "Allow", "Cancel", new Runnable() {
                        @Override
                        public void run() {*/
			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        /*}
                    });*/
		}
		else {
			Log.i(TAG, "CheckLocationEnabledOrNot: "+gps_enabled);

		}


	}



	/****************************END OF RUNTIME PERMISSIONS**************************/


	/****************************START OF GetLastKnownLocation**************************/
	private void GetLastKnownLocation()
	{
		fusedLocationProviderClient.getLastLocation()
				.addOnSuccessListener(this, new OnSuccessListener<Location>() {
					@Override
					public void onSuccess(Location location) {
						// Got last known location. In some rare situations this can be null.
						if (location != null) {
							// Logic to handle location object
							String addressOfLocation = GetAddressFromLatLng(location.getLatitude(), location.getLongitude());
							// PinEditText = findViewById(R.id.PinEditText);
							PinEditText.setText(addressOfLocation);
							PinLatitude = location.getLatitude();
							PinLongitude = location.getLongitude();
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
		//Geocoder geocoder = null;
		List<Address> addresses;
		String address = "";
		//Geocoder geocoder = new Geocoder(HomeMapsActivity.this, Locale.getDefault());

		Geocoder geocoder = CustomApplication.geoCoder;
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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



	/****************************START OF ON MAP READY**************************/

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
		//
		//        // Add a marker in Sydney and move the camera
		//        LatLng sydney = new LatLng(-34, 151);
		//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
		//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
		GetLastKnownLocation();
		//mMap = googleMap;

		try {
			mMap.setMyLocationEnabled(true);
			mMap.getUiSettings().setMyLocationButtonEnabled(true);
			mMap.setMyLocationEnabled(true);
			View mapView = mapFragment.getView();
			View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
			RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
			// position on right bottom
			rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			rlp.setMargins(0, 0, 50, 100);

		}catch (Exception e)
		{

		}

		//Initialize Google Play Services
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_FINE_LOCATION)
					== PackageManager.PERMISSION_GRANTED) {
				buildGoogleApiClient();
				mMap.setMyLocationEnabled(true);
			}
		}
		else {
			buildGoogleApiClient();
			mMap.setMyLocationEnabled(true);
		}

		mMap.setOnMarkerClickListener(this);
	}

	/**
	 * buildGoogleApiClient
	 */
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this
		)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}


	/****************************END OF ON MAP READY**************************/




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_maps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_notify:
				Intent i = new Intent(HomeMapsActivity.this,NotificationScreen.class);

				startActivity(i);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration)
				|| super.onSupportNavigateUp();
	}

	/********************************START OF ONNAVIGATIONITEMSELECTED******************/
	/**
	 *
	 * @param menuItem
	 * @return
	 */
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		switch (menuItem.getItemId()) {

			case R.id.Emergency_nav:
				Intent sosintent = new Intent(HomeMapsActivity.this, SOSActivity.class);
				sosintent.putExtra("FROMACTIVITY", "HomeScreen");
				startActivity(sosintent);
				break;

			case R.id.scheduling_ride:
				Intent schedule_intent = new Intent(HomeMapsActivity.this, SchedulingRideScreen.class);
				schedule_intent.putExtra("FROMACTIVITY", "HomeScreen");
				startActivity(schedule_intent);
				break;

			case R.id.youRide_nav:
				Intent i = new Intent(HomeMapsActivity.this, YourRidesActivity.class);
				i.putExtra("FROMACTIVITY", "HomeScreen");
				startActivity(i);
				break;

			case R.id.wallet_nav:
				//                Intent intent = new Intent(HomeMapsActivity.this, WalletActivity.class);
				//                intent.putExtra("FROMACTIVITY", "HomeScreen");
				//                startActivity(intent);
				break;

			case R.id.chat_nav:
				Intent inti = new Intent(HomeMapsActivity.this, UsersActivity.class);
				inti.putExtra("FROMACTIVITY", "HomeScreen");
				startActivity(inti);
				break;

			case R.id.refer_nav:
				Intent pricingIntent = new Intent(HomeMapsActivity.this, ReferEarnActivity.class);
				// pricingIntent.putExtra("Url", Constants.SubscriptionURL);
				startActivity(pricingIntent);
				break;

			case R.id.help_nav:
				Intent termsIntent = new Intent(HomeMapsActivity.this, HelpActivity.class);
				//                termsIntent.putExtra("Url", Constants.TermsAndConditionsURL);
				startActivity(termsIntent);
				break;

			case R.id.howitworks_nav:
				Intent hiwIntent = new Intent(HomeMapsActivity.this, HowtoWorksActivity.class);
				//                termsIntent.putExtra("Url", Constants.TermsAndConditionsURL);
				startActivity(hiwIntent);
				break;

			case R.id.Show_qr:
				Intent qrIntent = new Intent(HomeMapsActivity.this, UploadQRActivity.class);
				//privacyIntent.putExtra("Url", Constants.PrivacyAgreementURL);
				startActivity(qrIntent);
				break;

			case R.id.settings_nav:
				Intent privacyIntent = new Intent(HomeMapsActivity.this, SettingsActivity.class);
				//privacyIntent.putExtra("Url", Constants.PrivacyAgreementURL);
				startActivity(privacyIntent);
				break;

			case R.id.terms_con_nav:
				Intent feedIntent = new Intent(HomeMapsActivity.this, TermsConditionsActivity.class);
				startActivity(feedIntent);
				break;

			case R.id.privacy_policy_nav:
				Intent Acintent = new Intent(HomeMapsActivity.this, PrivacyPolicyActivity.class);
				startActivity(Acintent);
				break;


			case R.id.aboutus_nav:
				Intent historyIntent = new Intent(HomeMapsActivity.this, AboutUsActivity.class);
				startActivity(historyIntent);
				break;


			case R.id.logout_nav:
				DialogWithTwoButtons(HomeMapsActivity.this, getString(R.string.Logout), getString(R.string.AreYouSure), getString(R.string.Yes), new Runnable() {
					@Override
					public void run() {
						Intent intent1 = new Intent(HomeMapsActivity.this, MobileNumberRegisterScreen.class);
						startActivity(intent1);
						FirebaseAuth.getInstance().signOut();
						//                        SharedPreferences settings = getApplicationContext().getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
						//                        settings.edit().remove("user_id").commit();
						finish();
						SharedPreferences settings = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
						settings.edit().remove("user_id").commit();
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

	/********************************START OF GET USER INFO*******************************/
	/*
	 * CallGetUserInfoAPI
	 * */
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
							Toast.makeText(HomeMapsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
						}
						else if(mResponse.getStatus() == 1)
						{
							String username = mResponse.getResult().get(0).getName();
							String mobileNo = mResponse.getResult().get(0).getMobileNumber();
							String emailId = mResponse.getResult().get(0).getEmailId();
							String pflPic = mResponse.getResult().get(0).getProfilePic();
							Log.i(TAG, "pflpic url: "+ APIConstants.IMAGE_URL+pflPic);
							UserNameTv.setText(username);
							MobileNoTv.setText(mobileNo);
							String accepting_other_gender = mResponse.getResult().get(0).getAcceptingOtherGender();
							if(accepting_other_gender.equals(""))
							{
								DialogWithTwoButtons(HomeMapsActivity.this, getString(R.string.AcceptOtherGender), getString(R.string.TravelWithOtherGender), getString(R.string.Yes), new Runnable() {
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
							LoadImageFromUrl(HomeMapsActivity.this, APIConstants.IMAGE_URL+pflPic,profilePicInSideBar);
							//                            // Firebase login
							FirebaseLogin(username,mobileNo,Email);
							Log.i(TAG, "onNext: "+username + "->"+mobileNo+"=>"+Email);
						}
					}
				});

	}

	/**
	 * Json object of GetUserInfoObject
	 *
	 * @return
	 */
	private JsonObject GetUserInfoObject()
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

	/************************START OF GET RIDE DETAILS API******************************/
	/*
	 * GetRideDetailsAPI
	 * */
	private void GetRideDetailsAPI(String locationStart, String locationdrop, LatLng pinLatLong, LatLng dropLatLong) {
		Log.i(TAG, "GetRideDetailsAPI: "+locationStart + "\n"+locationdrop+"\n"+pinLatLong+"\n"+dropLatLong);
		JsonObject object = getRideDetailsObject(locationStart, locationdrop,pinLatLong,dropLatLong);
		AtroadsService service = ServiceFactory.createRetrofitService(HomeMapsActivity.this, AtroadsService.class);
		mSubscription = service.GetRideDetailsResponse(object)
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<GetRideDetailsResponseModel>() {
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
					public void onNext(GetRideDetailsResponseModel mRespone) {
						hideProgressDialog();
						Log.i(TAG, "GetRideDetailsResponseModel: "+mRespone);
						// Toast.makeText(mContext, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
						if(mRespone.getStatus() == 1)
						{
							Intent intent = new Intent(HomeMapsActivity.this, PairActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("currentlatLngstart", String.valueOf(mRespone.getOutput().get(0).getUserSourceLatLong()));
							intent.putExtra("currentlatLngdrop",   String.valueOf(mRespone.getOutput().get(0).getUserDestLatLong()));
							intent.putExtra("pinLoc",   String.valueOf(mRespone.getOutput().get(0).getUserSourceAddress()));
							intent.putExtra("DropLoc",   String.valueOf(mRespone.getOutput().get(0).getUserDestAddress()));
							intent.putExtra("RideId", mRespone.getOutput().get(0).getRideId());
							Log.i(TAG, "onNext: GetRideDetailsAPI: RideId"+mRespone.getOutput().get(0).getRideId());
							startActivity(intent);
							finish();
							RideId = mRespone.getOutput().get(0).getRideId();
							UserSourceAddress = mRespone.getOutput().get(0).getUserSourceAddress();
							UserDestAddress = mRespone.getOutput().get(0).getUserDestAddress();
							//                            SharedPrefsData.putString(getActivity(), Constants.CURRENTSTART, currentLocationstart, Constants.PREF_NAME);
							//                            SharedPrefsData.putString(getActivity(), Constants.CURRENTDROP, currentLocationdrop, Constants.PREF_NAME);
						}
						else {
							//RideAlreadyInitiatedDialog(HomeMapsActivity.this,"",mRespone.getMessage(),"Ok");
							String pinLoc = PinEditText.getText().toString();
							String dropLoc = DropEditText.getText().toString();
							LatLng pinLatLng = getLocationFromAddress(HomeMapsActivity.this,pinLoc);
							LatLng dropLatLng = getLocationFromAddress(HomeMapsActivity.this,dropLoc);
							//update ride details
							GetRideDetailsUpdateAPI(pinLoc,dropLoc,pinLatLng,dropLatLng);
						}


					}
				});

	}

	/**
	 * Json object of ridedetailsObject
	 * @return
	 */
	private JsonObject getRideDetailsObject(String locationStart, String locationdrop, LatLng pinLatLong, LatLng dropLatLong)
	{
		//        LatLng PinLatLng = new LatLng(PinLatitude,PinLongitude);
		//        LatLng DropLatLng = new LatLng(DropLatitude,DropLongitude);
		String currentlatlongStartStr=String.valueOf(pinLatLong);
		String currentlatLngstartStr=currentlatlongStartStr.replace("lat/lng:","");
		String newcurrentlatLngstartStr=currentlatLngstartStr.replaceAll("\\(","[")
				.replaceAll("\\)","]");
		Log.i(TAG, "getRideDetailsObject: newcurrentlatLngstartStr"+newcurrentlatLngstartStr);

		String currentlatlongDropStr=String.valueOf(dropLatLong);
		String currentlatLngdropStr=currentlatlongDropStr.replace("lat/lng:","");
		String newcurrentlatLngdropStr=currentlatLngdropStr.replaceAll("\\(","[")
				.replaceAll("\\)","]");
		Log.i(TAG, "getRideDetailsObject: currentlatlongDropStr"+newcurrentlatLngstartStr);

		String removePinSplChars = locationStart.replaceAll("[^a-zA-Z0-9]", " ");
		String removeDropSplChars = locationdrop.replaceAll("[^a-zA-Z0-9]", " ");
		GetRideDetailsRequestModel requestModel = new GetRideDetailsRequestModel();
		requestModel.setUserId(UserId);
		requestModel.setUserSourceAddress(removePinSplChars);
		requestModel.setUserSourceLatLong(newcurrentlatLngstartStr);
		requestModel.setUserDestAddress(removeDropSplChars);
		requestModel.setUserDestLatLong(newcurrentlatLngdropStr);
		requestModel.setShareWith(Integer.valueOf(shareTxt));
		requestModel.setVehicleType("Auto");
		// requestModel.setRideDateTime(currentdatetime);
		//  Log.i(TAG, "getRideDetailsObject: "+ new Gson().toJsonTree(requestModel).getAsJsonObject());
		return new Gson().toJsonTree(requestModel).getAsJsonObject();
	}


	/**
	 *  @param context
	 * @param Title
	 * @param Msg
	 * @param buttonNam1
	 */
	public void RideAlreadyInitiatedDialog(Context context, String Title, String Msg, String buttonNam1)
	{
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialogwithonebtn);
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
				// GetRideDetailsAPI(currentLocationstart, currentLocationdrop);
				//FindPairAPI();
				String pinLoc = PinEditText.getText().toString();
				String dropLoc = DropEditText.getText().toString();
				LatLng pinLatLng = getLocationFromAddress(HomeMapsActivity.this,pinLoc);
				LatLng dropLatLng = getLocationFromAddress(HomeMapsActivity.this,dropLoc);
				//update ride details
				GetRideDetailsUpdateAPI(pinLoc,dropLoc,pinLatLng,dropLatLng);
			}
		});
	}

	/************************END OF GET RIDE DETAILS API******************************/


	/**************************START OF CANCEL RIDE API******************************/

	/*
	 * CancelRideAPI
	 * */
	private void CancelRideAPI() {

		JsonObject object = cancelRideDetailsObject();
		AtroadsService service = ServiceFactory.createRetrofitService(HomeMapsActivity.this, AtroadsService.class);
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
						Toast.makeText(HomeMapsActivity.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
						if(mRespone.getStatus() == 1)
						{
							DropEditText = findViewById(R.id.dropLocationEdt);
							DropEditText.setText("");
							mMap.clear();
							Glide.with(HomeMapsActivity.this).load(R.drawable.auto_grey)
									.fitCenter()
									.error(R.drawable.auto_grey)
									.into(autoImage);
							autoText.setTextColor(getColor(R.color.colorPrimary));


						}else {

						}
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
		cancelRideDetailsRequestModelObj.setRideId((int) RideId);
		return new Gson().toJsonTree(cancelRideDetailsRequestModelObj).getAsJsonObject();
	}

	/**************************END OF CANCEL RIDE API******************************/



	/************************START OF GET RIDE DETAILS UPDATE API******************************/
	/*
	 * GetRideDetailsUpdateAPI
	 * */
	private void GetRideDetailsUpdateAPI(String locationStart, String locationdrop, LatLng pinLatLong, LatLng dropLatLong)
	{
		JsonObject object = getRideDetailsUpdateObject(locationStart, locationdrop,pinLatLong,dropLatLong);
		AtroadsService service = ServiceFactory.createRetrofitService(HomeMapsActivity.this, AtroadsService.class);
		mSubscription = service.GetRideDetailsUpdateResponse(object)
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<GetRideDetailsUpdateResponseModel>() {
					@Override
					public void onCompleted() {
						Log.i(TAG, "onCompleted: ");
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
					public void onNext(GetRideDetailsUpdateResponseModel mRespone) {
						Log.i(TAG, "GetRideDetailsUpdateResponseModel: "+mRespone.getMessage());
						//                         Toast.makeText(HomeMapsActivity.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
						if(mRespone.getStatus() == 1)
						{
							Intent intent = new Intent(HomeMapsActivity.this, PairActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("currentlatLngstart", String.valueOf(mRespone.getResult().get(0).getUserSourceLatLong()));
							intent.putExtra("currentlatLngdrop",   String.valueOf(mRespone.getResult().get(0).getUserDestLatLong()));
							intent.putExtra("pinLoc",   String.valueOf(mRespone.getResult().get(0).getUserSourceAddress()));
							intent.putExtra("DropLoc",   String.valueOf(mRespone.getResult().get(0).getUserDestAddress()));
							intent.putExtra("RideId", mRespone.getResult().get(0).getRideId());
							Log.i(TAG, "onNext: GetRideDetailsUpdateAPI: RideID:"+ mRespone.getResult().get(0).getRideId());
							startActivity(intent);
							finish();
							//                            RideId = mRespone.getOutput().get(0).getRideId();
							//                            UserSourceAddress = mRespone.getOutput().get(0).getUserSourceAddress();
							//                            UserDestAddress = mRespone.getOutput().get(0).getUserDestAddress();
							//                            SharedPrefsData.putString(getActivity(), Constants.CURRENTSTART, currentLocationstart, Constants.PREF_NAME);
							//                            SharedPrefsData.putString(getActivity(), Constants.CURRENTDROP, currentLocationdrop, Constants.PREF_NAME);
						}
						else {
							if(mRespone.getRstatus() == 0)
							{
								//   GetPairedDetailsAPI();
								if(mRespone.getMessage().equals("User already paired"))
								{
									Intent intent = new Intent(HomeMapsActivity.this, PairActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
									intent.putExtra("currentlatLngstart", String.valueOf(mRespone.getResult().get(0).getUserSourceLatLong()));
									intent.putExtra("currentlatLngdrop",   String.valueOf(mRespone.getResult().get(0).getUserDestLatLong()));
									intent.putExtra("pinLoc",   String.valueOf(mRespone.getResult().get(0).getUserSourceAddress()));
									intent.putExtra("DropLoc",   String.valueOf(mRespone.getResult().get(0).getUserDestAddress()));
									intent.putExtra("RideId", mRespone.getResult().get(0).getRideId());
									Log.i(TAG, "onNext: GetRideDetailsUpdateAPI: RideID:"+ mRespone.getResult().get(0).getRideId());

									startActivity(intent);
									finish();
								}
							}
							//RideAlreadyInitiatedDialog(HomeMapsActivity.this,"",mRespone.getMessage(),"Ok");
						}


					}
				});

	}

	/**
	 * Json object of getRideDetailsUpdateObject
	 * @return
	 */
	private JsonObject getRideDetailsUpdateObject(String locationStart, String locationdrop, LatLng pinLatLong, LatLng dropLatLong)
	{
		//        LatLng PinLatLng = new LatLng(PinLatitude,PinLongitude);
		//        LatLng DropLatLng = new LatLng(DropLatitude,DropLongitude);
		String currentlatlongStartStr=String.valueOf(pinLatLong);
		String currentlatLngstartStr=currentlatlongStartStr.replace("lat/lng:","");
		String newcurrentlatLngstartStr=currentlatLngstartStr.replaceAll("\\(","[")
				.replaceAll("\\)","]");
		Log.i(TAG, "getRideDetailsUpdateObject: newcurrentlatLngstartStr"+newcurrentlatLngstartStr);

		String currentlatlongDropStr=String.valueOf(dropLatLong);
		String currentlatLngdropStr=currentlatlongDropStr.replace("lat/lng:","");
		String newcurrentlatLngdropStr=currentlatLngdropStr.replaceAll("\\(","[")
				.replaceAll("\\)","]");
		Log.i(TAG, "getRideDetailsObject: currentlatlongDropStr"+newcurrentlatLngstartStr);

		String removePinSplChars = locationStart.replaceAll("[^a-zA-Z0-9]", " ");
		String removeDropSplChars = locationdrop.replaceAll("[^a-zA-Z0-9]", " ");

		GetRideDetailsUpdateRequestModel requestModel = new GetRideDetailsUpdateRequestModel();
		requestModel.setUserId(UserId);
		requestModel.setUserSourceAddress(removePinSplChars);
		requestModel.setUserSourceLatLong(newcurrentlatLngstartStr);
		requestModel.setUserDestAddress(removeDropSplChars);
		requestModel.setUserDestLatLong(newcurrentlatLngdropStr);
		requestModel.setShareWith(Integer.valueOf(shareTxt));
		requestModel.setVehicleType("Auto");
		// requestModel.setRideDateTime(currentdatetime);
		return new Gson().toJsonTree(requestModel).getAsJsonObject();
	}

	/************************END OF GET RIDE DETAILS UPDATE API******************************/

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


	public static void RangeAlertDialog(Context context, String Title, String Msg, Runnable runnable)
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
		okBtn.setText("Ok");
		// if decline button is clicked, close the custom dialog
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				runnable.run();
			}
		});
	}


	/************************START OF GetPairedDetailsAPI******************************/
	/*
	 * GetPairedDetailsAPI
	 * */
	private void GetPairedDetailsAPI()
	{
		JsonObject object = getPairedDetailsObject();
		AtroadsService service = ServiceFactory.createRetrofitService(HomeMapsActivity.this, AtroadsService.class);
		mSubscription = service.PairedDetailsResponse(object)
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<PairedDetailsResponseModel>() {
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
					public void onNext(PairedDetailsResponseModel mRespone) {
						hideProgressDialog();
						Log.i(TAG, "PairedDetailsResponseModel: "+mRespone);
						if(mRespone.getStatus() == 1)
						{
							String Attention = mRespone.getResult().get(0).getAttention();
							String SrcAddress = mRespone.getResult().get(0).getUserSourceAddress();
							String DestAddress = mRespone.getResult().get(0).getUserDestAddress();
							int PUserId = mRespone.getResult().get(0).getUserId();
							int pstatus = mRespone.getResult().get(0).getmPstatus();
							int user_ride_id = mRespone.getResult().get(0).getUser_ride_id();
							//                          ShowDialogAccordingToPStatus(pstatus,Attention,SrcAddress,DestAddress);
							CustomDialogWithOneBtn(HomeMapsActivity.this,"Hey..! You are Already Paired", Attention + "\n" + SrcAddress + "\n" + DestAddress,"Continue", new Runnable() {
								@Override
								public void run() {
									Intent intent = new Intent(HomeMapsActivity.this, PairActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
									intent.putExtra("currentlatLngstart", String.valueOf(mRespone.getResult().get(0).getUserSourceLatLong()));
									intent.putExtra("currentlatLngdrop",   String.valueOf(mRespone.getResult().get(0).getUserDestLatLong()));
									intent.putExtra("pinLoc",   String.valueOf(mRespone.getResult().get(0).getUserSourceAddress()));
									intent.putExtra("DropLoc",   String.valueOf(mRespone.getResult().get(0).getUserDestAddress()));
									intent.putExtra("PSTATUS",pstatus);
									intent.putExtra("RideId",user_ride_id);
									startActivity(intent);
									finish();
								}
							});
						}
						else {
							//RideAlreadyInitiatedDialog(HomeMapsActivity.this,"",mRespone.getMessage(),"Ok");
						}


					}
				});

	}

	/**
	 * Json object of getPairedDetailsObject
	 * @return
	 */
	private JsonObject getPairedDetailsObject()
	{
		PairedDetailsRequestModel requestModel = new PairedDetailsRequestModel();
		requestModel.setUserId(UserId);
		return new Gson().toJsonTree(requestModel).getAsJsonObject();
	}

	/************************END OF GET RIDE DETAILS API******************************/


	@Override
	public void onTaskDone(Object... values) {
		if (currentPolyline != null)
			currentPolyline.remove();
		currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {


	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	@Override
	public void onLocationChanged(Location location) {
		//        mLastLocation = location;
		//        if (mCurrLocationMarker != null) {
		//            mCurrLocationMarker.remove();
		//        }
		//        String addressOfLocation = GetAddressFromLatLng(location.getLatitude(), location.getLongitude());
		//        Log.i(TAG, "onLocationChanged: "+addressOfLocation);
		//        //Place current location marker
		//        PinLatitude = location.getLatitude();
		//        PinLongitude = location.getLongitude();
		//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		//        MarkerOptions markerOptions = new MarkerOptions();
		//        markerOptions.position(latLng);
		//        markerOptions.title(addressOfLocation+"");
		//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		//        mMap.addMarker(markerOptions);
		//
		//        //move map camera
		//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		//        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
		//
		//        PinEditText.setText(addressOfLocation+"");
		//        //stop location updates
		//        if (mGoogleApiClient != null) {
		//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		//        }
	}

	@Override
	public boolean onMarkerClick(Marker marker)
	{
		return false;
	}

	/**********************************START OF PROGRESS DIALOG*************************/

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

	/**********************************END OF PROGRESS DIALOG*************************/


	@Override
	public void onBackPressed() {
		//        super.onBackPressed();
		DialogWithTwoButtons(HomeMapsActivity.this, getString(R.string.ClosingApp), getString(R.string.AreYouSureToExit), getString(R.string.Yes), new Runnable() {
			@Override
			public void run() {
				closeApplication();
			}
		}, getString(R.string.No), new Runnable() {
			@Override
			public void run() {

			}
		});
	}




	public void FirebaseLogin(String user, String pass, String email)
	{
		String url = "https://atroads-d26a5.firebaseio.com/AtroadsUsers.json";
		StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
			@Override
			public void onResponse(String s) {
				if(s.equals("null")){
					//                    Toast.makeText(HomeMapsActivity.this, "user not found", Toast.LENGTH_LONG).show();
				}
				else{
					try {
						JSONObject obj = new JSONObject(s);

						if(!obj.has(user)){
							//                            Toast.makeText(HomeMapsActivity.this, "user not found", Toast.LENGTH_LONG).show();
						}
						else if(obj.getJSONObject(user).getString("password").equals(pass)){
							UserDetails.username = user;
							UserDetails.password = pass;
							//                            SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginCreds", 0); // 0 - for private mode
							//                            SharedPreferences.Editor editor = pref.edit();
							//                            editor.putString("mobile_number", mobileNumber); // Storing string
							//                            editor.putString("user_name", userName);
							//                            editor.putInt("user_id", userIdInt);
							//                            editor.putString("email",email);
							//                            editor.putString("password", passwordEditText.getText().toString());
							//                            editor.commit();
							// startActivity(new Intent(LoginActivity.this, UsersActivity.class));
						}
						else {
							Toast.makeText(HomeMapsActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

			}
		},new Response.ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				System.out.println("" + volleyError);
			}
		});

		RequestQueue rQueue = Volley.newRequestQueue(HomeMapsActivity.this);
		rQueue.add(request);
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
							Toast.makeText(HomeMapsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
						}
						else if(mResponse.getStatus() == 1)
						{
							Toast.makeText(HomeMapsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
							//  LoadImageFromUrl(EditProfileScreen.this, APIConstants.IMAGE_URL+pflPic,profile_image);
						}
					}
				});

	}

	/**
	 * Json object of CallEditUserInfoAPI
	 *
	 * @return
	 */
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
						//                        Toast.makeText(HomeMapsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
						if(mResponse.getStatus() == 1)
						{
							user_ride_id = mResponse.getResult().get(0).getUser_ride_id();
							Log.i(TAG, "PairedUserDetailsResponseModel: "+user_ride_id);
							String Attention = mResponse.getResult().get(0).getAttention();
							PStatus = mResponse.getResult().get(0).getPstatus();
							String SourceAddress = mResponse.getResult().get(0).getUserSourceAddress();
							String DestAddress = mResponse.getResult().get(0).getUserDestAddress();

							/**
							 * saving Paired User one details
							 */
							SharedPreferences pref1 = getApplicationContext().getSharedPreferences("PairedUserPref", 0); // 0 - for private mode
							SharedPreferences.Editor editor = pref1.edit();
							editor.putInt("user_ride_id", user_ride_id);
							editor.commit();
							//                           CustomDialogWithOneBtn(HomeMapsActivity.this, "Attention!", mResponse.getResult().get(0).getAttention()+"\n"
							//                                   +mResponse.getResult().get(0).getSource()+"\n"+mResponse.getResult().get(0).getDestination()+"\n", "Ok", new Runnable() {
							//                               @Override
							//                               public void run() {
							//                                  CallOnGoingRideAPI();
							//                               }
							//                           });
							//
							CustomDialogWithOneBtn(HomeMapsActivity.this,"Hey..! You are Already Paired", Attention + "\n" + "From: "+SourceAddress + "\n" + "To: "+DestAddress,"Continue", new Runnable() {
								@Override
								public void run() {
									CallOnGoingRideAPI();
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




	/********************************START OF CallOnGoingRideAPI*******************************/
	/*
	 * CallOnGoingRideAPI
	 * */
	private void CallOnGoingRideAPI(){

		JsonObject object = OnGoingRideObject();
		AtroadsService service = ServiceFactory.createRetrofitService(HomeMapsActivity.this, AtroadsService.class);
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
						Toast.makeText(HomeMapsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
						if(mResponse.getStatus() == 0)
						{
							//Toast.makeText(PairSuccessScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
						}
						else if(mResponse.getStatus() == 1)
						{
							if(mResponse.getResult().size() == 0)
							{
								// to paired details screen
								Log.i(TAG, "onNext : paired details screen ");
								//                                List<Double> srcList = mResponse.getResult().get(0).getUserSourceLatLong();
								//                                List<Double> destList = mResponse.getResult().get(0).getUserDestLatLong();
								//                                LatLng srcLatLng = new LatLng(srcList.get(0),srcList.get(1));
								//                                LatLng destLatLng = new LatLng(destList.get(0),destList.get(1));
								//                                Intent i = new Intent(HomeMapsActivity.this, PairSuccessScreen.class);
								//                                i.putExtra("RideStatus","StartRide");
								//                                i.putExtra("UserRideId",user_ride_id);
								//                                i.putExtra("AutoNo",mResponse.getResult().get(0).getAutoNumber());
								//                                startActivity(i);

								CallDetailsOfRideAPI();
							}
							else {

								List<Double> srcList = mResponse.getResult().get(0).getUserSourceLatLong();
								List<Double> destList = mResponse.getResult().get(0).getUserDestLatLong();
								LatLng srcLatLng = new LatLng(srcList.get(0),srcList.get(1));
								LatLng destLatLng = new LatLng(destList.get(0),destList.get(1));
								Intent i = new Intent(HomeMapsActivity.this, PairSuccessScreen.class);
								i.putExtra("RideStatus","RideStarted");
								i.putExtra("UserRideId",user_ride_id);
								i.putExtra("AutoNo",mResponse.getResult().get(0).getAutoNumber());
								startActivity(i);

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

	/***********************End of OnGoing Rides API****************************/


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
							Toast.makeText(HomeMapsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();

						}
						else if(mResponse.getStatus() == 1)
						{
							if(mResponse.getResult().size() == 0)
							{
								if(mResponse.getMessage().equals("Please enter auto no"))
								{
									//                                    LatLng PinLatLong = getLocationFromAddress(HomeMapsActivity.this,PinEditText.getText().toString());
									//                                    LatLng DropLatLong = getLocationFromAddress(HomeMapsActivity.this,DropEditText.getText().toString());
									//
									//                                    GetRideDetailsAPI(PinEditText.getText().toString(), DropEditText.getText().toString(),PinLatLong,DropLatLong);
									String source = mResponse.getResult().get(0).getSource();
									String dest = mResponse.getResult().get(0).getDest();
									Intent intent = new Intent(HomeMapsActivity.this, PairActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
									//                                    intent.putExtra("currentlatLngstart", String.valueOf(mRespone.getResult().get(0).getUserSourceLatLong()));
									//                                    intent.putExtra("currentlatLngdrop",   String.valueOf(mRespone.getResult().get(0).getUserDestLatLong()));
									intent.putExtra("pinLoc",  source);
									intent.putExtra("DropLoc",   dest);
									intent.putExtra("PSTATUS",PStatus);
									intent.putExtra("RideId",user_ride_id);
									startActivity(intent);
									finish();
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
								Intent intent = new Intent(HomeMapsActivity.this, PairSuccessScreen.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("RideStatus","StartRide");
								intent.putExtra("UserRideId",mResponse.getResult().get(0).getUser_ride_id());
								intent.putExtra("AutoNo",AutoNo);
								startActivity(intent);
								finish();
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

	/**
	 * To close the Application
	 */
	public void closeApplication()
	{
		finish();
		moveTaskToBack(true);
	}


	public void onResume(){
		super.onResume();
		Log.i(TAG, "onResume: getlocation");
		//        CheckLocationEnabledOrNot();
		buildGoogleApiClient();
	}

	@Override
	public void onActivityReenter(int resultCode, Intent data) {
		super.onActivityReenter(resultCode, data);
		Log.i(TAG, "onActivityReenter: ");
	}

}
