package com.qf.liuyong.lotto_android.model.http.utils;

import android.text.TextUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class URLBuilder {

    /**
     * 构建带有参数的URL
     *
     * @param url    原始URL
     * @param params 参数
     * @return
     */
    public static String buildUrlWithParams(String url, Map<String, String> params) {
        //    url = url + "?client_id=1&appkey=1&platform=1&channelid=1&client_token=1";
        if (TextUtils.isEmpty(url) || params == null || params.size() == 0)
            return url;
        StringBuffer buffer = new StringBuffer(url);
        int i = 0;
        for (Map.Entry<String, String> p : params.entrySet()) {

            String paramsItem = p.getKey() + "=" + p.getValue();
            if (i == 0) {
                buffer.append("?" + paramsItem);
            } else {
                buffer.append("&" + paramsItem);
            }

            i++;
        }
        return buffer.toString();
    }

    public static String buildCookieParams(Map<String, String> params) {
        if (params == null || params.size() == 0)
            return null;

        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for (Map.Entry<String, String> p : params.entrySet()) {

            String paramsItem = p.getKey() + "=" + p.getValue();
            if (i == 0) {
                buffer.append(paramsItem);
            } else {
                buffer.append("&" + paramsItem);
            }

            i++;
        }
        return buffer.toString();
    }

}
