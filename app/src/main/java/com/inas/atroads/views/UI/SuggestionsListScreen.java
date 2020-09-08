package com.inas.atroads.views.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.sos.LocationUtils;
import com.inas.atroads.util.MyTouchListener;
import com.inas.atroads.views.Activities.PairActivity;
import com.inas.atroads.views.adapter.SuggestionsAdapter;
import com.inas.atroads.views.model.AcceptPairRequestModel;
import com.inas.atroads.views.model.AcceptPairResponseModel;
import com.inas.atroads.views.model.DeleteRequestModel;
import com.inas.atroads.views.model.DeleteRequestResponseModel;
import com.inas.atroads.views.model.GetRequestForOtherUserRequestModel;
import com.inas.atroads.views.model.GetRequestForOtherUserResponseModel;
import com.inas.atroads.views.model.GetRequestModel;
import com.inas.atroads.views.model.GetRequestResponseModel;
import com.inas.atroads.views.model.PairedUserDetailsRequestModel;
import com.inas.atroads.views.model.PairedUserDetailsResponseModel;
import com.inas.atroads.views.model.SendRequestModel;
import com.inas.atroads.views.model.SendRequestResponseModel;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.inas.atroads.views.Activities.PairActivity.suggestionsListModelList;

public class SuggestionsListScreen extends AppCompatActivity {
    private static final String TAG = "SuggestionsListScreen";
    private RecyclerView suggestionsRV;
    private SuggestionsAdapter suggestionsAdapter;
//  /  public List<SuggestionsListModel> suggestionsListModelList = new ArrayList<>();

    private Toolbar toolbar;
    private int UserId;
    private static final String DEFAULT = "N/A";
    private String Mobile,Email,Username;
    private Subscription mSubscription;
    int PStatus = -1;
    private Handler handler;
    private Handler otherHandler;
    SuggestionsAdapter.MyViewHolder SuggestionsHolder;
    private Integer user_ride_id,PairableUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions_list_screen);
        PairActivity pairActivity = new PairActivity();
