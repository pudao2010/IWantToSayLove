package com.bluewhaledt.saylove.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by zhenai-liliyan on 16/11/22.
 * 可以监听滚动的ScrollView
 */

public class ObservableScrollView extends ScrollView {
    private ScrollListener mScrollListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (mScrollListener != null) {
            mScrollListener.onScrollChanged( x, y, oldX, oldY);
        }
    }

}
