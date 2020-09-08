package com.inas.atroads.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.inas.atroads.R;
import com.inas.atroads.util.localData.BaseActivity;

import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class TermsConditionsActivity extends BaseActivity {

    private Context mContext;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        //assining layout
        setContentView(R.layout.activity_terms_condition);
        isNetworkAvailable(TermsConditionsActivity.this);
        /* intializing and assigning ID's */
        initViews();

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.terms_con));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsConditionsActivity.this, HomeMapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(TermsConditionsActivity.this, HomeScreen.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
        super.onBackPressed();
    }
}
