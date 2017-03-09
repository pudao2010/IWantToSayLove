package com.bluewhaledt.saylove.ui.register_login.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 7/1/14.
 * https://github.com/wangjiegulu/WheelView
 */
public class WheelView<T> extends ScrollView {

    public static final int OFF_SET_DEFAULT = 1;
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;


    // ----------added by yintaibing-----------
    // ----------我是分割线(*^__^*) ------------
    private int mSelectorStrokeWidth;
    private int mSelectorColor;
    private int mSelectorVerticalPadding;
    private int mTextSize;
    private List<T> mItems;
    private int mEms;

    public void setSelectorStrokeWidth(int selectorStrokeWidth) {
        this.mSelectorStrokeWidth = selectorStrokeWidth;
    }

    public void setTextEms(int ems){
        this.mEms = ems;
    }

    public void setSelectorColor(int selectorColor) {
        this.mSelectorColor = selectorColor;
    }

    public void setSelectorVerticalPadding(int selectorVerticalPadding) {
        this.mSelectorVerticalPadding = selectorVerticalPadding;
    }

    public void setTextSize(int sp) {
        this.mTextSize = sp;
    }
    // ----------我是分割线(*^__^*) ------------


    List<String> mDisplayNames;
    int offset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）
    int displayItemCount; // 每页显示的数量
    int selectedIndex = 1;
    int initialY;
    Runnable scrollerTask;
    int newCheck = 50;
    int itemHeight = 0;
    /**
     * 获取选中区域的边界
     */
    int[] selectedAreaBorder;
    Paint paint;
    int viewWidth;
    private Context context;
    private LinearLayout views;
    private int scrollDirection = -1;
    private OnWheelViewListener<T> onWheelViewListener;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public List<T> getItems() {
        return mItems;
    }


    public void setItems(List<T> list) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        mItems.clear();
        mItems.addAll(list);

        if (mDisplayNames == null) {
            mDisplayNames = new ArrayList<>();
        }
        mDisplayNames.clear();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            T t = list.get(i);
            if (t instanceof IWheelViewData) {
                mDisplayNames.add(((IWheelViewData) t).getDisplayName());
            } else {
                mDisplayNames.add(t.toString());
            }
        }

        // 前面和后面补全
        for (int i = 0; i < offset; i++) {
            mDisplayNames.add(0, "");
            mDisplayNames.add("");
        }

        initData();
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        selectedIndex += offset - 1;
        this.offset = offset;
    }

    private void init(Context context) {
        this.context = context;

        this.setVerticalScrollBarEnabled(false);

        views = new LinearLayout(context);
        views.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .WRAP_CONTENT));
        views.setOrientation(LinearLayout.VERTICAL);
        views.setGravity(Gravity.CENTER_HORIZONTAL);
        this.addView(views);

        scrollerTask = new Runnable() {

            public void run() {
                int newY = getScrollY();
                if (initialY - newY == 0) { // stopped
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
                    if (remainder == 0) {
                        selectedIndex = divided + offset;

                        callSelectedCallback();
                    } else {
                        if (remainder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder +
                                            itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    callSelectedCallback();
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder);
                                    selectedIndex = divided + offset;
                                    callSelectedCallback();
                                }
                            });
                        }
                    }
                } else {
                    initialY = getScrollY();
                    WheelView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    public void startScrollerTask() {
        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }

    private void initData() {
        displayItemCount = offset * 2 + 1;

        views.removeAllViews();
        for (String item : mDisplayNames) {
            views.addView(createView(item));
        }

        refreshItemView(0);
    }

    private TextView createView(String item) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT));
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        tv.setGravity(Gravity.CENTER);
            tv.setEms(mEms);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setSingleLine(true);
        tv.setLines(1);
        tv.setText(item);
        int padding = mSelectorVerticalPadding;
        tv.setPadding(padding, padding, padding, padding);
        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(tv);
            views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    itemHeight * displayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight *
                    displayItemCount));
        }
        return tv;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        refreshItemView(t);

        if (t > oldt) {
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
            scrollDirection = SCROLL_DIRECTION_UP;
        }
    }

    private void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }
        }

        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i);
            if (null == itemView) {
                return;
            }
            if (position == i) {
                itemView.setTextColor(Color.parseColor("#3a3a3a"));
            } else if(Math.abs(position - i ) == 1){
                itemView.setTextColor(Color.parseColor("#717171"));
            }else if(Math.abs(position - i ) == 2){
                itemView.setTextColor(Color.parseColor("#cccccc"));
            }
        }
    }

    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        return selectedAreaBorder;
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {

        if (viewWidth == 0) {
            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        }

        if (null == paint) {
            paint = new Paint();
            paint.setColor(getResources().getColor(R.color.basic_info_divider_color));
            paint.setStrokeWidth(mSelectorStrokeWidth);
        }

        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine(0, obtainSelectedAreaBorder()[0], viewWidth,
                        obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine(0, obtainSelectedAreaBorder()[1], viewWidth,
                        obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }
        };

        super.setBackgroundDrawable(background);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        setBackgroundDrawable(null);
    }

    /**
     * 选中回调
     */
    private void callSelectedCallback() {
        if (onWheelViewListener != null) {
            int index = getSelectedIndex();
            if (mItems != null && mItems.size() > index) {
                onWheelViewListener.onItemSelected(index, mItems.get(index));
            }
        }
    }

    public void setSelection(int position) {
        final int p = position;
        selectedIndex = p + offset;
        this.post(new Runnable() {
            @Override
            public void run() {
                WheelView.this.smoothScrollTo(0, p * itemHeight);
            }
        });
        callSelectedCallback();
    }

    public String getSelectedItem() {
        return mDisplayNames.get(selectedIndex);
    }

    public int getSelectedIndex() {
        return selectedIndex - offset;
    }


    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setMinimumWidth(int minWidth) {
        super.setMinimumWidth(minWidth);
        views.setMinimumWidth(minWidth);
    }

    public OnWheelViewListener<T> getOnWheelViewListener() {
        return onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener<T> onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    private int getViewMeasuredHeight(View view) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }

    public TextView getItemView(int index){
        return (TextView) views.getChildAt(index);
    }

    public interface OnWheelViewListener<T> {
        void onItemSelected(int selectedIndex, T item);
    }
}
