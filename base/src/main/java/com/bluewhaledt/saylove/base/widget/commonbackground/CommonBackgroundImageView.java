package com.bluewhaledt.saylove.base.widget.commonbackground;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bluewhaledt.saylove.base.util.ImageUtils;

/**
 * 支持XML配置的通用背景ImageView
 *
 * @author yintaibing
 * @date 2016/10/28
 */
public class CommonBackgroundImageView extends ImageView {

    private static final String STATE_SUPER = "state_super";
    private static final String STATE_COMMON_BACKGROUND = "state_common_background";

    private CommonBackgroundFactory.AttrSet mAttrSet;

    public CommonBackgroundImageView(Context context) {
        this(context, null);
    }

    public CommonBackgroundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonBackgroundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAttrSet = CommonBackgroundFactory.obtainAttrs(context, attrs);
        CommonBackgroundFactory.fromAttrSet(this, mAttrSet);
    }

    @Override
    public void setImageResource(int resId) {
        mAttrSet.bitmap = BitmapFactory.decodeResource(getResources(), resId);
        resetFillMode(mAttrSet);
        CommonBackgroundFactory.fromAttrSet(this, mAttrSet);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mAttrSet.bitmap = ImageUtils.drawableToBitmap(drawable);
        resetFillMode(mAttrSet);
        CommonBackgroundFactory.fromAttrSet(this, mAttrSet);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        mAttrSet.bitmap = bitmap;
        resetFillMode(mAttrSet);
        CommonBackgroundFactory.fromAttrSet(this, mAttrSet);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState());
        bundle.putSerializable(STATE_COMMON_BACKGROUND, mAttrSet);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER));
            mAttrSet = (CommonBackgroundFactory.AttrSet) bundle.getSerializable
                    (STATE_COMMON_BACKGROUND);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mAttrSet != null && mAttrSet.bitmap != null) {
            mAttrSet.bitmap.recycle();
        }
    }

    /**
     * 重设fillMode
     * solid -> bitmap
     * bitmap -> bitmap
     * solid|bitmap -> solid|bitmap
     *
     * @param attrSet 源属性集
     */
    private void resetFillMode(CommonBackgroundFactory.AttrSet attrSet) {
        if ((CommonBackground.FILL_MODE_BITMAP & attrSet.fillMode) == 0) {
            attrSet.fillMode = CommonBackground.FILL_MODE_BITMAP;
        }
    }
}
