package com.justimaginethat.gogodriver.activity_error_webview_pkg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;


import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.R;


public class activity_error_webview extends AppCompatActivity {

    private static final String TAG = "PayBit";
//    private view_pager_dashboard_adapter mSectionsPagerAdapter;
    private CustomProgressBar mProgress;
    public static activity_error_webview context;
    /*public static List<CattleDCM> cattleDCMList;
    public static List<ScheduleDCM> scheduleDCMList;*/
    private WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_webview);
        context = this;

        web_view = (WebView) findViewById(R.id.web_view);
        Intent i=getIntent();
        String str = i.getStringExtra("url");
        web_view.loadUrl(str);
        finish();


    }
}

