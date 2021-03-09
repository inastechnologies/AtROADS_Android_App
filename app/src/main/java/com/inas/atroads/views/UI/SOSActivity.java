package com.inas.atroads.views.UI;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.sos.LocationUtils;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.model.HelpRequestModel;
import com.inas.atroads.views.model.HelpResponseModel;
import com.inas.atroads.views.model.Req_AddEmergencyContact;
import com.inas.atroads.views.model.Req_GetEmergencyContacts;
import com.inas.atroads.views.model.Res_AddEmergencyContact;
import com.inas.atroads.views.model.Res_GetEmergencyContacts;
import com.inas.atroads.views.model.SMSRequestModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class SOSActivity extends BaseActivity
{
    private static final String TAG = SOSActivity.class.getSimpleName();
    LinearLayout cl_add_contacts;
    Button btn_sos, btn_add_contacts,btn_view_contacts, btn_add,add_contact;
    TextView edt_name, edt_mobile;
    EditText edt_email;
    String str_name = "", str_mobile = "", str_email = "";
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    List<Res_GetEmergencyContacts.Sos> contactsList = new ArrayList<Res_GetEmergencyContacts.Sos>();
    //String myLocation = "";
    RecyclerView rv_contacts;
    private final int REQUEST_CODE=99;
    String Username,Email,Mobile;
    int UserId;
     MediaPlayer mp;
    private static final String DEFAULT = "N/A";
    private Subscription mSubscription;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private Toolbar toolbar;
    public  static final int RequestPermissionCode  = 1 ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_o_s);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.SOS));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mp = MediaPlayer.create(SOSActivity.this, R.raw.sos_sound);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp.stop();
                onBackPressed();
            }
        });
        isNetworkAvailable(SOSActivity.this);
        //EnableRuntimePermission();
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else
        {
            //String myLocation = LocationUtils.getMyLocation(SOSActivity.this);
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;


        setViewsFromLayout();

    }



    /**********************************START OF SHARED PREFERENCES**************/

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

    /**********************************END OF SHARED PREFERENCES**************/



    // The request code used in ActivityCompat.requestPermissions()
    // and returned in the Activity's onRequestPermissionsResult()

    /**
     Description : Check the permissions
     @param context
     @param permissions
     @return
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     Description : Setting the vies from layout
     */
    private void setViewsFromLayout()
    {
        cl_add_contacts = findViewById(R.id.cl_add_contacts_sos);
        btn_sos = findViewById(R.id.btn_sos);
        btn_add_contacts = findViewById(R.id.btn_add_contacts_sos);
        btn_view_contacts = findViewById(R.id.btn_view_contacts_sos);
        btn_add = findViewById(R.id.btn_add_sos);
        edt_name = findViewById(R.id.txt_name_sos);
        edt_mobile = findViewById(R.id.txt_mobile_sos);
        edt_email = findViewById(R.id.txt_email_sos);
       // add_contact= findViewById(R.id.add_contact);
        GetSharedPrefs();
        getEmergencyContacts();

        edt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        setOnClickListenerForSOSBtn();
        setOnClickListenerForAddContactsBtn();
        setOnclickListenerForAddBtn();
        setOnClickListenerForViewContacts();
    }

    /**
     Description : Set OnClickListener for Add Button
     */
    private void setOnclickListenerForAddBtn()
    {
        btn_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                str_name = edt_name.getText().toString();
                str_mobile = edt_mobile.getText().toString();
                str_email = edt_email.getText().toString();

                if(str_name.equals(""))
                {
                    Toast.makeText(SOSActivity.this, getResources().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
                }
                else if(str_mobile.equals(""))
                {
                    Toast.makeText(SOSActivity.this, getResources().getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();
                }else if(!str_email.equals("")){
                    if(!isValidEmail(str_email)) {
                        Toast.makeText(SOSActivity.this, "Enter valid Email-id", Toast.LENGTH_SHORT).show();
                    }else{
                        if(contactsList.size() < 3)
                        {
                            addEmergencyContacts();
                        }
                        else
                        {
                            showDialogForLimit();
                        }
                    }
                } else
                {

                    if(contactsList.size() < 3)
                    {
                        addEmergencyContacts();
                    }
                    else
                    {
                        showDialogForLimit();
                    }
                }
            }
        });
    }

    /**
     Description : Add Emergency Contacts
     */
    private void addEmergencyContacts()
    {
        Req_AddEmergencyContact req_addEmergencyContact = getRequestForAddEmergencyContacts();

        AtroadsService service = ServiceFactory.createRetrofitService(this,AtroadsService.class);
        service.addEmergencyContact(req_addEmergencyContact)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Res_AddEmergencyContact>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.v(TAG, "e.getLocalizedMessage() : " + e.getLocalizedMessage());
                        Toast.makeText(SOSActivity.this,"Server Down:" + e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
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
                    public void onNext(Res_AddEmergencyContact res_AddEmergencyContact)
                    {
                        if(res_AddEmergencyContact.getStatus() == 1)
                        {
                            String message = res_AddEmergencyContact.getMessage();
                            if(message != null)
                            {
                                Toast.makeText(SOSActivity.this, message, Toast.LENGTH_SHORT).show();
                                setContactValues();
                            }
                            else
                            {
                                Log.v(TAG, "Message null");
                            }
                            getEmergencyContacts();
                        }
                        else
                        {
                            Log.v(TAG, "Response null");
                        }

                    }
                });


    }

    /**
     Description : Set Name, Mobile, Email values to empty after contact added
     */
    private void setContactValues()
    {
        edt_name.setText("");
        edt_mobile.setText("");
        edt_email.setText("");
    }

    private Req_AddEmergencyContact getRequestForAddEmergencyContacts()
    {
        String name = edt_name.getText().toString();
        String mobile = edt_mobile.getText().toString();
        String email;
        if(edt_email.getText().toString().equals("") || edt_email.getText().toString().isEmpty()){
            email="";
        }else{
             email = edt_email.getText().toString();
        }

        int userId = 10;

        Req_AddEmergencyContact req_addEmergencyContact = new Req_AddEmergencyContact(name,mobile,email,UserId);
        return req_addEmergencyContact;
    }

    /**
     Description : Add Emergency Contacts
     */
    private void getEmergencyContacts()
    {
        int userId = 10;
        Req_GetEmergencyContacts req_getEmergencyContact = new Req_GetEmergencyContacts(UserId);

        AtroadsService service = ServiceFactory.createRetrofitService(this,AtroadsService.class);
        service.getEmergencyContacts(req_getEmergencyContact)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Res_GetEmergencyContacts>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.v(TAG, "e.getLocalizedMessage() : " + e.getLocalizedMessage());
                        Toast.makeText(SOSActivity.this,"Server Down:" + e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
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
                    public void onNext(Res_GetEmergencyContacts res_getEmergencyContact)
                    {
                        if(res_getEmergencyContact.getStatus() == 1)
                        {
                            List<Res_GetEmergencyContacts.Result> resultList = res_getEmergencyContact.getResult();
//                            if(resultList.get(0).getSos().size() == 0)
//                            {
//                                btn_add_contacts.setVisibility(View.VISIBLE);
//                                btn_view_contacts.setVisibility(View.GONE);
//                            }
//                            else {
//                                btn_add_contacts.setVisibility(View.GONE);
//                                btn_view_contacts.setVisibility(View.VISIBLE);
//                            }
                            if(resultList.get(0) != null && resultList.size() > 0)
                            {
                                contactsList = resultList.get(0).getSos();

                            }
                            else
                            {
                                Log.v(TAG, "Result null");
                            }

                        }
                        else
                        {
                            Log.v(TAG, "Response null");
                        }

                    }
                });

    }

    /**
     Description : Show dialog for Limit Exceeded
     */
    private void showDialogForLimit()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SOSActivity.this);
        builder.setTitle(getResources().getString(R.string.alert_title_limit));
        builder.setMessage(getResources().getString(R.string.alert_msg_limit));

        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface,int i)
            {

                dialogInterface.dismiss();

            }
        });


        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    /**
     Description : Set OnClickListener For SOS Button
     */
    private void setOnClickListenerForAddContactsBtn()
    {
        btn_add_contacts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                cl_add_contacts.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     Description : Set OnClickListener For SOS Button
     */
    private void setOnClickListenerForSOSBtn()
    {
        btn_sos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogWithTwoButtons(SOSActivity.this,"Notify My Contacts", getString(R.string.AreYouSureSOS), getString(R.string.Yes), new Runnable() {
                    @Override
                    public void run() {
                        String myLocation = "";
                        if(contactsList != null && contactsList.size() > 0)
                        {
                            mp.start();
                            myLocation = LocationUtils.getMyLocation(SOSActivity.this);
                            String contact_no = contactsList.get(0).getMobileNumber();
                            String sendLocation=  getResources().getString(R.string.sos_msg).concat(" ").concat("My Location is : ").concat(myLocation) +"\n and contact me on this number-"+Mobile;
                            CallHelpAPI(sendLocation,contact_no);

                            if(contact_no != null)
                            {
                                if(isValidPhone(contact_no))
                                {

                                    callToEmergencyContact(contact_no);
                                }
                                else
                                {
                                    Log.v(TAG,"Invalid Contact No");
                                }
                            }
                            else
                            {
                                Toast.makeText(SOSActivity.this,"Contact No is null",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                           // final MediaPlayer mp = MediaPlayer.create(SOSActivity.this, R.raw.sos_sound);
                            //mp.start();
                            Toast.makeText(SOSActivity.this,"Please add contact.",Toast.LENGTH_SHORT).show();
                            btn_add_contacts.setVisibility(View.VISIBLE);
                        }
                    }
                }, getString(R.string.No), new Runnable() {
                    @Override
                    public void run() {

                    }
                });


            }
        });
    }

    /**
     *
     * @param context
     * @param Title
     * @param Msg
     * @param ButtonName
     * @param runnable
     * @param secondButtonName
     * @param secondRunnable
     */
    public static void DialogWithTwoButtons(Context context, String Title, String Msg, String ButtonName, final Runnable runnable,String secondButtonName, final Runnable secondRunnable)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_with_2buttons);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        TextView title = (TextView) dialog.findViewById(R.id.TimeTv);
        title.setText(Title);
        if(title.getText().toString().equals(""))
        {
            title.setBackgroundColor(Color.WHITE);
        }
        TextView msg = (TextView) dialog.findViewById(R.id.DescTv);
        msg.setText(Msg);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        okBtn.setText(ButtonName);
        // if ok button is clicked, close the custom dialog
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                runnable.run();

            }
        });

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setText(secondButtonName);
        // if decline button is clicked, close the custom dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                secondRunnable.run();
            }
        });
    }


    private final SensorEventListener mSensorListener = new SensorEventListener()
    {
        @Override
        public void onSensorChanged(SensorEvent event)
        {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Shake event detected");

                if(contactsList != null && contactsList.size() > 0)
                {
                    showDialog();
                }
                else
                {
                    Toast.makeText(SOSActivity.this, "No Contacts", Toast.LENGTH_SHORT).show();
                }


            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    /**
     Description : Send SMS@param myLocation

     */
    private void sendSMS(String myLocation)
    {
        Log.v(TAG, "Location = " + myLocation);

        if(contactsList != null)
        {
            for(int i = 0;i < contactsList.size();i++)
            {
                String tempMobileNumber = contactsList.get(i).getMobileNumber();

                try
                {
                    Log.v(TAG,"tempMobileNumber = " + contactsList.get(i).getMobileNumber());
                    String sos_msg = getResources().getString(R.string.sos_msg).concat(" ").concat("My Location is : ").concat(myLocation) +"\n and contact me on this number-"+Mobile;
                    MultipleSMS(tempMobileNumber,sos_msg);
                    Thread.sleep(1000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Log.v(TAG, "e--"+e.toString());
                }
            }
        }
        else
        {
            Log.v(TAG, "Contacts list is empty");
        }


    }

    /**
     Description : Show Dialog along with Practitioner contact no
     */
    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SOSActivity.this);
        builder.setTitle(getResources().getString(R.string.alert_title));
        builder.setMessage(getResources().getString(R.string.alert_msg));

        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface,int i)
            {

                String myLocation = LocationUtils.getMyLocation(SOSActivity.this);;

                mp.start();
                String contact_no = contactsList.get(0).getMobileNumber();//"8142327425";
                String sendLocation=  getResources().getString(R.string.sos_msg).concat(" ").concat("My Location is : ").concat(myLocation) +"\n and contact me on this number-"+Mobile;
                CallHelpAPI(sendLocation,contact_no);

                if(contact_no != null)
                {
                    if(isValidPhone(contact_no)) {
                        callToEmergencyContact(contact_no);
                    }
                    else
                    {
                        Log.v(TAG,"Invalid Contact No");
                    }
                }
                else
                {
                    Toast.makeText(SOSActivity.this, "Contact No is null", Toast.LENGTH_SHORT).show();
                }
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("No",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface,int i)
            {
                dialogInterface.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    /**
     Description : Send Email to multiple address@param myLocation

     */
    private void sendEmail(String myLocation)
    {
        List<String> emailsList = new ArrayList<String>();
        String email = "";
        for(int i=0; i<contactsList.size(); i++)
        {
            email = contactsList.get(i).getEmailId();
            emailsList.add(email);
        }
        String[] stringArray = emailsList.toArray(new String[0]);
        Log.v(TAG, "stringArray = " + stringArray);
        String msg = "My Location : ".concat(myLocation);
        Intent it = new Intent(Intent.ACTION_SEND);
        it.setData(Uri.parse("mailto:"));
        it.putExtra(Intent.EXTRA_EMAIL, stringArray);
        it.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.sos_msg));
        it.putExtra(Intent.EXTRA_TEXT,msg);
        it.setType("message/rfc822");
        startActivity(Intent.createChooser(it,"Send mail..."));
    }

    /**
     Description : Call intent to call the Practitioner@param dialog
     @param contact_no
     */

    private void callToEmergencyContact(String contact_no)
    {
        Intent call_intent = new Intent(Intent.ACTION_DIAL);
        call_intent.setData(Uri.parse("tel:" + contact_no));
        startActivity(call_intent);
        //dialog.dismiss();
    }



    /**
     Description : Set OnClickListener for View Contacts button
     */
    private void setOnClickListenerForViewContacts()
    {
        btn_view_contacts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(contactsList != null & contactsList.size() > 0)
                {
                    Intent intent = new Intent(SOSActivity.this,ViewEmergencyContacts.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(SOSActivity.this,getResources().getString(R.string.no_contacts),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     Description : Validation for mobile no
     @param contact_no
     @return
     */
    private boolean isValidPhone(String contact_no)
    {
        if (contact_no.length() != 10)
        {
            return false;
        }
        else
        {
            return android.util.Patterns.PHONE.matcher(contact_no).matches();
        }

    }



    /**
     Description : Send SMS to Multiple Numbers
     @param phoneNumber
     @param message
     */
    private void MultipleSMS(String phoneNumber,final String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this,0,new Intent(
                SENT),0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        //ContentValues values = new ContentValues();
						/*for (int i = 0; i < mobileNumbersList.size() - 1; i++) {
							values.put("address", mobileNumbersList.get(i).toString());
							// txtPhoneNo.getText().toString());
							//values.put("body", MessageText.getText().toString());
							values.put("body", message);
						}
						getContentResolver().insert(
							Uri.parse("content://sms/sent"), values);*/
                        Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                        Log.v(TAG, "SMS sent");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(),"Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        //Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                        Log.v(TAG,"SMS delivered");
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null,message, sentPI, deliveredPI);
    }




    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setOnClickListenerForSOSBtn();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                   // Toast.makeText(this, "This App needs to Location Access", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp!=null) {
            mp.stop();
        }
    }


    @Override public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query
                            (contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex
                                (ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex
                                (ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        String name = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query
                                    (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex
                                        (ContactsContract.CommonDataKinds.Phone.NUMBER));
                                name = numbers.getString(numbers.getColumnIndex
                                        (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                                num = num.replace("+91", "");
                                edt_name.setText(""+name);
                                edt_mobile.setText(""+num);
                            }
                        }
                    }
                    break;
                }
        }
    }



    /*
     * CallGetUserInfoAPI
     * */
    private void CallHelpAPI(String myLocation, String contact_no){

        JsonObject object = SmsObject(myLocation,contact_no);
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.SMSResponse(object)
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
                            Toast.makeText(SOSActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1) {
                            Toast.makeText(SOSActivity.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });

    }


    private JsonObject SmsObject(String myLocation, String contact_no)
    {
        SMSRequestModel requestModel = new SMSRequestModel();
        requestModel.setmessage(myLocation);
        requestModel.setmobilenumber(contact_no);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
