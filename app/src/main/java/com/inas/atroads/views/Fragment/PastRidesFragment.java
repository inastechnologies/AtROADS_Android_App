package com.inas.atroads.views.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.inas.atroads.views.Activities.PairSuccessScreen;
import com.inas.atroads.views.adapter.PastRidesHistoryAdapter;
import com.inas.atroads.views.model.EndRideRequestModel;
import com.inas.atroads.views.model.EndRideResponseModel;
import com.inas.atroads.views.model.RidesHistoryRequestModel;
import com.inas.atroads.views.model.RidesHistoryResponseModel;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PastRidesFragment extends Fragment
{
    private static final String DEFAULT = "N/A" ;
    private View view;
    private Subscription mSubscription;
    private static String TAG = "PastRidesFragment";
    String Username,Email,Mobile;
    LinearLayout lin_nodata;
    int UserId;
    private RecyclerView recyclerView;
    private PastRidesHistoryAdapter pastRidesHistoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.past_rides_fragment, container, false);
        lin_nodata= view.findViewById(R.id.lin_nodata);
        recyclerView = view.findViewById(R.id.recyclerView);
        GetSharedPrefs();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private void SetRecyclerView(View view, RidesHistoryResponseModel mResponse)
    {

        pastRidesHistoryAdapter = new PastRidesHistoryAdapter(getActivity(), mResponse);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pastRidesHistoryAdapter);
    }

    /********************************START OF GetSharedPrefs*******************************/
    /*
      GetSharedPrefs
      */
    private void GetSharedPrefs()
    {
//        SharedPreferences pref = getActivity().getSharedPreferences("LoginPref", 0);
//        UserId = pref.getInt("user_id", 0);
//        Mobile = pref.getString("mobile_number",DEFAULT);
//        Email =  pref.getString("email_id",DEFAULT);
//        Username = pref.getString("user_name",DEFAULT);
//        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
        SharedPreferences pref = getActivity().getSharedPreferences("RegPref", 0); // 0 - for private mode
        UserId = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Email =  pref.getString("email_id",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);
        CallPastRidesHistoryAPI();
    }

    /********************************END OF GetSharedPrefs*******************************/

    /********************************START OF CallPastRidesHistoryAPI*******************************/
    /*
     * CallPastRidesHistoryAPI
     * */
    private void CallPastRidesHistoryAPI(){

        JsonObject object = RidesHistoryObject();
        AtroadsService service = ServiceFactory.createRetrofitService(getActivity(), AtroadsService.class);
        mSubscription = service.RidesHistoryResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RidesHistoryResponseModel>() {
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
                    public void onNext(RidesHistoryResponseModel mResponse) {
                        Log.i(TAG, "RidesHistoryResponseModel: "+mResponse);
                        Toast.makeText(getActivity(), mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 0)
                        {
                            lin_nodata.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            //Toast.makeText(PairSuccessScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(mResponse.getStatus() == 1)
                        {
                            if(mResponse.getResult().size()>0) {
                                lin_nodata.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                SetRecyclerView(view, mResponse);
                            }else{
                                lin_nodata.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                    }
                });

    }

    /**
     * Json object of RidesHistoryObject
     *
     * @return
     */
    private JsonObject RidesHistoryObject()
    {
        RidesHistoryRequestModel requestModel = new RidesHistoryRequestModel();
        requestModel.setUserId(UserId);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    /*************************************END OF END RIDE *******************************/
}
