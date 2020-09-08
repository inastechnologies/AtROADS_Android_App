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
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.inas.atroads.R;
import com.inas.atroads.views.Activities.HomeMapsActivity;

public class PaymentScreen extends AppCompatActivity {
    private static final String TAG = "PaymentScreen" ;
    TextView payableAmountTv;
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
        payableAmount = getIntent().getDoubleExtra("PayableAmount",0.0);
        IdToGetYourBill = getIntent().getIntExtra("IdToGetYourBill",0);
        UserRideId = getIntent().getIntExtra("UserRideId",0);
        Log.i(TAG, "onCreate: "+ IdToGetYourBill + "-->"+UserRideId+"-->"+payableAmount);
        payableAmountTv.setText("Rs. "+payableAmount);
        UPICODE = getIntent().getStringExtra("UPICODE");
//        String UPI = "upi://pay?pa="+UPIAddressET.getText().toString()+"&pn="+nameET.getText().toString()+"&cu=INR&mode=02&purpose=00&orgid=189999&sign=MEYCIQDB+O7tRFKR3SUqaRa0Aceso4JS0gFFoD0vOrlj9hXufwIhALS/PYxogO38MXe2/SPv3w76FLdzTJ0DC8amenw5jw46";
        SetPayBtn();


    }


    private void SetPayBtn()
    {
        payBtn = findViewById(R.id.payBtn);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//                intent.setData(Uri.parse("upi://pay?pa=7026562080@upi&pn=SUSHMA%20GARELA&cu=INR&mode=02&purpose=00&orgid=189999&sign=MEUCIQD/VLGj1RKjIcISGrDOUdBD3Q/58qhfcrn5SKkVGooa+wIgVYgcYO/4KY4ve8OjEnK3z4CRTDmj16ResO4DmjGed2c="));
//                intent.setData(Uri.parse("upi://pay?pa=7026562080@upi&pn=SUSHMA&cu=INR&mode=02&purpose=00&orgid=189999&sign=MEUCIQD/VLGj1RKjIcISGrDOUdBD3Q/58qhfcrn5SKkVGooa+wIgVYgcYO/4KY4ve8OjEnK3z4CRTDmj16ResO4DmjGed2c="));
                intent.setData(Uri.parse(UPICODE));
                Intent chooser = Intent.createChooser(intent, "Pay with...");
                startActivityForResult(chooser, 1, null);
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