//        suggestionsListModelList = pairActivity.getSuggestionsListModelList();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.Suggestions));
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
        SetRecyclerView();
        CallGetRequestAPI();
        GetRequestTimer();
    }


    private void SetRecyclerView()
    {
        suggestionsRV = findViewById(R.id.suggestionsRV);
        suggestionsAdapter = new SuggestionsAdapter(SuggestionsListScreen.this, suggestionsListModelList, new SuggestionsAdapter.MyAdapterListener() {
            @Override
            public void AcceptOnClick(View view, int position, int pairableUserID, SuggestionsAdapter.MyViewHolder holder) {
                try {
                    handler.removeCallbacks(mRunnable);
                    handler.removeCallbacksAndMessages(null);
                }catch (Exception e)
                {

                }
                PairableUserID = pairableUserID;
                CallSendRequestAPI(pairableUserID,holder);
                CallGetRequestForOtherAPI(pairableUserID);
                GetRequestForOtherTimer();
            }

            @Override
            public void getHolder(SuggestionsAdapter.MyViewHolder holder) {
                SuggestionsHolder = holder;
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        suggestionsRV.setLayoutManager(layoutManager);
        suggestionsRV.setAdapter(suggestionsAdapter);
        suggestionsRV.addOnItemTouchListener(new MyTouchListener(SuggestionsListScreen.this,
                suggestionsRV,
                new MyTouchListener.OnTouchActionListener() {
                    @Override
                    public void onLeftSwipe(View view, int position) {
//code as per your need
                    }

                    @Override
                    public void onRightSwipe(View view, int position) {
//code as per your need
                    }

                    @Override
                    public void onClick(View view, int position) {
//code as per your need
//                        Log.i(TAG, "onClick: "+position);
//                        int pairableUserId = suggestionsListModelList.get(0).getPairableUserID();
//                        String name = suggestionsListModelList.get(0).getName();
////                        CallAcceptPairAPI(pairableUserId);
//                        CallSendRequestAPI(pairableUserId);
                    }
                }));
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


    /**************************START OF CallSendRequestAPI*********************************/

    /*
     * CallSendRequestAPI
     * */
    private void CallSendRequestAPI(int PairId, SuggestionsAdapter.MyViewHolder holder){
        int PairableUserId = PairId;
        JsonObject object = SendRequestObject(PairId);
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.SendRequestResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SendRequestResponseModel>() {
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
                    public void onNext(SendRequestResponseModel mResponse) {
                        Toast.makeText(SuggestionsListScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        // false ==> already sent
                        //true ==> success

                        try {
                            handler.removeCallbacks(mRunnable);
                            handler.removeCallbacksAndMessages(null);
                        }catch (Exception e)
                        {

                        }

                        if(mResponse.getRstatus() == false)
                        {
                            holder.tvAcceptButton.setText("Requested");
                            PairedUserDetailsAPI();
//                            CallGetRequestForOtherAPI(PairId);
//                            GetRequestForOtherTimer();
                        }
                        else {
                            holder.tvAcceptButton.setText("Requested");
                            PairedUserDetailsAPI();
//                            CallGetRequestForOtherAPI(PairId);
//                            GetRequestForOtherTimer();
                        }
                    }
                });

    }

    /**
     * Json object of SendRequestObject
     *
     * @return
     */
    private JsonObject SendRequestObject(int PairId)
    {
        SendRequestModel requestModel = new SendRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setPairId(PairId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /**************************END OF CallSendRequestAPI*********************************/



    /**************************START OF CallGetRequestAPI*********************************/

    /*
     * CallGetRequestAPI
     * */
    private void CallGetRequestAPI()
    {
        JsonObject object = getRequestObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.GETRequestResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetRequestResponseModel>() {
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
                    public void onNext(GetRequestResponseModel mResponse) {
                        if(mResponse.getStatus() == 1)
                        {
                            if(mResponse.getResult().size()!=0)
                            {
                               if(mResponse.getResult().get(0).getAccept() == 0)
                               {
                                   try {
                                       handler.removeCallbacks(mRunnable);
                                       handler.removeCallbacksAndMessages(null);
                                   }catch (Exception e)
                                   {

                                   }
                                   if(SuggestionsHolder!=null)
                                   {
                                       SuggestionsHolder.tvAcceptButton.setText("Accept");
                                   }
                                   DialogWithTwoButtons(SuggestionsListScreen.this,"Requested", mResponse.getResult().get(0).getPair_user_name()+" has sent you the Request. Do u want to pair?", getString(R.string.Yes), new Runnable() {
                                       @Override
                                       public void run() {
                                          CallAcceptPairAPI(mResponse.getResult().get(0).getPairId());
                                       }
                                   }, getString(R.string.No), new Runnable() {
                                       @Override
                                       public void run() {
                                           CallDeleteRequestAPI(mResponse.getResult().get(0).getPairId());
                                       }
                                   });
//                                   Toast.makeText(SuggestionsListScreen.this, mResponse.getResult().get(0).getPairId()+" Has sent the request to you", Toast.LENGTH_SHORT).show();
                               }
                               else {

                               }
                            }

                        }
                        else {

                        }
                    }
                });

    }

    /**
     * Json object of getRequestObject
     *
     * @return
     */
    private JsonObject getRequestObject()
    {
        GetRequestModel requestModel = new GetRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /**************************END OF CallGetRequestAPI*********************************/


    /**************************START OF CallGetRequestForOtherAPI
     * @param pairableUserID*********************************/

    /*
     * CallGetRequestForOtherAPI
     * */
    private void CallGetRequestForOtherAPI(int pairableUserID)
    {
        JsonObject object = getRequestForOtherObject(pairableUserID);
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.GETRequestForOtherResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetRequestForOtherUserResponseModel>() {
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
                    public void onNext(GetRequestForOtherUserResponseModel mResponse) {
                        Log.i(TAG, "onNext: GetRequestForOtherUserResponseModel");
                        if(mResponse.getStatus() == 1)
                        {
                            if(mResponse.getResult().size()!=0)
                            {
                                if(mResponse.getResult().get(0).getAccept() == 1)
                                {
                                    try {
                                        otherHandler.removeCallbacks(mRunnable);
                                        otherHandler.removeCallbacksAndMessages(null);
                                    }catch (Exception e)
                                    {

                                    }
                                    PairedUserDetailsAPI();
//                                    if(SuggestionsHolder!=null)
//                                    {
//                                        SuggestionsHolder.tvAcceptButton.setText("Accept");
//                                    }
//                                    DialogWithTwoButtons(SuggestionsListScreen.this,"Requested", mResponse.getResult().get(0).getPairUserName()+" has sent you the Request. Do u want to pair?", getString(R.string.Yes), new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            CallAcceptPairAPI(mResponse.getResult().get(0).getPairId());
//                                        }
//                                    }, getString(R.string.No), new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            CallDeleteRequestAPI(mResponse.getResult().get(0).getPairId());
//                                        }
//                                    });
//                                   Toast.makeText(SuggestionsListScreen.this, mResponse.getResult().get(0).getPairId()+" Has sent the request to you", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                }
                            }

                        }
                        else {

                        }
                    }
                });

    }

    /**
     * Json object of getRequestForOtherObject
     *
     * @return
     * @param pairableUserID
     */
    private JsonObject getRequestForOtherObject(int pairableUserID)
    {
        GetRequestForOtherUserRequestModel requestModel = new GetRequestForOtherUserRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setPairId(pairableUserID);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /**************************END OF getRequestForOtherObject*********************************/

    /**************************START OF GetLogsTimer()*********************************/
    private void GetRequestTimer()
    {
        handler = new Handler();
        handler.postDelayed(mRunnable, 5000);
    }

    private Runnable mRunnable = new Runnable() {

        @SuppressLint("LongLogTag")
        @Override
        public void run() {
            Log.e("GetRequestTimer Handlers", "Calls");
            CallGetRequestAPI();
            handler.postDelayed(mRunnable, 5000);
        }
    };
    /**************************END OF GetNotificationsTimer()*********************************/


    /**************************START OF GetRequestForOtherTimer()*********************************/
    private void GetRequestForOtherTimer()
    {
        otherHandler = new Handler();
        otherHandler.postDelayed(OthermRunnable, 5000);
    }

    private Runnable OthermRunnable = new Runnable() {

        @SuppressLint("LongLogTag")
        @Override
        public void run() {
            Log.e("GetRequestForOtherTimer Handlers", "Calls");
            CallGetRequestForOtherAPI(PairableUserID);
            Log.i(TAG, "run: pairableUserID"+PairableUserID);
            otherHandler.postDelayed(OthermRunnable, 5000);
        }
    };
    /**************************END OF GetRequestForOtherTimer()*********************************/



    /**************************START OF CallAcceptPairAPI*********************************/

    /*
     * CallAcceptPairAPI
     * */
    private void CallAcceptPairAPI(int PairId){
        int PairableUserId = PairId;
        JsonObject object = AcceptRequestObject(PairId);
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.AcceptFindPairResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AcceptPairResponseModel>() {
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
                    public void onNext(AcceptPairResponseModel mResponse) {
                        if(mResponse.getStatus() == 1)
                        {
                            int UserRideId = mResponse.getResult().get(0).getUserRideId();
                            int rideInitiaterId = mResponse.getResult().get(0).getRideInitiaterId().getRideInitiaterId();
                            for (int i = 0; i < mResponse.getResult().get(0).getPairedUser().size(); i++)
                            {
                                int userId = mResponse.getResult().get(0).getPairedUser().get(i).getUserId();
                                String Attention = mResponse.getResult().get(0).getPairedUser().get(i).getAttention();
                                int pstatus = mResponse.getResult().get(0).getPairedUser().get(i).getPstatus();
                                String name  = mResponse.getResult().get(0).getPairedUser().get(i).getName();
                                String srcAddress = mResponse.getResult().get(0).getPairedUser().get(i).getUserSourceAddress();
                                String destAddress = mResponse.getResult().get(0).getPairedUser().get(i).getUserDestAddress();
                                String acceptingOtherGender = mResponse.getResult().get(0).getPairedUser().get(i).getAcceptingOtherGender();
                                String gender = mResponse.getResult().get(0).getPairedUser().get(i).getGender();
                                CheckRideInitializer(rideInitiaterId,UserId,PairableUserId,mResponse);
                            }

                        }
                        else {

                        }
                    }
                });

    }

    /**
     * Json object of AcceptRequestObject
     *
     * @return
     */
    private JsonObject AcceptRequestObject(int PairId)
    {
        AcceptPairRequestModel requestModel = new AcceptPairRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setPairId(PairId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /**************************END OF CallAcceptPairAPI*********************************/

    /**************************START OF CustomDialog*********************************/
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
    public static void CustomDialog(Context context, String Title, String Msg, String buttonNam1, String buttonNam2, Runnable runnable, Runnable runnable1)
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





    /**
     *
     * @param ride_initiater_id
     * @param userOne
     * @param userTwo
     * @param mResponse
     */
    private void CheckRideInitializer(int ride_initiater_id, int userOne, int userTwo, AcceptPairResponseModel mResponse)
    {
        String Attention = null;
        String SourceAddress,DestAddress;
        int user_ride_id = 0;


        if(UserId == userOne)
        {
            user_ride_id = mResponse.getResult().get(0).getUserRideId();
            Attention = mResponse.getResult().get(0).getPairedUser().get(0).getAttention();
            PStatus = mResponse.getResult().get(0).getPairedUser().get(0).getPstatus();
            SourceAddress = mResponse.getResult().get(0).getPairedUser().get(0).getUserSourceAddress();
            DestAddress = mResponse.getResult().get(0).getPairedUser().get(0).getUserDestAddress();
            ShowDialogAccordingToPStatus(PStatus,Attention,"Source: "+SourceAddress,"Dest: "+DestAddress,user_ride_id);
        }
        else if(UserId == userTwo) {
            user_ride_id = mResponse.getResult().get(0).getUserRideId();
            Attention = mResponse.getResult().get(0).getPairedUser().get(1).getAttention();
            PStatus = mResponse.getResult().get(0).getPairedUser().get(1).getPstatus();
            SourceAddress = mResponse.getResult().get(0).getPairedUser().get(1).getUserSourceAddress();
            DestAddress = mResponse.getResult().get(0).getPairedUser().get(1).getUserDestAddress();
            ShowDialogAccordingToPStatus(PStatus,Attention,"Source: "+SourceAddress,"Dest: "+DestAddress,user_ride_id);
        }

    }

    /**
     * @param PStatus
     * @param Attention
     * @param Source
     * @param Dest
     * @param user_ride_id
     */
    private void ShowDialogAccordingToPStatus(int PStatus, String Attention, String Source, String Dest, int user_ride_id)
    {
        /*0- you are at farthest distance
    1- other user has to initiate ride
    2- other person in farthest distance
    3-you have to initiate
     */
        if(PStatus == 0)
        {
            Intent intent = new Intent(SuggestionsListScreen.this, PairedDetailsScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user_ride_id",user_ride_id);
            intent.putExtra("Message",Attention+ "\n"+"\n"+ Source+ "\n"+"\n"+ Dest);
            intent.putExtra("PStatus",PStatus);
            startActivity(intent);
            finish();
        }
        else if(PStatus == 1)
        {
            Intent intent = new Intent(SuggestionsListScreen.this, PairedDetailsScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user_ride_id",user_ride_id);
//            intent.putExtra("Message",Attention);
            intent.putExtra("Message",Attention+ "\n"+"\n"+ Source+ "\n"+"\n"+ Dest);
            intent.putExtra("PStatus",PStatus);
            startActivity(intent);
            finish();

        }
        else if(PStatus == 2)
        {
            Intent intent = new Intent(SuggestionsListScreen.this, PairedDetailsScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user_ride_id",user_ride_id);
//            intent.putExtra("Message",Attention);
            intent.putExtra("Message",Attention+ "\n"+"\n"+ Source+ "\n"+"\n"+ Dest);
            intent.putExtra("PStatus",PStatus);
            startActivity(intent);
            finish();
        }
        else if(PStatus == 3)
        {
            Intent intent = new Intent(SuggestionsListScreen.this, PairedDetailsScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user_ride_id",user_ride_id);
//            intent.putExtra("Message",Attention);
            intent.putExtra("Message",Attention+ "\n"+"\n"+ Source+ "\n"+"\n"+ Dest);
            intent.putExtra("PStatus",PStatus);
            startActivity(intent);
            finish();
        }
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





    /**************************START OF CallDeleteRequestAPI*********************************/

    /*
     * CallDeleteRequestAPI
     * */
    private void CallDeleteRequestAPI(int PairId)
    {
        JsonObject object = deleteRequestObject(PairId);
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.DeleteRequestResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeleteRequestResponseModel>() {
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
                    public void onNext(DeleteRequestResponseModel mResponse) {
                        if(mResponse.getStatus() == 1)
                        {
                            Toast.makeText(SuggestionsListScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SuggestionsListScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /**
     * Json object of deleteRequestObject
     *
     * @return
     */
    private JsonObject deleteRequestObject(int pairId)
    {
        DeleteRequestModel requestModel = new DeleteRequestModel();
        requestModel.setUserId(UserId);
        requestModel.setPairId(pairId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /**************************END OF CallGetRequestAPI*********************************/



    /**************************START OF PairedUserDetails API*********************************/
    /*
     * PairedUserDetailsAPI
     * */
    public void PairedUserDetailsAPI() {

        JsonObject object = PairedUserDetailsObject();
        AtroadsService service = ServiceFactory.createRetrofitService(this, AtroadsService.class);
        mSubscription = service.PairedUserDetailsResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PairedUserDetailsResponseModel>() {
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
                    public void onNext(PairedUserDetailsResponseModel mResponse) {
                        Log.i(TAG, "PairedUserDetailsResponseModel: "+mResponse);
                        Toast.makeText(SuggestionsListScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 1)
                        {
                            user_ride_id = mResponse.getResult().get(0).getUser_ride_id();
                            Log.i(TAG, "PairedUserDetailsResponseModel: "+user_ride_id);
                            String Attention = mResponse.getResult().get(0).getAttention();
                            int PStatus = mResponse.getResult().get(0).getPstatus();
                            String SourceAddress = mResponse.getResult().get(0).getUserSourceAddress();
                            String DestAddress = mResponse.getResult().get(0).getUserDestAddress();
                            ShowDialogAccordingToPStatus(PStatus,Attention,"Source: "+SourceAddress,"Dest: "+DestAddress,user_ride_id);
                            /**
                             * saving Paired User one details
                             */
                            SharedPreferences pref1 = getApplicationContext().getSharedPreferences("PairedUserPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref1.edit();
                            editor.putInt("user_ride_id", user_ride_id);
                            editor.commit();
                        }
                        else {

                        }

                        //   startRideBtn.setText("End Ride");
                    }
                });

    }

    /**
     * Json object of PairedUserDetailsObject
     * @return
     */
    private JsonObject PairedUserDetailsObject()
    {
        PairedUserDetailsRequestModel requestModel = new PairedUserDetailsRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /**************************END OF PairedUserDetailsAPI*********************************/



}
