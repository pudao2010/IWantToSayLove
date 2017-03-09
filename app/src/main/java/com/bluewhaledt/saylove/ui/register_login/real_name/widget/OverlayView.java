/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2016 Douglas Nassif Roma Junior
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bluewhaledt.saylove.ui.register_login.real_name.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.View;

import com.bluewhaledt.saylove.R;
/**
 * 描述：这个是我们pop，做动画，
 * 作者：shiming_li
 * 时间：2016/11/30 15:00
 * 包名：com.zhenai.saylove_icon.ui.register_login.real_name
 * 项目名：SayLove
 */
@SuppressLint("ViewConstructor")
public class OverlayView extends View {

    private static final int mDefaultOverlayCircleOffsetRes = R.dimen.simpletooltip_overlay_circle_offset;
    private static final int mDefaultOverlayAlphaRes = 120;

    private View mAnchorView;
    private Bitmap bitmap;
    private float offset = 0;
    private boolean invalidated = true;

    OverlayView(Context context, View anchorView) {
        super(context);
        this.mAnchorView = anchorView;
        this.offset = context.getResources().getDimension(mDefaultOverlayCircleOffsetRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (invalidated)
            createWindowFrame();

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private void createWindowFrame() {
        final int width = getMeasuredWidth(), height = getMeasuredHeight();
        if (width <= 0 || height <= 0)
            return;

        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas osCanvas = new Canvas(bitmap);

        RectF outerRectangle = new RectF(0, 0, width, height);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setAlpha(120);
        osCanvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        RectF anchorRecr = SimpleTooltipUtils.calculeRectInWindow(mAnchorView);
        RectF overlayRecr = SimpleTooltipUtils.calculeRectInWindow(this);

        float left = anchorRecr.left - overlayRecr.left;
        float top = anchorRecr.top - overlayRecr.top;
        RectF oval = new RectF(left - offset, top - offset, left + mAnchorView.getMeasuredWidth() + offset, top + mAnchorView.getMeasuredHeight() + offset);

        osCanvas.drawOval(oval, paint);

        invalidated = false;
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        invalidated = true;
    }

    public View getAnchorView() {
        return mAnchorView;
    }

    public void setAnchorView(View anchorView) {
        this.mAnchorView = anchorView;
        invalidate();
    }
}
