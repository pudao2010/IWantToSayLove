package com.bluewhaledt.saylove.ui.video.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by rade.chan on 2016/12/14.
 */

public class TouchSurfaceView extends GLSurfaceView {

    public static final int ORIENTATION_LEFT = 1;
    public static final int ORIENTATION_RIGHT = 2;

    private static final int SLIDE_DISTANCE = 20;

    private float downX;
    private OnSlideListener onSlideListener;

    public TouchSurfaceView(Context context) {
        super(context);
    }

    public TouchSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnSlideListener(OnSlideListener listener) {
        this.onSlideListener = listener;
    }

    public interface OnSlideListener {
        void onSlide(int orientation);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                return true;        //消费此事件，不往上传，交由surface view处理
            case MotionEvent.ACTION_UP:
                float pointX = event.getX();
                float diff = pointX - downX;
                if (Math.abs(diff) >= SLIDE_DISTANCE) {
                    if (onSlideListener != null) {
                        onSlideListener.onSlide(diff < 0 ? ORIENTATION_LEFT : ORIENTATION_RIGHT);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
