package com.qf.liuyong.lotto_android.model.http;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public interface UpdateListener {
    /**
     * @param current  当前传输大小
     * @param total    总大小
     * @param progress 当前进度
     */
    public void onUpdateProgress(long current, long total, int progress);
}
