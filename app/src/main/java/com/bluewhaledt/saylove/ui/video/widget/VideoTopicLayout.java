package com.bluewhaledt.saylove.ui.video.widget;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ui.video.entity.VideoTopicEntity;

/**
 * Created by rade.chan on 2016/12/8.
 */

public class VideoTopicLayout extends LinearLayout {

    private Context mContext;
    private RelativeLayout mTextLayout;
    private ImageView arrowIcon;
    private OnQuestionLayoutListener mListener;
    private boolean isShowArrow;
    private String currentText;
    private VideoTopicEntity topicEntity;


    public VideoTopicLayout(Context context) {
        this(context, null);
    }

    public VideoTopicLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public VideoTopicLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VideoTopicLayout);
        isShowArrow = array.getBoolean(R.styleable.VideoTopicLayout_isShowArrow, true);
        array.recycle();
        this.mContext = context;
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);
        addView();

    }


    private void addView() {
        mTextLayout = new RelativeLayout(mContext);
        LinearLayout.LayoutParams params = new LayoutParams(0, -2);
        params.weight = 1;
        params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.video_question_margin_left);
        mTextLayout.setLayoutParams(params);
        arrowIcon = new ImageView(mContext);
        arrowIcon.setImageResource(R.mipmap.arrow_right);
        int padding = mContext.getResources().getDimensionPixelSize(R.dimen.video_arrow_padding);
        arrowIcon.setPadding(padding, 0, padding, 0);
        this.addView(mTextLayout);
        this.addView(arrowIcon);
        arrowIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onNextButtonClick();
                }
            }
        });
        setArrowIconVisible(isShowArrow ? View.VISIBLE : View.GONE);
    }
    public void addText(VideoTopicEntity topicEntity){
        if (mTextLayout.getChildCount() >= 2) {
            return;
        }
        this.topicEntity=topicEntity;
        addText(topicEntity.content);
    }

    public void addText(String text) {
        if (mTextLayout.getChildCount() >= 2) {
            return;
        }
        this.currentText = text;
        TextView textView = getTextView(text);
        mTextLayout.addView(textView);
        if (mTextLayout.getChildCount() > 1) {
            View view = mTextLayout.getChildAt(0);
            animTextView(view, true);
            animTextView(textView, false);
        }

    }

    public void addListener(OnQuestionLayoutListener listener) {
        this.mListener = listener;
    }

    private void animTextView(final View view, final boolean isRemove) {
        int startX = -mTextLayout.getWidth();
        int endX = 0;
        if (isRemove) {
            startX = 0;
            endX = mTextLayout.getWidth();
        }
        view.clearAnimation();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", startX, endX).setDuration(500);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isRemove) {
                    mTextLayout.removeView(view);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }


    public String getCurrentText() {
        return currentText;
    }

    public VideoTopicEntity getCurrentTopicEntity() {
        return topicEntity;
    }

    private TextView getTextView(String text) {
        TextView textView = new TextView(mContext);
        textView.setText(text);
        textView.setTextColor(mContext.getResources().getColor(R.color.white));
        return textView;
    }

    public interface OnQuestionLayoutListener {
        void onNextButtonClick();
    }


    public void setArrowIconVisible(int visible) {
        arrowIcon.setVisibility(visible);
    }

}
