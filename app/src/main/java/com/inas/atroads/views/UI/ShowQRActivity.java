package com.inas.atroads.views.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.inas.atroads.R;

public class ShowQRActivity extends AppCompatActivity {
    TextView upiIdTv,NameTv;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_q_r);
        NameTv = findViewById(R.id.NameTv);
        upiIdTv = findViewById(R.id.upiIdTv);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.ShowQR));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String UPICODE = getIntent().getStringExtra("UPICODE");
        String UPIID = getIntent().getStringExtra("UPIID");
        String Name = getIntent().getStringExtra("Name");
        NameTv.setText(Name+"");
        upiIdTv.setText(UPIID+"");
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(UPICODE, BarcodeFormat.QR_CODE, 512, 512);
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
}