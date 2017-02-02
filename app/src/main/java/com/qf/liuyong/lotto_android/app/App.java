package com.qf.liuyong.lotto_android.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.qf.liuyong.lotto_android.model.global.GlobalConfig;
import com.qf.liuyong.lotto_android.model.http.JkhRequest;
import com.qf.liuyong.lotto_android.model.http.RequestConfig;
import com.qf.liuyong.lotto_android.model.http.cache.SimpleDiskCache;
import com.qf.liuyong.lotto_android.model.http.exception.RequestException;
import com.qf.liuyong.lotto_android.utils.CustomConstants;
import com.qf.liuyong.lotto_android.utils.PreferencesUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class App extends MultiDexApplication{

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public static JkhRequest request;
    public static App instance;
    private static Handler handler;

    public static Context applicationContext;
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    @Override
    public void onCreate() {
        super.onCreate();
//        /**极光推送 初始化*/
//        JkhRequestPushInterface.init(getApplicationContext());

        instance = this;
        initRequest();
        // Application context
        GlobalConfig.initContext(this);

//        //环信添加 start
//        applicationContext = this;
//        instance = this;
//        //init demo helper
//        DemoHelper.getInstance().init(applicationContext);
//        //环信添加 end

        removeTempFromPref();
    }

    public static App getInstance() {
        return instance;
    }

    /**
     * 相册
     */
    private void removeTempFromPref() {
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        sp.edit().remove(CustomConstants.PREF_TEMP_IMAGES).apply();
    }

    private void initRequest() {
        request = JkhRequest.getInstance();
        SimpleDiskCache requestDiskCache = new SimpleDiskCache(getCacheDir().getPath() + File.separator + "api");
        try {
            RequestConfig requestConfig = new RequestConfig.Builder()
                    .diskCache(requestDiskCache)
                    .defaultParams(getDefaultParams())
                    .showTipsOnNetWorkError("网络错误")
                    .showTipsOnServerError("服务器错误")
                    .build(this);

            request.init(requestConfig);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取请求默认参数
     */
    public Map<String, String> getDefaultParams() {
        return new HashMap<String, String>();
    }

    public double getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return Double.valueOf(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getImageCachePath() {
        return getCacheDir() + File.separator + "image";
    }

    public boolean ExistSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean post(Runnable runnable) {
        if (handler == null) {
            synchronized (App.class) {
                handler = new Handler(Looper.getMainLooper());
            }
        }
        return handler.post(runnable);
    }

    @Override
    public File getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return getExternalCacheDir();
        return super.getCacheDir();
    }

    /**
     * 判断是否第一次打开App
     */
    public boolean isFirst() {
        return PreferencesUtils.getBoolean("first", true);
    }

    public long getSavedTime(String key) {
        return PreferencesUtils.getLong(key, System.currentTimeMillis());
    }

    public void saveCurrentTime(String key) {
        PreferencesUtils.putLong(key, System.currentTimeMillis());
    }

    /**
     * 第一次启动应用后，改变为false
     */
    public void setFirst() {
        PreferencesUtils.putBoolean("first", false);
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    private SimpleDateFormat dateFormater2 = (SimpleDateFormat) SimpleDateFormat.getDateInstance();

    /**
     * 以友好的方式显示时间
     */
    public String friendly_time(long sdate) {
        dateFormater2.applyLocalizedPattern("yyyy-MM-dd");
        Date time = new Date(sdate);
        String ftime = "";
        Calendar cal = Calendar.getInstance();
        //判断是否是同一天
        String curDate = dateFormater2.format(cal.getTime());
        String paramDate = dateFormater2.format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }
        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.format(time);
        }
        return ftime;
    }
}
