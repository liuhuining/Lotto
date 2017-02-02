package com.qf.liuyong.lotto_android.model.http.internal;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class PageData<T> {

    private int errorCode;
    private String errorMessage;
    private PageInfo page;
    private T t;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }
}
