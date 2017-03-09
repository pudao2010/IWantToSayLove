package com.bluewhaledt.saylove.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 自动测量高度的ListView
 *
 * @author yintaibing
 * @date 2016/8/19
 */
public class AtMostListView extends ListView {
    public AtMostListView(Context ctx) {
        super(ctx);
    }

    public AtMostListView(Context ctx, AttributeSet attributeSet) {
        super(ctx, attributeSet);
    }

    public AtMostListView(Context ctx, AttributeSet attributeSet, int defStyle) {
        super(ctx, attributeSet, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        // 重新测量ListView的高度，以扩充外部ScrollView
        measureSelf(adapter);
    }

    /**
     * 重新测量自身高度
     *
     * @param adapter ListAdapter对象
     */
    private void measureSelf(ListAdapter adapter) {
        int totalHeight = 0;
        int itemCount = adapter.getCount();
        for (int i = 0; i < itemCount; i++) {
            View item = adapter.getView(i, null, this);
            item.measure(0, 0);
            totalHeight += item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = totalHeight + (getDividerHeight() * (itemCount - 1));
        setLayoutParams(params);
    }
}
