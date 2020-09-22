package com.inas.atroads.views.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.Activities.PairActivity;
import com.inas.atroads.views.Activities.PairSuccessScreen;
import com.inas.atroads.views.Activities.TravelModeActivity;
import com.inas.atroads.views.model.CancelRideDetailsRequestModel;
import com.inas.atroads.views.model.CancelRideDetailsResponseModel;
import com.inas.atroads.views.model.DeletePairRequestModel;
import com.inas.atroads.views.model.DeletePairResponseModel;
import com.inas.atroads.views.model.GetDetailsOfRideRequestModel;
import com.inas.atroads.views.model.GetDetailsOfRideResponseModel;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class PairedDetailsScreen extends AppCompatActivity {
    private static final String DEFAULT = "N/A";
    private TextView DescTv;
    private Button okBtn,cancelBtn;
    private String Username,Email,Mobile,ProfilePic,Message;
    private int UserId,user_ride_id,PStatus;
    private Subscription mSubscription;
    private  Handler handler;
    private String AutoNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_details_screen);
        SetViewsFromLayout();
    }

    private void SetViewsFromLayout()
    {
        cancelBtn = findViewById(R.id.cancelBtn);
        okBtn = findViewById(R.id.okBtn);
        DescTv = findViewById(R.id.DescTv);
        user_ride_id = getIntent().getIntExtra("user_ride_id",0);
        Message = getIntent().getStringExtra("Message");
        PStatus = getIntent().getIntExtra("PStatus",0);

        if(PStatus == 0)
        {
            DescTv.setText(""+Message);
        }
        else if(PStatus == 1)
        {
            DescTv.setText(""+Message);
//          DescTv.setText("Please Wait.. While the Other User Starts the Ride..");
//          GetDetailsOfRideTimer();
        }
        else if(PStatus == 2)
        {
            DescTv.setText(""+Message);
//            DescTv.setText("Please Wait.. While the Other User Starts the Ride..");
//            GetDetailsOfRideTimer();
        }
        else if(PStatus == 3)
        {
            DescTv.setText(""+Message);
        }
        GetSharedPrefs();
        SetCancelBtn();
        SetOkButton();
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

    private void SetOkButton()
    {
//        if(DescTv.getText().toString().contains("Please"))
//        {
//            okBtn.setEnabled(false);
//            okBtn.setAlpha(0.5f);
//        }
//        else {
//            okBtn.setEnabled(true);
//            okBtn.setAlpha(1.0f);
//            okBtn.setBackgroundResource(R.drawable.round_rect_button_bg);
//        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(PStatus == 0)
                {
                    DescTv.setText(""+Message);
                    Intent intent = new Intent(PairedDetailsScreen.this, TravelModeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("user_ride_id",user_ride_id);
                    startActivity(intent);
                    finish();
                }
                else if(PStatus == 1)
                {
                    DescTv.setText("Please Wait.. While the Other User Starts the Ride..");
                    GetDetailsOfRideTimer();
                }
                else if(PStatus == 2)
                {
                    DescTv.setText("Please Wait.. While the Other User Starts the Ride..");
                    GetDetailsOfRideTimer();
                }
                else if(PStatus == 3)
                {
                    DescTv.setText(""+Message);
                    Intent intent = new Intent(PairedDetailsScreen.this, TravelModeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("user_ride_id",user_ride_id);
                    startActivity(intent);
                    finish();
                }

//                if(PStatus == 0)
//                {
//                    Intent intent = new Intent(PairedDetailsScreen.this, TravelModeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("user_ride_id",user_ride_id);
//                    startActivity(intent);
//                    finish();
//                }
//                else if(PStatus == 1)
//                {
//                    Intent intent = new Intent(PairedDetailsScreen.this, PairSuccessScreen.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("RideStatus","StartRide");
//                    intent.putExtra("UserRideId",user_ride_id);
//                    intent.putExtra("AutoNo",AutoNo);
//                    startActivity(intent);
//                    finish();
//                }
//                else if(PStatus == 2)
//                {
//                    Intent intent = new Intent(PairedDetailsScreen.this, PairSuccessScreen.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("RideStatus","StartRide");
//                    intent.putExtra("UserRideId",user_ride_id);
//                    intent.putExtra("AutoNo",AutoNo);
//                    startActivity(intent);
//                    finish();
//                }
//                else if(PStatus == 3)
//                {
//                    Intent intent = new Intent(PairedDetailsScreen.this, TravelModeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("user_ride_id",user_ride_id);
//                    startActivity(intent);
//                    finish();
//                }

//                if(DescTv.getText().toString().contains("Your Auto Number"))
//                {
//                    Intent intent = new Intent(PairedDetailsScreen.this, PairSuccessScreen.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("RideStatus","StartRide");
//                    intent.putExtra("UserRideId",user_ride_id);
//                    intent.putExtra("AutoNo",AutoNo);
//                    startActivity(intent);
//                    finish();
//                }
//                else {
//                    Intent intent = new Intent(PairedDetailsScreen.this, TravelModeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("user_ride_id",user_ride_id);
//                    startActivity(intent);
//                    finish();
//                }
            }
        });
    }


    private void SetCancelBtn()
    {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelRideAPI();
            }
        });
    }
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
                            Intent intent = new Intent(PairedDetailsScreen.this, HomeMapsActivity.class);
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
        cancelRideDetailsRequestModelObj.setRideId(user_ride_id);
        return new Gson().toJsonTree(cancelRideDetailsRequestModelObj).getAsJsonObject();
    }

    /**************************END OF CANCEL RIDE API*********************************/




    /********************************START OF CallDeletePairAPI*******************************/
    /*
     * CallDeletePairAPI
     * */
    public void CallDeletePairAPI(){

        JsonObject object = DeletePairObject();
        AtroadsService service = ServiceFactory.createRetrofitService(PairedDetailsScreen.this, AtroadsService.class);
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
                            Toast.makeText(PairedDetailsScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            String username = mResponse.getResult().get(0).getFullname();
                            AutoNo = mResponse.getResult().get(0).getAutoNumber();
                            String source = mResponse.getResult().get(0).getSource();
                            String dest = mResponse.getResult().get(0).getDest();
//                            String Msg = username+" has started the Ride. Your Auto Number is "+AutoNo+"\n"+"\n"
////                                    +source+"\n"+dest;
                            String Msg = "Your Auto Number is "+AutoNo+"\n"+"\n"
                                    +source+"\n"+"\n"+dest;

                            DescTv.setText(Msg);

                            okBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(PairedDetailsScreen.this, PairSuccessScreen.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("RideStatus","StartRide");
                                    intent.putExtra("UserRideId",user_ride_id);
                                    intent.putExtra("AutoNo",AutoNo);
                                    startActivity(intent);
                                    finish();
                                }
                            });
//                            CustomDialogWithOneBtn(PairActivity.this,"Attention",Msg, "OK", new Runnable() {
//                                @Override
//                                public void run() {
//                                    Intent intent = new Intent(PairActivity.this, PairSuccessScreen.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    intent.putExtra("RideStatus","StartRide");
//                                    intent.putExtra("UserRideId",mResponse.getResult().get(0).getUser_ride_id());
//                                    intent.putExtra("AutoNo",AutoNo);
//                                    startActivity(intent);
//                                    finish();
//                                    //  StartRideAPI(AutoNo);
//                                }
//                            });
                            try {
                                handler.removeCallbacks(mRunnable);
                                handler.removeCallbacksAndMessages(null);
                            }catch (Exception e)
                            {

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

}
