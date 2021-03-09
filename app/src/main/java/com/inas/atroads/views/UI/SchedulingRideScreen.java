package com.inas.atroads.views.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.*;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.inas.atroads.util.Utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
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
import com.google.gson.*;
import com.inas.atroads.R;
import com.inas.atroads.services.*;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.util.localData.FetchURL;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.model.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.*;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import static com.inas.atroads.util.Utilities.isNetworkAvailable;
import static com.inas.atroads.views.Activities.BillingDetailsActivity.CustomDialog;

public class SchedulingRideScreen extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "SchedulingRideScreen";
    private static final String DEFAULT = "N/A";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private TextView from_timer,end_timer,edt_date;
    private ImageView iv_date;
    private EditText home_edit,work_edit,another_edit;
    private Calendar calender;
    private int current_hour;
    private int current_min;
    private TimePickerDialog timePickerDialog;
    private Spinner mspinner;
    private Button submit,another_button;
    private String am_pm;
    private Toolbar toolbar;
    private Switch pairSwitch;
    private int isPairing = 0;
    private Subscription mSubscription;
    private int UserId;
    private String Mobile,Email,Username;
    private String selectedTime;
    GoogleApiClient mGoogleApiClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager mLocationManager;
    private String FromEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduling_ride_screen);
        isNetworkAvailable(SchedulingRideScreen.this);
        GetSharedPrefs();
        initViews();
        initGAPIClient();
        CallGetSchedulingRideAPI();
    }

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
    }

    private void initViews()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.SchedulingRide));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edt_date = findViewById(R.id.edt_date);
        iv_date = findViewById(R.id.iv_date);
        from_timer=findViewById(R.id.start_time);
        end_timer=findViewById(R.id.end_time);
        mspinner=findViewById(R.id.spinner);

        setDate();
        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
//                Toast.makeText(parent.getContext(), "Selected: " + selectedTime, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit=findViewById(R.id.submit);
        home_edit=findViewById(R.id.home);
        work_edit=findViewById(R.id.work);
        another_edit=findViewById(R.id.another_loc);
        home_edit.setInputType(InputType.TYPE_NULL);
        work_edit.setInputType(InputType.TYPE_NULL);
        another_edit.setInputType(InputType.TYPE_NULL);
        home_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FromEditText = "HomeEditText";
                AutoCompleteIntent();
            }
        });
        work_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FromEditText = "WorkEditText";
                AutoCompleteIntent();
            }
        });
        another_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FromEditText = "AnotherEditText";
                AutoCompleteIntent();
            }
        });

        another_button=findViewById(R.id.another_btn);
        pairSwitch=findViewById(R.id.pairSwitch);
        SetEndTimer();
        SetFromTimer();
        SetSubmitBtn();
        AddAnotherBtnOnclick();
        PairSwitch();
    }

/*****************************START OF INIT GAPICLIENT********************************/
    /**
     * initGAPIClient()
     */
    private void initGAPIClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(SchedulingRideScreen.this)
                .addConnectionCallbacks(SchedulingRideScreen.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SchedulingRideScreen.this);
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
            Places.initialize(SchedulingRideScreen.this, getString(R.string.google_maps_key));
