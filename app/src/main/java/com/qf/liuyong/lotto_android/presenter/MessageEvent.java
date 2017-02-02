package com.qf.liuyong.lotto_android.presenter;

/**
 * Created by Administrator on 2017/1/31 0031.
 */
public class MessageEvent {

    public String msg;
    public String id;
    public String type;

    public MessageEvent(String msg,String id,String type){
        this.msg = msg;
        this.id = id;
        this.type = type;
    }
}
