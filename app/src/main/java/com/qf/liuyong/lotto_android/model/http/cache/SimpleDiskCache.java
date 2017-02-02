package com.qf.liuyong.lotto_android.model.http.cache;

import com.qf.liuyong.lotto_android.model.http.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/1/29 0029.
 */
public class SimpleDiskCache implements DiskCache {

    private String mCachePath;

    private File mCacheDir;

    public SimpleDiskCache(String cachePath) {
        this.mCachePath = cachePath;
        verifyPath();
    }


    private void verifyPath() {
        try {
            mCacheDir = new File(mCachePath);
            if (mCacheDir.exists() && mCacheDir.isDirectory())
                return;
            mCacheDir.mkdirs();
        } catch (Exception ex) {
        }
    }


    public File getCacheDir() {
        return mCacheDir;
    }

    @Override
    public boolean put(String key, InputStream inputStream) throws IOException {


        try {
            String path = getCacheDir().getPath() + File.separator + key.hashCode();
            createFile(path);
            FileOutputStream outputStream = new FileOutputStream(path);
            IOUtils.copyStream(outputStream, inputStream);
        } catch (Exception ex) {
        } finally {
            // outputStream.flush();
            inputStream.close();
        }
        return false;
    }


    private void createFile(String path) {
        File file = new File(path);
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public InputStream get(String key) {

        String path = getCacheDir().getPath() + File.separator + key.hashCode();
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(String key) {

        String path = getCacheDir().getPath() + File.separator + key;

        File file = new File(path);
        if (file.exists())
            file.delete();
    }

    @Override
    public boolean put(String key, String value) throws IOException {

        return put(key, value.getBytes());

    }

    @Override
    public boolean put(String key, byte[] data) throws IOException {
        return put(key, new ByteArrayInputStream(data));
    }
}