//            Places.initialize(HomeMapsActivity.this, "AIzaSyCl6GKb2QSL6jL0tAumlzbRJaYfzSLhFgs");
//            Places.initialize(HomeMapsActivity.this, "AIzaSyBKKbYBBBaKBwLRxEzcb9DyGaMYwQlYb0I");
        }

        // Create a new Places client instance
        PlacesClient placesClient = Places.createClient(SchedulingRideScreen.this);

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
                .build(SchedulingRideScreen.this);
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
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                Log.i(TAG, "PlaceAddress: " + place.getAddress());
                Log.i(TAG, "PlaceAddressComponents(): " + place.getAddressComponents());
                Log.i(TAG, "place Viewport: " + place.getViewport() + place.getAttributions());
                if(FromEditText.equals("HomeEditText"))
                {
                    home_edit.setText(place.getName());
                    PinlatLng = place.getLatLng();
                    Log.i(TAG, "onActivityResult:PinlatLng "+PinlatLng);
                }else if(FromEditText.equals("WorkEditText"))
                {
                    work_edit.setText(place.getName());
                    DropLatLng = place.getLatLng();
                    Log.i(TAG, "onActivityResult:DropLatLng "+DropLatLng);
                }
                else if(FromEditText.equals("AnotherEditText"))
                {
                    another_edit.setText(place.getName());
                    DropLatLng = place.getLatLng();
                    Log.i(TAG, "onActivityResult:DropLatLng "+DropLatLng);
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

        AtroadsService service = ServiceFactory.createRetrofitService(SchedulingRideScreen.this, AtroadsService.class);
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
                        Log.i(TAG, "onNext: "+formattedAddress);

                        if(mResponse.getStatus().equals("OK")) {
                            if (FromEditText.equals("HomeEditText")) {
                                home_edit.setText(formattedAddress);
                            }else if(FromEditText.equals("WorkEditText")) {
                                work_edit.setText(formattedAddress);
                            }else if(FromEditText.equals("AnotherEditText")) {
                                another_edit.setText(formattedAddress);
                            }
                        }
                    }
                });
    }


    /********************************END OF getAddressComponentsAPI******************/


    private void PairSwitch()
    {
        pairSwitch = (Switch) findViewById(R.id.pairSwitch);
        pairSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    isPairing = 1;
                    //CallSchedulingRideNotifyAPI();
                } else {
                    // The toggle is disabled
                    isPairing = 0;
                }
            }
        });
    }


    private void AddAnotherBtnOnclick() {
        another_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                another_edit.setVisibility(View.VISIBLE);
            }
        });
    }


    private void SetSubmitBtn() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (home_edit.length() == 0) {
                    home_edit.setError("Please enter home location");
                } else if (work_edit.length() == 0) {
                    work_edit.setError("Please enter work location");
                } else {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                    String inTime = from_timer.getText().toString();
                    String outTime = end_timer.getText().toString();

                    if (inTime.compareTo(outTime) < 0) {
                        // Do your staff
                        //Log.i("app", "Date1 is before Date2");
                        showProgressDialog();
                        CallSchedulingRideAPI();
                    } else {
                        Log.i("app", "Date1 is after Date2");
                        Toast.makeText(SchedulingRideScreen.this, "Please enter correct time", Toast.LENGTH_LONG).show();
                        return;
                    }

                /*String pattern = "HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                try {
                    Date date1 = sdf.parse(inTime);
                    Date date2 = sdf.parse(outTime);

                       *//* if(date1.compareTo(date2)== -1){
                            Log.d("Return","intime should be greater that outtime ");

                        }else*//* if(date2.compareTo(date1) == 1){
                            Log.d("Return","Perfect Time");

                        }else{
                            Log.d("Return","intime should be greater that outtime");

                        }
                    // Outputs -1 as date1 is before date2
                    System.out.println("1-"+date1.compareTo(date2));

                    // Outputs 1 as date1 is after date1
                    System.out.println("2-"+date2.compareTo(date1));

                    date2 = sdf.parse("19:28");
                    // Outputs 0 as the dates are now equal
                    System.out.println("3-"+date1.compareTo(date2));

                } catch (ParseException e){
                    // Exception handling goes here
                }*/


              /*  String inTime= from_timer.getText().toString();
                String outTime= end_timer.getText().toString();

                if (inTime .compareTo(outTime) > 0) {
                    // Do your staff
                    Log.d("Return","outTime should be greater that intime ");
                }
                else {
                    Log.d("Return","intime should be greater that outtime ");
                }*/
                /*else
                {
                    showProgressDialog();
//                    Toast.makeText(SchedulingRideScreen.this,"submitted",Toast.LENGTH_SHORT).show();
                    CallSchedulingRideAPI();
                }*/
                }
            }
        });
    }
    private void SetEndTimer()
    {
        end_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calender=Calendar.getInstance();
                current_hour=calender.get(Calendar.HOUR_OF_DAY);
                current_min=calender.get(Calendar.MINUTE);
                timePickerDialog= new TimePickerDialog(SchedulingRideScreen.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        if(hour>=12)
                            am_pm="PM";
                        else
                            am_pm="AM";

//                        end_timer.setText(String.format("%02d:%02d",hour,min)+am_pm);
                        end_timer.setText(String.format("%02d:%02d",hour,min));
                    }
                },current_hour,current_min,true);
                timePickerDialog.show();
            }
        });
    }

    private void SetFromTimer()
    {
        from_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calender=Calendar.getInstance();
                current_hour=calender.get(Calendar.HOUR_OF_DAY);
                current_min=calender.get(Calendar.MINUTE);
                timePickerDialog= new TimePickerDialog(SchedulingRideScreen.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        if(hour>=12)
                            am_pm="PM";
                        else
                            am_pm="AM";

                        from_timer.setText(String.format("%02d:%02d",hour,min));
                    }
                },current_hour,current_min,true);
                timePickerDialog.show();
            }
        });
    }

    /********************************START OF CallSchedulingRideAPI*******************************/
    /*
     * CallSchedulingRideAPI
     * */
    private void CallSchedulingRideAPI(){

        JsonObject object = SchedulingRideObject();
        AtroadsService service = ServiceFactory.createRetrofitService(SchedulingRideScreen.this, AtroadsService.class);
        mSubscription = service.SchedulingRide(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SchedulingRideResponseModel>() {
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
                    public void onNext(SchedulingRideResponseModel mResponse) {
                        Log.i(TAG, "ScheduleRideNotifyResponseModel: "+mResponse);
//                        Toast.makeText(YourBillScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {

                        }
                        else if(mResponse.getStatus() == 1)
                        {

                            CallSchedulingRideNotifyAPI();

                        }
                    }
                });
    }




    /**
     * Json object of SchedulingRideObject
     *
     * @return
     */
    private JsonObject SchedulingRideObject()
    {
        GetSharedPrefs();
        SchedulingRideRequestModel requestModel = new SchedulingRideRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setFromTime(from_timer.getText().toString());
        requestModel.setToTime(end_timer.getText().toString());
        requestModel.setHome(home_edit.getText().toString());
        requestModel.setOffice(work_edit.getText().toString());
        requestModel.setOther(another_edit.getText().toString());
        requestModel.setDate(edt_date.getText().toString());
        requestModel.setPairing(isPairing);
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(selectedTime);
        while (m.find()) {
            int n = Integer.parseInt(m.group());
            // append n to list
            requestModel.setWhenYouWantToNotify(n);
        }
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF CallSchedulingRideAPI *******************************/




    /********************************START OF CallSchedulingRideAPI*******************************/
    /*
     * CallSchedulingRideNotifyAPI
     * */
    private void CallSchedulingRideNotifyAPI(){

        JsonObject object = SchedulingRideNotifyObject();
        AtroadsService service = ServiceFactory.createRetrofitService(SchedulingRideScreen.this, AtroadsService.class);
        mSubscription = service.SchedulingRideNotify(object)
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
                        if(mResponse.getStatus() == 0)
                        {
                            hideProgressDialog();
                            Toast.makeText(SchedulingRideScreen.this, mResponse.getMessage(),Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            hideProgressDialog();
                            CustomDialog(SchedulingRideScreen.this, "Schedule Ride", "Scheduling ride added successfully", "Done", new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    //Intent i = new Intent(SchedulingRideScreen.this, HomeMapsActivity.class);
//                            i.putExtra("payableAmount",payableAmount.getText().toString());
                                 //   startActivity(i);
                                }
                            });
                         //schedule notification here
                        }
                    }
                });

    }

    /**
     * Json object of SchedulingRideNotifyObject
     *
     * @return
     */
    private JsonObject SchedulingRideNotifyObject()
    {
        ScheduleRideNotifiyRequestModel requestModel = new ScheduleRideNotifiyRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF CallSchedulingRideAPI *******************************/




    /********************************START OF GetSchedulingRideObject*******************************/
    /*
     * GetSchedulingRideObject
     * */
    private void CallGetSchedulingRideAPI(){

        JsonObject object = GetSchedulingRideObject();
        AtroadsService service = ServiceFactory.createRetrofitService(SchedulingRideScreen.this, AtroadsService.class);
        mSubscription = service.GetSchedulingRideResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetSchedulingRideResponseModel>() {
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
                    public void onNext(GetSchedulingRideResponseModel mResponse) {
                        Log.i(TAG, "GetSchedulingRideResponseModel: "+mResponse);
//                        Toast.makeText(YourBillScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {

                        }
                        else if(mResponse.getStatus() == 1)
                        {
                           String FromTime = mResponse.getResult().get(0).getSchedulingRide().getFromTime();
                           from_timer.setText(FromTime+"");
                            String ToTime = mResponse.getResult().get(0).getSchedulingRide().getToTime();
                            end_timer.setText(ToTime+"");
                            String Home = mResponse.getResult().get(0).getSchedulingRide().getHome();
                            home_edit.setText(Home+"");
                            String Office = mResponse.getResult().get(0).getSchedulingRide().getOffice();
                            work_edit.setText(Office+"");
                            String date= mResponse.getResult().get(0).getSchedulingRide().getDate();
                            edt_date.setText(""+date);
                            String Another = mResponse.getResult().get(0).getSchedulingRide().getOther();
                            if(Another.equals(""))
                            {
                                another_edit.setText(Another+"");
                                another_edit.setVisibility(View.GONE);
                            }
                            else {
                                another_edit.setText(Another+"");
                                another_edit.setVisibility(View.VISIBLE);
                            }

                            int pairing = mResponse.getResult().get(0).getSchedulingRide().getPairing();
                            //0- off , 1- on
                            if(pairing == 0)
                            {
                                pairSwitch.setChecked(false);
                            }
                            else if(pairing == 1){
                                pairSwitch.setChecked(true);
                            }
                            int mins = mResponse.getResult().get(0).getSchedulingRide().getWhenYouWantToNotify();
                            if(mins == 10)
                            {
                                mspinner.setSelection(0);
                            }
                            else if(mins == 20)
                            {
                                mspinner.setSelection(1);
                            }
                            else if(mins == 30)
                            {
                                mspinner.setSelection(2);
                            }
                            else if(mins == 40)
                            {
                                mspinner.setSelection(3);
                            }
                            else if(mins == 50)
                            {
                                mspinner.setSelection(4);
                            }
                            else if(mins == 60)
                            {
                                mspinner.setSelection(5);
                            }
                        }
                    }
                });

    }

    /**
     * Json object of GetSchedulingRideObject
     *
     * @return
     */
    private JsonObject GetSchedulingRideObject()
    {
        GetSchedulingRideRequestModel requestModel = new GetSchedulingRideRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
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

    /*************************************END OF GetSchedulingRideObject *******************************/

    private void setDate()
    {
        String date = Utilities.getCurrentDate();
        edt_date.setText(date);

        edt_date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setDatePickerDialog();

            }
        });
        iv_date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setDatePickerDialog();
            }
        });
    }

    /**
     Description : Show Date picker dialog
     */
    private void setDatePickerDialog()
    {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        long currentDate = c.getTimeInMillis();
        long maxDate = currentDate + (1000*60*60*24*15);
        Log.v(TAG, " In setDatePickerDialog() currentDate = " + currentDate);

        Log.v(TAG, " In setDatePickerDialog() maxDate = " + maxDate);

        Date max_date = new Date(maxDate);
        c.setTime(max_date);


        DatePickerDialog datePickerDialog = new DatePickerDialog(SchedulingRideScreen.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view,int year,
                                          int monthOfYear,int dayOfMonth) {

                        String date=String.format("%02d/%02d/%d ", dayOfMonth,(monthOfYear + 1),year);
                        edt_date.setText(""+date.trim());


                    }
                },mYear,mMonth,mDay);
        datePickerDialog.getDatePicker().setMinDate(currentDate);

        datePickerDialog.show();

    }
}