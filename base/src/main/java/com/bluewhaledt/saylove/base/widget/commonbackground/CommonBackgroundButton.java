package com.bluewhaledt.saylove.base.widget.commonbackground;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 支持XML配置的通用背景Button
 *
 * @author yintaibing
 * @date 2016/10/28
 */
public class CommonBackgroundButton extends Button {

    public CommonBackgroundButton(Context context) {
        this(context, null);
    }

    public CommonBackgroundButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonBackgroundButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CommonBackgroundFactory.fromXml(this, attrs);
    }
}
