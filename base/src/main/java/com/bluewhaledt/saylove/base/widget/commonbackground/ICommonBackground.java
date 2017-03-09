package com.bluewhaledt.saylove.base.widget.commonbackground;

import android.graphics.Bitmap;
import android.view.View;

/**
 * 通用背景接口
 */
public interface ICommonBackground {
    /**
     * 设置形状
     *
     * @param shape 形状
     * @return
     */
    ICommonBackground shape(int shape);

    /**
     * 设置填充模式
     *
     * @param fillMode 填充模式
     * @return
     */
    ICommonBackground fillMode(int fillMode);

    /**
     * 设置缩放类型
     *
     * @param scaleType 缩放类型
     * @return
     */
    ICommonBackground scaleType(int scaleType);

    /**
     * 设置描边模式
     *
     * @param strokeMode 描边模式
     * @return
     */
    ICommonBackground strokeMode(int strokeMode);

    /**
     * 设置描边宽度
     *
     * @param strokeWidth 设置描边宽度
     * @return
     */
    ICommonBackground strokeWidth(int strokeWidth);

    /**
     * 设置虚线描边时，单个实线的长度
     *
     * @param strokeDashSolid 单个实线的长度
     * @return
     */
    ICommonBackground strokeDashSolid(int strokeDashSolid);

    /**
     * 设置虚线描边时，单个空白的长度
     *
     * @param strokeDashSpace 单个空白的长度
     * @return
     */
    ICommonBackground strokeDashSpace(int strokeDashSpace);

    /**
     * 设置圆角或圆形的半径
     *
     * @param radius 圆角或圆形的半径
     * @return
     */
    ICommonBackground radius(int radius);

    /**
     * 设置填充颜色
     *
     * @param colorFill 填充颜色
     * @return
     */
    ICommonBackground colorFill(int colorFill);

    /**
     * 设置描边颜色
     *
     * @param colorStroke 描边颜色
     * @return
     */
    ICommonBackground colorStroke(int colorStroke);

    /**
     * 设置填充位图
     *
     * @param bitmap 填充位图
     * @return
     */
    ICommonBackground bitmap(Bitmap bitmap);

    /**
     * 显示到View上
     *
     * @param yourView
     */
    void showOn(View yourView);
}
