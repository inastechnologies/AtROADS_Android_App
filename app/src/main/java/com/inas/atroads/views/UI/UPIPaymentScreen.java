package com.inas.atroads.views.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.inas.atroads.R;
import com.inas.atroads.views.Activities.HomeMapsActivity;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

public class UPIPaymentScreen extends AppCompatActivity {
    private static final String TAG = "UPIPaymentScreen" + "";
    private EditText UPIAddressET,nameET,amountET,DescET;
    private Button submitBtn;
    private ImageView scanQrIV,showQrIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upipayment_screen);
        setViews();
    }


    private void setViews()
    {
        UPIAddressET = findViewById(R.id.UPIAddressET);
        nameET = findViewById(R.id.nameET);
        amountET = findViewById(R.id.amountET);
        DescET = findViewById(R.id.DescET);
        scanQrIV = findViewById(R.id.scanQrIV);
        showQrIV = findViewById(R.id.showQrIV);
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                initPayment();

                String UPI = "upi://pay?pa="+UPIAddressET.getText().toString()+"&pn="+nameET.getText().toString()+"&cu=INR&mode=02&purpose=00&orgid=189999&sign=MEYCIQDB+O7tRFKR3SUqaRa0Aceso4JS0gFFoD0vOrlj9hXufwIhALS/PYxogO38MXe2/SPv3w76FLdzTJ0DC8amenw5jw46";

                Intent intent = new Intent();
//                intent.setData(Uri.parse("upi://pay?pa=7026562080@upi&pn=SUSHMA%20GARELA&cu=INR&mode=02&purpose=00&orgid=189999&sign=MEUCIQD/VLGj1RKjIcISGrDOUdBD3Q/58qhfcrn5SKkVGooa+wIgVYgcYO/4KY4ve8OjEnK3z4CRTDmj16ResO4DmjGed2c="));
//                intent.setData(Uri.parse("upi://pay?pa=7026562080@upi&pn=SUSHMA&cu=INR&mode=02&purpose=00&orgid=189999&sign=MEUCIQD/VLGj1RKjIcISGrDOUdBD3Q/58qhfcrn5SKkVGooa+wIgVYgcYO/4KY4ve8OjEnK3z4CRTDmj16ResO4DmjGed2c="));
                intent.setData(Uri.parse(UPI));
                Intent chooser = Intent.createChooser(intent, "Pay with...");
                startActivityForResult(chooser, 1, null);
            }
        });
        scanQrIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    IntentIntegrator integrator = new IntentIntegrator(UPIPaymentScreen.this);
                    integrator.setPrompt("Scan");
                    integrator.initiateScan();
                }
                catch(Exception e)
                {
                    e.printStackTrace();

                }
            }
        });

        showQrIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UPI = "upi://pay?pa="+UPIAddressET.getText().toString()+"&pn="+nameET.getText().toString()+"&cu=INR&mode=02&purpose=00&orgid=189999&sign=MEYCIQDB+O7tRFKR3SUqaRa0Aceso4JS0gFFoD0vOrlj9hXufwIhALS/PYxogO38MXe2/SPv3w76FLdzTJ0DC8amenw5jw46";


                QRCodeWriter writer = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(UPI, BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }


                    ((ImageView) findViewById(R.id.qrIV)).setImageBitmap(bmp);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);

        if(result != null)
        {
            if (resultCode == RESULT_CANCELED){

            }
            else
            {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String UPCScanned = scanResult.getContents();
                Toast.makeText(this, ""+UPCScanned, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onActivityResult: "+UPCScanned);
                Intent i = new Intent(UPIPaymentScreen.this,PaymentScreen.class);
                i.putExtra("UPICODE",UPCScanned);
                startActivity(i);
            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //payment was successful
                Toast.makeText(UPIPaymentScreen.this, "Payment Success", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(UPIPaymentScreen.this, HomeMapsActivity.class);
                        startActivity(i);
                        finish();
                    }
                },1000);

            }else if (resultCode == RESULT_CANCELED) {
                //payment was canceled
                Toast.makeText(UPIPaymentScreen.this, "Payment Cancelled", Toast.LENGTH_SHORT).show();

            }
            else {

            }

        }
    }

}
