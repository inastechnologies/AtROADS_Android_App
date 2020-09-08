package com.inas.atroads.views.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.views.adapter.ViewContactsAdapter;
import com.inas.atroads.views.model.Req_GetEmergencyContacts;
import com.inas.atroads.views.model.Res_GetEmergencyContacts;

import java.io.IOException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewEmergencyContacts extends BaseActivity {
    private static final String TAG = ViewEmergencyContacts.class.getSimpleName();
    RecyclerView rv_view_contacts;
    String Username,Email,Mobile;
    int UserId;
    private static final String DEFAULT = "N/A";
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emergency_contacts);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.view_contacts));
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
        setViewsFromLayout();

    }

    /**
     Description : Set Views From Layout
     */
    private void setViewsFromLayout()
    {
        rv_view_contacts = findViewById(R.id.rv_contacts);
        getEmergencyContacts();
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


    /**
     Description : Get Emergency Contacts
     */
    private void getEmergencyContacts()
    {
        //int userId = 1;
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
                        Toast.makeText(ViewEmergencyContacts.this,"Server Down:" + e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
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
//                            if(resultList.get(0) != null)
                            if(resultList.size()!=0)
                            {
                                List<Res_GetEmergencyContacts.Sos> contactsList = resultList.get(0).getSos();
                                if(contactsList != null && contactsList.size() > 0)
                                {
                                    setEmergencyContacts(contactsList);
                                }
                                else
                                {
                                    Log.v(TAG,"contactsList null");
                                }
                            }
                            else
                            {
                                Log.v(TAG,"Result null");
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
     Description : Set Emergency Contacts to Adapter@param contactsList

     */
    private void setEmergencyContacts(List<Res_GetEmergencyContacts.Sos> contactsList)
    {
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(ViewEmergencyContacts.this);
        rv_view_contacts.setLayoutManager(mlayoutManager);
        mlayoutManager.requestLayout();
        rv_view_contacts.setItemAnimator(new DefaultItemAnimator());
       // rv_view_contacts.addItemDecoration(new DividerItemDecoration(ViewEmergencyContacts.this,DividerItemDecoration.VERTICAL));
        //rv_view_contacts.setAdapter(adapter);

        if(rv_view_contacts.getAdapter() != null)
        {
            ViewContactsAdapter adapter= new ViewContactsAdapter(ViewEmergencyContacts.this,contactsList);
            if(adapter.getEmergencyContactsList() != null)
            {
                if(! adapter.getEmergencyContactsList().equals(contactsList))
                {
                    adapter.setEmergencyContactsList(contactsList);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    adapter.notifyDataSetChanged();
                }
            }
            else
            {
                adapter.setEmergencyContactsList(contactsList);
                adapter.notifyDataSetChanged();
                //the list is same and no need to perform any further operations on the list
            }
        }
        else
        {
            final ViewContactsAdapter adapter = new ViewContactsAdapter(ViewEmergencyContacts.this,contactsList);

            rv_view_contacts.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    public void refreshContacts()
    {
        getEmergencyContacts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // todo: goto back activity from here
                onBackPressed();
                break;


            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}