package com.inas.atroads.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.views.model.HelpRequestModel;
import com.inas.atroads.views.model.HelpResponseModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.Base64ToBitmap;
import static com.inas.atroads.util.Utilities.BitmapToBase64;
import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class HelpActivity extends BaseActivity {

    private static final String DEFAULT = "N/A";
    private Context mContext;
    private Toolbar toolbar;
    Address address;
    LatLng currentlatLng;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    LocationManager mLocationManager;
    private String provider;
    private static String TAG = "MAP LOCATION";
    FusedLocationProviderClient fusedLocationProviderClient;
    private Subscription mSubscription;
    private EditText titleEt,DescEt,AttachmentEt;
    private String Mobile,Email,Username;
    private Button selectImageBtn,submitBtn;
    int UserId;
    private static final int RESULT_GALLERY = 1;
    private Uri PflImageUri;
    private ImageView reportImg;
    private String PflImageBase64 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        //assining layout
        setContentView(R.layout.activity_help);

        isNetworkAvailable(HelpActivity.this);
        /* intializing and assigning ID's */
        initViews();

        setViews();

    }

    /* intializing and assigning ID's */
    private void initViews()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.help));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });
       
        GetSharedPrefs();
    }

    private void setViews()
    {
        AttachmentEt = findViewById(R.id.AttachmentEt);
        titleEt = findViewById(R.id.titleEt);
        DescEt = findViewById(R.id.DescEt);

        reportImg = findViewById(R.id.reportImg);
        selectImageBtn = findViewById(R.id.selectImageBtn);
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallHelpAPI();
            }
        });

    }

    /**
     * This method is used to open gallery
     */
    private void OpenGallery()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent , RESULT_GALLERY);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_GALLERY:
                if (null != data) {
                    PflImageUri = data.getData();
                    Log.i("PflImageUri", String.valueOf(PflImageUri));
                    String ProfileImagePath = getPath(getApplicationContext(), PflImageUri);
                    Log.d("PflImagePath", ProfileImagePath);
                    String filename=ProfileImagePath.substring(ProfileImagePath.lastIndexOf("/")+1);
                    AttachmentEt.setText(filename+"");
                    String FrontFileName = ProfileImagePath.substring(ProfileImagePath.lastIndexOf("/") + 1);
                    Log.i("PflFileName", FrontFileName);
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(PflImageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        String encodedImage = encodeImage(selectedImage);
                        Log.i("encodedImage", encodedImage);
                        Bitmap ImgBitmap = Base64ToBitmap(encodedImage);
                        reportImg.setImageBitmap(ImgBitmap);
                        PflImageBase64 = BitmapToBase64(ImgBitmap);
                        int maxLogSize = 1000;
                        for (int i = 0; i <= encodedImage.length() / maxLogSize; i++) {
                            int start = i * maxLogSize;
                            int end = (i + 1) * maxLogSize;
                            end = end > encodedImage.length() ? encodedImage.length() : end;
                            Log.v("PflImageBase64", encodedImage.substring(start, end));
                        }
                       // CallEditUserInfoAPI();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     *
     * @param bm
     * @return
     */
    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    /**
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }


    /*
     * CallGetUserInfoAPI
     * */
    private void CallHelpAPI(){

        JsonObject object = HelpObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.HelpResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HelpResponseModel>() {
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
                    public void onNext(HelpResponseModel mResponse) {
                        Log.i(TAG, "HelpResponseModel: "+mResponse);

                        // Toast.makeText(PairActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            Toast.makeText(HelpActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1) {
                            Toast.makeText(HelpActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });

    }

    /**
     * Json object of GetUserInfoObject
     *
     * @return
     */
    private JsonObject HelpObject()
    {
        HelpRequestModel requestModel = new HelpRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setTitle(titleEt.getText().toString());
        requestModel.setAttachment(PflImageBase64);
        requestModel.setDescription(DescEt.getText().toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
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


    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(HelpActivity.this, HomeScreen.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
        super.onBackPressed();
    }
}
