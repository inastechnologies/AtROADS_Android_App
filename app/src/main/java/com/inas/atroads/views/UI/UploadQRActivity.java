package com.inas.atroads.views.UI;

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
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.style.UpdateLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.inas.atroads.R;
import com.inas.atroads.services.APIConstants;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.views.Activities.CaptureActivityPortrait;
import com.inas.atroads.views.Activities.ChatActivity;
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

import org.json.JSONObject;

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

public class UploadQRActivity extends BaseActivity {
    private static final int RESULT_GALLERY = 1;
    private static final String TAG = "UploadQRActivity";
    private static final String DEFAULT = "N/A";
    private Button uploadBtn;
    private ImageView qrIV;
  //  private Uri PflImageUri;
    private String PflImageBase64;
    private Subscription mSubscription;
    String Username, Email, Mobile;
    int UserId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_q_r);
        qrIV = findViewById(R.id.qrIV);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.ShowQR));
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
        SharedPreferences pref = getApplicationContext().getSharedPreferences("QRImage", 0); // 0 - for private mode
        PflImageBase64 = pref.getString("PflImageBase64",DEFAULT);
      showProgressDialog();
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
    private void GetSharedPrefs() {
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
    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,50,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_GALLERY) {
            if (null != data) {

                final Uri imageUri = data.getData();
                InputStream imageStream = null;
                try {
                      imageStream = getContentResolver().openInputStream(imageUri);
                }catch(Exception e){
                    e.printStackTrace();
                }
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                String encodedImage = encodeImage(selectedImage);

               /* PflImageUri = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = this.getContentResolver().openInputStream(PflImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                String image_64= encodeTobase64(yourSelectedImage);*/
               /* Log.i("PflImageUri", String.valueOf(PflImageUri));
                String ProfileImagePath = getPath(UploadQRActivity.this, PflImageUri);

                String FrontFileName = ProfileImagePath.substring(ProfileImagePath.lastIndexOf("/") + 1);
                Log.i("PflFileName", FrontFileName);

                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(PflImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageStream);
                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
                    byte[] byteArray = byteStream.toByteArray();
                    String baseString = Base64.encodeToString(byteArray,Base64.DEFAULT);*/


                /*    String encodedImage = encodeImage(selectedImage);
                    Log.i("encodedImage", encodedImage);
                    Bitmap ImgBitmap = Base64ToBitmap(encodedImage);
                    qrIV.setImageBitmap(ImgBitmap);
                    PflImageBase64 = BitmapToBase64(ImgBitmap);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("QRImage", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("PflImageBase64", PflImageBase64); // Storing string
                    editor.apply();
                    editor.commit();*/
                  /*  int maxLogSize = 1000;
                    for (int i = 0; i <= encodedImage.length() / maxLogSize; i++) {
                        int start = i * maxLogSize;
                        int end = (i + 1) * maxLogSize;
                        end = end > encodedImage.length() ? encodedImage.length() : end;
                        Log.v("PflImageBase64", encodedImage.substring(start, end));
                    }*/
                    showProgressDialog();
                    CallUploadQRAPI(encodedImage);
                    // CallEditUserInfoAPI();
            }
        }
        if(result!=null) {
            if (resultCode == RESULT_OK) {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                String UPCScanned = scanResult.getContents();

                String ScannedQRDetails = "";

              //  Toast.makeText(this, "" + UPCScanned, Toast.LENGTH_SHORT).show();
                String newStr="";
                if (UPCScanned.contains("upi://pay?pa")) {
                    String appendStr = "&cu=INR";
                            //mode=02&purpose=00&orgid=189999&sign=MEUCIQD/VLGj1RKjIcISGrDOUdBD3Q/58qhfcrn5SKkVGooa+wIgVYgcYO/4KY4ve8OjEnK3z4CRTDmj16ResO4DmjGed2c=";
                    if (UPCScanned.contains("aid")) {
                         newStr = UPCScanned.substring(0, UPCScanned.indexOf("&aid="));
                        ScannedQRDetails = newStr + appendStr;
                    } else {
                        ScannedQRDetails = UPCScanned;
                    }

                    Log.i(TAG, "newStr "+newStr);
                    Intent i = new Intent(UploadQRActivity.this, PaymentScreen.class);
                    i.putExtra("PayableAmount", 0.0);
                    i.putExtra("UPICODE", ScannedQRDetails);
                    //i.putExtra("IdToGetYourBill",IdToGetYourBill);
                    //i.putExtra("UserRideId",  UserRideId);
                    startActivity(i);
                } else {
                    CustomDialogWithOneBtn(UploadQRActivity.this, "Attention!!", "Please scan the proper QR code", "OK", new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        }
    }

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

    /********************************START OF CallYourBillAPI*******************************/
    /*
     * CallUploadQRAPI
     * */
    private void CallUploadQRAPI(String image_64){

        JsonObject object = UploadQRObject(image_64);
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
                        hideProgressDialog();
//                        Toast.makeText(YourBillScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            qrIV.setAlpha(1.0f);
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                           // qrIV.setAlpha(1.0f);
                            CustomDialog(UploadQRActivity.this, "Success!", mResponse.getMessage(), "Ok", new Runnable() {
                                @Override
                                public void run() {

                                    showProgressDialog();
                                    CallGetQRAPI();
                                }
                            });
                        }
                    }
                });

    }


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

    /**
     * Json object of UploadQRObject
     *
     * @return
     */
    private JsonObject UploadQRObject(String image_64)
    {
        UploadQRRequestModel requestModel = new UploadQRRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setAttachment(image_64);
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
                        hideProgressDialog();
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
                        hideProgressDialog();
                        // Toast.makeText(BillingDetailsActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0) {
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            LoadImageFromUrl(UploadQRActivity.this,  mResponse.getResult().get(0).getQrcode(), qrIV);
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

        if (!imageUrl.isEmpty()) {

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                imageBytes = Base64.decode(imageUrl, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                qrIV.setImageBitmap(decodedImage);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        //Picasso.with(context).load(imageUrl).error(R.drawable.profile).into(imageView);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.qr_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.scan_qr:
                IntentIntegrator integrator = new IntentIntegrator(UploadQRActivity.this);
                integrator.setOrientationLocked(false);
                integrator.setPrompt("Scan");
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}