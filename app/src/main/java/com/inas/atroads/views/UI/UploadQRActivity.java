package com.inas.atroads.views.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.style.UpdateLayout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.APIConstants;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.views.Activities.BillingDetailsActivity;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.model.DeletePairRequestModel;
import com.inas.atroads.views.model.DeletePairResponseModel;
import com.inas.atroads.views.model.GetQRRequestModel;
import com.inas.atroads.views.model.GetQRResponseModel;
import com.inas.atroads.views.model.UploadQRRequestModel;
import com.inas.atroads.views.model.UploadQRResponseModel;
import com.inas.atroads.views.model.YourBillRequestModel;
import com.inas.atroads.views.model.YourBillResponseModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.Base64ToBitmap;
import static com.inas.atroads.util.Utilities.BitmapToBase64;
import static com.inas.atroads.views.Activities.BillingDetailsActivity.CustomDialog;

public class UploadQRActivity extends AppCompatActivity {
    private static final int RESULT_GALLERY = 1;
    private static final String TAG = "UploadQRActivity";
    private static final String DEFAULT = "N/A";
    private Button uploadBtn;
    private ImageView qrIV;
    private Uri PflImageUri;
    private String PflImageBase64;
    private Subscription mSubscription;
    String Username, Email, Mobile;
    int UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_q_r);
        qrIV = findViewById(R.id.qrIV);
        GetSharedPrefs();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("QRImage", 0); // 0 - for private mode
        PflImageBase64 = pref.getString("PflImageBase64",DEFAULT);
        CallGetQRAPI();
        uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

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
     * This method is used to open gallery
     */
    private void OpenGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_GALLERY);
    }

    /**
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    /**
     * @param bm
     * @return
     */
    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_GALLERY:
                if (null != data) {
                    PflImageUri = data.getData();
                    Log.i("PflImageUri", String.valueOf(PflImageUri));
                    String ProfileImagePath = getPath(UploadQRActivity.this, PflImageUri);
                    Log.d("PflImagePath", ProfileImagePath);
                    String filename = ProfileImagePath.substring(ProfileImagePath.lastIndexOf("/") + 1);
                    // uploadTv.setText(filename+"");
                    String FrontFileName = ProfileImagePath.substring(ProfileImagePath.lastIndexOf("/") + 1);
                    Log.i("PflFileName", FrontFileName);
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(PflImageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        String encodedImage = encodeImage(selectedImage);
                        Log.i("encodedImage", encodedImage);
                        Bitmap ImgBitmap = Base64ToBitmap(encodedImage);
                        qrIV.setImageBitmap(ImgBitmap);
                        PflImageBase64 = BitmapToBase64(ImgBitmap);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("QRImage", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("PflImageBase64", PflImageBase64); // Storing string
                        editor.apply();
                        editor.commit();
                        int maxLogSize = 1000;
                        for (int i = 0; i <= encodedImage.length() / maxLogSize; i++) {
                            int start = i * maxLogSize;
                            int end = (i + 1) * maxLogSize;
                            end = end > encodedImage.length() ? encodedImage.length() : end;
                            Log.v("PflImageBase64", encodedImage.substring(start, end));
                        }
                        CallUploadQRAPI();
                        // CallEditUserInfoAPI();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
                
        }
    }



    /********************************START OF CallYourBillAPI*******************************/
    /*
     * CallUploadQRAPI
     * */
    private void CallUploadQRAPI(){

        JsonObject object = UploadQRObject();
        AtroadsService service = ServiceFactory.createRetrofitService(UploadQRActivity.this, AtroadsService.class);
        mSubscription = service.QRUpload(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadQRResponseModel>() {
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
                    public void onNext(UploadQRResponseModel mResponse) {
                        Log.i(TAG, "UploadQRResponseModel: "+mResponse);
//                        Toast.makeText(YourBillScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {

                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            qrIV.setAlpha(1.0f);
                            CustomDialog(UploadQRActivity.this, "Success!", mResponse.getMessage(), "Ok", new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    }
                });

    }

    /**
     * Json object of UploadQRObject
     *
     * @return
     */
    private JsonObject UploadQRObject()
    {
        UploadQRRequestModel requestModel = new UploadQRRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setAttachment(PflImageBase64);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF YourBillObject *******************************/


    /********************************START OF CallDeletePairAPI*******************************/
    /*
     * CallDelayDeletePairAPI
     * */
    public void CallGetQRAPI(){

        JsonObject object = GetQRPairObject();
        AtroadsService service = ServiceFactory.createRetrofitService(UploadQRActivity.this, AtroadsService.class);
        mSubscription = service.GetQRResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetQRResponseModel>() {
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
                    public void onNext(GetQRResponseModel mResponse) {
                        Log.i(TAG, "GetQRResponseModel: "+mResponse);
                        // Toast.makeText(BillingDetailsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            //Toast.makeText(PairSuccessScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            LoadImageFromUrl(UploadQRActivity.this, APIConstants.IMAGE_URL + mResponse.getResult().get(0).getQrcode(), qrIV);
                        }
                    }
                });

    }

    /**
     * Json object of GetQRPairObject
     *
     * @return
     */
    private JsonObject GetQRPairObject()
    {
        GetQRRequestModel requestModel = new GetQRRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF CallDeletePairAPI *******************************/


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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}