package com.inas.atroads.views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.BuildConfig;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.util.AtroadsConstant;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.views.model.GetUserInfoRequestModel;
import com.inas.atroads.views.model.GetUserInfoResponseModel;
import com.inas.atroads.views.model.OnGoingRideRequestModel;
import com.inas.atroads.views.model.OnGoingRidesResponseModel;
import com.inas.atroads.views.model.PairedDetailsForChatRequestModel;
import com.inas.atroads.views.model.PairedDetailsForChatResponseModel;
import com.inas.atroads.views.model.ScheduleRideNotifiyRequestModel;
import com.inas.atroads.views.model.ScheduleRideNotifyResponseModel;
import com.inas.atroads.views.model.UploadImageToFBStorage;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.Base64ToBitmap;
import static com.inas.atroads.util.Utilities.BitmapToBase64;

public class ChatActivity extends BaseActivity implements OnMapReadyCallback {
    private static final int RESULT_GALLERY = 1 ;
    private static final int REQUEST_CAPTURE_IMAGE = 2;
    private static final int REQUEST_WRITE_PERMISSION = 3;
    private static final int REQUEST_CAMERA_PERMISSION = 4;
    LinearLayout layoutchat;
    ImageView sendButton,shareLocationBtn;
    EditText messageArea;
    ScrollView scrollView_chat;
    Firebase reference1, reference2,reference3,reference4;
    private int PLACE_PICKER_REQUEST = 1;
    private String TAG = "ChatActivity";
    String FROMACTIVITY = "ChatActivity";
    GoogleMap googleMap;
    MapFragment mapFragment;
    String Location = "";
    Double latitude,longitude;
    private Bitmap SelectedImgBitmap;
    private Uri PflImageUri;
    private String PflImageBase64 = "";
    private ProgressDialog progressDialog1;
    //firebase objects
    private StorageReference storageReference;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<UploadImageToFBStorage> uploads;

