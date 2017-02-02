package com.qf.liuyong.lotto_android.model.http;

import com.qf.liuyong.lotto_android.model.http.exception.ResponseException;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public interface ResponseHandler<T> {
    public T handle(ResponseInfo responseInfo, boolean fromCache) throws Exception;

    public T getResult() throws ResponseException;

    public boolean isFromCache();
}
