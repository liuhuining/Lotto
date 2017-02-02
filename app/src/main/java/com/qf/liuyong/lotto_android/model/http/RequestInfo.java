package com.qf.liuyong.lotto_android.model.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class RequestInfo implements Cloneable{

    protected final String url;
    protected final Map<String, String> params;
    protected final int method;
    protected Map<String, File> files;
    /**
     * 请求网络
     */
    public static final int REQUEST_NETWORK = 0;
    /**
     * 加载缓存
     */
    public static final int LOAD_CACHE = 1;
    /**
     * 先加载缓存再请求网络
     */
    public static final int REQUEST_NETWORK_AFTER_LOADCACHE = 2;
    /**
     * 先加载缓存，如果没有缓存则请求网络
     */
    public static final int REQUEST_NETWORK_IF_NO_CACHE = 3;
    /**
     * 先请求网络，如果网络失败则加载缓存
     */
    public static final int LOAD_CACHE_IF_NETWORK_ERROR = 4;


    protected int requestMode = REQUEST_NETWORK_AFTER_LOADCACHE;

    protected JkhRequest requestClient;

    protected boolean showTips;

    protected boolean withDefaultParams;

    private RequestInfo(Builder builder) {
        url = builder.url;
        params = builder.params;
        method = builder.method;
        requestMode = builder.requestMode;
        showTips = builder.showTips;
        withDefaultParams = builder.withDefaultParams;
        files = builder.files;
    }


    public static class Builder {
        private String url;
        private Map<String, String> params;
        private Map<String, File> files;
        private int method;

        private int requestMode = REQUEST_NETWORK_AFTER_LOADCACHE;
        private boolean showTips = true;
        private boolean withDefaultParams = true;

        public RequestInfo build() {
            return new RequestInfo(this);
        }

        public Builder url(String url) {

            this.url = url;
            return this;

        }

        public Builder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder method(int method) {
            this.method = method;
            return this;
        }

        public Builder addFile(String name, File file) {
            if (files == null)
                files = new HashMap<String, File>();
            files.put(name, file);
            return this;
        }

        /**
         * 上传多个文件
         */
        public Builder addFiles(String name, List<File> file) {
            if (files == null)
                files = new HashMap<String, File>();
            int id = 0;
            for (File f : file) {
                files.put(name+"_"+id, f);
                id++;
            }
            return this;
        }

        /**
         * 是否显示提示信息
         *
         * @param show true 为显示  flase  不显示
         */
        public Builder showTips(boolean show) {
            this.showTips = show;
            return this;
        }

        /**
         * 是否添加默认参数
         *
         * @param with true表示添加 flase表示 不添加
         */
        public Builder withDefaultParams(boolean with) {
            this.withDefaultParams = with;
            return this;
        }

        /**
         *
         */


        /**
         * 设置请求模式
         *
         * @param mode 为{@link #REQUEST_NETWORK}
         *             ,{@link #LOAD_CACHE}
         *             ,{@link #REQUEST_NETWORK_AFTER_LOADCACHE}
         *             ,{@link #REQUEST_NETWORK_IF_NO_CACHE}
         *             ,{@link #LOAD_CACHE_IF_NETWORK_ERROR}其中之一
         */
        public Builder requestMode(int mode) {
            this.requestMode = mode;
            return this;
        }
    }
}
