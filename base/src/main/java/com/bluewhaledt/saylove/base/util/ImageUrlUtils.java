package com.bluewhaledt.saylove.base.util;

import java.util.regex.Pattern;

/**
 * 图片路径工具类
 */
public class ImageUrlUtils {

    public static final String TYPE_1 = "_1";
    public static final String TYPE_2 = "_2";
    public static final String TYPE_3 = "_3";
    public static final String TYPE_4 = "_4";
    public static final String TYPE_5 = "_5";
    public static final String TYPE_6 = "_6";
    public static final String TYPE_7 = "_7";
    public static final String TYPE_8 = "_8";
    public static final String TYPE_9 = "_9";
    public static final String TYPE_10 = "_10";
    public static final String TYPE_78 = "_78";
    public static final String TYPE_104 = "_104";
    public static final String TYPE_208 = "_208";

    /**
     * 根据类型格式化图片路径
     *
     * @param url  原始URL
     * @param type 图片规格
     * @return 格式化后的路径
     */
    public static String format(String url, String type) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }

        int index = url.lastIndexOf(".");
        if (index != -1) {
            if (Pattern.matches(".*_\\d{1}\\.{1}\\w+", url)) {
                StringBuilder sb = new StringBuilder(url);
                sb.deleteCharAt(index - 1).deleteCharAt(index - 2);
                url = sb.toString();
                index = url.lastIndexOf(".");
            }
            return new StringBuilder(url).insert(index, type).toString();
        }

        return url;
    }
}
