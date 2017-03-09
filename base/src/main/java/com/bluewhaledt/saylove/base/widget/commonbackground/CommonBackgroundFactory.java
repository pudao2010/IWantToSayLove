package com.bluewhaledt.saylove.base.widget.commonbackground;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.bluewhaledt.saylove.base.R;

import java.io.Serializable;


/**
 * 通用背景工厂类
 *
 * @author yintaibing
 * @date 2016/10/29
 */
public class CommonBackgroundFactory {

    /**
     * 从XML解析通用背景
     *
     * @param view         View
     * @param attributeSet AttributeSet
     */
    public static void fromXml(View view, AttributeSet attributeSet) {
        AttrSet attrs = obtainAttrs(view.getContext(), attributeSet);
        fromAttrSet(view, attrs);
    }

    /**
     * 从AttrSet解析通用背景
     *
     * @param view View
     * @param attrSet AttrSet
     */
    public static void fromAttrSet(View view, AttrSet attrSet) {
        if (attrSet != null) {
            if (attrSet.stateful) {
                CommonBackgroundSet set = stateful(attrSet);
                set.showOn(view);
            } else {
                ICommonBackground drawable = stateless(attrSet);
                drawable.showOn(view);
            }

            attrSet.recycle();
        }
    }

    /**
     * 创建不区分状态（disabled, normal, pressed）的通用背景
     *
     * @return 单独一个CommonBackground
     */
    public static CommonBackground createStateless() {
        return new CommonBackground();
    }

    /**
     * 创建区分状态（disabled, normal, pressed）的通用背景
     *
     * @return CommonBackgroundSet
     */
    public static CommonBackgroundSet createStateful() {
        return new CommonBackgroundSet();
    }

    /**
     * 提取自定义的属性集
     *
     * @param context      Context
     * @param attributeSet AttributeSet
     * @return AttrSet
     */
    public static AttrSet obtainAttrs(Context context, AttributeSet attributeSet) {
        if (context != null && attributeSet != null) {
            AttrSet attrs = new AttrSet();
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable
                    .common_background);
            attrs.stateful = a.getBoolean(R.styleable.common_background_stateful, false);
            attrs.shape = a.getInt(R.styleable.common_background_shape,
                    CommonBackground.SHAPE_RECT); // 默认直角矩形
            attrs.fillMode = a.getInt(R.styleable.common_background_fill_mode,
                    CommonBackground.FILL_MODE_SOLID); // 默认颜色填充
            attrs.scaleType = a.getInt(R.styleable.common_background_scale_type,
                    CommonBackground.SCALE_TYPE_NONE); // 默认无缩放
            attrs.strokeMode = a.getInt(R.styleable.common_background_stroke_mode,
                    CommonBackground.STROKE_MODE_NONE); // 默认无描边
            if (attrs.strokeMode == CommonBackground.STROKE_MODE_NONE) {
                attrs.strokeWidth = 0;
            } else {
                attrs.strokeWidth = a.getDimensionPixelSize(
                        R.styleable.common_background_stroke_width, 0);
            }
            attrs.radius = a.getDimensionPixelSize(R.styleable.common_background_radius, 0);
            attrs.strokeDashSolid = a.getDimensionPixelSize(
                    R.styleable.common_background_stroke_dash_solid, 0);
            attrs.strokeDashSpace = a.getDimensionPixelSize(
                    R.styleable.common_background_stroke_dash_space, 0);
            attrs.colorDisabled = a.getColor(R.styleable.common_background_color_disabled,
                    Color.LTGRAY); // disabled状态默认使用浅灰色
            attrs.colorNormal = a.getColor(R.styleable.common_background_color_normal,
                    Color.WHITE); // normal状态默认使用白色
            attrs.colorPressed = a.getColor(R.styleable.common_background_color_pressed,
                    attrs.colorNormal); // pressed状态默认与normal状态相同
            attrs.colorStroke = a.getColor(R.styleable.common_background_color_stroke,
                    Color.TRANSPARENT); // 描边默认使用透明
            int bitmapResId = a.getResourceId(R.styleable.common_background_bitmap,
                    android.R.drawable.ic_delete);
            attrs.bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResId);

            a.recycle();
            return attrs;
        }
        return null;
    }

    private static ICommonBackground stateless(AttrSet attrs) {
        return new CommonBackground()
                .shape(attrs.shape)
                .fillMode(attrs.fillMode)
                .scaleType(attrs.scaleType)
                .strokeMode(attrs.strokeMode)
                .strokeWidth(attrs.strokeWidth)
                .strokeDashSolid(attrs.strokeDashSolid)
                .strokeDashSpace(attrs.strokeDashSpace)
                .radius(attrs.radius)
                .colorStroke(attrs.colorStroke)
                .colorFill(attrs.colorNormal)
                .bitmap(attrs.bitmap);
    }

    private static CommonBackgroundSet stateful(AttrSet attrs) {
        CommonBackgroundSet set = new CommonBackgroundSet();
        set.forEach()
                .shape(attrs.shape)
                .fillMode(attrs.fillMode)
                .scaleType(attrs.scaleType)
                .strokeMode(attrs.strokeMode)
                .strokeWidth(attrs.strokeWidth)
                .strokeDashSolid(attrs.strokeDashSolid)
                .strokeDashSpace(attrs.strokeDashSpace)
                .colorStroke(attrs.colorStroke)
                .radius(attrs.radius)
                .bitmap(attrs.bitmap);
        set.theDisabled().colorFill(attrs.colorDisabled);
        set.theNormal().colorFill(attrs.colorNormal);
        set.thePressed().colorFill(attrs.colorPressed);
        return set;
    }

    /**
     * 属性集
     */
    public static class AttrSet implements Serializable {
        boolean stateful;
        int shape;
        int fillMode;
        int scaleType;
        int strokeMode;
        int radius;
        int strokeWidth;
        int strokeDashSolid;
        int strokeDashSpace;
        int colorStroke;
        int colorDisabled;
        int colorNormal;
        int colorPressed;
        Bitmap bitmap;

        void recycle() {
            bitmap = null;
        }
    }
}
