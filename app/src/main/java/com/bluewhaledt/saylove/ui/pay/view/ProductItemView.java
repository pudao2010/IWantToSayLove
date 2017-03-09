package com.bluewhaledt.saylove.ui.pay.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;

/**
 * Created by zhenai-liliyan on 16/12/5.
 */

public class ProductItemView extends FrameLayout {

    private TextView limitTimeTV;
    private TextView priceTV;
    private TextView averagePerDayTV;
    private Button purchaseBtn;
    private View averagePerDayLL;

    public ProductItemView(Context context) {
        this(context, null);
    }

    public ProductItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProductItemView);
        String limitTime = typedArray.getString(R.styleable.ProductItemView_limit_time);

        limitTimeTV.setText(limitTime);

        String price = typedArray.getString(R.styleable.ProductItemView_price);
        int color = typedArray.getColor(R.styleable.ProductItemView_price_color, Color.BLACK);

        priceTV.setText(price);
        priceTV.setTextColor(color);

        boolean isShow = typedArray.getBoolean(R.styleable.ProductItemView_is_show_averabe, false);

        if (isShow) {
            String averagePerDay = typedArray.getString(R.styleable.ProductItemView_average_per_day);
            averagePerDayTV.setText(averagePerDay);
            averagePerDayLL.setVisibility(VISIBLE);
        } else {
            averagePerDayLL.setVisibility(GONE);
        }

        int bg = typedArray.getResourceId(R.styleable.ProductItemView_purchase_btn_bg, R.color.black);
        purchaseBtn.setBackgroundResource(bg);

        int textColor = typedArray.getColor(R.styleable.ProductItemView_purchase_btn_text_color, Color.BLACK);
        purchaseBtn.setTextColor(textColor);

        int textId = typedArray.getResourceId(R.styleable.ProductItemView_purchase_btn_text, R.string.purchase_text);
        purchaseBtn.setText(textId);
        typedArray.recycle();
    }


    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View child = inflater.inflate(R.layout.pay_item_view, this, false);
        addView(child);
        averagePerDayTV = ((TextView) child.findViewById(R.id.tv_average_per_day));
        averagePerDayLL = child.findViewById(R.id.ll_average_per_day);
        purchaseBtn = (Button) child.findViewById(R.id.btn_purchase);
        priceTV = (TextView) child.findViewById(R.id.tv_price);
        limitTimeTV = (TextView) child.findViewById(R.id.tv_limit_time);
    }


    public void setLimitTime(String limitTime) {
        limitTimeTV.setText(limitTime);
    }

    public void setPrice(String price) {
        priceTV.setText(price);
    }

    public void setPriceColor(int color) {
        priceTV.setTextColor(color);
    }

    public void setPriceColor(String color) {
        priceTV.setTextColor(Color.parseColor(color));
    }

    public void setAveragePerDay(String price) {
        averagePerDayTV.setText(price);
    }

    public void showAveragePerDay(boolean isShow) {
        if (isShow) {
            averagePerDayLL.setVisibility(VISIBLE);
        } else {
            averagePerDayLL.setVisibility(GONE);
        }
    }

    public void setAveragePerDayTextColor(int color) {
        averagePerDayTV.setTextColor(color);
    }

    public void setPurchaseBg(int resId) {
        purchaseBtn.setBackgroundResource(resId);
    }

    public void setPurchaseBtnText(int textId) {
        purchaseBtn.setText(textId);
    }

    public void setPurchaseBtnOnClick(OnClickListener listener) {
        purchaseBtn.setOnClickListener(listener);
    }

    public void setPurchaseBtnTextColor(int color) {
        purchaseBtn.setTextColor(color);
    }

}
