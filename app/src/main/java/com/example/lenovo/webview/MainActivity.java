package com.example.lenovo.webview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Tag";
    private ProgressBar bar;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//         github
    }

    private void initView() {
        bar = findViewById(R.id.proBar);
        webView = findViewById(R.id.webView);
        load(webView);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void load(WebView webView) {
//        webView.loadUrl("http://139.196.35.30:8080/OkHttpTest/apppackage/test.html");
        webView.addJavascriptInterface(this, "ysh");

        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(webViewClient);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);

        settings.setDisplayZoomControls(true);

//设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webView.loadUrl("https://www.baidu.com/");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "是否有上一个页面:" + webView.canGoBack());
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        webView.clearCache(true);
        webView.destroy();
        webView = null;
        super.onDestroy();
    }

    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage(message).setPositiveButton("确定", null);
            builder.setCancelable(false);
            builder.create().show();
            result.confirm();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            Log.d(TAG, "onReceivedTitle: " + title);
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            bar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    };
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.d(TAG, "onReceivedError: ");
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG, "拦截url:" + url);
            if (url.startsWith("http://") || url.startsWith("https://")) {
                view.loadUrl(url);
                view.stopLoading();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted: " + url);
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished: " + url);
            bar.setVisibility(View.GONE);
        }
    };

}
