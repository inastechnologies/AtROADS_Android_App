package com.inas.atroads.views.Activities;

import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.views.model.CancelRideDetailsRequestModel;
import com.inas.atroads.views.model.CancelRideDetailsResponseModel;
import com.inas.atroads.views.model.StartRideRequestModel;
import com.inas.atroads.views.model.StartRideResponseModel;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class TravelModeActivity extends BaseActivity
{
    private Context mContext;
    private Toolbar toolbar;
    private RadioButton manualRadioBtn,meterRadioBtn;
    private EditText autoNumberEdtTxt,AmountEdtTxt;
    private String TravelType = "Manual";
    private Button startRideBtn;
    int UserId;
    private Subscription mSubscription;
    private int RideId,USER_RIDES_ID;
    private static final String TAG = "TravelModeActivity";
    private String currentLocationstart = "", currentLocationdrop = "";
    private static final String DEFAULT = "N/A";
    private String Mobile,Email,Username;
    private int user_ride_id,ride_initiater_id,userOneId,userTwoId, UserRideId;
    private String userOneName,userTwoName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        //assigning layout
        setContentView(R.layout.activity_travel_mode);
        initViews();
        isNetworkAvailable(TravelModeActivity.this);
      //  USER_RIDES_ID = SharedPrefsData.getInt(mContext, "" + Constants.USER_RIDES_ID, Constants.PREF_NAME);
        RideId = getIntent().getIntExtra("RideId",0);
        UserRideId = getIntent().getIntExtra("user_ride_id",0);

    }


    private void initViews()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Travel Mode");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TravelModeActivity.this, HomeMapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        autoNumberEdtTxt = findViewById(R.id.autoNumberEdtTxt);
        AmountEdtTxt = findViewById(R.id.AmountEdtTxt);
        final RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedRadioButtonID = rg.getCheckedRadioButtonId();
                // If nothing is selected from Radio Group, then it return -1
                if (selectedRadioButtonID != -1) {

                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                    String selectedRadioButtonText = selectedRadioButton.getText().toString();
                    TravelType = selectedRadioButtonText;
                    if(TravelType.equals("Meter"))
                    {
                        AmountEdtTxt.setVisibility(View.GONE);
                        AmountEdtTxt.setText("0");
                    }
                    else {
                        AmountEdtTxt.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    TravelType = "Manual";
//                    tv_result.setText("Nothing selected from Radio Group.");
                }
            }
        });

        //   GetSharedPrefs();
        GetSharedPrefs();
        GetRidePrefs();
        SetStartBtn();

    }


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
        ride_initiater_id = pref.getInt("ride_initiater_id", 0);
        userOneId = pref.getInt("userOneId", 0);
        userTwoId = pref.getInt("userTwoId", 0);
        user_ride_id = pref.getInt("user_ride_id", user_ride_id);
        userOneName = pref.getString("userOneName", DEFAULT);
        userTwoName = pref.getString("userTwoName", DEFAULT);

//        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("RideidPref", 0); // 0 - for private mode
//        user_ride_id = pref1.getInt("user_ride_id", 0);

    }


    private void SetStartBtn()
    {
        startRideBtn = findViewById(R.id.startRideBtn);
        startRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartRideAPI();
//                if(startRideBtn.getText().toString().equals("End Ride"))
//                {
//                    CancelRideAPI();
//                }
//                else {
//                    StartRideAPI();
//                }
            }
        });
    }


    /*
     * StartRideAPI
     * */
    public void StartRideAPI() {

        JsonObject object = StartRideDetailsObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.StartRideResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StartRideResponseModel>() {
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
                    public void onNext(StartRideResponseModel mRespone) {
                        Log.i(TAG, "StartRideResponse: "+mRespone);
                        Toast.makeText(mContext, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            CustomDialog(TravelModeActivity.this,"Success",mRespone.getMessage(),"Ok", new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(TravelModeActivity.this, PairSuccessScreen.class);
                                    intent.putExtra("RideStatus","RideStarted");
                                    intent.putExtra("UserRideId",UserRideId);
                                    intent.putExtra("AutoNo",autoNumberEdtTxt.getText().toString());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }, new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                        else {
                            if(mRespone.getMessage().equals("Other User Cancelled the ride.Please try again"))
                            {
                                CustomDialog(TravelModeActivity.this,"Sorry!",mRespone.getMessage(),"Ok", new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(TravelModeActivity.this, HomeMapsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }

                     //   startRideBtn.setText("End Ride");
                    }
                });

    }

    /**
     * Json object of ridedetailsObject
     * @return
     */
    private JsonObject StartRideDetailsObject()
    {
//        LatLng currentLatLng = new LatLng(pairingcurrentlatLng.latitude,pairingcurrentlatLng.longitude);
//        String cLatLng = String.valueOf(currentLatLng);
//        String currentlatLngStartStr=cLatLng.replace("lat/lng:","");
//        String newcurrentlatLngStr=currentlatLngStartStr.replaceAll("\\(","[")
//                .replaceAll("\\)","]");


        StartRideRequestModel requestModel = new StartRideRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setUserRideId(UserRideId);
        requestModel.setAutoNumber(autoNumberEdtTxt.getText().toString());
        requestModel.setAmount(Double.valueOf(AmountEdtTxt.getText().toString()));
        requestModel.setType(TravelType+"");
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }



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
                        Toast.makeText(TravelModeActivity.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus()==1)
                        {
                            Intent intent = new Intent(TravelModeActivity.this, HomeMapsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
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
        cancelRideDetailsRequestModelObj.setRideId(RideId);
        return new Gson().toJsonTree(cancelRideDetailsRequestModelObj).getAsJsonObject();
    }



    public static void CustomDialog(Context context, String Title, String Msg, String buttonNam1, Runnable runnable,Runnable runnable1)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.single_button_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView title = (TextView) dialog.findViewById(R.id.TitleTv);
        title.setText("Success");
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TravelModeActivity.this, HomeMapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
