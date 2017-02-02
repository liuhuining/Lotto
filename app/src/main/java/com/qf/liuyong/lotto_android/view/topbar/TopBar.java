package com.qf.liuyong.lotto_android.view.topbar;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public abstract class TopBar {

    /**
     * 点击返回按钮
     */
    public static final int FEARURE_BACK = 0X1;
    /**
     * 点击Action按钮
     */
    public static final int FEATRUE_ACTION = 0X2;

    /**
     * 点击TopBar
     */
    public static final int FEATRUE_CLICK = 0X3;

    /**
     * 点击左侧button
     */
    public static final int LEFT_BUTTON_CLICK = 0X4;

    /**
     * 点击右侧button
     */
    public static final int RIGHT_BUTTON_CLICK = 0X5;

    /**
     * 点击注意按钮
     */
    public static final int FEATURE_MARK = 0X6;


    OnItemSelectedListener mOnItemSelectedListener;

    /**
     * 设置TopBar的标题
     * <p> 默认为显示 ， {@link #showTitle(boolean)}决定是否显示
     *
     * @param resId 标题显示文本的资源Id
     * @see #setTitle(CharSequence)
     */
    public abstract void setTitle(int resId);

    /**
     * 获取TopBar 标题
     *
     * @return
     */
    public abstract CharSequence getTitle();

    /**
     * 设置TopBar的标题
     * <p> 默认为显示 ， {@link #showTitle(boolean)}决定是否显示
     *
     * @param text 标题显示的文本
     * @see #setTitle(int)
     */
    public abstract void setTitle(CharSequence text);

    /**
     * 设置title 颜色
     *
     * @param color 颜色
     */
    public abstract void setTitleColor(int color);

    /**
     * 设置TopBar的副标题
     * <p> 默认为不显示 ， {@link #showSubTitle(boolean)}决定是否显示
     *
     * @param resid 副标题显示文本的资源Id
     * @see #setSubTitle(CharSequence)
     */
    public abstract void setSubTitle(int resid);

    /**
     * 设置TopBar的副标题
     * <p> 默认为不显示 ， {@link #showSubTitle(boolean)}决定是否显示
     *
     * @param text 副标题显示的文本
     * @see #setSubTitle(int)
     */
    public abstract void setSubTitle(CharSequence text);

    /**
     * 设置TopBar的标题是否显示
     * <p>显示内容由 {@link #setTitle(CharSequence)}{@link #setTitle(int)} 决定
     *
     * @param show <p> true --> 显示</p> <p> false --> 不显示</p>
     */
    public abstract void showTitle(boolean show);

    /**
     * 设置TopBar的副标题是否显示
     * <p>显示内容由 {@link #setSubTitle(CharSequence)}{@link #setSubTitle(int)} 决定
     *
     * @param show <p> true --> 显示</p> <p> false --> 不显示</p>
     */
    public abstract void showSubTitle(boolean show);

    /**
     * 设置是否显示返回
     *
     * @param show <p> true --> 显示</p> <p> false --> 不显示</p>
     */
    public abstract void showBack(boolean show);

    /**
     * 设置是否显示Action         默认不显示
     * 可通过 {@link #setActionImageResource(int)}
     * {@link #setActionImageDrawable(Drawable)} 设置Action的Icon
     *
     * @param show <p> true --> 显示</p> <p> false --> 不显示</p>
     */
    public abstract void showAction(boolean show);

    public abstract void showMark(boolean show);

    public abstract void showRightButton(boolean show);
    /**
     * 设置TopBar的自定义视图
     *
     * @param view TopBar要显示的内容View
     * @see #setCustomView(int)
     * @see #setCustomView(View, ViewGroup.LayoutParams)
     */
    public abstract void setCustomView(View view);

    /**
     * 设置TopBar的自定义视图
     *
     * @param layoutId TopBar要显示的内容View的layoutId
     * @see #setCustomView(View)
     * @see #setCustomView(View, ViewGroup.LayoutParams)
     */
    public abstract void setCustomView(int layoutId);

    /**
     * 设置TopBar的自定义视图
     *
     * @param view   TopBar要显示的内容View
     * @param params 显示View的LayoutParams
     * @see #setCustomView(int)
     * @see #setCustomView(View)
     */
    public abstract void setCustomView(View view, ViewGroup.LayoutParams params);

    public abstract void addView(View view, RelativeLayout.LayoutParams params);

    public void setOnTopBarSelectedListener(OnItemSelectedListener onTopBarSelectedListener){
        mOnItemSelectedListener = onTopBarSelectedListener;
    }

    public interface OnItemSelectedListener{
        /**
         * TopBar动作回调
         * @param featrueId 动作Id
         */
        void onItemSelected(int featrueId);
    }

    /**
     * 移除自定义的视图
     * <p/>  自定义的视图可以通过
     * {@link #setCustomView(int)}
     * {@link #setCustomView(View)}
     * {@link #setCustomView(View, ViewGroup.LayoutParams)} 设置
     */
    public abstract void removeCustomViews();

    /**
     * 隐藏TopBar     可以通过{@link #show()} 显示TopBar
     */
    public abstract void hide();

    /**
     * 显示TopBar  可以通过{@link #hide()} 隐藏TopBar
     */
    public abstract void show();

    /**
     * 设置用来替换Activity的ContentView
     * {@link android.app.Activity#setContentView(int)}
     *
     * @param layoutResId View 的layoutId
     * @see #setContentView(View)
     * @see #setContentView(View, ViewGroup.LayoutParams)
     */
    public abstract void setContentView(int layoutResId);

    /**
     * 设置用来替换Activity的ContentView
     * {@link android.app.Activity#setContentView(View, ViewGroup.LayoutParams)}
     *
     * @param view   填充的View
     * @param params 填充View 的layoutParams
     * @see #setContentView(int)
     * @see #setContentView(View)
     */
    public abstract void setContentView(View view, ViewGroup.LayoutParams params);

    /**
     * 设置用来替换Activity的ContentView
     * {@link android.app.Activity#setContentView(View)}
     *
     * @param view 填充的View
     * @see #setContentView(int)
     * @see #setContentView(View, ViewGroup.LayoutParams)
     */
    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置TopBar的背景色
     *
     * @param color 背景色
     *              {@link android.graphics.Color#parseColor(String)}
     *              {@link  android.content.res.Resources#getColor(int)}
     * @see #setBackground(Drawable)
     * @see #setBackgroundResource(int)
     */
    public abstract void setBackgroundColor(int color);

    /**
     * 设置TopBar的背景
     *
     * @param background 背景
     * @see #setBackgroundColor(int)
     * @see #setBackgroundResource(int)
     */
    public abstract void setBackground(Drawable background);

    /**
     * 设置TopBar的背景
     *
     * @param resId 背景的资源Id
     * @see #setBackgroundColor(int)
     * @see #setBackgroundResource(int)
     */
    public abstract void setBackgroundResource(int resId);

    /**
     * 设置ActionImage图标
     *
     * @param resId 图标的资源 Id
     * @see #setActionImageDrawable(Drawable)
     */
    public abstract void setActionImageResource(int resId);

    public abstract void setMarkImageResource(int resId);

    /**
     * 设置ActionImage图标
     *
     * @param drawable 图标的资源
     * @see #setActionImageResource(int)
     */
    public abstract void setActionImageDrawable(Drawable drawable);

    /**
     * 设置Back图标
     *
     * @param resId 图标的资源 Id
     * @see #setBackImageDrawable(Drawable)
     */
    public abstract void setBackImageResource(int resId);

    /**
     * 设置Back图标
     *
     * @param drawable 图标的资源
     * @see #setBackImageResource(int)
     */
    public abstract void setBackImageDrawable(Drawable drawable);

    public abstract void showAtTop();

    public abstract void showAtBottom();

    public abstract void floating();

    public abstract View getLeftView();

    public abstract View getRightView();

    public abstract View getRootView();

    public abstract View getRightButtonView();

    public abstract View getLeftButtonView();

    /*public abstract View getMarkView();*/

    public abstract void setLogo(int resId);

    public abstract void setTitleLogo(int resId);

    public abstract void showTitleLogo(boolean show);
    public abstract void blur();

    public abstract void clearBlur();

}
