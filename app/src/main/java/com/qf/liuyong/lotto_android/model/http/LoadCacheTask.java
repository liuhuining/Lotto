package com.qf.liuyong.lotto_android.model.http;



import android.text.TextUtils;

import com.qf.liuyong.lotto_android.model.http.cache.DiskCache;
import com.qf.liuyong.lotto_android.model.http.utils.IOUtils;
import com.qf.liuyong.lotto_android.model.http.utils.URLBuilder;

import java.io.InputStream;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class LoadCacheTask implements Runnable {

    private DiskCache cache = null;
    private RequestInfo requestInfo = null;
    private ResponseInfo responseInfo = null;
    private RequestConfig config = null;

    public LoadCacheTask(RequestInfo requestInfo, ResponseInfo responseInfo, RequestConfig config) {
        this.requestInfo = requestInfo;
        this.responseInfo = responseInfo;
        this.config = config;
    }

    @Override
    public void run() {
        String data = null;
        InputStream inputStream = config.diskCache.get(URLBuilder.buildUrlWithParams(requestInfo.url, requestInfo.params));
        if (inputStream != null) {
            data = IOUtils.readInStream(inputStream);
        }
        if (responseInfo.getListener() != null) {
            responseInfo.getListener().onCacheResponse(data);
        }
        responseInfo.setCacheResponde(data);
        requestInfo.requestClient.execute(new DataHandlerTask(responseInfo, true));
        int rMode = requestInfo.requestMode;
        if (rMode == RequestInfo.REQUEST_NETWORK_AFTER_LOADCACHE || (
                rMode == RequestInfo.REQUEST_NETWORK_IF_NO_CACHE && TextUtils.isEmpty(data)
        )) {

            requestInfo.requestClient.execute(new NetworkTask(requestInfo, responseInfo, config));
        }

//        if (TextUtils.isEmpty(data) && requestInfo.isFromCache()) {
//            new NetworkTask(requestInfo, responseInfo, config).run();
//        } else {
//
//            if (responseInfo.getListener() != null) {
//                responseInfo.getListener().onCacheResponse(data);
//            }
//        }
    }

}
