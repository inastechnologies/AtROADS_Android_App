package com.inas.atroads.views.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.views.UI.EnterUPIDetailsActivity;
import com.inas.atroads.views.UI.PaymentScreen;
import com.inas.atroads.views.UI.ShowQRActivity;
import com.inas.atroads.views.UI.UploadQRActivity;
import com.inas.atroads.views.UI.YourBillScreen;
import com.inas.atroads.views.model.DeletePairRequestModel;
import com.inas.atroads.views.model.DeletePairResponseModel;
import com.inas.atroads.views.model.EndRideRequestModel;
import com.inas.atroads.views.model.EndRideResponseModel;
import com.inas.atroads.views.model.ManualCalculationRequestModel;
import com.inas.atroads.views.model.ManualCalculationResponseModel;
import com.inas.atroads.views.model.MeterCalculationResponeModel;
import com.inas.atroads.views.model.MeterCalculationsRequestModel;
import com.inas.atroads.views.model.PaymentGatewayRequestModel;
import com.inas.atroads.views.model.PaymentGatewayResponseModel;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.GenerateOrderID;
import static com.inas.atroads.util.Utilities.hideKeyboard;

public class BillingDetailsActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    private TextView bookingDetailsTv,AutoNo,totalfare,payableAmount,distance,mopTv,
            AutoNumberTv,metertotalFareTv,payableAmountTv,distanceTv;
    private LinearLayout meterLLayout,fareLLayout,distanceLLayout;
    private EditText metertotalfare;
    private Button okBtn,payNowbtn;
    private String MOPType = "Cash",AutoNumber,FareType;
    private Subscription mSubscription;
    private String TAG = "BillingDetailsActivity";
    private int UserId,UserRideId;
    private RadioGroup rg;
    String orderId="", mid="SMARTB27891758515968";
    private String DEFAULT = "N/A";
    Dialog QRDialog;
    int IdToGetYourBill;
    private Double AmountToBePaid;
    double distance_travell,payAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.BillingDetails));
        toolbar.setTitleMargin(0,0,0,0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        UserId = getIntent().getIntExtra("UserId",0);
        UserRideId = getIntent().getIntExtra("UserRideId",0);
        AutoNumber = getIntent().getStringExtra("AutoNumber");
        FareType = getIntent().getStringExtra("FareType");
        SetViews();
    }


    private void SetViews()
    {
        AutoNumberTv = findViewById(R.id.AutoNumberTv);
        metertotalFareTv = findViewById(R.id.metertotalFareTv);
        payableAmountTv = findViewById(R.id.payableAmountTv);
        payNowbtn = findViewById(R.id.payNowbtn);
        okBtn = findViewById(R.id.okBtn);
        bookingDetailsTv = findViewById(R.id.bookingDetailsTv);
        bookingDetailsTv.setText("Billing Type - "+FareType);
        AutoNo = findViewById(R.id.AutoNo);
        AutoNo.setText(": "+AutoNumber);
        totalfare = findViewById(R.id.totalfare);
        payableAmount = findViewById(R.id.payableAmount);
        distance = findViewById(R.id.distance);
        distanceLLayout = findViewById(R.id.distanceLLayout);
        distanceTv = findViewById(R.id.distanceTv);
        mopTv = findViewById(R.id.mopTv);
        rg = findViewById(R.id.radioGroup);
        metertotalfare = findViewById(R.id.metertotalfare);
        meterLLayout = findViewById(R.id.meterLLayout);
        fareLLayout = findViewById(R.id.fareLLayout);
        if(FareType.equals("Meter"))
        {
            metertotalfare.setVisibility(View.VISIBLE);
            metertotalFareTv.setVisibility(View.VISIBLE);
            meterLLayout.setVisibility(View.VISIBLE);
            fareLLayout.setVisibility(View.INVISIBLE);
        }
        else{
            metertotalfare.setVisibility(View.INVISIBLE);
            metertotalFareTv.setVisibility(View.INVISIBLE);
            meterLLayout.setVisibility(View.INVISIBLE);
            fareLLayout.setVisibility(View.VISIBLE);
            CallManualCalculationAPI();
        }
        HideViews();
        SetRadioButton();
        MeterTotalFareTextWatcher();
        SetOKBtn();
    }

    private void HideViews()
    {
        payNowbtn.setVisibility(View.INVISIBLE);
        payableAmount.setVisibility(View.INVISIBLE);
        payableAmountTv.setVisibility(View.INVISIBLE);
        distance.setVisibility(View.INVISIBLE);
        mopTv.setVisibility(View.INVISIBLE);
        rg.setVisibility(View.INVISIBLE);
        distanceLLayout.setVisibility(View.GONE);
    }

    private void ShowViews()
    {
        payNowbtn.setVisibility(View.VISIBLE);
        payableAmount.setVisibility(View.VISIBLE);
        payableAmountTv.setVisibility(View.VISIBLE);
        //distance.setVisibility(View.VISIBLE);
        mopTv.setVisibility(View.VISIBLE);
        rg.setVisibility(View.VISIBLE);
        metertotalFareTv.setVisibility(View.VISIBLE);
    }


    private void MeterTotalFareTextWatcher()
    {
        metertotalfare.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(""))
                {
                    okBtn.setEnabled(false);
                    okBtn.setAlpha(0.5f);
                }
                else {
                    okBtn.setEnabled(true);
                    okBtn.setAlpha(1.0f);
                }
            }
        });
    }


    private void SetOKBtn()
    {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(metertotalfare.getText().toString().equals("")){
                    Toast.makeText(BillingDetailsActivity.this, "Enter valid amount",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    CallMeterCalculationAPI();
                    hideKeyboard(v, BillingDetailsActivity.this);
                    metertotalfare.setEnabled(false);
                }
            }
        });
    }


    private void SetRadioButton()
    {
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedRadioButtonID = rg.getCheckedRadioButtonId();
                // If nothing is selected from Radio Group, then it return -1
                if (selectedRadioButtonID != -1) {

                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                    String selectedRadioButtonText = selectedRadioButton.getText().toString();
                    MOPType = selectedRadioButtonText;
                    if(MOPType.equals("Cash"))
                    {
//                        AmountEdtTxt.setVisibility(View.GONE);
//                        AmountEdtTxt.setText("0");
                    }
                    else if(MOPType.equals("UPI Payment")) {
                    }
                    else {
//                        SetPayNowBtn();
                    }
                }
                else{
                    MOPType = "Cash";
//                    tv_result.setText("Nothing selected from Radio Group.");
                }
            }
        });
        SetPayNowBtn(0.0,0);
    }


    private void SetPayNowBtn(Double amountToBePaid, int idToGetYourBill)
    {
        payNowbtn = findViewById(R.id.payNowbtn);
        payNowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callPayNowApi(amountToBePaid,idToGetYourBill);
            }
        });
    }


    private void SetQRDialog()
    {
        QRDialog = new Dialog(BillingDetailsActivity.this);
        QRDialog.setContentView(R.layout.qr_dialog);
        QRDialog.setCanceledOnTouchOutside(false);
        QRDialog.show();
        ImageView scanQrIV = QRDialog.findViewById(R.id.scanQrIV);
        ImageView showQrIV = QRDialog.findViewById(R.id.showQrIV);
        scanQrIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    IntentIntegrator integrator = new IntentIntegrator(BillingDetailsActivity.this);
                    integrator.setOrientationLocked(false);
                    integrator.setPrompt("Scan");
                    integrator.setCaptureActivity(CaptureActivityPortrait.class);
                    integrator.initiateScan();

                }
                catch(Exception e)
                {
                    e.printStackTrace();

                }
            }
        });

        showQrIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(BillingDetailsActivity.this, EnterUPIDetailsActivity.class);