    //uri to store file
    private Uri filePath;
    String Username,Email,Mobile,OtherUsername;
    int UserId,OtheruserId;
    private String DEFAULT = "N/A";
    private Subscription mSubscription;
    private String OtherProfilePic;
    private String Thisusername;
    private LinearLayout chatLinerarActivity;
    int UserRideId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        SetViewsFromLayout();
        GetSharedPrefs();
        CallGetUserInfoAPI();
    }


    /**
     * SetViewsFromLayout
     */
    private void SetViewsFromLayout()
    {
        layoutchat = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        fab();
        scrollView_chat = (ScrollView)findViewById(R.id.scrollView_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("OtheruserName"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Log.i(TAG, "SetViewsFromLayout: UserDetails.chatWith"+UserDetails.chatWith);
        Log.i(TAG, "SetViewsFromLayout: UserDetails.username"+UserDetails.username);
        SetProgressDialog();

        scrollView_chat.post(new Runnable() {
            @Override
            public void run() {
                scrollView_chat.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        scrollView_chat.scrollTo(0, scrollView_chat.getBottom());
       // scrollView.fullScroll(View.FOCUS_DOWN);
    }


    private void SetProgressDialog()
    {
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setCanceledOnTouchOutside(false);
        progressDialog1.setTitle("Loading...");
        progressDialog1.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog1.dismiss();
            }
        },3000);
    }

    /**
     * SendBtnOnClick
     */
    private void SendBtnOnClick()
    {
        SetFBReference();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
//                    map.put("user", UserDetails.username);
                    map.put("user", Thisusername);
                    map.put("msg_type","1");
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
            }
        });
    }

    /**
     * SetFBReference
     */
    private void SetFBReference()
    {
        Firebase.setAndroidContext(this);
//        reference1 = new Firebase(AtroadsConstant.DATABASE_MSGS_URL + UserDetails.username + "_" + UserDetails.chatWith);
        reference1 = new Firebase(AtroadsConstant.DATABASE_MSGS_URL + Thisusername + "_" + getIntent().getStringExtra("OtheruserName"));
//        reference2 = new Firebase(AtroadsConstant.DATABASE_MSGS_URL + UserDetails.chatWith + "_" + UserDetails.username);
        reference2 = new Firebase(AtroadsConstant.DATABASE_MSGS_URL + UserDetails.chatWith + "_" + Thisusername);
    }


    /**
     * FBReferenceChildEventListener
     */
    private void FBReferenceChildEventListener()
    {
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String msg_type = map.get("msg_type").toString();

//                if(userName.equals(UserDetails.username)){
                if(userName.equals(Thisusername)){
                    // addMessageBox("You \n" + message, Integer.parseInt(msg_type));
                    addMessageBox(message, Integer.parseInt(msg_type));
                }
                else{
                    if(message.contains("https") && msg_type.equals("5"))
                    {
                        // Image
                        addMessageBox(message, 6);
                    }
                    else if (message.contains("-") && msg_type.equals("3")) {
                        //code
                        String[] parts = message.split("-");
                        String lat = parts[0];
                        String lng = parts[1];
                        // System.out.println(message + " is a number");
                        //Location
                        addMessageBox(UserDetails.chatWith + "\n" + message, 4);
                    }
                    else {
                        //Text Message
                        addMessageBox(UserDetails.chatWith + "\n" + message, 2);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
     * CheckLocationSharingFromActivityIntent
     */
    private void CheckLocationSharingFromActivityIntent()
    {
        FROMACTIVITY = getIntent().getStringExtra("FROMACTIVITY");
        if(FROMACTIVITY.equals("SelectCurrentLocationActivity"))
        {
            Location = getIntent().getStringExtra("CurrentLocation");
            latitude = getIntent().getDoubleExtra("latitude",0.0);
            longitude = getIntent().getDoubleExtra("longitude",0.0);
            Log.i(TAG, "onCreate: Location"+Location);
            Log.i(TAG, "onCreate: latitude"+latitude);
            Log.i(TAG, "onCreate: longitude"+longitude);
            Map<String, String> map = new HashMap<String, String>();
            map.put("message", latitude+"-"+longitude);
//            map.put("user", UserDetails.username);
            map.put("user", Thisusername);
            map.put("msg_type","3");
            reference1.push().setValue(map);
            reference2.push().setValue(map);
            if(FROMACTIVITY.equals("SelectCurrentLocationActivity"))
            {
                // addMessageBox("You \n" + Location, 3);
            }
            else {

            }
        }
        else if(FROMACTIVITY.equals("UsersActivity")){

        }
    }
    /**
     * Floating action button Animation
     */
    private void fab()
    {
        com.leinardi.android.speeddial.SpeedDialView speedDial = findViewById(R.id.speedDial);
        speedDial.inflate(R.menu.fab_menu);
        speedDial.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId())
                {
                    case R.id.fab_location:
                        //Toast.makeText(ChatActivity.this, "fab_location", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ChatActivity.this,SelectCurrentLocationActivity.class);
                        startActivityForResult(i, 101);
                        break;

                    case R.id.fab_add:
                        SelectImageDialog();
                        // Toast.makeText(ChatActivity.this, "fab_add", Toast.LENGTH_SHORT).show();

                        break;

                    case R.id.fab_call:

                        CustomDialogWithOneBtn(ChatActivity.this,"Request for CALL","You will get call from a number to connect with paired user!!", "Call Request",new Runnable() {
                            @Override
                            public void run() {
                                showProgressDialog();
                                callingAPI();
                            }
                        });
                        break;
                }
                return false;
            }
        });
    }


    private void myStoragePermission() {
        if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            myCameraPermission();
        } else {
            //changed here
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            }
        }
    }

    //+10 changed its sinature as Fragment; without it  onRequestPermissionsResult won't bbe called
    private void myCameraPermission() {
        if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCameraIntent();
        } else {
            //changed here
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        }
    }

    /**
     *
     */
    private void SelectImageDialog() {
        final Dialog dialog = new Dialog(ChatActivity.this);
        dialog.setContentView(R.layout.camera_gallery_alert);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView title = (TextView) dialog.findViewById(R.id.TitleTv);
        ImageView camImg = dialog.findViewById(R.id.camImg);
        ImageView GalImg = dialog.findViewById(R.id.GallImg);
        camImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStoragePermission();
//                openCameraIntent();
                dialog.dismiss();
            }
        });
        GalImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
                dialog.dismiss();
            }
        });
    }

    /**
     * openCameraIntent
     */
    private void openCameraIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_CAPTURE_IMAGE);
        } catch (ActivityNotFoundException e) {
            Log.d("exception", e.toString());
            // display error state to the user
        }
    }


    /**
     * This method is used to open gallery
     */
    private void OpenGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_GALLERY);
    }

    public String getRealPathFromURIForGallery(Context context,Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
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


    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_GALLERY:
                if (null != data) {
                    PflImageUri = data.getData();
                    Log.i("PflImageUri", String.valueOf(PflImageUri));
                    String ProfileImagePath = getRealPathFromURIForGallery(ChatActivity.this, PflImageUri);
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
//                        ProfileIV.setImageBitmap(ImgBitmap);
                        SelectedImgBitmap = ImgBitmap;
                        PflImageBase64 = BitmapToBase64(ImgBitmap);
                        int maxLogSize = 1000;
                        for (int i = 0; i <= encodedImage.length() / maxLogSize; i++) {
                            int start = i * maxLogSize;
                            int end = (i + 1) * maxLogSize;
                            end = end > encodedImage.length() ? encodedImage.length() : end;
                            Log.v("PflImageBase64Gallery", encodedImage.substring(start, end));
                        }
                        uploadFile(PflImageUri,String.valueOf(UserId));
//                        uploadFile(PflImageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            case REQUEST_CAPTURE_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null) {

                        PflImageUri = data.getData();
                       // Log.i("PflImageUri", String.valueOf(PflImageUri));
                        //String ProfileImagePath = getRealPathFromURIForGallery(ChatActivity.this, PflImageUri);
                       // Log.d("PflImagePath", ProfileImagePath);
                      //  String filename = ProfileImagePath.substring(ProfileImagePath.lastIndexOf("/") + 1);
                        // uploadTv.setText(filename+"");
                        //String FrontFileName = ProfileImagePath.substring(ProfileImagePath.lastIndexOf("/") + 1);
                       // Log.i("PflFileName", FrontFileName);
                       /* final InputStream imageStream;

                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            String encodedImage = encodeImage(imageBitmap);
                            SelectedImgBitmap = imageBitmap;
                            PflImageBase64 = BitmapToBase64(imageBitmap);
                            int maxLogSize = 1000;
                            for (int i = 0; i <= encodedImage.length() / maxLogSize; i++) {
                                int start = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > encodedImage.length() ? encodedImage.length() : end;
                                //Log.v("PflImageBase64Camera", encodedImage.substring(start, end));
                            }*/
                        try {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");
                            UploadCamImg(photo);
                            //uploadFile(PflImageUri,String.valueOf(UserId));
//                        uploadFile(PflImageUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //imageView.setImageBitmap(imageBitmap);

                       /* PflImageUri = data.getData();
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        String encodedImage = encodeImage(imageBitmap);
                        SelectedImgBitmap = imageBitmap;
                        PflImageBase64 = BitmapToBase64(imageBitmap);
                        int maxLogSize = 1000;
                        for (int i = 0; i <= encodedImage.length() / maxLogSize; i++) {
                            int start = i * maxLogSize;
                            int end = (i + 1) * maxLogSize;
                            end = end > encodedImage.length() ? encodedImage.length() : end;
                            Log.v("PflImageBase64Camera", encodedImage.substring(start, end));
                        }
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        UploadCamImg(photo);*/
                    }
                }
                break;

            case 101:
                Location = getIntent().getStringExtra("CurrentLocation");
                latitude = getIntent().getDoubleExtra("latitude",0.0);
                longitude = getIntent().getDoubleExtra("longitude",0.0);
                Log.i(TAG, "onCreate: Location"+Location);
                Log.i(TAG, "onCreate: latitude"+latitude);
                Log.i(TAG, "onCreate: longitude"+longitude);
                Map<String, String> map = new HashMap<String, String>();
                map.put("message", latitude+"-"+longitude);
//            map.put("user", UserDetails.username);
                map.put("user", Thisusername);
                map.put("msg_type","3");
                reference1.push().setValue(map);
                reference2.push().setValue(map);
                if(FROMACTIVITY.equals("SelectCurrentLocationActivity"))
                {
                    // addMessageBox("You \n" + Location, 3);
                }
                else {

                }
        }
    }

    private void UploadCamImg(Bitmap photo)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "Title", null);
        Uri f= Uri.parse(path);
       // ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //photo.compress(Bitmap.CompressFormat.PNG, 100, stream);

       // byte[] b = stream.toByteArray();
        //  StorageReference filepath = storageReference.child(AtroadsConstant.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".jpg");

        StorageReference storageReference =FirebaseStorage.getInstance().getReference().child(AtroadsConstant.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".jpg");
        //StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
        storageReference.putFile(f).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Uri downloadUri = taskSnapshot.getUploadSessionUri();
              //  Toast.makeText(ChatActivity.this, "uploaded"+downloadUri, Toast.LENGTH_SHORT).show();

                Log.i(TAG, "onSuccess: "+ taskSnapshot.getStorage().getDownloadUrl());
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();
                Log.i(TAG, "onSuccess: downloadUrl"+downloadUrl);
                //Adding image message
                Map<String, String> map = new HashMap<String, String>();
                map.put("message",String.valueOf(downloadUrl));
