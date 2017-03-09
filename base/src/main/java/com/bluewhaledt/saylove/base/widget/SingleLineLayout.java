package com.bluewhaledt.saylove.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class SingleLineLayout extends LinearLayout {


    public SingleLineLayout(Context context) {
        super(context);
    }

    public SingleLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 控制子控件的换行
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int pL = SingleLineLayout.this.getPaddingLeft();
        int residualSpace = r - l;
        int y = t;
        int left = l + pL;

        int count = getChildCount();
        for (int j = 0; j < count; j++) {
            final View childView = getChildAt(j);
            childView.measure(0, 0);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            LayoutParams lp = (LayoutParams) childView.getLayoutParams();
            if (childWidth + lp.leftMargin + lp.rightMargin < residualSpace) {
                int top = 0;
                childView.layout(left + lp.leftMargin, top + lp.topMargin, left + lp.leftMargin + childWidth + lp.rightMargin, top + childHeight + lp.topMargin + lp.bottomMargin);
                left += childWidth + lp.leftMargin + lp.rightMargin;
                residualSpace = residualSpace - childWidth - lp.leftMargin - lp.rightMargin;
            } else {
                break;
            }
        }
    }
}
