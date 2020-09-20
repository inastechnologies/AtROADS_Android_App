package com.inas.atroads.views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.util.AtroadsConstant;
import com.inas.atroads.views.adapter.MyListAdapter;
import com.inas.atroads.views.model.PairedDetailsForChatRequestModel;
import com.inas.atroads.views.model.PairedDetailsForChatResponseModel;
import com.inas.atroads.views.model.PairedDetailsRequestModel;
import com.inas.atroads.views.model.PairedDetailsResponseModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UsersActivity extends AppCompatActivity {

    private static final String TAG = "UsersActivity";
    RecyclerView usersList;
    TextView noUsersText;
    LinearLayout lin_nodata;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al_pic = new ArrayList<>();
    ArrayList<Integer> al_userId = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    private Subscription mSubscription;
    String Username,Email,Mobile,OtherUsername,OtherProfilePic;
    int UserId,OtheruserId;
    private String DEFAULT = "N/A";
    private String MobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        GetSharedPrefs();
        usersList = (RecyclerView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        lin_nodata= findViewById(R.id.lin_nodata);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.PairedUser));
        toolbar.setTitleMargin(0,0,0,0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        pd = new ProgressDialog(UsersActivity.this);
//        pd.setMessage("Loading...");
//        pd.show();
//
//        String url = "https://atroads-d26a5.firebaseio.com/users.json";
//
//        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
//            @Override
//            public void onResponse(String s) {
//                doOnSuccess(s);
//            }
//        },new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                System.out.println("" + volleyError);
//            }
//        });
//
//        RequestQueue rQueue = Volley.newRequestQueue(UsersActivity.this);
//        rQueue.add(request);
//
//        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                UserDetails.chatWith = al.get(position);
//                //startActivity(new Intent(UsersActivity.this, ChatActivity.class));
//                Intent i = new Intent(UsersActivity.this, ChatActivity.class);
//                i.putExtra("FROMACTIVITY","UsersActivity");
//                startActivity(i);
//            }
//        });
    }

    public void doOnSuccess(String s, String mobileNumber, String otherUsername, String otherProfilePic, int otheruserId){
        try {
            JSONObject obj = new JSONObject(s);
            JSONArray array = obj.names();

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

//                if(!key.equals(UserDetails.username)) {
////                    if(key.equals(OtherUsername))
////                    {
////                        al.add(key);
////                    }
//                    al.add(key);
//                }
                if(key.equals(mobileNumber))
                {
                    al.add(otherUsername);
                    al_pic.add(otherProfilePic);
                    al_userId.add(otheruserId);
                    String[] name = {};

                }
                Log.i(TAG, "doOnSuccess: number"+al);

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "doOnSuccess: totalUsers:"+totalUsers);

        if(totalUsers <=1){
            lin_nodata.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            lin_nodata.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);

            String[] name ={
                    "Title 1","Title 2"
            };

            String[] images ={
                    "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
                    "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg"
            };

//            HashMap<String,String> map = new HashMap<>();
//            map.clear();
//            map.put(OtherUsername,OtherProfilePic);
            MyListAdapter adapter = new MyListAdapter(this,al, al_pic,al_userId);
            usersList.setHasFixedSize(true);
            usersList.setLayoutManager(new LinearLayoutManager(this));
            usersList.setAdapter(adapter);
            //MyListAdapter adapter=new MyListAdapter(this, al,al_pic);
            //usersList.setAdapter(adapter);
           // usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al,al_pic));
        }

        pd.dismiss();
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
//        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("RegPref", 0); // 0 - for private mode
        UserId = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Email =  pref.getString("email_id",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
        GetPairedDetailsForChatAPI();
    }

    /**********************************END OF SHARED PREFERENCES**************/



    /************************START OF GetPairedDetailsAPI******************************/
    /*
     * GetPairedDetailsAPI
     * */
    private void GetPairedDetailsForChatAPI()
    {
        JsonObject object = getPairedDetailsObject();
        AtroadsService service = ServiceFactory.createRetrofitService(UsersActivity.this, AtroadsService.class);
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
                        if(mRespone.getStatus() == 1) {

                            if (mRespone.getResult().size() > 0) {
                                lin_nodata.setVisibility(View.GONE);
                                usersList.setVisibility(View.VISIBLE);
                                OtherUsername = mRespone.getResult().get(0).getName();
                                Log.i(TAG, "onNext:OtherUsername " + OtherUsername);
                                OtherProfilePic = mRespone.getResult().get(0).getProfilePic();
                                OtheruserId = mRespone.getResult().get(0).getUserId();
                                MobileNumber = mRespone.getResult().get(0).getMobile_number();

                                SharedPreferences pref = getApplicationContext().getSharedPreferences("OtherCreds", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("OtheruserId", OtheruserId);
                                editor.apply();
                                editor.commit();
                                pd = new ProgressDialog(UsersActivity.this);
                                pd.setMessage("Loading...");
                                pd.show();

                                //String url = "https://atroads-d26a5.firebaseio.com/users.json";
                                String url = AtroadsConstant.AtroadsUsers_URL_JSON;

                                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        doOnSuccess(s, MobileNumber, OtherUsername, OtherProfilePic, OtheruserId);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        System.out.println("" + volleyError);
                                    }
                                });

                                RequestQueue rQueue = Volley.newRequestQueue(UsersActivity.this);
                                rQueue.add(request);

//                            usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    UserDetails.chatWith = al.get(position);
//                                    //startActivity(new Intent(UsersActivity.this, ChatActivity.class));
//                                    Intent i = new Intent(UsersActivity.this, ChatActivity.class);
//                                    i.putExtra("OtheruserId",OtheruserId);
//                                    i.putExtra("FROMACTIVITY","UsersActivity");
//                                    startActivity(i);
//                                }
//                            });

                            }else{

                                lin_nodata.setVisibility(View.VISIBLE);
                                usersList.setVisibility(View.GONE);
                            }
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

}
