package com.inas.atroads.views.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.model.GetPhoneNumberRequestModel;
import com.inas.atroads.views.model.GetPhoneNumberResponseModel;
import com.inas.atroads.views.model.GetUserDetailsRequestModel;
import com.inas.atroads.views.model.GetUserDetailsResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static com.inas.atroads.util.AtroadsConstant.AtroadsUsers_URL;
import static com.inas.atroads.util.AtroadsConstant.AtroadsUsers_URL_JSON;

/**
 * This screen is used to Get the details of the user
 */
public class GetUserDetailsScreen extends AppCompatActivity
{
    private static final String DEFAULT = "N/A";
    private static final String TAG = "GetUserDetailsScreen";
    private EditText UserNameET,EmailET;
    private RadioGroup radioUser;
    private Button SubmitBtn;
    private RadioButton radioMale,radioFemale,radioother;
    private String Username,Email,Mobile,ProfilePic;
    private int UserId;
    private Subscription mSubscription;
    private String selectedGender = "male";
    private FirebaseAuth mAuth;
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_details_screen);
        SetViewsFromLayout();

        mobileNumber = getIntent().getStringExtra("mobileNumber");
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * SetViewsFromLayout
     */
    private void SetViewsFromLayout()
    {
        UserNameET = findViewById(R.id.UserNameET);
        EmailET = findViewById(R.id.EmailET);
        radioUser = findViewById(R.id.radioUser);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        radioother = findViewById(R.id.radioother);
        SubmitBtn = findViewById(R.id.SubmitBtn);
        SetSubmitBtn();
        GetSharedPrefs();
        setUserNameWatcher();
        setEmailWatcher();
    }


    /**
     * SetSubmitBtn
     */
    private void SetSubmitBtn()
    {
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioMale.isChecked())
                {
                    Log.i(TAG, "onClick: "+radioMale.getText().toString());
                    selectedGender = "male";
                }
                else if(radioFemale.isChecked())
                {
                    Log.i(TAG, "onClick: "+radioFemale.getText().toString());
                    selectedGender = "female";
                }
                else if(radioother.isChecked())
                {
                    Log.i(TAG, "onClick: "+radioother.getText().toString());
                    selectedGender = "other";
                }
                Validation();

            }
        });
    }



    private void setUserNameWatcher()
    {
        UserNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(EmailET.getText().toString()))
                {
                    SubmitBtn.setBackgroundResource(R.drawable.round_rect_white_button_bg);
                    SubmitBtn.setTextColor(Color.parseColor("#FEAE33"));
                }
                else if (!isValidEmail(EmailET.getText().toString())){
                    SubmitBtn.setBackgroundResource(R.drawable.round_rect_white_button_bg);
                    SubmitBtn.setTextColor(Color.parseColor("#FEAE33"));
                }
                else {
                    SubmitBtn.setBackgroundResource(R.drawable.round_rect_button_bg);
                    SubmitBtn.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
    }

    private void setEmailWatcher()
    {
        EmailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(UserNameET.getText().toString()))
                {
                    SubmitBtn.setBackgroundResource(R.drawable.round_rect_white_button_bg);
                    SubmitBtn.setTextColor(Color.parseColor("#FEAE33"));
                }
                else if (!isValidEmail(EmailET.getText().toString())){
                    SubmitBtn.setBackgroundResource(R.drawable.round_rect_white_button_bg);
                    SubmitBtn.setTextColor(Color.parseColor("#FEAE33"));
                }
                else {
                    SubmitBtn.setBackgroundResource(R.drawable.round_rect_button_bg);
                    SubmitBtn.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
    }



    /**
     * Validation
     */
    private void Validation()
    {
        if(TextUtils.isEmpty(UserNameET.getText().toString()))
        {
            Toast.makeText(GetUserDetailsScreen.this, "Please Enter the Name.", Toast.LENGTH_SHORT).show();
        }
        else
            if(TextUtils.isEmpty(EmailET.getText().toString()))
        {
            Toast.makeText(GetUserDetailsScreen.this, "Please Enter the Email.", Toast.LENGTH_SHORT).show();
        }
        else if (!isValidEmail(EmailET.getText().toString())){
            Toast.makeText(GetUserDetailsScreen.this, "Please Enter a valid Email ID.", Toast.LENGTH_SHORT).show();
        }
        else {
            CallGetUserDetailsApi();
        }
    }


    /**
     * valid email or not
     *
     * @param email
     * @return
     */
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /*
      GetSharedPrefs
      */
    private void GetSharedPrefs()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MobileRegPref", 0); // 0 - for private mode
        UserId = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
    }


    /**
     * Call CallGetUserDetails API
     */
    private void CallGetUserDetailsApi()
    {
        JsonObject object = getUserDetailsObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.GetUserDetails(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUserDetailsResponseModel>() {
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
                    public void onNext(GetUserDetailsResponseModel mRespone) {
                        Log.i(TAG, "onNext: GetUserDetailsResponseModel"+mRespone.getMessage());
                        Toast.makeText(GetUserDetailsScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            int userId = mRespone.getResult().get(0).getUserId();
                            String name =  mRespone.getResult().get(0).getName();
                            String email =  mRespone.getResult().get(0).getEmailId();

                            SaveSharedPrefs(mobileNumber,email,userId);
                            FirebaseRegister(userId,mobileNumber,email,name);
                            //mobile as password
                            FirebaseCreateUserWithMail(email,mobileNumber);

                            Intent i = new Intent(GetUserDetailsScreen.this, HomeMapsActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {


                        }

                    }
                });
    }

    /**
     * Json object of getUserDetailsObject
     *
     * @return
     */
    private JsonObject getUserDetailsObject()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Token", 0); // 0 - for private mode
        String token = pref.getString("Dev_Token","N/A");
        GetUserDetailsRequestModel requestModel = new GetUserDetailsRequestModel();
        requestModel.setName(UserNameET.getText().toString());
        requestModel.setUserId(UserId);
        requestModel.setEmail_id(EmailET.getText().toString());
        requestModel.setGender(selectedGender);
        Log.i(TAG, "getUserDetailsObject:"+requestModel.toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    /**
     *
     * @param mobileNumber
     * @param email
     * @param user_id
     */
    private void SaveSharedPrefs(String mobileNumber,String email,int user_id)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("mobile_number", mobileNumber); // Storing string
        editor.putString("email_id", email);
        editor.putInt("user_id", user_id);
        editor.commit();
    }


    /**
     *
     * @param userID
     * @param mobileNumber
     * @param emailId
     * @param name
     */
    private void FirebaseRegister(int userID, String mobileNumber, String emailId, String name)
    {
        // String url = "https://android-chat-app-e711d.firebaseio.com/users.json";
        String url = AtroadsUsers_URL_JSON;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase(AtroadsUsers_URL);

                if(s.equals("null")) {
//                    reference.child(name).child("password").setValue(mobileNumber);
//                    reference.child(name).child("user_id").setValue(userID);
//                    reference.child(name).child("email_id").setValue(emailId);
//                    reference.child(name).child("mobile").setValue(mobileNumber);
                    reference.child(mobileNumber).child("password").setValue(mobileNumber);
                    reference.child(mobileNumber).child("user_id").setValue(userID);
                    reference.child(mobileNumber).child("email_id").setValue(emailId);
                    reference.child(mobileNumber).child("name").setValue(name);
                    Toast.makeText(GetUserDetailsScreen.this, "registration successful", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(name)) {
//                            reference.child(name).child("password").setValue(mobileNumber);
//                            reference.child(name).child("user_id").setValue(userID);
//                            reference.child(name).child("email_id").setValue(emailId);
//                            reference.child(name).child("mobile").setValue(mobileNumber);
                            reference.child(mobileNumber).child("password").setValue(mobileNumber);
                            reference.child(mobileNumber).child("user_id").setValue(userID);
                            reference.child(mobileNumber).child("email_id").setValue(emailId);
                            reference.child(mobileNumber).child("name").setValue(name);
                            Toast.makeText(GetUserDetailsScreen.this, "registration successful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(GetUserDetailsScreen.this, "username already exists", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(GetUserDetailsScreen.this);
        rQueue.add(request);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }


    private void FirebaseCreateUserWithMail(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(GetUserDetailsScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

}
