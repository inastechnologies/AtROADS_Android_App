package com.inas.atroads.views.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inas.atroads.R;
import com.inas.atroads.views.Activities.BillingDetailsActivity;
import com.inas.atroads.views.Activities.HomeMapsActivity;

public class EnterUPIDetailsActivity extends AppCompatActivity {
    private static final String TAG = "EnterUPIDetailsActivity";
    private EditText UPIAddressET,nameET;
    private Button submitBtn;
    private int UserId,UserRideId;
    private String AutoNumber,FareType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_u_p_i_details);
        setViews();
    }

    private void setViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("UPI Details");
        setSupportActionBar(toolbar);
        UPIAddressET = findViewById(R.id.UPIAddressET);
        nameET = findViewById(R.id.nameET);
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UPIAddressET.getText().toString().equals(""))
                {
                    UPIAddressET.setError("Please Enter UPI Address");
                    UPIAddressET.requestFocus();
                }
                else if(nameET.getText().toString().equals(""))
                {
                    nameET.setError("Please Enter Name");
                    nameET.requestFocus();
                }
                else {
                    String UPI = "upi://pay?pa="+UPIAddressET.getText().toString()+"&pn="+nameET.getText().toString()+"&cu=INR&mode=02&purpose=00&orgid=189999&sign=MEYCIQDB+O7tRFKR3SUqaRa0Aceso4JS0gFFoD0vOrlj9hXufwIhALS/PYxogO38MXe2/SPv3w76FLdzTJ0DC8amenw5jw46";
                    Intent i = new Intent(EnterUPIDetailsActivity.this, ShowQRActivity.class);
                    i.putExtra("UPICODE", UPI);
                    i.putExtra("UPIID", UPIAddressET.getText().toString());
                    i.putExtra("Name", nameET.getText().toString());
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        UserId = getIntent().getIntExtra("UserId",0);
        UserRideId = getIntent().getIntExtra("UserRideId",0);
        AutoNumber = getIntent().getStringExtra("AutoNumber");
        FareType = getIntent().getStringExtra("FareType");
        Intent i = new Intent(EnterUPIDetailsActivity.this, BillingDetailsActivity.class);
        i.putExtra("UserId",UserId);
        i.putExtra("UserRideId",UserRideId);
        i.putExtra("AutoNumber",AutoNumber);
        i.putExtra("FareType",FareType);
        startActivity(i);
        finish();
      super.onBackPressed();
    }
}