//                map.put("user", UserDetails.username);
                map.put("user", Thisusername);
                map.put("msg_type","5");
                reference1.push().setValue(map);
                reference2.push().setValue(map);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });

    }



    /**
     *
     * @param uri
     * @return
     */
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /**
     *
     * @param filePath
     * @param s
     */
    private void uploadFile(Uri filePath, String s) {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(AtroadsConstant.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            //creating the upload object to store uploaded image details
                            UploadImageToFBStorage upload = new UploadImageToFBStorage("FileName", taskSnapshot.getUploadSessionUri().toString(),s);

                            //adding an upload to firebase database
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);

                            Log.i(TAG, "onSuccess: "+ taskSnapshot.getStorage().getDownloadUrl());
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            Log.i(TAG, "onSuccess: downloadUrl"+downloadUrl);
                            //Adding image message
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("message",String.valueOf(downloadUrl));
//                            map.put("user", UserDetails.username);
                            map.put("user", Thisusername);
                            map.put("msg_type","5");
                            reference1.push().setValue(map);
                            reference2.push().setValue(map);

                            GetDownloadURL(filePath);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    RetrieveFile();
                                }
                            },10000);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");


                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

    private void GetDownloadURL(Uri file) {
        UploadTask uploadTask;
        final StorageReference ref = storageReference.child("images/mountains.jpg");
        uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                Log.i(TAG, "then: DownloadURL" + ref.getDownloadUrl());
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.i(TAG, "onComplete: " + downloadUri);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    private void RetrieveFile()
    {
        progressDialog = new ProgressDialog(this);

        uploads = new ArrayList<>();

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference(AtroadsConstant.DATABASE_PATH_UPLOADS);

        //adding an event listener to fetch values
        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//
//            }

            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadImageToFBStorage upload = postSnapshot.getValue(UploadImageToFBStorage.class);
                    uploads.add(upload);
                    Log.i(TAG, "Retrieve onDataChange: "+upload.url + "\n"+ upload.id + "\n");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }



    /*****************************START OF CallPlacesAPIClient********************************/
    /**
     * CallPlacesAPIClient
     */
    private void CallPlacesAPIClient()
    {
        if (!Places.isInitialized()) {
            Places.initialize(ChatActivity.this, getString(R.string.google_maps_key));
        }

        // Create a new Places client instance
        PlacesClient placesClient = Places.createClient(ChatActivity.this);

        placesClient.fetchPlace(new FetchPlaceRequest() {
            @NonNull
            @Override
            public String getPlaceId() {

                return null;
            }

            @NonNull
            @Override
            public List<com.google.android.libraries.places.api.model.Place.Field> getPlaceFields() {
                return null;
            }

            @Nullable
            @Override
            public AutocompleteSessionToken getSessionToken() {
                return null;
            }

            @Nullable
            @Override
            public CancellationToken getCancellationToken() {
                return null;
            }
        });
        //PlacesSelection();
    }

    /*****************************END OF CallPlacesAPIClient********************************/


    /**
     * addMessageBox according to msg type
     * @param message
     * @param type
     */
    public void addMessageBox(String message, int type)
    {
        if(type == 1) {
            TextView textView = new TextView(ChatActivity.this);
            textView.setText(message);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(180, 10, 10, 10);
            textView.setLayoutParams(lp);

            textView.setBackgroundResource(R.drawable.rounded_corner2);
            layoutchat.addView(textView);
            messageArea.setText("");
            scrollView_chat.fullScroll(View.FOCUS_DOWN);
        }
        else if(type == 2){
            TextView textView = new TextView(ChatActivity.this);
            textView.setText(message);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 10, 180, 10);
            textView.setLayoutParams(lp);
            textView.setBackgroundResource(R.drawable.rounded_corner1);
            layoutchat.addView(textView);
            messageArea.setText("");
            scrollView_chat.fullScroll(View.FOCUS_DOWN);
        }
        else if(type == 3)
        {
            ImageView button = new ImageView(ChatActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 10, 10, 10);
            lp.gravity = Gravity.RIGHT;
            lp.width = 400;
            lp.height =400;
            button.setLayoutParams(lp);
//            button.setForegroundGravity(View.FOCUS_RIGHT);
            button.setBackgroundResource(R.drawable.map_chat);
//            Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
//            button.startAnimation(aniSlide);
            button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ChatActivity.this,SelectCurrentLocationActivity.class);
                    startActivity(i);
                }
            });
            layoutchat.addView(button);
            messageArea.setText("");
            scrollView_chat.fullScroll(View.FOCUS_DOWN);
        }
        else if(type == 4) {
            ImageView button = new ImageView(ChatActivity.this);
//            button.setText("Show Location");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 10, 180, 10);
            lp.gravity = Gravity.LEFT;
            lp.width = 400;
            lp.height =400;
            button.setLayoutParams(lp);
            button.setBackgroundResource(R.drawable.map_chat);
//            Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
//            button.startAnimation(aniSlide);
            button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    int OtherID = getIntent().getIntExtra("OtheruserId",0);
                    Intent i = new Intent(ChatActivity.this,TrackerDisplayActivity.class);
                    i.putExtra("OtherID",OtherID);
                    startActivity(i);
                }
            });
            layoutchat.addView(button);
            messageArea.setText("");
            scrollView_chat.fullScroll(View.FOCUS_DOWN);
        }
        else if(type == 5) {
            ImageView imageView = new ImageView(ChatActivity.this);
            imageView.setImageBitmap(SelectedImgBitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 10, 10, 10);
            lp.gravity = Gravity.RIGHT;
            lp.width = 400;
            lp.height =400;
            imageView.setLayoutParams(lp);
            imageView.setBackgroundResource(R.drawable.rounded_corner2);
            Log.i(TAG, "addMessageBox5: URL"+message);
            String str1=message.replace("\"", "");
            Log.i(TAG, "addMessageBox: RemovedQuotes"+str1);
            LoadImageFromUrl(ChatActivity.this,message,imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(ChatActivity.this);
                    dialog.setContentView(R.layout.dialog_with_imageview);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    ImageView image = (ImageView) dialog.findViewById(R.id.ImgView);
                    //  image.setImageResource(R.drawable.ic_menu_gallery);
                    LoadImageFromUrl(ChatActivity.this,message,image);
                    dialog.show();
                }
            });

            layoutchat.addView(imageView);
            messageArea.setText("");
            scrollView_chat.fullScroll(View.FOCUS_DOWN);
        }
        else if(type == 6) {
            ImageView imageView = new ImageView(ChatActivity.this);
            imageView.setImageBitmap(SelectedImgBitmap);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 10, 10, 10);
            lp.gravity = Gravity.LEFT;
            lp.width = 400;
            lp.height =400;
            imageView.setLayoutParams(lp);
            imageView.setBackgroundResource(R.drawable.rounded_corner1);
            Log.i(TAG, "addMessageBox6: URL"+message);
            LoadImageFromUrl(ChatActivity.this,message,imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(ChatActivity.this);
                    dialog.setContentView(R.layout.dialog_with_imageview);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    ImageView image = (ImageView) dialog.findViewById(R.id.ImgView);
                    //image.setImageResource(R.drawable.ic_menu_gallery);
                    LoadImageFromUrl(ChatActivity.this,message,image);
                    dialog.show();
                }
            });
            layoutchat.addView(imageView);
            messageArea.setText("");
        }
        scrollView_chat.fullScroll(View.FOCUS_DOWN);
    }


    /**
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    private void LoadImageFromUrl(Context context, String imageUrl, ImageView imageView) {
        Picasso.with(context).load(imageUrl).error(R.drawable.ic_menu_gallery).into(imageView);
    }

    /**
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException se) {

        }
        //Edit the following as per you needs
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng placeLocation = new LatLng(latitude, longitude); //Make them global
        Marker placeMarker = googleMap.addMarker(new MarkerOptions().position(placeLocation)
                .title(Location));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent i = new Intent(ChatActivity.this,PairSuccessScreen.class);
//        startActivity(i);
//        CallOnGoingRideAPI();
    }




    /********************************START OF CallOnGoingRideAPI*******************************/
    /*
     * CallOnGoingRideAPI
     * */
    private void CallOnGoingRideAPI(){

        JsonObject object = OnGoingRideObject();
        AtroadsService service = ServiceFactory.createRetrofitService(ChatActivity.this, AtroadsService.class);
        mSubscription = service.OnGoingRidesResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OnGoingRidesResponseModel>() {
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
                    public void onNext(OnGoingRidesResponseModel mResponse) {
                        Log.i(TAG, "OnGoingRidesResponseModel: "+mResponse);
                        if(mResponse.getStatus() == 0)
                        {
                            //Toast.makeText(PairSuccessScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            Intent i = new Intent(ChatActivity.this,PairSuccessScreen.class);
                            i.putExtra("AutoNo",mResponse.getResult().get(0).getAutoNumber());
//                            i.putExtra("UserRideId",mResponse.getResult().get(0).get);
//                            i.putExtra("AutoNo",mResponse.getResult().get(0).getAutoNumber());
                            startActivity(i);
                        }
                    }
                });

    }

    /**
     * Json object of RidesHistoryObject
     *
     * @return
     */
    private JsonObject OnGoingRideObject()
    {
        OnGoingRideRequestModel requestModel = new OnGoingRideRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's an upload in progress, save the reference so you can query it later
        if (storageReference != null) {
            outState.putString("reference", storageReference.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // If there was an upload in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString("reference");
        if (stringRef == null) {
            return;
        }
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

        // Find all UploadTasks under this StorageReference (in this example, there should be one)
        List<UploadTask> tasks = storageReference.getActiveUploadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the upload
            UploadTask task = tasks.get(0);

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot state) {
                    // Success!
                    // ...
                }
            });
        }
    }




    /************************START OF GetPairedDetailsAPI******************************/
    /*
     * GetPairedDetailsAPI
     * */
    private void GetPairedDetailsForChatAPI()
    {
        JsonObject object = getPairedDetailsObject();
        AtroadsService service = ServiceFactory.createRetrofitService(ChatActivity.this, AtroadsService.class);
        mSubscription = service.PairedDetailsForChatResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PairedDetailsForChatResponseModel>() {
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
                    public void onNext(PairedDetailsForChatResponseModel mRespone) {
                        Log.i(TAG, "PairedDetailsResponseModel: "+mRespone);
                        if(mRespone.getStatus() == 1)
                        {
                            OtherUsername = mRespone.getResult().get(0).getName();
                            Log.i(TAG, "onNext:OtherUsername "+OtherUsername);
                            OtherProfilePic = mRespone.getResult().get(0).getProfilePic();
                            OtheruserId = mRespone.getResult().get(0).getUserId();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("OtherUserCreds", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("OtheruserId", OtheruserId);
                            editor.putString("OtherUsername",OtherUsername);
                            editor.putString("OtherProfilePic",OtherProfilePic);
                            editor.commit();


                        }
                        else {
                            //RideAlreadyInitiatedDialog(HomeMapsActivity.this,"",mRespone.getMessage(),"Ok");
                        }


                    }
                });

    }

    /**
     * Json object of getPairedDetailsObject
     * @return
     */
    private JsonObject getPairedDetailsObject()
    {
        PairedDetailsForChatRequestModel requestModel = new PairedDetailsForChatRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /************************END OF GET RIDE DETAILS API******************************/




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
                            Toast.makeText(ChatActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            Thisusername = mResponse.getResult().get(0).getName();
                            Log.i(TAG, "onNext: Thisusername:"+Thisusername);
                            GetPairedDetailsForChatAPI();
                            CallPlacesAPIClient();
                            SendBtnOnClick();
                            CheckLocationSharingFromActivityIntent();
                            FBReferenceChildEventListener();
                            storageReference = FirebaseStorage.getInstance().getReference();
                            mDatabase = FirebaseDatabase.getInstance().getReference(AtroadsConstant.DATABASE_PATH_UPLOADS);
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



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myStoragePermission();
                } else {

                    showSnackbar("Allow Permission","Settings", new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraIntent();
                } else {

                    showSnackbar("Allow Permission","Settings", new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }
        }
    }

    private void showSnackbar(String msg,String buttonName,Runnable runnable)
    {
        chatLinerarActivity = findViewById(R.id.chatLinerarActivity);
        Snackbar snackbar = Snackbar
                .make(chatLinerarActivity, "Message is deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        runnable.run();
                    }
                });

        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.chat_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // todo: goto back activity from here

                onBackPressed();
                break;

            case R.id.menu_call:
                CustomDialogWithOneBtn(ChatActivity.this,"Request for CALL","You will get call from a number to connect with paired user!!", "Call Request",new Runnable() {
                    @Override
                    public void run() {
                        showProgressDialog();
                        callingAPI();
                    }
                });
                //Toast.makeText(ChatActivity.this,"Call",Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void callingAPI(){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("PairedUserPref", 0); // 0 - for private mode
        UserRideId = pref.getInt("user_ride_id",0);

        JsonObject object = callingObject();
        AtroadsService service = ServiceFactory.createRetrofitService(ChatActivity.this, AtroadsService.class);
        mSubscription = service.callingApi(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ScheduleRideNotifyResponseModel>() {
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
                    public void onNext(ScheduleRideNotifyResponseModel mResponse) {
                        Log.i(TAG, "ScheduleRideNotifyResponseModel: "+mResponse);
//                        Toast.makeText(YourBillScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0) {
                            hideProgressDialog();
                            finish();
                        }
                        else if(mResponse.getStatus() == 1) {
                            hideProgressDialog();
                            Toast.makeText(ChatActivity.this,"We are connecting for you!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private JsonObject callingObject()
    {
        ScheduleRideNotifiyRequestModel requestModel = new ScheduleRideNotifiyRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setuser_ride_id(UserRideId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
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
}
