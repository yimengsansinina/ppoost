package com.exp.post.user;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.exp.post.R;

public class UserRememberActivity extends AppCompatActivity {
    private WebView webView;
    private View loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().hide();
        // 找到WebView
        webView = findViewById(R.id.webView);
        TextView title_tv = findViewById(R.id.title_tv);
        loadingView = findViewById(R.id.loadingView);
        loadingView.setVisibility(View.VISIBLE);
        View back_fl = findViewById(R.id.back_fl);
        back_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 设置WebView客户端
        webView.setWebViewClient(new MyWebViewClient());

        // 获取WebView的设置对象
        WebSettings webSettings = webView.getSettings();

        // 启用JavaScript（根据需要启用）
        webSettings.setJavaScriptEnabled(true);

        // 设置缩放控件
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        // 支持通过手势进行缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        title_tv.setText(title);
        // 加载网页
//
        webView.loadUrl(url);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            // 页面加载完成时的操作
            loadingView.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 页面加载失败时的操作
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        // 如果WebView中可以返回上一页，则执行返回操作，否则退出Activity
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        // 销毁WebView
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
