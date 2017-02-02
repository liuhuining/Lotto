package com.qf.liuyong.lotto_android.model.http;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.qf.liuyong.lotto_android.model.http.exception.RequestError;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class ResponseInfo {

    private final int HANDLER_COMPLETE = 1;
    private String networkResponse;
    private String cacheResponde;
    private ResponseHandler responseHandler;
    private ResponseListener listener;
    private Handler handler = null;
    private UpdateListener updateListener;

    public ResponseInfo() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (listener == null)
                    return;
                switch (msg.what) {
                    case HANDLER_COMPLETE:
                        try {
                            listener.onHandlerComplete(responseHandler, responseHandler.getResult());
                        } catch (Exception ex) {
                            listener.onError(new RequestError(ex));
                        }
                        break;
                }
            }
        };
    }

    public void runOnUIThread(
            Runnable runnable
    ) {
        handler.post(runnable);
    }

    public ResponseListener getListener() {
        return listener;

    }

    public void setListener(ResponseListener listener) {
        this.listener = listener;
    }

    protected void invokeOnHandlerComplete() {
        handler.sendEmptyMessage(HANDLER_COMPLETE);
    }

    public String getNetworkResponse() {
        return networkResponse;
    }

    public void setNetworkResponse(String networkResponse) {
        this.networkResponse = networkResponse;
    }

    public String getCacheResponde() {
        return cacheResponde;
    }

    public void setCacheResponde(String cacheResponde) {
        this.cacheResponde = cacheResponde;
    }

    //   public ResponseException getResponseException() {
    //     return responseException;
    //  }

    //  public void setResponseException(ResponseException responseException) {
    //    this.responseException = responseException;
    //}

    public String getResponse() {
        return TextUtils.isEmpty(networkResponse) ? cacheResponde : networkResponse;
    }

    public ResponseHandler getResponseHandler() {

        return responseHandler;
    }

    public void setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public UpdateListener getUpdateListener() {
        return updateListener;
    }
}
