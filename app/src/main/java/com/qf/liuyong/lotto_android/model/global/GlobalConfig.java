package com.qf.liuyong.lotto_android.model.global;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class GlobalConfig {

    private static Context sContext;

    //upload video timeout
    public static final int DEFAULT_TIMEOUT = 30 * 60 * 1000;

    public static int pushNotificationId = 0;

    public static Context getContext() {
        return sContext;
    }

    public static void initContext(Application application) {
        sContext = application.getApplicationContext();
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    public static int getScreenWidthPxs() {
        return getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeightPxs() {
        return getDisplayMetrics().heightPixels;
    }


    public static int getDimens(int dimenId) {
        return Math.round(getResources().getDimension(dimenId));
    }

    public static int getInteger(int resId) {
        return getResources().getInteger(resId);
    }

    public static int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static String getString(int id, Object... args) {
        return getResources().getString(id, args);
    }

    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    public static String getPackageName() {
        return GlobalConfig.getContext().getPackageName();
    }
}
