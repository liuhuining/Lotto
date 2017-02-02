package com.qf.liuyong.lotto_android.model.http.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class IOUtils {

    private static final int BUFSZ = 8196;


    /**
     * 将文件/文件夹拷贝到制定目录
     *
     * @param src      原文件 /文件夹
     * @param destdDir 目标文件夹
     * @throws IOException
     */
    public static void copy(File src, File destdDir) throws IOException {
        copy(src, destdDir, src.getName());
    }

    /**
     * 将文件/文件夹拷贝到制定目录
     *
     * @param src      原文件 /文件夹
     * @param destdDir 目标文件夹
     * @throws IOException
     */
    public static void copy(File src, File destdDir, String rName)
            throws IOException {
        if (!src.exists())
            throw new FileNotFoundException();
        if (!destdDir.exists() || !destdDir.isDirectory())
            destdDir.mkdirs();
        if (TextUtils.isEmpty(rName))
            rName = src.getName();
        if (src.isDirectory()) {
            File dir = new File(destdDir + File.separator + rName);
            for (File file : src.listFiles()) {
                copy(file, dir);
            }

        } else {
            InputStream inputStream = new FileInputStream(src);
            File destFile = new File(destdDir + File.separator + rName);
            destFile.createNewFile();
            OutputStream outputStream = new FileOutputStream(destFile);
            copyStream(outputStream, inputStream);
        }
    }

    public static void copyStream(OutputStream out, InputStream in, int bufsz)
            throws IOException {
        byte[] buf = new byte[bufsz];
        int n = 0;
        while ((n = in.read(buf)) > 0) {
            out.write(buf, 0, n);
        }
    }

    public static void copyStream(OutputStream out, InputStream in)
            throws IOException {
        copyStream(out, in, BUFSZ);
    }

    /**
     * 把二进制数据写入文件
     *
     * @param data 要写入的数据
     * @param file 目标文件
     */
    public static void writeToFile(byte[] data, File file) {


        FileOutputStream outputStream;
        try {
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            outputStream.write(data);

            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String readFromFile(String path) {

        File file = new File(path);
        if (!file.exists())
            return "";
        try {
            return readInStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readInStream(InputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i("FileTest", e.getMessage());
        }
        return null;
    }

}
