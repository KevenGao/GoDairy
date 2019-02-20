package com.example.kevengao.godairy;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WeiboActivity extends Activity {
    private WebView web_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo);

       init ();
        web_view=(WebView)findViewById(R.id.web_view);
        web_view.loadUrl("http://www.chinanews.com/");

    }

   private void init() {
        web_view = (WebView)findViewById(R.id.web_view);
        //支持javascript
       web_view.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
       web_view.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
       web_view.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        web_view.getSettings().setUseWideViewPort(true);
        //自适应屏幕
       web_view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
       web_view.getSettings().setLoadWithOverviewMode(true);


    }
    }
