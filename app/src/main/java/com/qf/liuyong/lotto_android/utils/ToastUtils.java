package com.qf.liuyong.lotto_android.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class ToastUtils {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void show(Context mContext, CharSequence text, int duration) {

        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, duration);
        mHandler.postDelayed(r, 1000);

        mToast.show();
    }

    public static void show(Context mContext, int resId, int duration) {
        show(mContext, mContext.getResources().getString(resId), duration);
    }
}
