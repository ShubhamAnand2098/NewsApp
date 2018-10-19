package com.example.a1605594.newsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.victor.loading.rotate.RotateLoading;

public class WebDisplayActivity extends AppCompatActivity {

    private WebView webView;
    private RotateLoading progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_display);

        //getSupportActionBar().hide();
        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.normal_progress_loading_bar);

        progressBar.start();
        webView.setWebViewClient(new CustomWebViewClient());
        webView.loadUrl(getIntent().getStringExtra("URL"));

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.stop();
        }
    }

}
