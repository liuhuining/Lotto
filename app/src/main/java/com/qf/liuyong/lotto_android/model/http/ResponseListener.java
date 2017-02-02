package com.qf.liuyong.lotto_android.model.http;

import com.qf.liuyong.lotto_android.model.http.exception.RequestError;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public interface ResponseListener<T> {
    public void onHandlerComplete(ResponseHandler handler, T t);

    public void onError(RequestError error);

    public void onCacheResponse(String data);

    public void onResponse(String data);
}
