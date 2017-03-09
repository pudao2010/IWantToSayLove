package com.bluewhaledt.saylove.ui.pay.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;

/**
 * Created by zhenai-liliyan on 16/12/5.
 */

public class PrivilegeItemView extends FrameLayout {

    private ImageView leftIconIV;

    private TextView titleTV;

    private TextView tipsTV;

    public PrivilegeItemView(Context context) {
        this(context,null);
    }

    public PrivilegeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PrivilegeItemView);
        int redId = typedArray.getResourceId(R.styleable.PrivilegeItemView_left_icon,R.mipmap.saylove_icon);
        leftIconIV.setImageResource(redId);

        String title = typedArray.getString(R.styleable.PrivilegeItemView_title);
        titleTV.setText(title);

        String tips = typedArray.getString(R.styleable.PrivilegeItemView_tips);
        tipsTV.setText(tips);

    }

    public void setLeftIcon(int resId){
        leftIconIV.setImageResource(resId);
    }

    public void setLeftIcon(String url){
        ImageLoaderFactory.getImageLoader()
                .with(getContext())
                .load(url)
                .circle()
                .into(leftIconIV);
    }

    public void setTitle(String title){
        titleTV.setText(title);
    }

    public void setTips(String tips){
        tipsTV.setText(tips);
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View child = inflater.inflate(R.layout.acvtivity_pay_vip_privilege,this,false);
        addView(child);

        leftIconIV = (ImageView) child.findViewById(R.id.iv_left_icon);
        titleTV = (TextView) child.findViewById(R.id.tv_title);
        tipsTV = (TextView) child.findViewById(R.id.tv_tips);
    }
}
