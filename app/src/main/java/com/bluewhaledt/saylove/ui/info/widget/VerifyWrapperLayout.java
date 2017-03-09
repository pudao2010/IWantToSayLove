package com.bluewhaledt.saylove.ui.info.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ui.info.entity.VerifyItemInfoEntity;

import java.util.List;

/**
 * Created by rade.chan on 2016/11/30.
 */

public class VerifyWrapperLayout extends LinearLayout {


    private LayoutInflater mInflater;
    private Context mContext;
    public static final int TYPE_VERIFY = 1;
    public static final int TYPE_VERIFY_DETAIL = 2;

    public VerifyWrapperLayout(Context context) {
        this(context, null);
    }

    public VerifyWrapperLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public VerifyWrapperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        mInflater = LayoutInflater.from(context);
    }

    public void addVerifyInfo(List<VerifyItemInfoEntity> list, int verify) {
        this.removeAllViews();
        if (list == null || list.size() == 0)
            return;

        if (verify == TYPE_VERIFY) {
            this.setOrientation(LinearLayout.HORIZONTAL);
            for (VerifyItemInfoEntity entity : list) {
                View view = mInflater.inflate(R.layout.item_verify_layout, null);
                LayoutParams params = new LinearLayout.LayoutParams(0, -2);
                params.weight = 1;
                view.setLayoutParams(params);

                ImageView iconView = (ImageView) view.findViewById(R.id.verify_icon_view);
                iconView.setImageResource(entity.iconRes);

                TextView verifyTitleView = (TextView) view.findViewById(R.id.verify_title_view);
                verifyTitleView.setText(entity.verifyTitle);
                if (entity.isVerifySuccess){
                    verifyTitleView.setTextColor(Color.BLACK);
                }else{
                    verifyTitleView.setTextColor(getResources().getColor(R.color.third_party_page_verify_item_text));
                }


                TextView verifyContentView = (TextView) view.findViewById(R.id.verify_info_text_view);
                verifyContentView.setText(entity.showText);

                this.addView(view);
            }
        } else if (verify == TYPE_VERIFY_DETAIL) {
            this.setOrientation(LinearLayout.VERTICAL);
            for (VerifyItemInfoEntity entity : list) {
                View view = mInflater.inflate(R.layout.item_verify_detail_layout, null);
                LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
                params.topMargin=mContext.getResources().getDimensionPixelSize(R.dimen.verify_detail_item_top_margin);
                view.setLayoutParams(params);

                TextView verifyTitleView = (TextView) view.findViewById(R.id.verify_title_view);
                verifyTitleView.setText(entity.verifyTitle);

                TextView verifyContentView = (TextView) view.findViewById(R.id.verify_info_text_view);
                if(!TextUtils.isEmpty(entity.showText)) {
                    verifyContentView.setText(entity.showText);
                }else{
                    verifyContentView.setVisibility(View.GONE);
                }

                TextView verifyDetailView=(TextView)view.findViewById(R.id.verify_detail_text_view);
                if(!TextUtils.isEmpty(entity.showDetailText)) {
                    verifyDetailView.setText(entity.showDetailText);
                }else{
                    verifyDetailView.setVisibility(View.GONE);
                }

                TextView verifyTimeView=(TextView)view.findViewById(R.id.verify_time_view);
                if(!TextUtils.isEmpty(entity.verifyTime)){
                    verifyTimeView.setText(entity.verifyTime);
                }else{
                    verifyTimeView.setVisibility(View.GONE);
                }

                this.addView(view);
            }
        }
    }
}
