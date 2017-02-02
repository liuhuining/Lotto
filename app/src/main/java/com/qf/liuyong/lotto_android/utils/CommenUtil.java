package com.qf.liuyong.lotto_android.utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/31 0031.
 */
public class CommenUtil {

    /**
     * 判断字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    public static String list2String(List SceneList) {
        try {
            // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // 然后将得到的字符数据装载到ObjectOutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
            objectOutputStream.writeObject(SceneList);
            // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
            String sceneListString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            // 关闭objectOutputStream
            objectOutputStream.close();
            return sceneListString;
        } catch (Exception e) {
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static List string2List(String SceneListString) {
        try {
            byte[] mobileBytes = Base64.decode(SceneListString.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            List sceneList = (List) objectInputStream.readObject();
            objectInputStream.close();
            return sceneList;
        } catch (Exception e) {
        }
        return null;
    }
}
