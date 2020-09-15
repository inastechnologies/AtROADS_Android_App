package com.inas.atroads.views.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.model.ResendOTPRequestModel;
import com.inas.atroads.views.model.ResendOTPResponseModel;
import com.inas.atroads.views.model.VerifyOTPRequestModel;
import com.inas.atroads.views.model.VerifyOTPResponseModel;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.isNetworkAvailable;

/**
 *
 */
public class OtpActivity extends BaseActivity
{
    private static final String TAG = "OtpActivity";
    private Context mContext;
    private Button submitButton, resendotpButton;
    private Toolbar toolbar;
    private String otpStr, mobileStr, FromActivity;
    private Subscription mSubscription;
    Intent intent;
    String Username,Email,Mobile,ProfilePic;
    int UserId;
    private static final String DEFAULT = "N/A";
    private ProgressDialog mProgressDialog;
    String LMobileNo;
    private EditText num1EdtTxt,num2EdtTxt,num3EdtTxt,num4EdtTxt;
    private TextView timerTv;
    private String mobileNumber,RegisterStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        //assigning layout
        setContentView(R.layout.activity_otp);
        isNetworkAvailable(OtpActivity.this);
        /* intializing and assigning ID's */
        initViews();
        /* Navigation's and using the views */
        setViews();
        SetFromActivity();
    }

    /**
     * To initialize the views
     */
    private void initViews()
    {
        SetToolBar();
        num1EdtTxt =(EditText)findViewById(R.id.edttxt1);
        num2EdtTxt =(EditText)findViewById(R.id.edttxt2);
        num3EdtTxt =(EditText)findViewById(R.id.edttxt3);
        num4EdtTxt =(EditText)findViewById(R.id.edttxt4);
        timerTv = (TextView)findViewById(R.id.timerTv);
        submitButton = findViewById(R.id.submitButton);
        resendotpButton = findViewById(R.id.resendotpButton);
        EditTextListeners();
        SetCountDownTimer();
//        GetSharedPrefs();
    }

    /**
     * SetFromActivity
     */
    private void SetFromActivity()
    {
        FromActivity = getIntent().getStringExtra("FromActivity");
        Log.d("OTPActivity", "Activity: " + FromActivity);
        if(FromActivity.equals("MobileNumberRegisterScreen"))
        {
            mobileNumber = getIntent().getStringExtra("mobileNumber");
            Log.i(TAG, "mobileNumber: "+mobileNumber);
            RegisterStatus = getIntent().getStringExtra("RegisterStatus");
            if(RegisterStatus.equals("New"))
            {

            }
            else {
                CallResendOtp(mobileNumber);
            }
        }
    }


    /**
     * EditText listeners to move from one box to other
     */
    private void EditTextListeners()
    {
        EditTxt1Watcher();
        EditTxt2Watcher();
        EditTxt3Watcher();
        EditTxt4Watcher();
    }


    /**
     * EditTxt1Watcher
     */
    private void EditTxt1Watcher()
    {
        num1EdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(!num1EdtTxt.getText().toString().equals(""))
                {
                    num2EdtTxt.requestFocus();
                }

            }
        });

        num1EdtTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    num1EdtTxt.requestFocus();
                }
                return false;
            }
        });

    }

    /**
     * EditTxt2Watcher
     */
    private void EditTxt2Watcher()
    {
        num2EdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!num2EdtTxt.getText().toString().equals(""))
                {
                    num3EdtTxt.requestFocus();
                }
            }
        });

        num2EdtTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(num2EdtTxt.getText().toString().equals(""))
                    {
                        num1EdtTxt.requestFocus();
                    }

                }
                return false;
            }
        });
    }

    /**
     * EditTxt3Watcher
     */
    private void EditTxt3Watcher()
    {
        num3EdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!num3EdtTxt.getText().toString().equals(""))
                {
                    num4EdtTxt.requestFocus();
                }
            }
        });


        num3EdtTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(num3EdtTxt.getText().toString().equals(""))
                    {
                        num2EdtTxt.requestFocus();
                    }

                }
                return false;
            }
        });
    }

    /**
     * EditTxt4Watcher
     */
    private void EditTxt4Watcher()
    {
        num4EdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!num4EdtTxt.getText().toString().equals(""))
                {
                    num4EdtTxt.requestFocus();
                }
                else {
                }

            }
        });
        num4EdtTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(num4EdtTxt.getText().toString().equals(""))
                    {
                        num3EdtTxt.requestFocus();
                    }
                }
                return false;
            }
        });
    }


    /**
     *SetCountDownTimer
     */
    private void SetCountDownTimer()
    {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), " %02d min: %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                timerTv.setText(text);
                // timerTv.setText("00:00:" + millisUntilFinished / 1000);
                resendotpButton = (Button)findViewById(R.id.resendotpButton);
                resendotpButton.setEnabled(false);
                resendotpButton.setAlpha(0.5f);

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                timerTv.setText("");
                resendotpButton = (Button)findViewById(R.id.resendotpButton);
                resendotpButton.setEnabled(true);
                resendotpButton.setAlpha(1.0f);
            }

        }.start();
    }

    /**
     * SetToolBar
     */
    private void SetToolBar()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.otp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToMobileRegScreen();
            }
        });
    }

    /**
     * Set the views
     */
    private void setViews()
    {
        SetSubmitBtn();
        SetResendOTP();
    }


    /**
     * SetSubmitBtn
     */
    private void SetSubmitBtn()
    {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpEntered = num1EdtTxt.getText().toString()+num2EdtTxt.getText().toString()+
                        num3EdtTxt.getText().toString()+num4EdtTxt.getText().toString();
                    VerifyOTPAPI(mobileNumber,otpEntered);
                }
        });
    }


    /**
     * SetResendOTP
     */
    private void SetResendOTP()
    {
        resendotpButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        resendotpButton.setAlpha(0.5f);
                        SetCountDownTimer();
                        CallResendOtp(mobileNumber);
                        //clear edittext fields
                        num1EdtTxt.setText("");
                        num1EdtTxt.requestFocus();
                        num2EdtTxt.setText("");
                        num3EdtTxt.setText("");
                        num4EdtTxt.setText("");
                        break;

                    case MotionEvent.ACTION_UP:
                        resendotpButton.setAlpha(1.0f);
                        break;
                }

                return true;
            }
        });
    }



    /**
     * MoveToMobileRegScreen
     */
    private void MoveToMobileRegScreen()
    {
        Intent intent = new Intent(OtpActivity.this, MobileNumberRegisterScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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


    /**
     * CallResendOtp
     */
    private void CallResendOtp(String Mobile)
    {
        JsonObject object = resendotpObject(Mobile);
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.ResendOTPResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResendOTPResponseModel>() {
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
                    public void onNext(ResendOTPResponseModel mRespone)
                    {
                        hideProgressDialog();
                        Toast.makeText(mContext, "" + mRespone.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    /**
     * Json object of resendotpObject
     *
     * @return
     */
    private JsonObject resendotpObject(String mobile) {
        ResendOTPRequestModel requestModel = new ResendOTPRequestModel();
        requestModel.setMobileNumber(mobile);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    /**
     * call VerifyOTPAPI
     */
    private void VerifyOTPAPI(String mobile, String otpEntered)
    {
        JsonObject object = verifyotpObject(mobile,otpEntered);
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.VerifyOTPResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VerifyOTPResponseModel>() {
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
                    public void onNext(VerifyOTPResponseModel mRespone)
                    {
                        hideProgressDialog();
                        Toast.makeText(mContext, "" + mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            String RegisterStatus = getIntent().getStringExtra("RegisterStatus");
                            if(RegisterStatus.equals("New"))
                            {
                                MoveToUserDetailsScreen();
                            }
                            else {
                                MoveToHomeScreen();
                            }
                        }
                        else
                        {

                        }
                    }
                });
    }

    /**
     * Json object of verifyotpObject
     *
     * @return
     */
    private JsonObject verifyotpObject(String mobile, String otpEntered) {
        VerifyOTPRequestModel requestModel = new VerifyOTPRequestModel();
        requestModel.setMobileNumber(mobile);
        requestModel.setOtpEntered(otpEntered);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /**
     * MoveToHomeScreen
     */
    private void MoveToHomeScreen()
    {
        Intent intent = new Intent(OtpActivity.this, HomeMapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     *  MoveToUserDetailsScreen
     */
    private void MoveToUserDetailsScreen()
    {
        Intent i = new Intent(OtpActivity.this, GetUserDetailsScreen.class);
        i.putExtra("mobileNumber",mobileNumber);
        startActivity(i);
        finish();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


