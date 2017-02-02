package com.qf.liuyong.lotto_android.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.utils.ViewUtils;

/**
 * Created by Administrator on 2017/1/27 0027.
 */
public class LoadingLayout extends FrameLayout implements View.OnClickListener{

    private RelativeLayout mErrorLayout;
    private TextView mMessage;
    private ImageView mErrorImage;
    private TextView loadingTextView;
    private LinearLayout loadingProgress;

    public LoadingLayout(Context context) {
        super(context);
        init();
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater .from(getContext()).inflate(R.layout.loading_layout,this,true);
        mErrorLayout = (RelativeLayout) findViewById(R.id.error_layout);
        mErrorImage = (ImageView) findViewById(R.id.error);
        mMessage = (TextView) findViewById(R.id.message);
        loadingProgress = (LinearLayout) findViewById(R.id.loading_progress);
        loadingTextView = (TextView) findViewById(R.id.loading_text);
        findViewById(R.id.refresh_btn).setOnClickListener(this);
        Glide.with(getContext())
                .load(R.drawable.loading_gif)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((ImageView) findViewById(R.id.loading_img));
    }

    public void onResume(){

    }

    public void onStop(){

    }

    public void onLoadComplete(){
        ViewUtils.setGone(loadingProgress, true);
        ViewUtils.setGone(mErrorLayout, true);
        onStop();
    }

    public void showError(String message){
        mMessage.setText(message);
        ViewUtils.setGone(mErrorLayout,false);
        ViewUtils.setGone(loadingProgress,true);
        onResume();
    }

    public void setErrorImage(int resId){
        mErrorImage.setImageResource(resId);
    }
    public void setErrorText(CharSequence text){
        mMessage.setText(text);
    }

    public void showLoading(boolean b, String loadingText){
        if (b){
            loadingProgress.setBackgroundColor(Color.TRANSPARENT);
        }else {
            loadingProgress.setBackgroundColor(Color.WHITE);
        }
        loadingTextView.setText(loadingText);
        ViewUtils.setGone(mErrorLayout,true);
        ViewUtils.setGone(loadingProgress,false);
        onResume();
    }

    public interface OnRefreshListener{
        void onRefresh();
    }

    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        this.onRefreshListener = onRefreshListener;
    }

    @Override
    public void onClick(View v) {
        if (onRefreshListener != null){
            onRefreshListener.onRefresh();
        }
    }
}
