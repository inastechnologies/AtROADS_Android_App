package com.inas.atroads.views.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.inas.atroads.R;
import com.inas.atroads.services.MyServiceToCheckIsAppClosed;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.Activities.StartUpActivity;

import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class SpalshScreen extends AppCompatActivity
{
    private String DEFAULT = "N/A";
    private  int user_id;
    private String TAG = SpalshScreen.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);
        isNetworkAvailable(SpalshScreen.this);

       // ZohoSalesIQ.init(getApplication(), "eRqJXR4b8ZkjQaUexhVYWUp6MndKADaGE8IA0L0udBo%3D_in", "G03VQIyK3OM6NoUCuxuXCN04u2acsB6OT0MnFvNgoSOWaYLA0RQenYGWwecr4vfHvBGTvMul%2BDK3YJIUrs4wnXEAyE3rd35WHUnFt4bKyePfFwuXxpEWHwVx099XyoM3ie1aOOkN%2BipUQ6epHLtDpbPSZK%2FbHDVv" );

        //ChatManager mChatManager = AGApplication.the().getChatManager();
        //mRtmClient = mChatManager.getRtmClient();
        MoveToRegister();
    }
    /*
      This method is used to move from Splash screen to Register screen
    */
    private void MoveToRegister() {
        Log.i(TAG, "onCreate: Start Of MoveToRegister");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0);
                user_id = pref.getInt("user_id", 0);
                Log.i("user_id:", String.valueOf(user_id));
                if(user_id == 0) {

                    boolean landFlag = pref.getBoolean("isStartUp",false);

                    if(landFlag) {
                        Intent i = new Intent(SpalshScreen.this, MobileNumberRegisterScreen.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(SpalshScreen.this, StartUpActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent i = new Intent(SpalshScreen.this, HomeMapsActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        },1000);
        Log.i(TAG, "onCreate: End Of MoveToRegister");
    }



    public void UpdateApp(){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(result -> {

            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
//                requestUpdate(result);
                android.view.ContextThemeWrapper ctw = new android.view.ContextThemeWrapper(this,R.style.Widget_AppCompat_Button_ButtonBar_AlertDialog);
                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctw);
                alertDialogBuilder.setTitle("Update NTT Netmagic");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setIcon(R.drawable.atroads_logo);
                alertDialogBuilder.setMessage("Fitness Trainer recommends that you update to the latest version for a seamless & enhanced performance of the app.");
                alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try{
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id="+getPackageName())));
                        }
                        catch (ActivityNotFoundException e){
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("No Thanks",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                alertDialogBuilder.show();

            } else {

            }
        });
    }


}
