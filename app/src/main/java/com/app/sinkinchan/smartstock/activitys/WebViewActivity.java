package com.app.sinkinchan.smartstock.activitys;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.utils.LogUtil;

import org.jsoup.helper.StringUtil;

public class WebViewActivity extends BaseActivity {

    WebView webView;
    ProgressBar progressBar;
    TextView btn_reload;
    String url, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        url = getIntent().getStringExtra(WEB_VIEW_LOAD_URL);
        title = getIntent().getStringExtra(WEB_VIEW_TITLE);
        init();
        load();
        setBackMenu();
        setTitle(title);
    }

    private void load() {
        if (!StringUtil.isBlank(url)) {
            webView.loadUrl(url);
        }
    }

    @Override
    protected void init() {
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_reload = (TextView) findViewById(R.id.btn_reload);
        initWebView();
    }

    private void initWebView() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (progressBar.getVisibility() != View.VISIBLE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(newProgress);
                LogUtil.d("newProgress=" + newProgress);
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                webView.setVisibility(View.GONE);
                btn_reload.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                webView.setVisibility(View.GONE);
                btn_reload.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_reload.setVisibility(View.GONE);
                WebViewActivity.this.setTitle("加载中...");
                if (webView.getVisibility() != View.VISIBLE) {
                    webView.setVisibility(View.VISIBLE);
                    webView.loadUrl(url);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void registerListener() {

    }

    @Override
    protected void unRegisterListener() {

    }

    @Override
    public void onEventMainThread(MessageEvent event) {

    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
        }
    };

}
