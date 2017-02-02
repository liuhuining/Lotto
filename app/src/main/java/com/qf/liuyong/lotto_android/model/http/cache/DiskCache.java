package com.qf.liuyong.lotto_android.model.http.cache;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public interface DiskCache extends Cache{
    public boolean put(String key, String value) throws IOException;

    public boolean put(String key, byte[] data) throws IOException;
}
