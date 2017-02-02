package com.qf.liuyong.lotto_android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.qf.liuyong.lotto_android.model.global.GlobalConfig;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class PreferencesUtils {

    /**
     * realName   真实姓名          portrait  头像            school       学校
     * identify   身份证号          mobile    手机号           workposition    工作
     * sex        性别              email     邮箱            inviter      邀请我的人
     * collectCount  我喜欢的       supportCount     我支持的      msgCount   消息个数
     * identityValidated    实名认证是否认证    emailValidated  邮箱是否认证  introduction 个人简介
     * number账号 pw密码
     */

    private static final String PREFERENCES_NAME = "config";
    private static SharedPreferences sPreferences;

    public static SharedPreferences getPreferences() {
        if (sPreferences == null) {
            sPreferences = GlobalConfig
                    .getContext()
                    .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        }

        return sPreferences;
    }

    /**
     * @return 是否含有key
     */
    public static boolean containsKey(String key) {
        return getPreferences().contains(key);
    }

    /**
     * @return 清除key
     */
    public static void removeKey(String key) {
        getPreferences().edit().remove(key).apply();
    }

    /**
     * @return 获取key对应的布尔值
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    /**
     * @return 存放key对应的布尔值
     */
    public static void putBoolean(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    /**
     * @return 获取key对应的整型值
     */
    public static int getInt(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    /**
     * @return 存放key对应的整型值
     */
    public static void putInt(String key, int value) {
        getPreferences().edit().putInt(key, value).apply();
    }

    /**
     * @return 获取key对应的长整型值
     */
    public static long getLong(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    /**
     * @return 存放key对应的长整型值
     */
    public static void putLong(String key, long value) {
        getPreferences().edit().putLong(key, value).apply();
    }

    /**
     * @return 获取key对应的字符串
     */
    public static String getString(String key) {
        return getString(key, null);
    }

    /**
     * @return 获取key对应的字符串
     */
    public static String getString(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    /**
     * @return 存放key对应的字符串
     */
    public static void putString(String key, String value) {
        getPreferences().edit().putString(key, value).apply();
    }

    /**
     * @return 存放key对应的字符串(线程阻塞)
     */
    public static void putStringImmediately(String key, String value) {
        getPreferences().edit().putString(key, value).commit();
    }

    /**
     * 清除所有key
     */
    public static void clearAll() {
        getPreferences().edit().clear().commit();
    }

    /**
     *退出登录
     */
    public static void quit(){
        removeKey("access_token");
        removeKey("loginPhoneNumber");
        removeKey("isLogin");
        removeKey("isShowPublishTopBar");
        removeKey("userId");
//        if (DemoHelper.getInstance().isLoggedIn()){
//            DemoHelper.getInstance().logout(true, null);
//        }
    }
}
