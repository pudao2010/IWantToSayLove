package com.bluewhaledt.saylove.ui.info.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;


/**
 * Created by rade.chan on 2016/11/8.
 */

public class ItemLayout extends FrameLayout {

    private Context mContext;
    private String mLeftText;                            //左边文字
    private String mRightText;                           //右边文字
    private int mRightLayoutVisible;                    //右边layout是否可见，默认可见

    private int mRightIconVisible;                      //右边图标是否可见，默认不可见

    private int mTopLineVisible;                         //顶部划线是否可见，默认不可见
    private int mBottomLineVisible;                     //底部下划线是否可见，默认不可见

    private int mLeftTextColor;                         //左边文字颜色
    private int mLeftTextSize;                          //左边文字大小
    private int mRightTextColor;                        //右边文字颜色
    private int mRightTextSize;                         //右边文字大小

    private Drawable mLeftIconDrawable;                 //左右图标
    private Drawable mRightIconDrawable;                //右边图标

    private int mLineColor;                       //底部颜色


    private LinearLayout leftLayout;
    private LinearLayout rightLayout;

    private TextView leftTextView;
    private TextView rightTextView;

    private ImageView leftIconView;
    private ImageView rightIconView;

    private View topLineView;
    private View bottomLineView;

    private int leftMargin;
    private int rightMargin;


    private int minHeight;


    public ItemLayout(Context context) {
        this(context, null);
    }

    public ItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ItemLayout);

        mLeftText = array.getString(R.styleable.ItemLayout_leftText);
        mRightText = array.getString(R.styleable.ItemLayout_rightText);

        mLeftTextColor = array.getColor(R.styleable.ItemLayout_leftTextColor, getResources().getColor(R.color.item_layout_text_color));
        mLeftTextSize = array.getDimensionPixelSize(R.styleable.ItemLayout_leftTextSize, getResources().getDimensionPixelSize(R.dimen.item_layout_text_size));
        mRightTextColor = array.getColor(R.styleable.ItemLayout_rightTextColor, getResources().getColor(R.color.item_layout_text_color));
        mRightTextSize = array.getDimensionPixelSize(R.styleable.ItemLayout_rightTextSize,
                getResources().getDimensionPixelSize(R.dimen.item_layout_text_size));

        mRightLayoutVisible = array.getInt(R.styleable.ItemLayout_rightLayoutVisible, View.VISIBLE);


        mRightIconVisible = array.getInt(R.styleable.ItemLayout_rightIconVisible, View.GONE);

        mTopLineVisible = array.getInt(R.styleable.ItemLayout_topLineVisible, View.GONE);
        mBottomLineVisible = array.getInt(R.styleable.ItemLayout_bottomLineVisible, View.GONE);

        leftMargin = array.getDimensionPixelOffset(R.styleable.ItemLayout_leftMargin, 0);
        rightMargin = array.getDimensionPixelOffset(R.styleable.ItemLayout_rightMargin, 0);

        mLeftIconDrawable = array.getDrawable(R.styleable.ItemLayout_leftIconDrawable);
        mRightIconDrawable = array.getDrawable(R.styleable.ItemLayout_rightIconDrawable);

        minHeight = array.getDimensionPixelOffset(R.styleable.ItemLayout_minHeight, 0);

        if (mRightIconDrawable == null) {
            mRightIconDrawable = getResources().getDrawable(R.mipmap.arrow_right);
        }


        mLineColor = array.getColor(R.styleable.ItemLayout_bottomLineColor, getResources().getColor(R.color.item_bottom_line_color));
        array.recycle();
        addView();
        init();
    }


    private void addView() {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.widget_item_layout, this, false);
        leftLayout = (LinearLayout) itemView.findViewById(R.id.left_layout);
        rightLayout = (LinearLayout) itemView.findViewById(R.id.right_layout);

        leftTextView = (TextView) itemView.findViewById(R.id.left_text_view);
        rightTextView = (TextView) itemView.findViewById(R.id.right_text_view);

        leftIconView = (ImageView) itemView.findViewById(R.id.left_icon_view);
        rightIconView = (ImageView) itemView.findViewById(R.id.right_icon_view);

        topLineView = itemView.findViewById(R.id.top_line_view);
        bottomLineView = itemView.findViewById(R.id.bottom_line_view);


        if (minHeight > 0) {
            itemView.setMinimumHeight(minHeight);
        }

        this.addView(itemView);
    }

    private void init() {
        leftTextView.setText(mLeftText);
        leftTextView.setTextColor(mLeftTextColor);
        leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);

        RelativeLayout.LayoutParams leftLayoutParams = (RelativeLayout.LayoutParams) leftLayout.getLayoutParams();
        leftLayoutParams.leftMargin = leftMargin;
        leftLayout.setLayoutParams(leftLayoutParams);


        rightLayout.setVisibility(mRightLayoutVisible);
        RelativeLayout.LayoutParams rightLayoutParams = (RelativeLayout.LayoutParams) rightLayout.getLayoutParams();
        rightLayoutParams.rightMargin = rightMargin;
        rightLayout.setLayoutParams(rightLayoutParams);


        rightTextView.setText(mRightText);
        rightTextView.setTextColor(mRightTextColor);
        rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
        if (mLeftIconDrawable != null) {
            setLeftDrawable(mLeftIconDrawable);
        } else {
            leftIconView.setVisibility(View.GONE);
        }
        rightIconView.setVisibility(mRightIconVisible);
        rightIconView.setImageDrawable(mRightIconDrawable);

        topLineView.setVisibility(mTopLineVisible);
        topLineView.setBackgroundColor(mLineColor);
        bottomLineView.setVisibility(mBottomLineVisible);
        bottomLineView.setBackgroundColor(mLineColor);

    }

    public void setLeftDrawable(Drawable drawable) {
        leftIconView.setVisibility(View.VISIBLE);
        leftIconView.setImageDrawable(drawable);
    }

    public void setLeftDrawable(int drawableRes) {
        leftIconView.setVisibility(View.VISIBLE);
        leftIconView.setImageResource(drawableRes);
    }

    public void setLeftText(String text) {
        leftTextView.setText(text);
    }

    public void setLeftTextColor(int color) {
        leftTextView.setTextColor(getResources().getColor(color));
    }

    public void setRightText(String text) {
        rightTextView.setText(text);
    }

    public void setRightMargin(int dimen){
        RelativeLayout.LayoutParams rightLayoutParams = (RelativeLayout.LayoutParams) rightLayout.getLayoutParams();
        rightLayoutParams.rightMargin = dimen;
        rightLayout.setLayoutParams(rightLayoutParams);
    }

    public void setRightTextColor(int color) {
        rightTextView.setTextColor(getResources().getColor(color));
    }

    public void setRightLayoutVisibility(int visibility) {
        rightLayout.setVisibility(visibility);
    }

    public void setRightIconVisibility(int visibility) {
        rightIconView.setVisibility(visibility);
    }

    public void setBottomLineVisibility(int visibility) {
        bottomLineView.setVisibility(visibility);
    }

    public void setBottomLineBackground(int background) {
        bottomLineView.setBackgroundColor(getResources().getColor(background));
    }

    public void setRightIconDrawable(Drawable drawable) {
        rightIconView.setImageDrawable(drawable);
    }

    public String getRightText() {
        return rightTextView.getText().toString();
    }


    public void setLeftTextVisible(int visible) {
        leftTextView.setVisibility(visible);
    }


}
