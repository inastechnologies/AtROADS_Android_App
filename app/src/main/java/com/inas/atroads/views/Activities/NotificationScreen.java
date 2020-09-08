package com.inas.atroads.views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.views.adapter.NotificationAdapter;
import com.inas.atroads.views.model.NotificationRequestModel;
import com.inas.atroads.views.model.NotificationResponseModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotificationScreen extends AppCompatActivity {
    private static final String TAG = "NotificationScreen";
    public List<NotificationResponseModel> notificationResponseModelList = new ArrayList<>();
    private RecyclerView NotificationRv;
    private NotificationAdapter notificationAdapter;
    public Subscription mSubscription;
    private int UserId;
    private String Username,Email,Mobile;
    private static final String DEFAULT = "N/A";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_screen);
        GetSharedPrefs();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.notification));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationScreen.this, HomeMapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


    }


    /**********************************START OF SHARED PREFERENCES**************/

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
//
//        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
        UserId = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Email =  pref.getString("email_id",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
        getNotificationListAPI();
    }

    /**********************************END OF SHARED PREFERENCES**************/




    private void getNotificationListAPI() {

        JsonObject object = NotificationListObject();
        AtroadsService service = ServiceFactory.createRetrofitService(getApplicationContext(), AtroadsService.class);
        mSubscription = service.getNotificationListResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NotificationResponseModel>() {
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
                    public void onNext(NotificationResponseModel mResponse) {
                        NotificationRv = findViewById(R.id.NotificationRv);
                        notificationAdapter = new NotificationAdapter(NotificationScreen.this, mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
                        NotificationRv.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        NotificationRv.setItemAnimator(new DefaultItemAnimator());
                        NotificationRv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                        NotificationRv.setAdapter(notificationAdapter);
                    }

                });
    }



    /**
     * Json Object of blockListObject
     *
     * @return
     */
    private JsonObject NotificationListObject() {
        NotificationRequestModel mRequest = new NotificationRequestModel();
        mRequest.setUserId(UserId);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NotificationScreen.this, HomeMapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
