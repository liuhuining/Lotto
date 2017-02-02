package com.qf.liuyong.lotto_android.model.http;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public abstract class BaseResponseHandler<T> implements ResponseHandler {

    private boolean fromCache;

    @Override
    public boolean isFromCache() {
        return fromCache;
    }

    @Override
    public T handle(ResponseInfo responseInfo, boolean fromCache) throws Exception {
        this.fromCache = fromCache;
        return handle(isFromCache() ? responseInfo.getCacheResponde() : responseInfo.getNetworkResponse());
    }

    public abstract T handle(String data) throws Exception;
}
