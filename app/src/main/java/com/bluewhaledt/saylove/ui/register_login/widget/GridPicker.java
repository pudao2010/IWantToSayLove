package com.bluewhaledt.saylove.ui.register_login.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.DensityUtils;

/**
 * 方格式选择器
 *
 * @author yintaibing
 * @date 2016/11/4
 */
public class GridPicker<T> extends LinearLayout {
    private LinearLayout mLayoutLeftTitle;
    private AtMostGridView mGridView;
    private TextView mTvTitle, mTvUnit;

    public GridPicker(Context context) {
        super(context);
    }

    public GridPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public GridPicker(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
    }

    public void setLeftTitleVisibility(int visibility) {
        mLayoutLeftTitle.setVisibility(visibility);
        int leftMargin = 0;
        if (visibility == GONE || visibility == INVISIBLE) {
            leftMargin = DensityUtils.dp2px(getContext(), 0.5f);
        } else {
            leftMargin = DensityUtils.dp2px(getContext(), -0.5f);
        }
        LayoutParams params = (LayoutParams) mGridView.getLayoutParams();
        params.leftMargin = leftMargin;
        mGridView.setLayoutParams(params);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setUnit(String unit) {
        mTvUnit.setText(unit);
    }

    public void setUnitVisibility(int visibility) {
        mTvUnit.setVisibility(visibility);
    }

    public void setGridViewAdapter(GridPickerAdapter<T> adapter) {
        mGridView.setAdapter(adapter);
    }

    private void findViews() {
        mLayoutLeftTitle = (LinearLayout) findViewById(R.id.ll_left_title);
        mTvTitle = (TextView) findViewById(R.id.tv_decade);
        mTvUnit = (TextView) findViewById(R.id.tv_unit);
        mGridView = (AtMostGridView) findViewById(R.id.gv_data);
    }
}
