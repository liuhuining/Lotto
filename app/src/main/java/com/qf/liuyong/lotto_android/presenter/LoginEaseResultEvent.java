package com.qf.liuyong.lotto_android.presenter;

/**
 * Created by Administrator on 2017/1/31 0031.
 */
public class LoginEaseResultEvent {

    private boolean isLoginSuccess;
    private String userId;
    private String phoneNum;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LoginEaseResultEvent(boolean isLoginSuccess, String userId,String phoneNum) {
        this.isLoginSuccess = isLoginSuccess;
        this.userId = userId;
        this.phoneNum = phoneNum;
    }

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }

    public void setIsLoginSuccess(boolean isLoginSuccess) {
        this.isLoginSuccess = isLoginSuccess;
    }
}
