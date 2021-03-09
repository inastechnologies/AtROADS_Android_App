package com.inas.atroads.views.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.inas.atroads.R;
import com.inas.atroads.views.Activities.HomeMapsActivity;

public class PaymentScreen extends AppCompatActivity {
    private static final String TAG = "PaymentScreen" ;
    TextView payableAmountTv;
    EditText payableAmounted;
    private int IdToGetYourBill,UserRideId;
    private Button payBtn;
    String UPICODE;
    Double payableAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.Payment));
        setSupportActionBar(toolbar);
        payableAmountTv = findViewById(R.id.payableAmountTv);
        payableAmounted = findViewById(R.id.payableAmounted);
        payableAmount = getIntent().getDoubleExtra("PayableAmount",0.0);
        IdToGetYourBill = getIntent().getIntExtra("IdToGetYourBill",0);
        UserRideId = getIntent().getIntExtra("UserRideId",0);
        UPICODE = getIntent().getStringExtra("UPICODE");
        SetPayBtn();

        if(payableAmount.equals(0.0) || payableAmount.equals(null)){
            payableAmounted.setVisibility(View.VISIBLE);
            payableAmountTv.setVisibility(View.GONE);
        }else{
            payableAmounted.setVisibility(View.GONE);
            payableAmountTv.setVisibility(View.VISIBLE);
            payableAmountTv.setText("Rs. "+payableAmount);
        }


    }


    private void SetPayBtn()
    {
        payBtn = findViewById(R.id.payBtn);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UPICODE=UPICODE+"&am="+payableAmounted.getText().toString()+"&tn=";

                Log.d("UPICODE-",UPICODE);
/*
                Uri uri = Uri.parse("upi://pay").buildUpon()
                        .appendQueryParameter("pa", upiId)
                        .appendQueryParameter("pn", name)
                        .appendQueryParameter("tn", note)
                        .appendQueryParameter("am", amount)
                        .appendQueryParameter("cu", "INR")
                        .build();


                Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
                upiPayIntent.setData(uri);

                // will always show a dialog to user to choose an app
                Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

                // check if intent resolves
                if(null != chooser.resolveActivity(getPackageManager())) {
                    startActivityForResult(chooser, UPI_PAYMENT);
                } else {
                    Toast.makeText(this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
                }*/

              /*  Uri uri = Uri.parse("upi://pay").buildUpon()
                        .appendQueryParameter("pa", "vkn2601@okicici")
                        .appendQueryParameter("pn", "vijay%20naidu")
                        .appendQueryParameter("tn", "")
                        .appendQueryParameter("am", "10")
                        .appendQueryParameter("cu", "INR")
                        .build();*/

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(UPICODE));
                Intent chooser = Intent.createChooser(intent, "Pay with...");
                startActivityForResult(chooser, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //payment was successful
//                Toast.makeText(PaymentScreen.this, "Payment Success", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        payableAmount = getIntent().getDoubleExtra("PayableAmount",0.0);
                        Log.i(TAG, "run: payableAmount:"+payableAmount);
//                        Intent i = new Intent(PaymentScreen.this, YourBillScreen.class);
                        Intent i = new Intent(PaymentScreen.this, HomeMapsActivity.class);
                        i.putExtra("IdToGetYourBill",IdToGetYourBill);
                        i.putExtra("payableAmount",payableAmount);
                        i.putExtra("UserRideId",UserRideId);
                        startActivity(i);
                        finish();
                    }
                },1000);

            }else if (resultCode == RESULT_CANCELED) {
                //payment was canceled
                Toast.makeText(PaymentScreen.this, "Payment Cancelled", Toast.LENGTH_SHORT).show();

            }
            else {

            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
