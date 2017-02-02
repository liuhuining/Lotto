package com.qf.liuyong.lotto_android.view.topbar;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.view.widget.BlurLayout;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class TopBarImpl extends TopBar implements TopBarView.OnActionListener {

    private Activity activity;
    private TopBarView mTopBarView;
    private ViewGroup mDecorView;
    private ViewGroup mContentFrame;
    private BlurLayout mBlurLayout;

    private void initContentFrame(){
        mDecorView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        mDecorView.removeAllViews();
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = null;
        try{
            view = inflater.inflate(R.layout.base_screen,null);
        }catch (Throwable t){
            t.printStackTrace();
        }

        mBlurLayout = new BlurLayout(activity,view);
        mDecorView.addView(mDecorView);
        mDecorView.setId(View.NO_ID);
        mContentFrame = (ViewGroup) view.findViewById(R.id.screen_content);
        mContentFrame.setId(android.R.id.content);
    }

    void initTopBarView(){
        if (mContentFrame == null){
            initContentFrame();
        }
        mTopBarView = (TopBarView) mDecorView.findViewById(R.id.top_bar);
        mTopBarView.setTitle(activity.getTitle());
        mTopBarView.setmOnActionListener(this);
    }

    public TopBarImpl(Activity activity) {
        this.activity = activity;
    }

    private TopBarView getTopBarView(){
        if (mTopBarView == null){
            initTopBarView();
        }
        return mTopBarView;
    }

    @Override
    public void onBack() {
        if (mOnItemSelectedListener != null){
            mOnItemSelectedListener.onItemSelected(FEARURE_BACK);
        }
    }

    @Override
    public void onAction() {
        if (mOnItemSelectedListener != null){
            mOnItemSelectedListener.onItemSelected(FEATRUE_ACTION);
        }
    }

    @Override
    public void onClick() {
        if (mOnItemSelectedListener != null){
            mOnItemSelectedListener.onItemSelected(FEATRUE_CLICK);
        }
    }

    @Override
    public void onLeftClick() {
        if (mOnItemSelectedListener != null){
            mOnItemSelectedListener.onItemSelected(LEFT_BUTTON_CLICK);
        }
    }

    @Override
    public void onRightClick() {
        if (mOnItemSelectedListener != null){
            mOnItemSelectedListener.onItemSelected(RIGHT_BUTTON_CLICK);
        }
    }

    @Override
    public void onMarkClick() {
        mOnItemSelectedListener.onItemSelected(FEATURE_MARK);
    }

    @Override
    public void setTitle(int resId) {
        getTopBarView().setTitle(resId);
        if (activity != null){
            activity.setTitle(resId);
        }
    }

    @Override
    public CharSequence getTitle() {
        return getTopBarView().getTitle();
    }

    @Override
    public void setTitle(CharSequence text) {
        if (text == null){
            return;
        }
        getTopBarView().setTitle(text);
        if (activity != null){
            activity.setTitle(text);
        }
    }

    @Override
    public void setTitleColor(int color) {
        getTopBarView().setTitleColor(color);
    }

    @Override
    public void setSubTitle(int resid) {
        getTopBarView().setSubTitle(resid);
    }

    @Override
    public void setSubTitle(CharSequence text) {
        getTopBarView().setSubTitle(text);
    }

    @Override
    public void showTitle(boolean show) {
        getTopBarView().showTitle(show);
    }

    @Override
    public void showSubTitle(boolean show) {
        getTopBarView().showSubTitle(show);
    }

    @Override
    public void showBack(boolean show) {
        getTopBarView().showBack(show);
    }

    @Override
    public void showAction(boolean show) {
        getTopBarView().showAction(show);
    }

    @Override
    public void showMark(boolean show) {
        getTopBarView().showMark(show);
    }

    @Override
    public void showRightButton(boolean show) {
        getTopBarView().showRightButton(show);
    }

    @Override
    public void setCustomView(View view) {
        getTopBarView().setCustomView(view);
    }

    @Override
    public void setCustomView(int layoutId) {
        getTopBarView().setCustomView(layoutId);
    }

    @Override
    public void setCustomView(View view, ViewGroup.LayoutParams params) {
        getTopBarView().setCustomView(view, params);
    }

    @Override
    public void addView(View view, RelativeLayout.LayoutParams params) {
        getTopBarView().addView(view,params);
    }

    @Override
    public void removeCustomViews() {
        getTopBarView().removeCustomViews();
    }

    @Override
    public void hide() {
        getTopBarView().hide();
    }

    @Override
    public void show() {
        getTopBarView().show();
    }

    @Override
    public void setContentView(int layoutResId) {
        if (mContentFrame == null){
            initContentFrame();
        }else {
            mContentFrame.removeAllViews();
        }
        activity.getLayoutInflater().inflate(layoutResId, mContentFrame,true);
        android.view.Window.Callback callback = activity.getWindow().getCallback();
        if (callback != null){
            callback.onContentChanged();
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mContentFrame == null){
            initContentFrame();
        }else {
            mContentFrame.removeAllViews();
        }
        mContentFrame.addView(view,params);
        android.view.Window.Callback callback = activity.getWindow().getCallback();
        if (callback != null){
            callback.onContentChanged();
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        getTopBarView().setBackgroundColor(color);
    }

    @Override
    public void setBackground(Drawable background) {
        getTopBarView().setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundResource(int resId) {
        getTopBarView().setBackgroundResource(resId);
    }

    @Override
    public void setActionImageResource(int resId) {
        getTopBarView().setActionImageResource(resId);
    }

    @Override
    public void setMarkImageResource(int resId) {
        getTopBarView().setMarkImageResource(resId);
    }

    @Override
    public void setActionImageDrawable(Drawable drawable) {
        getTopBarView().setActionImageDrawable(drawable);
    }

    @Override
    public void setBackImageResource(int resId) {
        getTopBarView().setBackImageResource(resId);
    }

    @Override
    public void setBackImageDrawable(Drawable drawable) {
        getTopBarView().setBackImageDrawable(drawable);
    }

    @Override
    public void showAtTop() {
        ViewGroup v = (ViewGroup) mDecorView.findViewById(R.id.base_screen);
        View topBar = mDecorView.findViewById(R.id.top_bar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mContentFrame.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,0);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mContentFrame.getLayoutParams();
        params2.addRule(RelativeLayout.ABOVE, 0);
        params2.addRule(RelativeLayout.BELOW, R.id.top_bar);
        topBar.setLayoutParams(params);
        mContentFrame.setLayoutParams(params2);
    }

    @Override
    public void showAtBottom() {
        ViewGroup v = (ViewGroup) mDecorView.findViewById(R.id.base_screen);
        View topBar = mDecorView.findViewById(R.id.top_bar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topBar.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mContentFrame.getLayoutParams();
        params2.addRule(RelativeLayout.BELOW, 0);
        params2.addRule(RelativeLayout.ABOVE, R.id.top_bar);
        topBar.setLayoutParams(params);
        mContentFrame.setLayoutParams(params2);
    }

    @Override
    public void floating() {
        ViewGroup v = (ViewGroup) mDecorView.findViewById(R.id.base_screen);
        View topBar = mDecorView.findViewById(R.id.top_bar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topBar.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mContentFrame.getLayoutParams();
        params2.addRule(RelativeLayout.ABOVE, 0);
        params2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        topBar.setLayoutParams(params);
        mContentFrame.setLayoutParams(params2);
        topBar.bringToFront();
    }

    @Override
    public View getLeftView() {
        return getTopBarView().getLeftView();
    }

    @Override
    public View getRightView() {
        return getTopBarView().getRightView();
    }

    @Override
    public View getRootView() {
        return getTopBarView();
    }

    @Override
    public View getRightButtonView() {
        return getTopBarView().getRightButtonView();
    }

    @Override
    public View getLeftButtonView() {
        return getTopBarView().getLeftButtonView();
    }

    @Override
    public void setLogo(int resId) {
        getTopBarView().setLogo(resId);
    }

    @Override
    public void setTitleLogo(int resId) {
        getTopBarView().setTitleLogo(resId);
    }

    @Override
    public void showTitleLogo(boolean show) {
        getTopBarView().showTitleLogo(show);
    }

    @Override
    public void blur() {
        mBlurLayout.render();
    }

    @Override
    public void clearBlur() {
        mBlurLayout.handleRecycle();
    }
}
