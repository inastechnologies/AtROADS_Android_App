package com.inas.atroads.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.util.localData.Constants;
import com.inas.atroads.util.localData.SharedPrefsData;
import com.inas.atroads.views.model.PasswordRequestModel;
import com.inas.atroads.views.model.PasswordResponseModel;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class SettingsActivity extends BaseActivity {

    private static final String DEFAULT = "N/A";
    private static final String TAG = "SettingsActivity";
    private Context mContext;
    private Toolbar toolbar;
    private TextInputLayout oldpasswordTextInputLayout, newpasswordTextInputLayout, conpasswordTextInputLayout;
    private EditText oldpasswordEditText, newpasswordEditText, conpasswordEditText;
    private Button updateBtn;
    private Subscription mSubscription;
    private String Mobile,Email,Username;
    int UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        //assining layout
        setContentView(R.layout.activity_settings);

        isNetworkAvailable(SettingsActivity.this);
        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
        setViews();
        GetSharedPrefs();
    }

    private void initViews() {

       // userId = SharedPrefsData.getInt(mContext, "" + Constants.USERID, Constants.PREF_NAME);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.settings));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HomeMapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        oldpasswordTextInputLayout = findViewById(R.id.oldpasswordTextInputLayout);
        newpasswordTextInputLayout = findViewById(R.id.newpasswordTextInputLayout);
        conpasswordTextInputLayout = findViewById(R.id.conpasswordTextInputLayout);
        oldpasswordEditText = findViewById(R.id.oldpasswordEditText);
        newpasswordEditText = findViewById(R.id.newpasswordEditText);
        conpasswordEditText = findViewById(R.id.conpasswordEditText);
        updateBtn = findViewById(R.id.updateBtn);
    }

    private void setViews() {
        oldpasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    oldpasswordTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newpasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    newpasswordTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        conpasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    conpasswordTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validationPassword()) {
                    getPasswordChange();
                }

            }
        });


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



    private boolean validationPassword() {

        String newPassword = newpasswordEditText.getText().toString();
        if (TextUtils.isEmpty(oldpasswordEditText.getText().toString())) {
            oldpasswordTextInputLayout.setError(getString(R.string.err_please_enter_old_password));
            oldpasswordTextInputLayout.setErrorEnabled(true);
            oldpasswordTextInputLayout.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(newpasswordEditText.getText().toString())) {
            newpasswordTextInputLayout.setError(getString(R.string.err_please_enter_create_password));
            newpasswordTextInputLayout.requestFocus();
            newpasswordTextInputLayout.setErrorEnabled(true);
            return false;
        } else if (newPassword.isEmpty() || newPassword.length() < 6) {
            newpasswordTextInputLayout.setError(getString(R.string.err_please_enter_six_characters));
            newpasswordTextInputLayout.requestFocus();
            newpasswordTextInputLayout.setErrorEnabled(true);
            return false;
        } else if (TextUtils.isEmpty(conpasswordEditText.getText().toString())) {
            conpasswordTextInputLayout.setError(getString(R.string.err_please_enter_password));
            conpasswordTextInputLayout.requestFocus();
            conpasswordTextInputLayout.setErrorEnabled(true);

            return false;
        } else if (!(newpasswordEditText.getText().toString().toLowerCase()
                .equalsIgnoreCase(conpasswordEditText.getText().toString().toLowerCase()))) {
            conpasswordTextInputLayout.setError(getString(R.string.err_password_do_not_match));
            conpasswordTextInputLayout.setErrorEnabled(true);
            conpasswordTextInputLayout.requestFocusFromTouch();
            conpasswordTextInputLayout.requestFocus();

            return false;
        }
        return true;
    }

    private void getPasswordChange()
    {
        JsonObject object = passwordObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.PasswordResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PasswordResponseModel>() {
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
                    public void onNext(PasswordResponseModel mRespone) {
                        Toast.makeText(SettingsActivity.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SettingsActivity.this, HomeMapsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            },1500);
                        }
                        else {

                        }

                    }
                });
    }

    /**
     * Json object of passwordObject
     *
     * @return
     */
    private JsonObject passwordObject() {
        PasswordRequestModel requestModel = new PasswordRequestModel();
        requestModel.setPassword(oldpasswordEditText.getText().toString());
        requestModel.setNewPassword(newpasswordEditText.getText().toString());
        requestModel.setConfirmPassword(conpasswordEditText.getText().toString());
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(SettingsActivity.this, HomeScreen.class);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        startActivity(intent);
////        finish();
        super.onBackPressed();
    }
}
