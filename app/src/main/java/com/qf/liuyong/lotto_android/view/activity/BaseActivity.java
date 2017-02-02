package com.qf.liuyong.lotto_android.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.utils.ViewUtils;
import com.qf.liuyong.lotto_android.view.topbar.TopBar;
import com.qf.liuyong.lotto_android.view.topbar.TopBarFragmentActivity;
import com.qf.liuyong.lotto_android.view.widget.LoadingLayout;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class BaseActivity extends TopBarFragmentActivity{

    private TopBar mTopBar;
    private LoadingLayout loadingLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar = getTopBar();
        mTopBar.setBackImageResource(R.drawable.nav_btn_back_normal);
        mTopBar.setBackgroundColor(getResources().getColor(R.color.black));
        mTopBar.setTitle("");
        mTopBar.setBackgroundColor(Color.parseColor("#000000"));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        ViewGroup contentView = ((ViewGroup)findViewById(android.R.id.content));
        loadingLayout = new LoadingLayout(this);
        loadingLayout.setVisibility(View.GONE);

        contentView.addView(loadingLayout,params);
        if (onRefreshListener != null){
            loadingLayout.setOnRefreshListener(onRefreshListener);
        }
        if (showloading)
            loadingLayout.showLoading(false,"加载中...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingLayout.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void onLoadComplete() {
        if (loadingLayout == null)
            return;
        loadingLayout.onLoadComplete();
        ViewUtils.setGone(loadingLayout, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        loadingLayout.onStop();
    }

    private boolean showloading = false;

    public void showLoading(boolean b, String str) {
        if (loadingLayout == null) {
            showloading = true;
            return;
        }
        loadingLayout.showLoading(b, str);
        ViewUtils.setGone(loadingLayout, false);
    }

    public void setErrorImage(int resId) {
        loadingLayout.setErrorImage(resId);
    }

    public void showError(String message) {
        loadingLayout.showError(message);
        ViewUtils.setGone(loadingLayout, false);
    }

    LoadingLayout.OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(LoadingLayout.OnRefreshListener onRefreshListener){
        this.onRefreshListener = onRefreshListener;
        if (loadingLayout != null){
            loadingLayout.setOnRefreshListener(onRefreshListener);
        }
    }
}
