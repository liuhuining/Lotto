package com.qf.liuyong.lotto_android.model.http;

import com.google.gson.Gson;
import com.qf.liuyong.lotto_android.model.http.exception.ResponseException;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class GsonHandler<T> extends BaseResponseHandler {

    protected T t;
    protected Class<T> clazz;
    protected Type type;
    protected boolean handled;

    public GsonHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    public GsonHandler(Type type) {
        this.type = type;
    }


    @Override
    public T handle(String data) throws Exception {

        return handleData(data);
    }

    protected synchronized T handleData(String json) throws Exception {

        handled = true;

        if (clazz != null) {
            if (clazz == String.class)
                t = (T) json;
            t = new Gson().fromJson(json, clazz);
        }
        if (type != null) {
            t = new Gson().fromJson(json, type);
        }
        return t;
    }


    @Override
    public T getResult() throws ResponseException {
        if (!handled)
            throw new ResponseException("不能再handle之前调用getResult");
        return t;
    }
}
