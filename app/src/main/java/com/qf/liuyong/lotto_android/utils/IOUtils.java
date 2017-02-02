package com.qf.liuyong.lotto_android.utils;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/1/30 0030.
 */
public class IOUtils {

    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;

    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * The number of bytes in a gigabyte.
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;

    /**
     * 合并文件夹
     *
     * @param srcDir   原文件夾
     * @param destdDir 目标文件夹
     * @throws java.io.IOException
     */
    public static void Merge(String srcDir, String destdDir) throws IOException {
        Merge(new File(srcDir), new File(destdDir));
    }

    /**
     * 合并文件夹
     *
     * @param srcDir   原文件夾
     * @param destdDir 目标文件夹
     * @throws java.io.IOException
     */
    public static void Merge(File srcDir, File destdDir) throws IOException {
        if (!srcDir.isDirectory()) {
            throw new IOException("srcDir is not a dir !");
        }
        if (!destdDir.exists() || !destdDir.isDirectory())
            destdDir.mkdirs();
        for (File file : srcDir.listFiles()) {
            Copy(file, destdDir);
        }
    }

    /**
     * 将文件/文件夹拷贝到制定目录
     *
     * @param src      原文件 /文件夹
     * @param destdDir 目标文件夹
     * @throws java.io.IOException
     */
    public static void Copy(String src, String destdDir, String rName)
            throws IOException {
        Copy(new File(src), new File(destdDir), rName);
    }

    /**
     * 将文件/文件夹拷贝到制定目录
     *
     * @param src      原文件 /文件夹
     * @param destdDir 目标文件夹
     * @throws java.io.IOException
     */
    public static void Copy(File src, File destdDir) throws IOException {
        Copy(src, destdDir, src.getName());
    }

    /**
     * 将文件/文件夹拷贝到制定目录
     *
     * @param src      原文件 /文件夹
     * @param destdDir 目标文件夹
     * @throws java.io.IOException
     */
    public static void Copy(File src, File destdDir, String rName)
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
                Copy(file, dir);
            }

        } else {
            InputStream inputStream = new FileInputStream(src);
            File destFile = new File(destdDir + File.separator + rName);
            destFile.createNewFile();
            OutputStream outputStream = new FileOutputStream(destFile);
            copyStream(inputStream, outputStream);
        }
    }

    /**
     * 将输入流数据拷贝到输出流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @throws java.io.IOException
     */
    public static void copyStream(InputStream inputStream,
                                  OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
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

    /**
     * 将一个输入流转换为字符串
     *
     * @param inputStream 输入流
     * @return 字符串
     * @throws java.io.IOException
     */
    public static String Stream2String(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        copyStream(inputStream, arrayOutputStream);
        return new String(arrayOutputStream.toByteArray());
    }

//-----------------------------------------------------------------------

    /**
     * Counts the size of a directory recursively (sum of the length of all files).
     *
     * @param directory directory to inspect, must not be <code>null</code>
     * @return size of directory in bytes, 0 if directory is security restricted
     * @throws NullPointerException if the directory is <code>null</code>
     */
    public static long sizeOfDirectory(File directory) {
        long size = 0;

        if (directory == null)
            return size;

        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            return 0L;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory()) {
                size += sizeOfDirectory(file);
            } else {
                size += file.length();
            }
        }

        return size;
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param directory directory to clean
     * @throws java.io.IOException in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }


    //-----------------------------------------------------------------------

    /**
     * Deletes a file. If file is a directory, delete it and all sub-directories.
     * <p/>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     * (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file file or directory to delete, must not be <code>null</code>
     * @throws NullPointerException          if the directory is <code>null</code>
     * @throws java.io.FileNotFoundException if the file was not found
     * @throws java.io.IOException           in case deletion is unsuccessful
     */
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                String message =
                        "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }
    //-----------------------------------------------------------------------

    /**
     * Deletes a directory recursively.
     *
     * @param directory directory to delete
     * @throws java.io.IOException in case deletion is unsuccessful
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (directory == null)
            return;
        if (!directory.exists()) {
            return;
        }

        cleanDirectory(directory);
        if (!directory.delete()) {
            String message =
                    "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }


    //-----------------------------------------------------------------------

    /**
     * Returns a human-readable version of the file size, where the input
     * represents a specific number of bytes.
     *
     * @param size the number of bytes
     * @return a human-readable display value (includes units)
     */
    public static String byteCountToDisplaySize(long size) {
        String displaySize;

        if (size / ONE_GB > 0) {
            displaySize = String.valueOf(size / ONE_GB) + " GB";
        } else if (size / ONE_MB > 0) {
            displaySize = String.valueOf(size / ONE_MB) + " MB";
        } else if (size / ONE_KB > 0) {
            displaySize = String.valueOf(size / ONE_KB) + " KB";
        } else {
//            displaySize = String.valueOf(size) + " bytes";
            displaySize = String.valueOf(size) + " MB";
        }
        return displaySize;
    }
}
