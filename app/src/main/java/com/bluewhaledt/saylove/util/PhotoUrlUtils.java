package com.bluewhaledt.saylove.util;


import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.DensityUtils;
import com.bluewhaledt.saylove.base.util.StringUtils;
import com.bluewhaledt.saylove.constant.Constants;

/**
 * 图片路径工具类.
 *
 * @author DengZhaoyong
 * @version 1.0.0
 * @date 2012-9-27
 */
public class PhotoUrlUtils {

    private static final String PARAM = "?imageView2/";
    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;
    public static final int TYPE_3 = 3;
    public static final int TYPE_4 = 4;
    public static final int TYPE_5 = 5;
    public static final int TYPE_6 = 6;
    public static final int TYPE_7 = 7;
    public static final int TYPE_8 = 8;
    public static final int TYPE_9 = 9;
    public static final int TYPE_10 = 10;
    public static final int TYPE_11 = 11;
    public static final int TYPE_12 = 12;
    public static final int TYPE_13 = 13;
    public static final int TYPE_14 = 14;
    public static final int TYPE_15 = 15;
    public static final int TYPE_16 = 16;
    public static final int TYPE_17 = 17;
    public static final int TYPE_18 = 18;
    public static final int TYPE_19 = 19;
    public static final int TYPE_20 = 20;

    /**
     * 限定缩略图的长边和短边的最大值分别为LongEdge和ShortEdge，进行等比压缩；如果只指定一边，则另一边自适应
     */
    public static final int MODE_0 = 0;
    /**
     * 限定缩略图的宽和高的最小值分别为Width和Height，进行等比压缩，剧中裁剪；
     * 如果只指定一边，则表示宽高相等的正方形；缩放后的图片的大小为<Width>x<Height>（其中一边多余的部分会被裁剪掉）
     */
    public static final int MODE_1 = 1;
    /**
     * 限定缩略图的宽和高的最大值分别为Width和Height，进行等比压缩；如果只指定一边，则另一边自适应
     */
    public static final int MODE_2 = 2;
    /**
     * 限定缩略图的宽和高的最小值分别为Width和Height，进行等比压缩；如果只指定一边，代表另外一边为同样的值
     */
    public static final int MODE_3 = 3;
    /**
     * 限定缩略图的长边和短边的最小值分别为LongEdge和ShortEdge，进行等比压缩；如果只指定一边，代表另外一边为同样的值
     */
    public static final int MODE_4 = 4;
    /**
     * 限定缩略图的长边和短边的最大值分别为LongEdge和ShortEdge，进行等比压缩，居中裁剪；
     * 如果只指定一边，则表示宽高相等的正方形；同模式1，缩放后其中一边多余的部分会被裁剪掉
     */
    public static final int MODE_5 = 5;

    private static final int BASE_SIZE = 16;

    private static int DEFAULT_MODE = MODE_1;

    /**
     * 根据类型格式化图片路径.
     *
     * @param url  原始URL
     * @param type 图片规格
     * @return
     */
    public static String format(String url, int type) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }

        if (!isQcloudUrl(url)) {
            return url;
        }

        int size = DensityUtils.dp2px(ZhenaiApplication.getContext(), BASE_SIZE * type);
        StringBuffer sb = new StringBuffer(url);
        sb.append(PARAM).append(DEFAULT_MODE)
                .append("/w/").append(size)
                .append("/h/").append(size);

        return sb.toString();
    }

    /**
     * 根据类型格式化图片路径.
     *
     * @param url   原始URL
     * @param type  图片规格
     * @param multi 高为宽的倍数
     * @return
     */
    public static String format(String url, int type, float multi) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }

        if (!isQcloudUrl(url)) {
            return url;
        }

        int size = DensityUtils.dp2px(ZhenaiApplication.getContext(), BASE_SIZE * type);
        StringBuffer sb = new StringBuffer(url);
        sb.append(PARAM).append(DEFAULT_MODE)
                .append("/w/").append(size)
                .append("/h/").append((int) (size * multi));

        return sb.toString();
    }

    /**
     * 根据类型格式化图片路径.
     *
     * @param url      原始URL
     * @param baseSize 基础大小
     * @param type     图片规格
     * @return
     */
    public static String formatBaseSize(String url, int baseSize, int type) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }

        if (!isQcloudUrl(url)) {
            return url;
        }

        int size = DensityUtils.dp2px(ZhenaiApplication.getContext(), baseSize * type);
        StringBuffer sb = new StringBuffer(url);
        sb.append(PARAM).append(DEFAULT_MODE)
                .append("/w/").append(size)
                .append("/h/").append(size);

        return sb.toString();
    }

    /**
     * 根据类型格式化图片路径.
     *
     * @param url      原始URL
     * @param baseSize 基础大小
     * @param type     图片规格
     * @param multi    高为宽的倍数
     * @return
     */
    public static String formatBaseSize(String url, int baseSize, int type, float multi) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        if (!isQcloudUrl(url)) {
            return url;
        }

        int size = DensityUtils.dp2px(ZhenaiApplication.getContext(), baseSize * type);
        StringBuffer sb = new StringBuffer(url);
        sb.append(PARAM).append(DEFAULT_MODE)
                .append("/w/").append(size)
                .append("/h/").append((int) (size * multi));

        return sb.toString();
    }

    /**
     * 根据类型格式化图片路径.
     *
     * @param url    原始URL
     * @param width  图片宽
     * @param height 图片高
     * @return
     */
    public static String format(String url, int width, int height) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        if (!isQcloudUrl(url)) {
            return url;
        }

        StringBuffer sb = new StringBuffer(url);
        sb.append(PARAM).append(DEFAULT_MODE)
                .append("/w/").append(width)
                .append("/h/").append(height);

        return sb.toString();
    }

    /**
     * 根据类型格式化图片路径.
     *
     * @param mode   裁剪模式
     * @param url    原始URL
     * @param width  图片宽
     * @param height 图片高
     * @return
     */
    public static String format(String url, int mode, int width, int height) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        if (!isQcloudUrl(url)) {
            return url;
        }

        StringBuffer sb = new StringBuffer(url);
        sb.append(PARAM).append(mode)
                .append("/w/").append(width)
                .append("/h/").append(height);

        return sb.toString();
    }

    private static boolean isQcloudUrl(String url) {
        return url != null && url.contains("4saylove-" + Constants.QCLOUD_APPID);
    }


}
