package com.qf.liuyong.lotto_android.model.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.qf.liuyong.lotto_android.model.http.cache.DiskCache;


import java.util.Map;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class RequestConfig {

    protected final DiskCache diskCache;
    protected Map<String, String> defaultParams;
    protected final RequestQueue requestQueue;
    protected final Context context;

    protected CharSequence networkErrorTips;
    protected CharSequence serverErrorTips;

    protected RequestConfig(Builder builder) {
        diskCache = builder.diskCache;
        defaultParams = builder.defaultParams;
        context = builder.context;

        if (builder.requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        } else {
            requestQueue = builder.requestQueue;
        }

        networkErrorTips = builder.networkErrorTips;
        serverErrorTips = builder.serverErrorTips;
    }

    public void updateDefaultParams(Map<String, String> defaultParams) {
        this.defaultParams = defaultParams;
    }


    public static RequestConfig createDefault(Context context) {
        return new Builder().build(context);
    }

    public static class Builder {

        private DiskCache diskCache = null;

        private Map<String, String> defaultParams = null;
        private RequestQueue requestQueue = null;
        private Context context = null;
        private CharSequence networkErrorTips = null;
        private CharSequence serverErrorTips = null;


        public RequestConfig build(Context context) {
            this.context = context;
            return new RequestConfig(this);
        }

        /**
         * 配置SD卡的缓存
         *
         * @param diskCache SD卡缓存的处理者
         */
        public Builder diskCache(DiskCache diskCache) {
            this.diskCache = diskCache;
            return this;

        }

        /**
         * 设置默认参数
         *
         * @param defaultParams 默认参数
         * @return
         */
        public Builder defaultParams(Map<String, String> defaultParams) {
            this.defaultParams = defaultParams;
            return this;
        }

        /**
         * 设置网络失败的提示信息
         *
         * @param tips 网络失败的提示信息
         */
        public Builder showTipsOnNetWorkError(CharSequence tips) {
            this.networkErrorTips = tips;
            return this;
        }

        /**
         * 设置服务器错误的提示信息
         *
         * @param tips 服务器错误的提示信息
         */
        public Builder showTipsOnServerError(CharSequence tips) {
            this.serverErrorTips = tips;
            return this;
        }


    }
}
