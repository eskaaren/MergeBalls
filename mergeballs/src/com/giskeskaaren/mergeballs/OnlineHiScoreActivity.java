package com.giskeskaaren.mergeballs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/30/12
 * Time: 11:36 PM
 */
public class OnlineHiScoreActivity extends  Activity {
    private WebView web;
    private Typeface tf;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onlinehiscore);

        tf = Typeface.createFromAsset(getAssets(), "data/fonts/Welbut__.ttf");
        TextView tv = (TextView) this.findViewById(android.R.id.title);
        tv.setTypeface(tf);

        web = (WebView) findViewById(R.id.onlineWebView);
        web.setWebViewClient(new WebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("http://www.giskeskaaren.com/mergeballs/dbconnect.php");
    }


    public static class WebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}



