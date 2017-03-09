package com.bluewhaledt.saylove.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;

/**
 * Created by rade.chan on 2016/12/23.
 */

public class ExpandTextView extends LinearLayout {

    private Context mContext;
    private TextView contentTv;
    private int maxLine;
    private int textColor;
    private int textSize;


    private TextView expandTv;

    public static final int STATUS_EXPAND = 1;
    public static final int STATUS_RETRACT = 2;

    private int currentStatus = STATUS_RETRACT;

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView);
        maxLine = array.getInt(R.styleable.ExpandTextView_expandMaxLine, 3);
        textColor = array.getColor(R.styleable.ExpandTextView_expandTextColor, context.getResources().getColor(R.color.black));
        textSize = array.getDimensionPixelSize(R.styleable.ExpandTextView_expandTextSize,
                context.getResources().getDimensionPixelSize(R.dimen.expand_text_view_text_size));
        int padding = array.getDimensionPixelSize(R.styleable.ExpandTextView_expandTextPadding,
                context.getResources().getDimensionPixelSize(R.dimen.expand_text_view_padding));

        array.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.END);
        setPadding(padding, padding, padding, padding);
        initView();
    }

    private void initView() {
        contentTv = new TextView(mContext);
        contentTv.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f,  getResources().getDisplayMetrics()), 1.0f);
        contentTv.setEllipsize(TextUtils.TruncateAt.END);
        contentTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        contentTv.setTextColor(textColor);
        LinearLayout.LayoutParams textParams = new LayoutParams(-1, -2);
        contentTv.setLayoutParams(textParams);

        expandTv = new TextView(mContext);
        LinearLayout.LayoutParams expandTvParams = new LayoutParams(-2, -2);
        expandTvParams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.expand_text_view_margin);
        expandTv.setLayoutParams(expandTvParams);
        expandTv.setText(mContext.getString(R.string.expand));
        this.addView(contentTv);
        this.addView(expandTv);
        expandTv.setVisibility(View.GONE);
        expandTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStatus == STATUS_RETRACT) {
                    currentStatus = STATUS_EXPAND;
                    contentTv.setMaxLines(Integer.MAX_VALUE);
                    expandTv.setText(mContext.getString(R.string.retract));
                } else {
                    currentStatus = STATUS_RETRACT;
                    contentTv.setMaxLines(maxLine);
                    expandTv.setText(mContext.getString(R.string.expand));
                }
            }
        });


    }

    public void setText(String text) {
        contentTv.setMaxLines(Integer.MAX_VALUE);   //测量行数
        contentTv.setText(text.trim());
        if(contentTv.getLineCount()>maxLine){
            expandTv.setVisibility(View.VISIBLE);
            contentTv.setMaxLines(maxLine);
        }else{
            expandTv.setVisibility(View.GONE);
        }


    }

    public String getText() {
        return contentTv.getText().toString();
    }

}
