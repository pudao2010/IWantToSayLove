package com.bluewhaledt.saylove.base.widget.commonbackground;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * 通用背景Drawable
 *
 * @author yintaibing
 * @date 2016/10/28
 */
public class CommonBackground extends Drawable implements ICommonBackground {
    public static final int SHAPE_RECT = 0;
    public static final int SHAPE_ROUND_RECT = 1;
    public static final int SHAPE_SIDE_CIRCLE_RECT = 2;
    public static final int SHAPE_CIRCLE = 3;

    public static final int FILL_MODE_SOLID = 0x01;
    public static final int FILL_MODE_BITMAP = 0x02;

    public static final int SCALE_TYPE_NONE = 0;
    public static final int SCALE_TYPE_AT_MOST = 1;
    public static final int SCALE_TYPE_AT_LEAST = 2;
    public static final int SCALE_TYPE_FIT_FRAME = 3;

    public static final int STROKE_MODE_NONE = 0;
    public static final int STROKE_MODE_SOLID = 1;
    public static final int STROKE_MODE_DASH = 2;

    // user intentData
    private Bitmap mBitmap;
    private int mShape;
    private int mFillMode;
    private int mScaleType;
    private int mStrokeMode;
    private int mColorFill = Color.WHITE;
    private int mColorStroke = Color.TRANSPARENT;
    private float mStrokeWidth;       // px
    private float[] mStrokeDash;      // px
    private float mRadius;            // px

    // inner intentData
    private BitmapShader mShader;
    private ColorMatrixColorFilter mColorFilter;
    private Paint mPaint;
    private RectF mBounds;

