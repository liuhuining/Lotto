package com.qf.liuyong.lotto_android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.utils.UrlUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/1/30 0030.
 */
public class BannerPictureDetailActivity extends BaseActivity{

    @BindView(R.id.webview)
    WebView mWebView;

    private Unbinder unbinder;

    public static void start(Context context, String url) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.setClass(context, BannerPictureDetailActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, String url,String title) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.putExtra("title",title);
        intent.setClass(context, BannerPictureDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_picture_detail);
        unbinder = ButterKnife.bind(this);
        String title = getIntent().getStringExtra("title");
        getTopBar().setBackImageResource(R.drawable.topbar_back);
        getTopBar().setBackgroundColor(Color.parseColor("#f5f5f5"));
        if (TextUtils.isEmpty(title)){
            getTopBar().showTitleLogo(true);
        }else {
            getTopBar().setTitleColor(Color.BLACK);
            getTopBar().setTitle(title);
        }
        getTopBar().setTitleLogo(R.drawable.icon_title);

        WebSettings ws = mWebView.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        ws.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading(true, "加载中...");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                onLoadComplete();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onLoadComplete();
            }
        });
        mWebView.loadUrl(UrlUtils.BANNER_DETAIL + "?imgpath=" + getIntent().getStringExtra("url"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
