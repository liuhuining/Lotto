package com.qf.liuyong.lotto_android.model.http.cache;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public interface Cache {

    /**
     * 缓存一个输入流
     * @param key 作为缓存的key ，<br/> 一般为http请求的URL ＋ params
     * @param inputStream   输入流
     *                      @return <br>true</br> - 缓存成功 ;<br>false</br> -缓存失败;
     * @throws IOException
     */
    public boolean  put(String key, InputStream inputStream) throws IOException;

    /**
     * 获取缓存内容
     * @param key 作为缓存的key ，<br/> 一般为http请求的URL ＋ params
     * @return 缓存内容 若返回<br>null<br/>则无缓存
     */
    public InputStream get(String key);


    public void remove(String key);
}
