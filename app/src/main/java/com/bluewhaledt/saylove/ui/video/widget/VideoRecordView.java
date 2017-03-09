package com.bluewhaledt.saylove.ui.video.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.bluewhaledt.saylove.R;

/**
 * Created by rade.chan on 2016/12/8.
 */

public class VideoRecordView extends View implements Handler.Callback {

    public final static int STATUS_NORMAL = 1;
    public final static int STATUS_RECORDING = 2;
    public final static int STATUS_NOT_ENOUGH = 3;
    public final static int STATUS_RECORD_FINISH = 4;

    private final static int START_RECORD = 1001;
    private final static int RECORD_FINISH = 1002;

    private final static int TOTAL_SECOND = 15;

    private final static int TOTAL_ANGLE = 360;

    private final static int ANIM_TIME = 100;

    private Paint mPaint;
    private RectF mSweepRect;
    private RectF mStopRect;
    private int sweepWidth;

    private int endAngle = 0;

    private OnRecordListener mListener;
    private int countTime = TOTAL_SECOND * (1000 / ANIM_TIME);

    private final static int REQUIRE_SECOND = 3;

    private final static int STOP_SECOND = REQUIRE_SECOND * (1000 / ANIM_TIME);        //3秒
    private final static int pointAngle = 360 / (TOTAL_SECOND / REQUIRE_SECOND);

    private Handler mHandler;

    private int currentStatus = STATUS_NORMAL;

    private Context mContext;


    private Bitmap recordFinishBitmap;

    private int stopIconDimen;
    private float stopIconRadius;


    public VideoRecordView(Context context) {
        this(context, null);
    }

    public VideoRecordView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public VideoRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mHandler = new Handler(this);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        sweepWidth = mContext.getResources().getDimensionPixelSize(R.dimen.record_edge_width);
        stopIconRadius = mContext.getResources().getDimension(R.dimen.record_stop_icon_radius);
        mSweepRect = new RectF();
        stopIconDimen = mContext.getResources().getDimensionPixelOffset(R.dimen.record_stop_icon_dimen);
        mStopRect = new RectF();

        recordFinishBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_video_send);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getMeasuredWidth() / 2;
        float centerY = getMeasuredHeight() / 2;
        float radius = Math.min(centerX, centerY);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mContext.getResources().getColor(R.color.record_bg));
        canvas.drawCircle(centerX, centerY, radius, mPaint);


        mPaint.setColor(mContext.getResources().getColor(R.color.record_edge_color));
        mPaint.setStrokeWidth(sweepWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mSweepRect.set(sweepWidth / 2, sweepWidth / 2, getWidth() - sweepWidth / 2, getHeight() - sweepWidth / 2);
        canvas.drawArc(mSweepRect, -90 + endAngle, TOTAL_ANGLE - endAngle, false, mPaint);

        if (currentStatus == STATUS_RECORDING || currentStatus == STATUS_NOT_ENOUGH) {

            mPaint.setColor(mContext.getResources().getColor(R.color.record_edge_color));
            mPaint.setStyle(Paint.Style.FILL);
            mStopRect.set(centerX - stopIconDimen / 2, centerY - stopIconDimen / 2, centerX + stopIconDimen / 2, centerY + stopIconDimen / 2);
            canvas.drawRoundRect(mStopRect, stopIconRadius, stopIconRadius, mPaint);
            if (currentStatus == STATUS_NOT_ENOUGH) {
                mPaint.setColor(mContext.getResources().getColor(R.color.fragment_msg_of_list_item_red_point));
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setStrokeWidth(sweepWidth);
                float x = (float) (centerX + Math.sin(Math.toRadians(pointAngle)) * radius) - sweepWidth / 2;
                float y = (float) (centerY - Math.cos(Math.toRadians(pointAngle)) * radius) - sweepWidth / 2;
                canvas.drawPoint(x, y, mPaint);
            }
        } else if (currentStatus == STATUS_RECORD_FINISH) {
            canvas.drawBitmap(recordFinishBitmap, centerX - recordFinishBitmap.getWidth() / 2,
                    centerY - recordFinishBitmap.getHeight() / 2, mPaint);
        } else if (currentStatus == STATUS_NORMAL) {
            mPaint.setColor(mContext.getResources().getColor(R.color.fragment_msg_of_list_item_red_point));
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(sweepWidth);
            float x = (float) (centerX + Math.sin(Math.toRadians(pointAngle)) * radius) - sweepWidth / 2;
            float y = (float) (centerY - Math.cos(Math.toRadians(pointAngle)) * radius) - sweepWidth / 2;
            canvas.drawPoint(x, y, mPaint);
        }

    }

    public boolean isCanSend() {
        return (currentStatus == STATUS_RECORD_FINISH);
    }

    public void setRecordListener(OnRecordListener listener) {
        this.mListener = listener;
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case START_RECORD:
                if (countTime > 0) {
                    int step = (TOTAL_ANGLE - endAngle) / countTime;
                    if (endAngle <= 360) {
                        endAngle += step;
                    }
                    if (TOTAL_SECOND * (1000 / ANIM_TIME) - countTime >= STOP_SECOND) {
                        currentStatus = STATUS_RECORDING;
                    } else {
                        currentStatus = STATUS_NOT_ENOUGH;
                    }
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(START_RECORD, ANIM_TIME);
                } else {
                    mHandler.sendEmptyMessage(RECORD_FINISH);
                }
                countTime--;
                break;
            case RECORD_FINISH:
                currentStatus = STATUS_RECORD_FINISH;
                invalidate();
                if (mListener != null) {
                    mListener.onRecordFinish();
                }

                break;
        }
        return false;
    }

    public interface OnRecordListener {
        void onRecordFinish();

        void onRecordNotEnough();
    }

    public void clickRecord() {
        if (currentStatus == STATUS_NORMAL) {
            mHandler.removeCallbacksAndMessages(null);
            endAngle = 0;
            countTime = TOTAL_SECOND * (1000 / ANIM_TIME);
            mHandler.sendEmptyMessage(START_RECORD);
            currentStatus = STATUS_RECORDING;
        } else if (currentStatus == STATUS_RECORDING) {
            mHandler.removeCallbacksAndMessages(null);
            endAngle = TOTAL_ANGLE;
            mHandler.sendEmptyMessage(RECORD_FINISH);
        } else if (currentStatus == STATUS_NOT_ENOUGH) {
            mListener.onRecordNotEnough();
//            mHandler.removeCallbacksAndMessages(null);
//            endAngle = 0;
//            countTime = TOTAL_SECOND * (1000 / ANIM_TIME);
//            mHandler.sendEmptyMessage(START_RECORD);
//            currentStatus = STATUS_RECORDING;
        }
    }


    public void reset() {
        mHandler.removeCallbacksAndMessages(null);
        currentStatus = STATUS_NORMAL;
        endAngle = 0;
        invalidate();
    }
}
