package com.bluewhaledt.saylove.network.utils;

/**
 * 字符串工具类.
 *
 * @author DengZhaoyong
 * @version 1.0.0
 * @date 2012-9-12
 */
public class NetworkStringUtils {

    private NetworkStringUtils() {
    }

    /**
     * 判断字符是否为null或空串.
     *
     * @param src 待判断的字符
     * @return
     */
    public static boolean isEmpty(String src) {
        return src == null || "".equals(src.trim())
                || "null".equalsIgnoreCase(src);
    }



}
