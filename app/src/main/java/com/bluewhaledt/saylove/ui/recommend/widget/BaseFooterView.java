package com.bluewhaledt.saylove.ui.recommend.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 描述：加载更多的时候的抽象类
 * 作者：shiming_li
 * 时间：2016/11/25 17:34
 * 包名：com.zhenai.saylove_icon.ui.recommend
 * 项目名：SayLove
 */
public abstract class BaseFooterView extends FrameLayout implements FooterViewListener{

    public BaseFooterView(Context context) {
        super(context);
    }

    public BaseFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
