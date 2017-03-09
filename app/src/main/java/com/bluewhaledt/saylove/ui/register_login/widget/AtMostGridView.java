package com.bluewhaledt.saylove.ui.register_login.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class AtMostGridView extends GridView {
    public AtMostGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AtMostGridView(Context context) {
        super(context);
    }

    public AtMostGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}