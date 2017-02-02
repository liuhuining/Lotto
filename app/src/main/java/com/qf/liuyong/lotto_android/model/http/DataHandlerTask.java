package com.qf.liuyong.lotto_android.model.http;

import com.qf.liuyong.lotto_android.model.http.exception.RequestError;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class DataHandlerTask implements Runnable {

    private ResponseInfo responseInfo;
    private boolean fromCahce = false;

    public DataHandlerTask(ResponseInfo responseInfo, boolean fromCache) {
        this.responseInfo = responseInfo;
        this.fromCahce = fromCache;
    }

    @Override
    public void run() {
        try {
            ResponseHandler handler = responseInfo.getResponseHandler();
            if (handler == null) {
                handler = new StringResponseHandler();
                responseInfo.setResponseHandler(handler);
            }
            handler.handle(responseInfo, fromCahce);
        } catch (final Exception e) {

            responseInfo.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (responseInfo.getListener() != null) {
                        responseInfo.getListener().onError(new RequestError(e));
                    }
                }
            });
//            e.printStackTrace();
        }
        responseInfo.invokeOnHandlerComplete();
    }
}
