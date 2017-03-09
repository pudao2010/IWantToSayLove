package com.bluewhaledt.saylove.base.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * I/O流操作工具类.
 *
 * @author DengZhaoyong
 * @version 1.0.0
 * @date 2012-9-12
 */
public class StreamUtils {

    /**
     * 将输入流转换为字节数组.
     *
     * @param is 输入流
     * @return
     */
    public static byte[] convertStreamToByteArray(InputStream is) {
        if (is == null) {
            return null;
        }

        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            closeOutputStream(baos);
        }
    }

    /**
     * 将输入流转换为字符串.
     *
     * @param is 输入流
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        byte[] byteArray = convertStreamToByteArray(is);
        if (byteArray != null && byteArray.length > 0) {
            try {
                return new String(byteArray, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 关闭输入流, 释放资源.
     *
     * @param is 输入流
     */
    public static void closeInputStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流, 释放资源.
     *
     * @param os 输出流
     */
    public static void closeOutputStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
