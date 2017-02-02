package com.qf.liuyong.lotto_android.presenter;

/**
 * Created by Administrator on 2017/1/31 0031.
 */
public class LoginEaseEvent {

    private String name;
    private String password;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LoginEaseEvent(String name, String password, String userId) {
        this.name = name;
        this.password = password;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
