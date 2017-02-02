package com.qf.liuyong.lotto_android.model.http;

import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.model.http.exception.RequestError;
import com.qf.liuyong.lotto_android.model.http.utils.URLBuilder;
import com.qf.liuyong.lotto_android.utils.PreferencesUtils;
import com.qf.liuyong.lotto_android.utils.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class NetworkTask implements Response.Listener<String>, Response.ErrorListener, Runnable {

    private RequestInfo requestInfo = null;
    private RequestConfig config = null;
    private ResponseInfo responseInfo = null;
    private String mHeader;
    private String cookieFromResponse;

    public NetworkTask(RequestInfo requestInfo, ResponseInfo responseInfo, RequestConfig config) {
        this.requestInfo = requestInfo;
        this.responseInfo = responseInfo;
        this.config = config;
    }

    @Override
    public void run() {

        String requestUrl = requestInfo.url;
        final Map<String, String> requestParams = new HashMap<>();
        //先放默认参数
        if (config.defaultParams != null && requestInfo.withDefaultParams) {
            requestParams.putAll(config.defaultParams);
        }
        //后放请求参数，保证请求参数优先
        if (requestInfo.params != null) {
            requestParams.putAll(requestInfo.params);
        }

        if (requestInfo.method == Request.Method.GET) {
            requestUrl = URLBuilder.buildUrlWithParams(requestInfo.url, requestParams);
        }

        config.requestQueue.add(new StringRequest(requestInfo.method, requestUrl, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return new JSONObject(requestParams).toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("cookie", "_xh________u_c_t_t___________=" + PreferencesUtils.getString("cookie", ""));
                return headers;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    if (config.diskCache != null)
                        config.diskCache.put(URLBuilder.buildUrlWithParams(requestInfo.url, requestInfo.params), response.data);

                    mHeader = response.headers.toString();
                    //使用正则表达式从reponse的头中提取cookie内容的子串
                    Pattern pattern = Pattern.compile("Set-Cookie.*?;");
                    Matcher m = pattern.matcher(mHeader);
                    if (m.find()) {
                        cookieFromResponse = m.group();
                    }
                    //去掉cookie末尾的分号
                    if (!TextUtils.isEmpty(cookieFromResponse)){
                        cookieFromResponse = cookieFromResponse.substring(11, cookieFromResponse.length() - 1);
                        if (!TextUtils.isEmpty(cookieFromResponse)) {
                            String[] cookie = cookieFromResponse.split("=");
                            PreferencesUtils.putString("cookie", cookie[1]);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        }.setShouldCache(false).setRetryPolicy(new DefaultRetryPolicy(8 * 1000, 0, 0)));
    }

    @Override
    public void onResponse(String data) {
        if (responseInfo.getListener() != null) {
            responseInfo.getListener().onResponse(data);
        }
        responseInfo.setNetworkResponse(data.toString());
        requestInfo.requestClient.execute(new DataHandlerTask(responseInfo, false));

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        // responseInfo.setResponseException(new ResponseException(volleyError));

        if (volleyError instanceof NetworkError) {
            if (requestInfo.showTips && !TextUtils.isEmpty(config.networkErrorTips)) {
                ToastUtils.show(config.context, config.networkErrorTips, Toast.LENGTH_LONG);
            }
        } else if (volleyError instanceof ServerError) {
            if (requestInfo.showTips && !TextUtils.isEmpty(config.serverErrorTips))
                ToastUtils.show(config.context, config.serverErrorTips, Toast.LENGTH_LONG);
        } else if (volleyError instanceof TimeoutError) {
            ToastUtils.show(config.context, config.context.getResources().getString(R.string.network_request_timeout), Toast.LENGTH_LONG);
        }
        responseInfo.getListener().onError(new RequestError());
        int rMode = requestInfo.requestMode;
        if (rMode == RequestInfo.LOAD_CACHE_IF_NETWORK_ERROR) {
            requestInfo.requestClient.execute(new LoadCacheTask(requestInfo, responseInfo, requestInfo.requestClient.getRequestConfig()));
        }
    }
}
