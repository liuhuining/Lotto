package com.qf.liuyong.lotto_android.view.topbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qf.liuyong.lotto_android.R;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class TopBarView extends RelativeLayout implements View.OnClickListener {
    private TextView mTitle;
    private TextView mSubTitle;
    private ImageView mBack;
    private ImageView mAction;
    private ImageView mTitleLogo;
    private View right_top;
    private TextView mNotConfirmed;
    private TextView mConfirmed;
    private ImageView mark;

    public TopBarView(Context context) {
        super(context);
    }

    public TopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.topbar, this);
        setBackgroundResource(R.drawable.top_bar);
        mTitle = (TextView) findViewById(R.id.title);
        mSubTitle = (TextView) findViewById(R.id.subtitle);
        mBack = (ImageView) findViewById(R.id.back);
        mAction = (ImageView) findViewById(R.id.action);
        mTitleLogo = (ImageView) findViewById(R.id.logo);

        right_top = (View) findViewById(R.id.right_top);
        mNotConfirmed = (TextView)findViewById(R.id.not_confirmed);
        mConfirmed = (TextView) findViewById(R.id.has_confirmed);
        mark = (ImageView) findViewById(R.id.spefic_mark);

        mBack.setOnClickListener(this);
        mAction.setOnClickListener(this);
        mNotConfirmed.setOnClickListener(this);
        mConfirmed.setOnClickListener(this);
        mark.setOnClickListener(this);
        setOnClickListener(this);
    }

    public void setCustomView(int layoutId){
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(layoutId,this);
    }

    public void setCustomView(View view){
        setCustomView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setCustomView(View view, ViewGroup.LayoutParams params){
        removeAllViews();
        addView(view, params);
    }

    public void removeCustomViews(){
        removeAllViews();
        init();
    }

    public void setTitle(int resid){
        mTitle.setText(resid);
    }

    public void setTitle(CharSequence text){
        mTitle.setText(text);
    }

    public void setTitleColor(int color){
        mTitle.setTextColor(color);
    }

    public void setmTitlebg(){

    }
    public void setSubTitle(int resid) {
        mSubTitle.setText(resid);
    }

    public void setSubTitle(CharSequence text) {
        mSubTitle.setText(text);
    }

    /**
     * 是否显示标题
     *
     * @param show true -->  VISIBLE
     *             <p>false -->   GONE
     */
    public void showTitle(boolean show) {
        showView(mTitle, show);
    }

    /**
     * 是否显示副标题
     *
     * @see #showTitle(boolean)
     */
    public void showSubTitle(boolean show) {
        showView(mSubTitle, show);
    }

    /**
     * 是否显示View
     *
     * @param view
     * @param show : true -->  VISIBLE
     *             <p>false -->   GONE
     */
    private void showView(View view, boolean show){
        if (show){
            view.setVisibility(VISIBLE);
        }else {
            view.setVisibility(GONE);
        }
    }


    public interface OnActionListener{
        void onBack();

        void onAction();

        void onClick();

        void onLeftClick();
        void onRightClick();
        void onMarkClick();
    }

    private OnActionListener mOnActionListener;

    public void setmOnActionListener(OnActionListener onActionListener){
        mOnActionListener = onActionListener;
    }

    @Override
    public void onClick(View v) {
        if (mOnActionListener == null){
            return;
        }
        int i = v.getId();
        if (i == R.id.back){
            mOnActionListener.onBack();
        }else if (i == R.id.action){
            mOnActionListener.onAction();
        }else if (i == R.id.not_confirmed){
            mOnActionListener.onLeftClick();
        }else if (i == R.id.has_confirmed){
            mOnActionListener.onRightClick();
        }else if (i == R.id.spefic_mark){
            mOnActionListener.onMarkClick();
        }else{
            mOnActionListener.onClick();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTitle.setPadding(getPaddingLeft(),getPaddingTop(),getPaddingRight(),getPaddingBottom());


    }

    public CharSequence getTitle(){
        return mTitle.getText();
    }
    public void setLogo(int resId) {
        mTitle.setBackgroundDrawable(getResources().getDrawable(resId));
    }

    public void setTitleLogo(int resId) {
        mTitleLogo.setImageResource(resId);
    }

    public void showTitleLogo(boolean show) {
        // mTitleLogo.setVisibility(show);
        showView(mTitleLogo, show);
    }
    public void showBack(boolean show) {
        showView(mBack, show);
    }

    public void showAction(boolean show) {
        showView(mAction, show);
    }

    public void showMark(boolean show) {
        showView(mark, show);
    }

    public void showRightButton(boolean show) {
        showView(right_top, show);
    }

    public void hide() {

        showView(this, false);
    }

    public void show() {
        showView(this, true);
    }

    public void setActionImageDrawable(Drawable drawable) {
        mAction.setImageDrawable(drawable);
    }

    public void setActionImageResource(int resId) {
        mAction.setImageResource(resId);
    }

    public void setBackImageDrawable(Drawable drawable) {
        mBack.setImageDrawable(drawable);
    }

    public void setBackImageResource(int resId) {
        mBack.setImageResource(resId);
    }

    public void setMarkImageResource(int resId) {
        mark.setImageResource(resId);
    }


    public View getLeftView() {
        return mBack;
    }

    public View getRightView() {
        return mAction;
    }

    public View getRightButtonView() {
        return mConfirmed;
    }

    public View getLeftButtonView() {
        return mNotConfirmed;
    }

}
