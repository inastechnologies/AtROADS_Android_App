package com.inas.atroads.views.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.util.Utilities;
import com.inas.atroads.util.localData.FetchURL;
import com.inas.atroads.views.Activities.BillingDetailsActivity;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.Activities.PairSuccessScreen;
import com.inas.atroads.views.model.GetDetailsOfRideRequestModel;
import com.inas.atroads.views.model.GetDetailsOfRideResponseModel;
import com.inas.atroads.views.model.ManualCalculationRequestModel;
import com.inas.atroads.views.model.ManualCalculationResponseModel;
import com.inas.atroads.views.model.RouteSourceDestRequestModel;
import com.inas.atroads.views.model.RouteSourceDestResponseModel;
import com.inas.atroads.views.model.YourBillRequestModel;
import com.inas.atroads.views.model.YourBillResponseModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.views.Activities.BillingDetailsActivity.CustomDialog;

public class YourBillScreen extends AppCompatActivity {
    private static final String TAG = "YourBillScreen";
    private TextView pinTv,dropTV,amountTv;
    private EditText ReviewET;
    private RatingBar ratingBar;
    private int IdToGetYourBill,UserRideId;
    private Subscription mSubscription;
    private Double payableAmount;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_bill_screen);
        IdToGetYourBill = getIntent().getIntExtra("IdToGetYourBill",0);
        UserRideId = getIntent().getIntExtra("UserRideId",0);
        payableAmount = getIntent().getDoubleExtra("PayableAmount",0.0);
        Log.i(TAG, "onCreate: "+ IdToGetYourBill + "-->"+UserRideId+"-->"+payableAmount);
        SetViewsFromLayout();
    }

    private void SetViewsFromLayout()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Bill");
        setSupportActionBar(toolbar);
        pinTv = findViewById(R.id.pinTv);
        dropTV = findViewById(R.id.dropTV);
        amountTv = findViewById(R.id.amountTv);
        amountTv.setText(payableAmount+"");
        ReviewET = findViewById(R.id.ReviewET);
        ratingBar = findViewById(R.id.ratingBar);
//        CallDetailsOfRideAPI();
        RouteSourceDestDetailsAPI();
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallYourBillAPI();
            }
        });
    }

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
                            Toast.makeText(YourBillScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            String username = mResponse.getResult().get(0).getFullname();
                            String AutoNo = mResponse.getResult().get(0).getAutoNumber();
                            String source = mResponse.getResult().get(0).getSource();
                            String dest = mResponse.getResult().get(0).getDest();
                            pinTv.setText(source+"");
                            dropTV.setText(dest+"");
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
        requestModel.setUserRideId(UserRideId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /**************************END OF CallDetailsOfRideAPI*********************************/



    /********************************START OF CallYourBillAPI*******************************/
    /*
     * CallYourBillAPI
     * */
    private void CallYourBillAPI(){

        JsonObject object = YourBillObject();
        AtroadsService service = ServiceFactory.createRetrofitService(YourBillScreen.this, AtroadsService.class);
        mSubscription = service.YourBillResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<YourBillResponseModel>() {
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
                    public void onNext(YourBillResponseModel mResponse) {
                        Log.i(TAG, "YourBillResponseModel: "+mResponse);
//                        Toast.makeText(YourBillScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {

                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            CustomDialog(YourBillScreen.this, "Ride Completed!", "Thank You for using Atroads.", "Ok", new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(YourBillScreen.this, HomeMapsActivity.class);
//                            i.putExtra("payableAmount",payableAmount.getText().toString());
                                    startActivity(i);
                                }
                            });
                        }
                    }
                });

    }

    /**
     * Json object of ManualCalculationObject
     *
     * @return
     */
    private JsonObject YourBillObject()
    {
        YourBillRequestModel requestModel = new YourBillRequestModel();
        requestModel.setId(IdToGetYourBill);
        requestModel.setRateTheRide(String.valueOf(ratingBar.getRating()));
        requestModel.setReview(ReviewET.getText().toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF YourBillObject *******************************/



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
                    public void onNext(RouteSourceDestResponseModel mResponse) {
                        Log.i(TAG, "RouteSourceDestResponseModel: "+mResponse);
                        // Toast.makeText(PairSuccessScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 1)
                        {
                            String source = mResponse.getResult().get(0).getUserSourceAddress();
                            String dest = mResponse.getResult().get(0).getUserDestAddress();
                            pinTv.setText(source+"");
                            dropTV.setText(dest+"");

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



}
