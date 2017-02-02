package com.qf.liuyong.lotto_android.model.http.exception;

import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class ResponseException extends Exception{

    public ResponseException() {
    }

    public ResponseException(VolleyError error) {
    }

    public ResponseException(String s) {
        super(s);
    }
}
