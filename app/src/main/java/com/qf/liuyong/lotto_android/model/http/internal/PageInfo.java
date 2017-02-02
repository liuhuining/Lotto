package com.qf.liuyong.lotto_android.model.http.internal;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class PageInfo {

    private String status;
    private boolean isLast;
    private String pageNo;
    private String pageSize;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getStatus() {
        return status;
    }

    public boolean isIsLast() {
        return isLast;
    }

    public String getPageNo() {
        return pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }
}
