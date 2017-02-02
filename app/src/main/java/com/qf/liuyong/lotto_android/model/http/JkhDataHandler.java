package com.qf.liuyong.lotto_android.model.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.qf.liuyong.lotto_android.model.http.exception.ResponseException;
import com.qf.liuyong.lotto_android.model.http.internal.PageData;
import com.qf.liuyong.lotto_android.model.http.internal.PageInfo;
import com.qf.liuyong.lotto_android.utils.PreferencesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class JkhDataHandler<T> extends GsonHandler<PageData> {

    private String fieldName;

    private PageData pageData;

    public JkhDataHandler(Class<PageData> clazz) {

        this(clazz, "data");
    }

    public JkhDataHandler(Type type) {
        this(type, "data");
    }

    public JkhDataHandler(Class<PageData> clazz, String fieldName) {
        super(clazz);
        this.fieldName = fieldName;
    }


    public JkhDataHandler(Type type, String fieldName) {
        super(type);
        this.fieldName = fieldName;
    }

    @Override
    public PageData handle(String data) throws Exception {
        if (TextUtils.isEmpty(data))
            return null;

        JSONObject jsonObject = new JSONObject(data);
        pageData = new PageData();
        String msg = jsonObject.optString("resMsg");

        pageData.setErrorMessage(msg);
        int rs = jsonObject.optInt("resCode");
        pageData.setErrorCode(rs);
        if (rs != 1) {
            // throw new RsError(rs);
        }

//        //登录状态过期退出登录
//        if (rs == 3002) {
//            pageData.setErrorMessage("登录状态过期");
//            PreferencesUtils.quit();
//            App.instance.post(new Runnable() {
//                @Override
//                public void run() {
//                    //清除我的界面登录状态(刷新我的界面)
//                    BusProvider.getInstance().post(new MessageEvent(null, null, "login"));
//                    //通知登录状态过期
//                    BusProvider.getInstance().post(new TokenOutOfDateEvent());
//                }
//            });
//            return pageData;
//        }

        String page = jsonObject.optString("info").trim();

        PageInfo pageInfo = null;
        if (!TextUtils.isEmpty(page)) {
            Gson gson = new Gson();
            pageInfo = gson.fromJson(page, PageInfo.class);

        }
        pageData.setPage(pageInfo);
        String res = jsonObject.optString(fieldName).trim();

        if (type == null && res.startsWith("[")) {
            res = new JSONArray(res).get(0).toString();
        }

        T content = (T) super.handleData(res);


        pageData.setT(content);

        return pageData;
    }


    @Override
    public PageData getResult() throws ResponseException {
        return pageData;
    }
}
