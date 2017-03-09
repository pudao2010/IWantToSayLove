package com.bluewhaledt.saylove.ui.recommend.widget;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by zhenai-liliyan on 16/9/26.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean isSetTopSpace;

    public SpacesItemDecoration(int space) {
        this.space = space;
        this.isSetTopSpace=true;
    }

    public SpacesItemDecoration(int space,boolean isSetTopSpace) {
        this.space = space;
        this.isSetTopSpace=isSetTopSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.bottom = space;
        if(isSetTopSpace) {
            if (parent.getAdapter().getItemCount() % getSpanCount(parent) == 1) {
                outRect.top = space;
            }
        }
        if(parent.getChildLayoutPosition(view) % getSpanCount(parent) == 0)
            outRect.right = space;
    }

    private int getSpanCount(RecyclerView parent)
    {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager)
        {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }
}
