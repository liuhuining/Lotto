package com.qf.liuyong.lotto_android.model.http;

import android.os.Handler;
import android.os.Looper;

import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.model.http.exception.RequestException;
import com.qf.liuyong.lotto_android.model.http.exception.RequestNotInitException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class JkhRequest {

    /**
     * 唯一的request对象
     */
    private volatile static JkhRequest instance;
    /**
     * request的配置信息
     */
    private RequestConfig requestConfig = null;
    private ExecutorService executor;

    /**
     * 获取配置信息
     *
     * @return 配置信息
     */
    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    private Handler handler;

    /**
     * 构造方法私有化
     */
    private JkhRequest() {
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler();

    }


    /**
     * 获取唯一的request对象
     *
     * @return 唯一的request对象
     */
    public static JkhRequest getInstance() {
        if (instance == null) {
            synchronized (JkhRequest.class) {
                if (instance == null) {
                    instance = new JkhRequest();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化配置信息
     *
     * @param config 配置信息
     */
    public void init(RequestConfig config) throws RequestException {
        if (requestConfig != null)
            throw new RequestException(config.context.getString(R.string.initialized));
        requestConfig = config;
    }


    public void request(RequestInfo info, ResponseHandler handler, ResponseListener listener) {
        request(info, handler, listener, null);

    }

    /**
     * 发送一个request请求
     *
     * @param info     请求信息
     * @param handler  响应信息的处理者
     * @param listener 请求监听
     */

    public void request(RequestInfo info, ResponseHandler handler, ResponseListener listener, UpdateListener updateListener) {
        if (Looper.myLooper() != Looper.getMainLooper())
            throw new RuntimeException();
        checkConfig();
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setResponseHandler(handler);
        responseInfo.setListener(listener);
        responseInfo.setUpdateListener(updateListener);
        // TODO info为null是应该抛出RunTimeException异常，保证info不为空
        info.requestClient = this;
        if (info.files != null) {
            execute(new MultipartTask(info, responseInfo, requestConfig));
            return;
        }

        int rMode = info.requestMode;
        if (rMode == RequestInfo.LOAD_CACHE || rMode == RequestInfo.REQUEST_NETWORK_AFTER_LOADCACHE || rMode == RequestInfo.REQUEST_NETWORK_IF_NO_CACHE) {
            execute(new LoadCacheTask(info, responseInfo, requestConfig));
        } else {
            execute(new NetworkTask(info, responseInfo, requestConfig));
        }
    }


    private void checkConfig() {
        if (requestConfig == null) {
            throw new RequestNotInitException();
        }
    }


    /**
     * 执行一个任务
     *
     * @param task
     */
    protected synchronized void execute(final Runnable task) {
        if (task == null)
            return;
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    execute(task);
                }
            });
            return;
        }
        if (task instanceof NetworkTask) {
            task.run();
        }
        if (task instanceof LoadCacheTask) {
            executor.execute(task);
        }
        if (task instanceof DataHandlerTask) {
            executor.execute(task);
        }
        if (task instanceof MultipartTask) {
            executor.execute(task);
        }
    }
}
