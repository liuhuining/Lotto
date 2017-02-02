package com.qf.liuyong.lotto_android.model.http;

import com.qf.liuyong.lotto_android.model.http.exception.ResponseException;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class StringResponseHandler extends BaseResponseHandler<String> {

    private String result = null;

    @Override
    public String handle(String data) {

        return result = data;
    }

    @Override
    public String getResult() throws ResponseException {
        return result;
    }
}
