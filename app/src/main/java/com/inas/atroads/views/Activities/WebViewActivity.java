package com.inas.atroads.views.Activities;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.inas.atroads.R;
import com.inas.atroads.views.UI.SOSActivity;
import com.inas.atroads.util.localData.BaseActivity;

public class WebViewActivity extends BaseActivity {

    WebView webview_data;
    Toolbar toolbar;
    //ProgressBar progressBar;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webview_data = findViewById(R.id.webview_data);
       // progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("titile"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        url= getIntent().getStringExtra("url");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webview_data.getSettings().setJavaScriptEnabled(true);
        webview_data.clearCache(true);
        webview_data.getSettings().setAppCacheEnabled(false);
        webview_data.getSettings().setLoadWithOverviewMode(true);
        webview_data.getSettings().setUseWideViewPort(true);
        webview_data.getSettings().setDomStorageEnabled(true);
        webview_data.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //webview_data.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview_data.clearCache(true);
        webview_data.getSettings().setAppCacheEnabled(false);
        webview_data.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview_data.getSettings().setAllowFileAccess(true);
        webview_data.setWebViewClient(new WebViewClient());
        webview_data.setWebChromeClient(new WebChromeClient() {
        });
        //webView.setWebViewClient(new MyWebViewClient());
        //webView.setWebChromeClient(new WebChromeClient());
        showProgressDialog();
        webview_data.loadUrl(url);
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
           hideProgressDialog();
        }
    }
}
