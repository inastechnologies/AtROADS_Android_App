package com.inas.atroads.views.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.APIConstants;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.views.model.EditNameRequestModel;
import com.inas.atroads.views.model.EditNameResponseModel;
import com.inas.atroads.views.model.EditUserInfoRequestModel;
import com.inas.atroads.views.model.EditUserInfoResponseModel;
import com.inas.atroads.views.model.GetUserInfoRequestModel;
import com.inas.atroads.views.model.GetUserInfoResponseModel;
import com.inas.atroads.views.model.UploadPicRequestModel;
import com.inas.atroads.views.model.UploadPicResponseModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static com.inas.atroads.util.AtroadsConstant.AtroadsUsers_URL;
import static com.inas.atroads.util.Utilities.Base64ToBitmap;
import static com.inas.atroads.util.Utilities.BitmapToBase64;
import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class EditProfileScreen extends AppCompatActivity
{
    private static final int RESULT_GALLERY = 1;
    private static final String TAG = "EditProfileScreen";
    private static final String DEFAULT = "N/A" ;
    private Button editBtn,submitBtn;
    private EditText NameEt,MobileEt,emailEt;
    private ImageView profile_image,editIv;
    private Uri PflImageUri;
    //default profile img
    private String PflImageBase64 = "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";
    private Subscription mSubscription;
    private String Mobile,Email,Username;
    int UserId;
    private Toolbar toolbar;
    private String pflPicFromDB = "",username, emailId, mobileNo;
    private RadioButton yesBtn,noBtn;
    private String selectedRadio = "yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);
        SetViewsFromLayout();
        isNetworkAvailable(EditProfileScreen.this);
        GetSharedPrefs();
        CallGetUserInfoAPI();

    }

    /**
     * SetViewsFromLayout
     */
    private void SetViewsFromLayout()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // onBackPressed();
                Intent intent = new Intent(EditProfileScreen.this, HomeMapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        profile_image = findViewById(R.id.profile_image);
        NameEt = findViewById(R.id.NameEt);
        MobileEt = findViewById(R.id.MobileEt);
        emailEt = findViewById(R.id.emailEt);
        editIv = findViewById(R.id.editIv);
        yesBtn = findViewById(R.id.yesBtn);
        noBtn = findViewById(R.id.noBtn);
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setText("Update");
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CallEditUserInfoAPI();
                if(yesBtn.isChecked())
                {
                    Log.i(TAG, "onClick: "+yesBtn.getText().toString());
                    selectedRadio = "yes";
                }
                else {
                    Log.i(TAG, "onClick: "+yesBtn.getText().toString());
                    selectedRadio = "no";
                }
                CallEditUserInfoAPI();
            }
        });
        SetEditBtn();
        SetProfileImage();
    }


    private void Validation()
    {
        if(TextUtils.isEmpty(NameEt.getText().toString()))
        {
            Toast.makeText(this, getString(R.string.err_please_enter_full_name), Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(emailEt.getText().toString()))
        {
            Toast.makeText(this, getString(R.string.err_please_enter_emailid), Toast.LENGTH_SHORT).show();
        }
        else {
            submitBtn.setBackgroundResource(R.drawable.round_rect_button_bg);
            submitBtn.setTextColor(Color.parseColor("#ffffff"));

        }
    }


    /**
     * Set the Edit Button
     */
    private void SetEditBtn()
    {
        editIv = findViewById(R.id.editIv);
        editIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NameEt.setSelection(NameEt.getText().length());
                NameEt.setFocusableInTouchMode(true);

            }
        });
    }

    /**
     * SetProfileImage
     */
    private void SetProfileImage()
    {
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
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
                    String FrontFileName = ProfileImagePath.substring(ProfileImagePath.lastIndexOf("/") + 1);
                    Log.i("PflFileName", FrontFileName);
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(PflImageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        String encodedImage = encodeImage(selectedImage);
                        Log.i("encodedImage", encodedImage);
                        Bitmap ImgBitmap = Base64ToBitmap(encodedImage);
                        profile_image.setImageBitmap(ImgBitmap);
                        PflImageBase64 = BitmapToBase64(ImgBitmap);
                        int maxLogSize = 1000;
                        for (int i = 0; i <= encodedImage.length() / maxLogSize; i++) {
                            int start = i * maxLogSize;
                            int end = (i + 1) * maxLogSize;
                            end = end > encodedImage.length() ? encodedImage.length() : end;
                            Log.v("PflImageBase64", encodedImage.substring(start, end));
                        }
                        CallUploadPicAPI(PflImageBase64);
                        CallEditUserInfoAPI();
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
       GetSharedPrefs
       */
    private void GetSharedPrefs()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
        UserId = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Email =  pref.getString("email_id",DEFAULT);
        CallGetUserInfoAPI();
        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
    }


    /*
     * CallGetUserInfoAPI
     * */
    private void CallGetUserInfoAPI(){

        JsonObject object = GetUserInfoObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.GetUserInfoResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUserInfoResponseModel>() {
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
                    public void onNext(GetUserInfoResponseModel mResponse) {
                        Log.i(TAG, "GetUserInfoResponse: "+mResponse);

                        // Toast.makeText(PairActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            Toast.makeText(EditProfileScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            username = mResponse.getResult().get(0).getName();
                           mobileNo = mResponse.getResult().get(0).getMobileNumber();
                           emailId = mResponse.getResult().get(0).getEmailId();
                          pflPicFromDB = mResponse.getResult().get(0).getProfilePic();
                          MobileEt.setText(mobileNo+"");
                          NameEt.setText(username+"");
                          NameEt.setSelection(NameEt.getText().length());
                          emailEt.setText(emailId+"");
                          LoadImageFromUrl(EditProfileScreen.this, APIConstants.IMAGE_URL+pflPicFromDB,profile_image);
                            String accepting_other_gender = mResponse.getResult().get(0).getAcceptingOtherGender();
                            if(accepting_other_gender.equals(""))
                            {
                                yesBtn.setChecked(true);
                            }
                            else if(accepting_other_gender.equals("yes")){
                                yesBtn.setChecked(true);
                            }
                            else if(accepting_other_gender.equals("no")){
                                noBtn.setChecked(true);
                            }
                            Validation();
                        }
                    }
                });
    }

    /**
     * Json object of GetUserInfoObject
     *
     * @return
     */
    private JsonObject GetUserInfoObject()
    {
        GetUserInfoRequestModel requestModel = new GetUserInfoRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    /**
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    private void LoadImageFromUrl(Context context, String imageUrl, ImageView imageView)
    {
        Picasso.with(context).load(imageUrl).error(R.drawable.profile).into(imageView);
    }


    /*
     * CallGetUserInfoAPI
     * */
    private void CallEditUserInfoAPI(){

        JsonObject object = EditUserInfoObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.EditUserInfoResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EditUserInfoResponseModel>() {
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
                    public void onNext(EditUserInfoResponseModel mResponse) {
                        Log.i(TAG, "EditUserInfoResponseModel: "+mResponse);

                        // Toast.makeText(PairActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            Toast.makeText(EditProfileScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            Toast.makeText(EditProfileScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                            Firebase reference = new Firebase(AtroadsUsers_URL);
//                            Map<String, Object> updates = new HashMap<String,Object>();
//                            reference.child(username).child("email_id").setValue(mResponse.getResult().get(0).getEmailId());
//                            reference.child(username).setValue(mResponse.getResult().get(0).getName());

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getString(R.string.firebase_path_users));
//                            ref.child(mobileNo).child(emailId).setValue(mResponse.getResult().get(0).getEmailId());
//                            ref.child(mobileNo).child(username).setValue(mResponse.getResult().get(0).getName());
                            ref.child(mobileNo).child("name").setValue(mResponse.getResult().get(0).getName());
                            ref.child(mobileNo).child("email_id").setValue(mResponse.getResult().get(0).getEmailId());

                          //  LoadImageFromUrl(EditProfileScreen.this, APIConstants.IMAGE_URL+pflPic,profile_image);
                            Intent intent = new Intent(EditProfileScreen.this, HomeMapsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }

    /**
     * Json object of GetUserInfoObject
     *
     * @return
     */
    private JsonObject EditUserInfoObject()
    {
        EditUserInfoRequestModel requestModel = new EditUserInfoRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setProfilePic(PflImageBase64);
        requestModel.setName(NameEt.getText().toString());
        requestModel.setEmailId(emailEt.getText().toString());
        requestModel.setAcceptingOtherGender(selectedRadio);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    /*
     * CallGetUserInfoAPI
     * */
    private void CallUploadPicAPI(String pflImageBase64){

        JsonObject object = UploadPicObject(pflImageBase64);
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.UploadPhotoResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadPicResponseModel>() {
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
                    public void onNext(UploadPicResponseModel mResponse) {
                        Log.i(TAG, "UploadPicResponseModel: "+mResponse);

                        // Toast.makeText(PairActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            Toast.makeText(EditProfileScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            Toast.makeText(EditProfileScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            LoadImageFromUrl(EditProfileScreen.this, APIConstants.IMAGE_URL+mResponse.getResult().get(0).getProfilePic(),profile_image);
                        }
                    }
                });
    }

    /**
     * Json object of GetUserInfoObject
     *
     * @return
     * @param pflImageBase64
     */
    private JsonObject UploadPicObject(String pflImageBase64)
    {
        UploadPicRequestModel requestModel = new UploadPicRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setProfilePic(pflImageBase64);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }




    /*
     * CallEditNameAPI
     * */
    private void CallEditNameAPI()
    {
        JsonObject object = EditNameObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.EditNameResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EditNameResponseModel>() {
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
                    public void onNext(EditNameResponseModel mResponse) {
                        Log.i(TAG, "EditNameResponseModel: "+mResponse);

                        // Toast.makeText(PairActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            Toast.makeText(EditProfileScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            Toast.makeText(EditProfileScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfileScreen.this, HomeMapsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }

    /**
     * Json object of GetUserInfoObject
     *
     * @return
     */
    private JsonObject EditNameObject()
    {
        EditNameRequestModel requestModel = new EditNameRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setFullname(NameEt.getText().toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }




    /**
     *
     * @param context
     * @param Title
     * @param Msg
     * @param buttonNam1
     * @param buttonNam2
     * @param runnable
     * @param runnable1
     */
    public static void CustomDialog(Context context, String Title, String Msg, String buttonNam1,String buttonNam2, Runnable runnable,Runnable runnable1)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
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
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setText(buttonNam2);
        // if decline button is clicked, close the custom dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                runnable1.run();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditProfileScreen.this, HomeMapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
}
