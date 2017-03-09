package com.bluewhaledt.saylove.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public class EmptyView extends FrameLayout {

    private TextView emptyText1TV;

    private TextView emptyText2TV;

    private ImageView picIV;

    public EmptyView(Context context) {
        this(context,null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyView);
        int redId = typedArray.getResourceId(R.styleable.EmptyView_pic,R.mipmap.saylove_icon);
        picIV.setImageResource(redId);

        String text1 = typedArray.getString(R.styleable.EmptyView_text1);
        emptyText1TV.setText(text1);

        String text2 = typedArray.getString(R.styleable.EmptyView_text2);
        emptyText2TV.setText(text2);
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View child = inflater.inflate(R.layout.empty_view,this,false);
        addView(child);
        emptyText1TV = (TextView) findViewById(R.id.tv_empty_text1);
        emptyText2TV = (TextView) findViewById(R.id.tv_empty_text2);
        picIV = (ImageView) findViewById(R.id.iv_pic);

    }


}