    CommonBackground() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    /**
     * 显示到你的View上
     *
     * @param yourView 你的View
     */
    @Override
    public void showOn(View yourView) {
        if (yourView != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                yourView.setBackground(this);
            } else {
                yourView.setBackgroundDrawable(this);
            }
            yourView.setClickable(true);
        }
    }

    /**
     * 设置形状
     *
     * @param shape 形状
     * @return
     */
    @Override
    public CommonBackground shape(int shape) {
        mShape = shape;
        return this;
    }

    /**
     * 设置填充模式
     *
     * @param fillMode 填充模式
     * @return
     */
    @Override
    public CommonBackground fillMode(int fillMode) {
        mFillMode = fillMode;
        return this;
    }

    /**
     * 设置缩放类型
     *
     * @param scaleType 缩放类型
     * @return
     */
    @Override
    public CommonBackground scaleType(int scaleType) {
        mScaleType = scaleType;
        return this;
    }

    /**
     * 设置描边模式
     *
     * @param strokeMode 描边模式
     * @return
     */
    @Override
    public CommonBackground strokeMode(int strokeMode) {
        mStrokeMode = strokeMode;
        return this;
    }

    /**
     * 设置描边宽度
     *
     * @param strokeWidth 设置描边宽度
     * @return
     */
    @Override
    public CommonBackground strokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        return this;
    }

    /**
     * 设置虚线描边时，单个实线的长度
     *
     * @param strokeDashSolid 单个实线的长度
     * @return
     */
    @Override
    public CommonBackground strokeDashSolid(int strokeDashSolid) {
        if (mStrokeDash == null) {
            mStrokeDash = new float[2];
        }
        mStrokeDash[0] = strokeDashSolid;
        return this;
    }

    /**
     * 设置虚线描边时，单个空白的长度
     *
     * @param strokeDashSpace 单个空白的长度
     * @return
     */
    @Override
    public CommonBackground strokeDashSpace(int strokeDashSpace) {
        if (mStrokeDash == null) {
            mStrokeDash = new float[2];
        }
        mStrokeDash[1] = strokeDashSpace;
        return this;
    }

    /**
     * 设置圆角或圆形的半径
     *
     * @param radius 圆角或圆形的半径
     * @return
     */
    @Override
    public CommonBackground radius(int radius) {
        mRadius = radius;
        return this;
    }

    /**
     * 设置填充颜色
     *
     * @param colorFill 填充颜色
     * @return
     */
    @Override
    public CommonBackground colorFill(int colorFill) {
        mColorFill = colorFill;
        return this;
    }

    /**
     * 设置描边颜色
     *
     * @param colorStroke 描边颜色
     * @return
     */
    @Override
    public CommonBackground colorStroke(int colorStroke) {
        mColorStroke = colorStroke;
        return this;
    }

    /**
     * 设置填充位图
     *
     * @param bitmap 填充位图
     * @return
     */
    @Override
    public CommonBackground bitmap(Bitmap bitmap) {
        if (bitmap != null) {
            mBitmap = bitmap;
            mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }
        return this;
    }

    /**
     * 获取填充位图
     *
     * @return 填充位图
     */
    public Bitmap bitmap() {
        return mBitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        // 先绘制描边，再缩小边界，绘制填充
        drawStroke(canvas);
        drawFill(canvas);
    }

    /**
     * 绘制描边（进调整画笔）
     *
     * @param canvas 画板
     */
    private void drawStroke(Canvas canvas) {
        // STROKE_MODE_NONE
        if (mStrokeMode == STROKE_MODE_NONE) {
            return;
        }

        // STROKE_MODE_DASH or STROKE_MODE_SOLID
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColorStroke);
        mPaint.setShader(null);
        mPaint.setColorFilter(null);
        mPaint.setStrokeWidth(mStrokeWidth);
        if (mStrokeMode == STROKE_MODE_DASH) {
            if (mPaint.getPathEffect() == null) {
                mPaint.setPathEffect(new DashPathEffect(mStrokeDash, 1.0f));
            }
        }
        drawStrokeShape(canvas);
    }

    /**
     * 绘制描边（调整画笔后，实际绘制出形状）
     *
     * @param canvas 画板
     */
    private void drawStrokeShape(Canvas canvas) {
        final float narrowBy = mStrokeWidth / 2.0f; // 绘图半径需缩小mStrokeWidth / 2
        final float strokeRadius;

        switch (mShape) {
            case SHAPE_ROUND_RECT:
                strokeRadius = mRadius - narrowBy;
                canvas.drawRoundRect(narrowBounds(mBounds, narrowBy), strokeRadius, strokeRadius,
                        mPaint);
                break;
            case SHAPE_SIDE_CIRCLE_RECT:
                mRadius = (mBounds.top + mBounds.bottom) / 2.0f; // 自动计算半径
                strokeRadius = mRadius - narrowBy;
                canvas.drawRoundRect(narrowBounds(mBounds, narrowBy), strokeRadius, strokeRadius,
                        mPaint);
                break;
            case SHAPE_CIRCLE:
                // 计算圆心
                float cx = (mBounds.left + mBounds.right) / 2.0f;
                float cy = (mBounds.top + mBounds.bottom) / 2.0f;
                mRadius = Math.min(cx, cy);
                strokeRadius = mRadius - narrowBy;
                canvas.drawCircle(cx, cy, strokeRadius, mPaint);
                break;
            case SHAPE_RECT:
            default:
                canvas.drawRect(narrowBounds(mBounds, narrowBy), mPaint);
                break;
        }
    }

    /**
     * 绘制填充（仅调整画笔）
     *
     * @param canvas 画板
     */
    private void drawFill(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);

        if ((mFillMode & FILL_MODE_BITMAP) != 0) {
            if (mBitmap != null) {
                Matrix matrix = new Matrix();
                float parentWidth = mBounds.right - mBounds.left;
                float parentHeight = mBounds.bottom - mBounds.top;
                float marginX = Math.abs(parentWidth - mBitmap.getWidth());
                float marginY = Math.abs(parentHeight - mBitmap.getHeight());

                // 设置缩放
                float scaleX, scaleY;
                switch (mScaleType) {
                    case SCALE_TYPE_AT_LEAST:
                        if (marginX < marginY) {
                            scaleX = scaleY = parentWidth / mBitmap.getWidth();
                        } else {
                            scaleX = scaleY = parentHeight / mBitmap.getHeight();
                        }
                        break;
                    case SCALE_TYPE_AT_MOST:
                        if (marginX < marginY) {
                            scaleX = scaleY = parentHeight / mBitmap.getHeight();
                        } else {
                            scaleX = scaleY = parentWidth / mBitmap.getWidth();
                        }
                        break;
                    case SCALE_TYPE_FIT_FRAME:
                        scaleX = parentWidth / mBitmap.getWidth();
                        scaleY = parentHeight / mBitmap.getHeight();
                        break;
                    case SCALE_TYPE_NONE:
                    default:
                        scaleX = scaleY = 1.0f;
                        break;
                }
                matrix.postScale(scaleX, scaleY);

                // 图片居中
                marginX = Math.abs(parentWidth - mBitmap.getWidth() * scaleX);
                marginY = Math.abs(parentHeight - mBitmap.getHeight() * scaleY);
                float translateX = marginX / 2.0f;
                float translateY = marginY / 2.0f;
                matrix.postTranslate(translateX, translateY);

                mShader.setLocalMatrix(matrix);
            }
            mPaint.setShader(mShader);
            if ((mFillMode & FILL_MODE_SOLID) != 0) {
                // 如果fillMode == solid|bitmap，则根据设置图片的颜色蒙层
                if (mColorFilter == null) {
                    mColorFilter = new ColorMatrixColorFilter(parseColorMatrix(mColorFill));
                }
                mPaint.setColorFilter(mColorFilter);
            }
        } else {
            mPaint.setColor(mColorFill);
        }
        drawFillShape(canvas);
    }

    /**
     * 绘制填充（调整画笔后，实际绘制出填充形状）
     *
     * @param canvas 画板
     */
    private void drawFillShape(Canvas canvas) {
        final float narrowBy = ((mFillMode & FILL_MODE_BITMAP) == 0 && mStrokeWidth > 1.0f) ?
                (mStrokeWidth - 1.0f) : mStrokeWidth; // 当非图片填充时（即仅纯色填充时），-1.0调整误差
        float fillRadius;

        switch (mShape) {
            case SHAPE_ROUND_RECT:
                fillRadius = mRadius - narrowBy;
                canvas.drawRoundRect(narrowBounds(mBounds, narrowBy), fillRadius, fillRadius,
                        mPaint);
                break;
            case SHAPE_SIDE_CIRCLE_RECT:
                mRadius = (mBounds.top + mBounds.bottom) / 2.0f;
                fillRadius = mRadius - narrowBy;
                canvas.drawRoundRect(narrowBounds(mBounds, narrowBy), fillRadius, fillRadius, mPaint);
                break;
            case SHAPE_CIRCLE:
                // 计算圆心
                float cx = (mBounds.left + mBounds.right) / 2.0f;
                float cy = (mBounds.top + mBounds.bottom) / 2.0f;
                mRadius = Math.min(cx, cy);
                fillRadius = mRadius - narrowBy;
                canvas.drawCircle(cx, cy, fillRadius, mPaint);
                break;
            case SHAPE_RECT:
            default:
                canvas.drawRect(narrowBounds(mBounds, narrowBy), mPaint);
                break;
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mBounds = new RectF(left, top, right, bottom);
    }

    @Override
    public void setBounds(Rect bounds) {
        super.setBounds(bounds);
        mBounds = new RectF(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    @Override
    public int getIntrinsicWidth() {
        if (mBitmap != null) {
            return mBitmap.getWidth();
        }
        return super.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        if (mBitmap != null) {
            return mBitmap.getHeight();
        }
        return super.getIntrinsicHeight();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    /**
     * 缩小边界
     *
     * @param src 原边界大小
     * @param by  缩小的距离
     * @return 缩小后得到的边界
     */
    private RectF narrowBounds(RectF src, float by) {
        return new RectF(src.left + by, src.top + by, src.right - by, src.bottom - by);
    }

    /**
     * 根据颜色解析蒙层的ColorMatrix
     *
     * @param color 蒙层所需颜色
     * @return 蒙层的ColorMatrix
     */
    private ColorMatrix parseColorMatrix(int color) {
        float r = Color.red(color) / 255.0f;
        float g = Color.green(color) / 255.0f;
        float b = Color.blue(color) / 255.0f;
        float a = Color.alpha(color) / 255.0f;
        return new ColorMatrix(new float[]{
                r, 0, 0, 0, 0,
                0, g, 0, 0, 0,
                0, 0, b, 0, 0,
                0, 0, 0, a, 0
        });
    }
}
