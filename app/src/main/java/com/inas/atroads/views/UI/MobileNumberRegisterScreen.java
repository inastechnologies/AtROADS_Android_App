package com.inas.atroads.views.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.*;
import com.inas.atroads.R;
import com.inas.atroads.services.*;
import com.inas.atroads.views.model.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import static com.inas.atroads.util.Utilities.isNetworkAvailable;


/**
 * This Screen is used to Register the user with the help of Mobile Number
 */
public class MobileNumberRegisterScreen extends AppCompatActivity
{
    /******* Variable declarations*********/
    private EditText phoneNumberEditText;
    private Button GoBtn;
    private ImageView googleplusImageView,facebookImageView;
    private String TAG = "MobileNumberRegisterScreen";
    private Subscription mSubscription;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number_register_screen);
        SetViewsFromLayout();
        isNetworkAvailable(MobileNumberRegisterScreen.this);
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * SetViewsFromLayout
     */
    private void SetViewsFromLayout()
    {
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        GoBtn = findViewById(R.id.GoBtn);
        GoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallMobileRegisterApi();
            }
        });
        googleplusImageView = findViewById(R.id.googleplusImageView);
        facebookImageView = findViewById(R.id.facebookImageView);
        PhoneNoWatcher();
    }

    /**
     * Text watcher for phone number
     */
    private void PhoneNoWatcher()
    {
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidPhone())
                {
                    GoBtn.setBackgroundResource(R.drawable.round_rect_white_button_bg);
                    GoBtn.setTextColor(Color.parseColor("#FEAE33"));
                }
                else {
                    GoBtn.setBackgroundResource(R.drawable.round_rect_button_bg);
                    GoBtn.setTextColor(Color.parseColor("#ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * Valid mobile number or not
     *
     * @return
     */
    private boolean isValidPhone()
    {
        String target = phoneNumberEditText.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }



    /**
     * Call GetPhoneNumber API
     */
    private void CallMobileRegisterApi()
    {
        JsonObject object = getPhoneNumberObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.GetPhoneNumber(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetPhoneNumberResponseModel>() {
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
                    public void onNext(GetPhoneNumberResponseModel mRespone) {
                        Log.i(TAG, "onNext: GetPhoneNumberResponseModel"+mRespone.getMessage());
                        Toast.makeText(MobileNumberRegisterScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() ==1)
                        {
                            String token = mRespone.getResult().get(0).getDeviceToken();
                            int user_id = mRespone.getResult().get(0).getUserId();
                            String mobileNumber = mRespone.getResult().get(0).getMobileNumber();
                            String OTP = mRespone.getResult().get(0).getOTP();
                            Log.i(TAG, "onNext: "+user_id);
                            SaveSharedPrefs(mobileNumber,token,user_id);
                            MoveToOTPScreen("New",mobileNumber);
//                            FirebaseRegister(user_id,mobileNumber,token);
//                            FirebaseCreateUserWithMail(mRespone.getResult().get(0).getEmailId(),mRespone.getResult().get(0).getPassword());
                        }
                        else {
                            String token = mRespone.getResult().get(0).getDeviceToken();
                            int user_id = mRespone.getResult().get(0).getUserId();
                            String mobileNumber = mRespone.getResult().get(0).getMobileNumber();
                            Log.i(TAG, "onNext: "+user_id);
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("mobile_number", mobileNumber); // Storing string
                                editor.putInt("user_id", user_id);
                                editor.commit();
                            MoveToOTPScreen("Old",phoneNumberEditText.getText().toString());

                        }

                    }
                });
    }

    /**
     *
     * @param RegisterStatus
     * @param mobileNumber
     */
    private void MoveToOTPScreen(String RegisterStatus,String mobileNumber)
    {
        Intent i=new Intent(MobileNumberRegisterScreen.this, OtpActivity.class);
        i.putExtra("FromActivity","MobileNumberRegisterScreen");
        i.putExtra("RegisterStatus",RegisterStatus);
        i.putExtra("mobileNumber",mobileNumber);
        startActivity(i);
        finish();
    }

    /**
     *
     * @param mobileNumber
     * @param token
     * @param user_id
     */
    private void SaveSharedPrefs(String mobileNumber,String token,int user_id)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MobileRegPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("mobile_number", mobileNumber); // Storing string
        editor.putString("token", token);
        editor.putInt("user_id", user_id);
        editor.commit();
    }


    /**
     * Json object of getPhoneNumberObject
     *
     * @return
     */
    private JsonObject getPhoneNumberObject()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Token", 0); // 0 - for private mode
        String token = pref.getString("Dev_Token","N/A");
        GetPhoneNumberRequestModel requestModel = new GetPhoneNumberRequestModel();
        requestModel.setMobileNumber(phoneNumberEditText.getText().toString());
        requestModel.setDevice_token(token);
        Log.i(TAG, "getPhoneNumberObject: Token:"+token + "\n"+phoneNumberEditText.getText().toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

}

