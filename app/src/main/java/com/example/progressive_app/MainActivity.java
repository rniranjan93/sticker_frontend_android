package com.example.progressive_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private WebView mywebView;
    private FileChooser fileChooser = new FileChooser(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestPermissionHelper.verifyPermissions(this);
        setContentView(R.layout.activity_main);
        mywebView=(WebView) findViewById(R.id.webview);
        mywebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        mywebView.setWebViewClient(new WebViewClient());
        mywebView.loadUrl("http://192.168.0.106:3000");

        WebSettings webSettings=mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        /*mywebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                view.evaluateJavascript("document.getElementById('upload').onclick= function (){Android.uploadImage()}",null);
            }
        });*/
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            mywebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            mywebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            mywebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mywebView.setWebViewClient(new WebViewClient());
        mywebView.setWebChromeClient(fileChooser);
    }

    @Override
    public void onBackPressed(){
        if(mywebView.canGoBack()) {
            mywebView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }


    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            mywebView.post(new Runnable() {
                @Override
                public void run() {
                    mywebView.loadUrl("javascript:inc()");
                }
            });
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fileChooser.onActivityResult(requestCode,resultCode,data);
    }
}