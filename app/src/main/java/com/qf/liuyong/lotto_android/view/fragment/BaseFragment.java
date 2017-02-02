package com.qf.liuyong.lotto_android.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.qf.liuyong.lotto_android.utils.ViewUtils;
import com.qf.liuyong.lotto_android.view.widget.LoadingLayout;

/**
 * Created by Administrator on 2017/1/30 0030.
 */
public abstract class BaseFragment extends Fragment {

    private LoadingLayout loadingLayout;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FrameLayout frameLayout = new FrameLayout(getActivity());
        View contentView = onCreateContentView(inflater, container, savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(contentView, params);
        loadingLayout = new LoadingLayout(getActivity());
        loadingLayout.setVisibility(View.GONE);
        frameLayout.addView(loadingLayout, params);

        return frameLayout;
    }


    public void loadComplete() {
        loadingLayout.onLoadComplete();
        ViewUtils.setGone(loadingLayout, true);
    }

    boolean showloading = false;

    public void showLoading(boolean b, String str) {
        if (loadingLayout == null) {
            showloading = true;
            return;
        }

        loadingLayout.showLoading(b, str);
        ViewUtils.setGone(loadingLayout, false);
    }

    public void showError(String message) {
        loadingLayout.showError(message);
        ViewUtils.setGone(loadingLayout, false);
    }

    LoadingLayout.OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(LoadingLayout.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
        if (loadingLayout != null)
            loadingLayout.setOnRefreshListener(onRefreshListener);
    }

    public abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

}