//                i.putExtra("UserId",UserId);
//                i.putExtra("UserRideId",UserRideId);
//                i.putExtra("AutoNumber",AutoNumber);
//                i.putExtra("FareType",FareType);
//                startActivity(i);
//                finish();
                Intent i = new Intent(BillingDetailsActivity.this, UploadQRActivity.class);
                i.putExtra("UserId",UserId);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (result != null) {
            if (resultCode == RESULT_CANCELED) {

            } else {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String UPCScanned = scanResult.getContents();
                String ScannedQRDetails = "";
                Toast.makeText(this, "" + UPCScanned, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onActivityResult: " + UPCScanned);
                String UPI = "upi://pay?pa=7026562080@upi&pn=SUSHMA%20GARELA&cu=INR&mode=02&purpose=00&orgid=189999&sign=MEUCIQD/VLGj1RKjIcISGrDOUdBD3Q/58qhfcrn5SKkVGooa+wIgVYgcYO/4KY4ve8OjEnK3z4CRTDmj16ResO4DmjGed2c=";
                if(UPCScanned.contains("upi://pay?pa"))
                {
                    String appendStr = "&cu=INR&mode=02&purpose=00&orgid=189999&sign=MEUCIQD/VLGj1RKjIcISGrDOUdBD3Q/58qhfcrn5SKkVGooa+wIgVYgcYO/4KY4ve8OjEnK3z4CRTDmj16ResO4DmjGed2c=";
                    if(UPCScanned.contains("aid"))
                    {
                        ScannedQRDetails = UPCScanned+appendStr;
                    }
                    else {
                        ScannedQRDetails = UPCScanned;
                    }
                    Intent i = new Intent(BillingDetailsActivity.this, PaymentScreen.class);
                    i.putExtra("PayableAmount",AmountToBePaid);
                    i.putExtra("UPICODE", ScannedQRDetails);
                    i.putExtra("IdToGetYourBill",IdToGetYourBill);
                    i.putExtra("UserRideId",  UserRideId);
                    startActivity(i);
                }
                else {
                    if(QRDialog.isShowing())
                    {
                        QRDialog.dismiss();
                    }

                    CustomDialogWithOneBtn(BillingDetailsActivity.this,"Attention!!", "Please scan the proper QR code","OK", new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }

            }
        }
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




    /********************************START OF CallDeletePairAPI*******************************/
    /*
     * CallDeletePairAPI
     * */
    public void CallDeletePairAPI(){

        JsonObject object = DeletePairObject();
        AtroadsService service = ServiceFactory.createRetrofitService(BillingDetailsActivity.this, AtroadsService.class);
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
        requestModel.setUserRideId(UserRideId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF CallDeletePairAPI *******************************/


    /********************************START OF CallDeletePairAPI*******************************/
    /*
     * CallDelayDeletePairAPI
     * */
    public void CallDelayDeletePairAPI(){

        JsonObject object = DelayDeletePairObject();
        AtroadsService service = ServiceFactory.createRetrofitService(BillingDetailsActivity.this, AtroadsService.class);
        mSubscription = service.delayResponse(object)
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
                        Log.i(TAG, "CallDelayDeletePairAPI: "+mResponse);
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
    private JsonObject DelayDeletePairObject()
    {
        DeletePairRequestModel requestModel = new DeletePairRequestModel();
        requestModel.setUserRideId(UserRideId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF CallDeletePairAPI *******************************/

    /********************************START OF CallManualCalculationAPI*******************************/
    /*
     * CallManualCalculationAPI
     * */
    private void CallManualCalculationAPI(){

        JsonObject object = ManualCalculationObject();
        AtroadsService service = ServiceFactory.createRetrofitService(BillingDetailsActivity.this, AtroadsService.class);
        mSubscription = service.ManualCalculationResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ManualCalculationResponseModel>() {
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
                    public void onNext(ManualCalculationResponseModel mResponse) {
                        Log.i(TAG, "ManualCalculationResponseModel: "+mResponse);
                        Toast.makeText(BillingDetailsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {

                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            distanceLLayout.setVisibility(View.VISIBLE);
                            payableAmount.setVisibility(View.VISIBLE);
                            distance.setVisibility(View.VISIBLE);
                            distanceTv.setVisibility(View.VISIBLE);
                            payNowbtn.setVisibility(View.VISIBLE);
                            payableAmount.setVisibility(View.VISIBLE);
                            payableAmountTv.setVisibility(View.VISIBLE);
                            mopTv.setVisibility(View.VISIBLE);
                            rg.setVisibility(View.VISIBLE);
                            totalfare.setText(": "+mResponse.getResult().get(0).getTotalFare());
                            payableAmount.setText(": "+mResponse.getResult().get(0).getPayableAmount());
                            AmountToBePaid = mResponse.getResult().get(0).getPayableAmount();
                            distance.setText(": "+mResponse.getResult().get(0).getDistance());
                            payAmount=mResponse.getResult().get(0).getPayableAmount();
                            distance_travell= mResponse.getResult().get(0).getDistance();
                            IdToGetYourBill = mResponse.getResult().get(0).getIdToGetYourBill();
                            UserRideId = mResponse.getResult().get(0).getUserRideId();
                            SetPayNowBtn(AmountToBePaid,IdToGetYourBill);
                        }
                    }
                });

    }

    /**
     * Json object of ManualCalculationObject
     *
     * @return
     */
    private JsonObject ManualCalculationObject()
    {
        ManualCalculationRequestModel requestModel = new ManualCalculationRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setUserRideId(UserRideId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF Manual calculation *******************************/


    /********************************START OF CallMeterCalculationAPI*******************************/
    /*
     * CallMeterCalculationAPI
     * */
    private void CallMeterCalculationAPI(){

        JsonObject object = MeterCalculationObject();
        AtroadsService service = ServiceFactory.createRetrofitService(BillingDetailsActivity.this, AtroadsService.class);
        mSubscription = service.MeterCalculationResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MeterCalculationResponeModel>() {
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
                    public void onNext(MeterCalculationResponeModel mResponse) {
                        Log.i(TAG, "MeterCalculationResponeModel: "+mResponse);
                        Toast.makeText(BillingDetailsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            //Toast.makeText(PairSuccessScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            ShowViews();
//                            payableAmount.setVisibility(View.VISIBLE);
//                            payableAmountTv.setVisibility(View.VISIBLE);
//                            metertotalFareTv.setVisibility(View.VISIBLE);
//                            mopTv.setVisibility(View.VISIBLE);
                            payableAmount.setText(""+mResponse.getResult().get(0).getPayableAmount());
                            IdToGetYourBill = mResponse.getResult().get(0).getIdToGetYourBill();
                            AmountToBePaid = mResponse.getResult().get(0).getPayableAmount();
                            UserRideId = mResponse.getResult().get(0).getUserRideId();
//                            mopTv.setVisibility(View.VISIBLE);
                            okBtn.setVisibility(View.INVISIBLE);
                            SetPayNowBtn(AmountToBePaid, IdToGetYourBill);
                        }
                    }
                });

    }

    /**
     * Json object of MeterCalculationObject
     *
     * @return
     */
    private JsonObject MeterCalculationObject()
    {
        MeterCalculationsRequestModel requestModel = new MeterCalculationsRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setUserRideId(UserRideId);
        requestModel.setTotalFare(Double.valueOf(metertotalfare.getText().toString()));
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF Meter calculation *******************************/




    private void PayWithPaytm()
    {
        GenerateOrderID();
        mid = "SMARTB27891758515968"; /// your marchant id
        GenerateChecksumHashAPI();
    }

    /******************************START OF GenerateChecksumHashAPI*******************************/
    /*
     * GenerateChecksumHashAPI
     * */
    public void GenerateChecksumHashAPI()
    {
        JsonObject object = GenerateCheckSumHashObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.PaymentGatewayResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PaymentGatewayResponseModel>() {
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
                    public void onNext(PaymentGatewayResponseModel mRespone) {
                        Log.i(TAG, "PaymentGatewayResponseModel: "+mRespone);
                        // Toast.makeText(mContext, mRespone.getStatus(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            String CALLBACK_URL = mRespone.getResult().getCALLBACKURL();
                            String CHANNEL_ID = mRespone.getResult().getCHANNELID();
                            String CHECKSUMHASH = mRespone.getResult().getCHECKSUMHASH();
                            String CUST_ID = mRespone.getResult().getCUSTID();
                            String INDUSTRY_TYPE_ID = mRespone.getResult().getINDUSTRYTYPEID();
                            String MID = mRespone.getResult().getMID();
                            String ORDER_ID = mRespone.getResult().getORDERID();
                            String TXN_AMOUNT = mRespone.getResult().getTXNAMOUNT();
                            String WEBSITE = mRespone.getResult().getWEBSITE();
                            PaytmServiceCall(MID, ORDER_ID, CALLBACK_URL, CHANNEL_ID, CHECKSUMHASH, CUST_ID, INDUSTRY_TYPE_ID, TXN_AMOUNT, WEBSITE);
                        }
                        else {
                            Toast.makeText(BillingDetailsActivity.this, getString(R.string.SomethingWentWrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /**
     * Json object of GenerateCheckSumHashObject
     * @return
     */
    private JsonObject GenerateCheckSumHashObject()
    {
        orderId = GenerateOrderID();
        PaymentGatewayRequestModel requestModel = new PaymentGatewayRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setOrderId(orderId);
        requestModel.setAmount(10.00);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /******************************END OF GenerateChecksumHashAPI*******************************/


    /******************************START OF PaytmServiceCall*******************************/
    /**
     *
     * @param MID
     * @param ORDER_ID
     * @param CALLBACK_URL
     * @param CHANNEL_ID
     * @param CHECKSUMHASH
     * @param CUST_ID
     * @param INDUSTRY_TYPE_ID
     * @param TXN_AMOUNT
     * @param WEBSITE
     */
    private void PaytmServiceCall(String MID, String ORDER_ID, String CALLBACK_URL, String CHANNEL_ID, String CHECKSUMHASH, String CUST_ID, String INDUSTRY_TYPE_ID, String TXN_AMOUNT, String WEBSITE) {
        PaytmPGService Service = PaytmPGService.getStagingService();
        // when app is ready to publish use production service
//         PaytmPGService  Service = PaytmPGService.getProductionService();

        // now call paytm service here
        //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
        HashMap<String, String> paramMap = new HashMap<String, String>();
        //these are mandatory parameters
        paramMap.put("MID", MID); //MID provided by paytm
        paramMap.put("ORDER_ID", ORDER_ID);
        paramMap.put("CUST_ID", CUST_ID);
        paramMap.put("CHANNEL_ID", CHANNEL_ID);
        paramMap.put("TXN_AMOUNT",TXN_AMOUNT);
        paramMap.put("WEBSITE", WEBSITE);
        paramMap.put("CALLBACK_URL" ,CALLBACK_URL);
        //paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
        // paramMap.put( "MOBILE_NO" , "9144040888");  // no need
        paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
        //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
        paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);

        PaytmOrder Order = new PaytmOrder(paramMap);
        com.paytm.pgsdk.Log.e("checksum ", "param "+ paramMap.toString());
        Service.initialize(Order,null);
        // start payment service call here
        Service.startPaymentTransaction(BillingDetailsActivity.this, true, true,
                BillingDetailsActivity.this  );

    }

    /******************************END OF PaytmServiceCall*******************************/

    /******************************START OF PAYTM onTransactionResponse*******************************/
    @Override
    public void onTransactionResponse(Bundle bundle) {
        com.paytm.pgsdk.Log.e("checksum ", " respon true " + bundle.toString());
        Toast.makeText(BillingDetailsActivity.this, bundle.toString(), Toast.LENGTH_SHORT).show();
        //CallPaymentStatusToServer("success");
        CustomDialog(BillingDetailsActivity.this, "Success!", "Success","Ok", new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(BillingDetailsActivity.this, YourRidesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }
    /******************************END OF PAYTM onTransactionResponse*******************************/

    /******************************START OF CUSTOM DIALOG*******************************/
    public static void CustomDialog(Context context, String Title, String Msg, String buttonNam1, Runnable runnable)
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

    @Override
    public void networkNotAvailable() {

    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {

    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {

    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

    }

    @Override
    public void onBackPressedCancelTransaction() {

    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(BillingDetailsActivity.this, HomeMapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /*
     * callPayNow
     * */
    private void callPayNowApi(Double amountToBePaid, int idToGetYourBill){

        JsonObject object = EndRideObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.endPayNow(object)
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

                        Toast.makeText(BillingDetailsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            //Toast.makeText(PairSuccessScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {

                            if(MOPType.equals("Cash"))
                            {
                                CustomDialog(BillingDetailsActivity.this, "Ride Completed!", "Thank You for using Atroads.", "Ok", new Runnable() {
                                    @Override
                                    public void run() {
                                        CustomDialog(BillingDetailsActivity.this, "Dear User", "You have travelled the distance of "+""+distance_travell+  "Kms \nThe total billing of yours is Rs"+payAmount+"\n\n\nYour total bill is discounted from ATROADS.\n\n\n" +
                                                        "\n" +
                                                        "Thanks for using ATROADS."
                                                , "Ok", new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent i = new Intent(BillingDetailsActivity.this, HomeMapsActivity.class);
                                                        i.putExtra("IdToGetYourBill",idToGetYourBill);
                                                        i.putExtra("payableAmount",amountToBePaid);
                                                        i.putExtra("UserRideId",UserRideId);
                                                        startActivity(i);
                                                        finish();

                                                    }
                                                });
                                    }
                                });
                            }
                            else if(MOPType.equals("UPI Payment")){
                                SetQRDialog();
                            }
                            else {
                                PayWithPaytm();
                            }
                        }
                    }
                });
    }
    private JsonObject EndRideObject()
    {
        EndRideRequestModel requestModel = new EndRideRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setUserRideId(UserRideId);
        // requestModel.setEnd_lat_long(newcurrentlatLngStr);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }
}
