package com.inas.atroads.views.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.inas.atroads.R;
import com.inas.atroads.services.AtroadsService;
import com.inas.atroads.services.ServiceFactory;
import com.inas.atroads.views.UI.ViewEmergencyContacts;
import com.inas.atroads.views.model.Req_EditEmergencyContact;
import com.inas.atroads.views.model.Res_EditEmergencyContact;
import com.inas.atroads.views.model.Res_GetEmergencyContacts;

import java.io.IOException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewContactsAdapter  extends RecyclerView.Adapter<ViewContactsAdapter.MyViewHolder>
{
    private static final String TAG = ViewContactsAdapter.class.getSimpleName();
    Context context;
    AppCompatActivity mActivity;
    List<Res_GetEmergencyContacts.Sos> contactsList;
    String Username,Email,Mobile;
    int UserId;
    private static final String DEFAULT = "N/A";
    public ViewContactsAdapter(ViewEmergencyContacts mActivity, List<Res_GetEmergencyContacts.Sos> contactsList)
    {
        context = mActivity;
        this.mActivity = mActivity;
        this.contactsList = contactsList;
    }

    public List<Res_GetEmergencyContacts.Sos> getEmergencyContactsList()
    {
        return this.contactsList;
    }

    public void setEmergencyContactsList(List<Res_GetEmergencyContacts.Sos> list)
    {
        this.contactsList = list;
    }


    @NonNull
    @Override
    public ViewContactsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_view_contacts,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewContactsAdapter.MyViewHolder holder,int position)
    {
        final Res_GetEmergencyContacts.Sos emergencyContacts = contactsList.get(position);
        holder.txt_name.setText(emergencyContacts.getName());
        holder.txt_mobile.setText(emergencyContacts.getMobileNumber());
        holder.txt_email.setText(emergencyContacts.getEmailId());

        holder.iv_edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.v(TAG, "before editContacts");
                editContacts(emergencyContacts);
                Log.v(TAG, "after editContacts");
            }
        });


    }

    private void editContacts(Res_GetEmergencyContacts.Sos emergencyContacts)
    {
        Log.v(TAG, "in editContacts");
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_edit_contacts,null);
        builder.setView(dialogView);

        builder.setTitle(mActivity.getResources().getString(R.string.edit_contacts));

        EditText edt_name = dialogView.findViewById(R.id.txt_name_sos_edit);
        EditText edt_mobile = dialogView.findViewById(R.id.txt_mobile_sos_edit);
        EditText edt_email = dialogView.findViewById(R.id.txt_email_sos_edit);
        Button btn_update = dialogView.findViewById(R.id.btn_update_sos_edit);
        Button btn_cancel = dialogView.findViewById(R.id.btn_cancel_sos_edit);

        edt_name.setText(emergencyContacts.getName());
        edt_mobile.setText(emergencyContacts.getMobileNumber());
        edt_email.setText(emergencyContacts.getEmailId());
        Integer id = emergencyContacts.getId();

		/*String name = "", mobile = "", email = "";
		name = edt_name.getText().toString();
		mobile = edt_mobile.getText().toString();
		email = edt_email.getText().toString();
		Log.v(TAG, "In editContacts() " + name + "," + mobile + "," + email);*/
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        setonClickListenerForEdit(btn_update,edt_name,edt_mobile,edt_email,id,dialog);
        setOnClickListenerForCancel(btn_cancel, dialog);

    }

    /**
     Description : Set OnClickListener For Cancel Button
     @param btn_cancel
     @param dialog
     */
    private void setOnClickListenerForCancel(Button btn_cancel,final AlertDialog dialog)
    {
        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
    }

    /**
     Description : Set OnClickListener For ImageView Edit@param iv_edit

     */
    private void setonClickListenerForEdit(Button btn_update,final EditText name,final EditText mobile,final EditText email,final Integer id,final AlertDialog dialog)
    {
        btn_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                validateEmergencyContacts(name,mobile,email,id,dialog);
            }
        });
    }

    /**
     Description : Validate the Fields
     @param edt_name
     @param edt_mobile
     @param edt_email
     @param id
     @param dialog

     */
    private void validateEmergencyContacts(EditText edt_name,EditText edt_mobile,EditText edt_email,Integer id,AlertDialog dialog)
    {
        String name = "", mobile = "", email = "";
        name = edt_name.getText().toString();
        mobile = edt_mobile.getText().toString();
        email = edt_email.getText().toString();
        Log.v(TAG, "In editContacts() " + name + "," + mobile + "," + email);

        if(name.equals(""))
        {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
        }
        else if(mobile.equals(""))
        {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();
        }
        else if(email.equals(""))
        {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
        }
        else
        {

            callEditEmergencyContactsApi(name,mobile,email,id,dialog);
        }
    }


    /*
    GetSharedPrefs
    */
    private void GetSharedPrefs()
    {
        SharedPreferences pref = context.getSharedPreferences("RegPref", 0); // 0 - for private mode
        UserId = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Email =  pref.getString("email_id",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: UserId: "+UserId);

    }


    /**
     Description : Api Call to update the Emergency Contacts@param name
     @param mobile
     @param email
     @param id
     @param dialog

     */
    private void callEditEmergencyContactsApi(String name,String mobile,String email,Integer id,final AlertDialog dialog)
    {
        GetSharedPrefs();
        Req_EditEmergencyContact req_editEmergencyContact = new Req_EditEmergencyContact(name,mobile,email,UserId,id);

        AtroadsService service = ServiceFactory.createRetrofitService(mActivity,AtroadsService.class);
        service.editEmergencyContacts(req_editEmergencyContact)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Res_EditEmergencyContact>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.v(TAG,"e.getLocalizedMessage() : " + e.getLocalizedMessage());
                        Toast.makeText(mActivity,"Server Down:" + e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
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
                    public void onNext(Res_EditEmergencyContact res_editEmergencyContact)
                    {
                        if(res_editEmergencyContact.getStatus() == 1)
                        {
                            String message = res_editEmergencyContact.getMessage();
                            if(message != null)
                            {

                                Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                setEmergencyContactsList(contactsList);
                                ViewEmergencyContacts viewEmergencyContacts = new ViewEmergencyContacts();
                                viewEmergencyContacts.refreshContacts();
                                Log.v(TAG, "In onNext() contactsList = " + getEmergencyContactsList());
                                dialog.dismiss();
                            }
                            else
                            {
                                Log.v(TAG, "Message null");
                            }

                        }
                        else
                        {
                            Log.v(TAG, "Response null");
                        }

                    }
                });


    }

    @Override
    public int getItemCount()
    {
        int size = 0;
        if(contactsList != null && contactsList.size() > 0)
        {
            size = contactsList.size();
        }
        return size;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_name, txt_mobile, txt_email;
        ImageView iv_edit;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name_sos_view_contact);
            txt_mobile = itemView.findViewById(R.id.txt_mobile_sos_view_contact);
            txt_email = itemView.findViewById(R.id.txt_email_sos_view_contact);
            iv_edit = itemView.findViewById(R.id.iv_edit_sos_view_contact);

        }
    }



}

