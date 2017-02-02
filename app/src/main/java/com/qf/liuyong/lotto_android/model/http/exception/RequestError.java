package com.qf.liuyong.lotto_android.model.http.exception;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class RequestError extends Exception{

    public RequestError() {
    }

    public RequestError(String detailMessage) {
        super(detailMessage);
    }

    public RequestError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RequestError(Throwable throwable) {
        super(throwable);
    }
}
