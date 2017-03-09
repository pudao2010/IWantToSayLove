package com.bluewhaledt.saylove.base.widget.commonbackground;

import android.graphics.Bitmap;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;

/**
 * CommonBackground的集合
 *
 * @author yintaibing
 * @date 2016/10/28
 */
public class CommonBackgroundSet {
    private static final int STATE_COUNT = 3;
    private static final int STATE_DISABLED = 0;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_PRESSED = 2;

    private CommonBackground[] mDrawables;

    CommonBackgroundSet() {
        mDrawables = new CommonBackground[STATE_COUNT];
        for (int i = 0; i < STATE_COUNT; i++) {
            mDrawables[i] = new CommonBackground();
        }
    }

    /**
     * 返回当前集合的迭代器
     *
     * @return
     */
    public CommonBackgroundIterator forEach() {
        return new CommonBackgroundIterator(this);
    }

    /**
     * 返回对应disabled状态的CommonBackground对象
     *
     * @return 对应disabled状态的CommonBackground对象
     */
    public ICommonBackground theDisabled() {
        return mDrawables[STATE_DISABLED];
    }

    /**
     * 返回对应normal状态的CommonBackground对象
     *
     * @return 对应normal状态的CommonBackground对象
     */
    public ICommonBackground theNormal() {
        return mDrawables[STATE_NORMAL];
    }

    /**
     * 返回对应pressed状态的CommonBackground对象
     *
     * @return 对应pressed状态的CommonBackground对象
     */
    public ICommonBackground thePressed() {
        return mDrawables[STATE_PRESSED];
    }

    /**
     * 显示到你的View上
     *
     * @param yourView 你的View
     */
    public void showOn(View yourView) {
        if (yourView != null) {
            StateListDrawable stateList = new StateListDrawable();
            // 以下顺序不可更改
            // when disabled
            stateList.addState(new int[]{-android.R.attr.state_enabled},
                    mDrawables[STATE_DISABLED]);
            // View.PRESSED_ENABLED_STATE_SET
            stateList.addState(new int[]{android.R.attr.state_pressed, android.R.attr
                    .state_enabled}, mDrawables[STATE_PRESSED]);
            // View.ENABLED_FOCUSED_STATE_SET
            stateList.addState(new int[]{android.R.attr.state_enabled, android.R.attr
                    .state_focused}, mDrawables[STATE_NORMAL]);
            // View.ENABLED_STATE_SET
            stateList.addState(new int[]{android.R.attr.state_enabled}, mDrawables[STATE_NORMAL]);
            // View.FOCUSED_STATE_SET
            stateList.addState(new int[]{android.R.attr.state_focused}, mDrawables[STATE_NORMAL]);
            // View.EMPTY_STATE_SET
            stateList.addState(new int[]{}, mDrawables[STATE_NORMAL]);
            // View.WINDOW_FOCUSED_STATE_SET
            stateList.addState(new int[]{android.R.attr.state_window_focused},
                    mDrawables[STATE_DISABLED]);

            if (Build.VERSION.SDK_INT >= 16) {
                yourView.setBackground(stateList);
            } else {
                yourView.setBackgroundDrawable(stateList);
            }
            yourView.setClickable(true);
        }
    }

    public class CommonBackgroundIterator implements ICommonBackground {
        private CommonBackgroundSet mInner;

        CommonBackgroundIterator(CommonBackgroundSet set) {
            mInner = set;
        }

        /**
         * 设置形状
         *
         * @param shape 形状
         * @return
         */
        @Override
        public ICommonBackground shape(int shape) {
            for (ICommonBackground drawable : mInner.mDrawables) {
                drawable.shape(shape);
            }
            return this;
        }

        /**
         * 设置填充模式
         *
         * @param fillMode 填充模式
         * @return
         */
        @Override
        public ICommonBackground fillMode(int fillMode) {
            for (ICommonBackground drawable : mInner.mDrawables) {
                drawable.fillMode(fillMode);
            }
            return this;
        }

        /**
         * 设置缩放类型
         *
         * @param scaleType 缩放类型
         * @return
         */
        @Override
        public ICommonBackground scaleType(int scaleType) {
            for (ICommonBackground drawable : mInner.mDrawables) {
                drawable.scaleType(scaleType);
            }
            return this;
        }

        /**
         * 设置描边模式
         *
         * @param strokeMode 描边模式
         * @return
         */
        @Override
        public ICommonBackground strokeMode(int strokeMode) {
            for (ICommonBackground drawable : mInner.mDrawables) {
                drawable.strokeMode(strokeMode);
            }
            return this;
        }

        /**
         * 设置描边宽度
         *
         * @param strokeWidth 设置描边宽度
         * @return
         */
        @Override
        public ICommonBackground strokeWidth(int strokeWidth) {
            for (ICommonBackground drawable : mInner.mDrawables) {
                drawable.strokeWidth(strokeWidth);
            }
            return this;
        }

        /**
         * 设置虚线描边时，单个实线的长度
         *
         * @param strokeDashSolid 单个实线的长度
         * @return
         */
        @Override
        public ICommonBackground strokeDashSolid(int strokeDashSolid) {
            for (ICommonBackground drawable : mInner.mDrawables) {
                drawable.strokeDashSolid(strokeDashSolid);
            }
            return this;
        }

        /**
         * 设置虚线描边时，单个空白的长度
         *
         * @param strokeDashSpace 单个空白的长度
         * @return
         */
        @Override
        public ICommonBackground strokeDashSpace(int strokeDashSpace) {
            for (ICommonBackground drawable : mInner.mDrawables) {
                drawable.strokeDashSpace(strokeDashSpace);
            }
            return this;
        }

        /**
         * 设置圆角或圆形的半径
         *
         * @param radius 圆角或圆形的半径
         * @return
         */
        @Override
        public ICommonBackground radius(int radius) {
            for (ICommonBackground drawable : mInner.mDrawables) {
                drawable.radius(radius);
            }
            return this;
        }

        /**
         * 设置填充颜色
         *
         * @param colorFill 填充颜色
         * @return
         */
        @Override
        public ICommonBackground colorFill(int colorFill) {
            for (ICommonBackground drawable : mInner.mDrawables) {
                drawable.colorFill(colorFill);
            }
            return this;
        }

        /**
         * 设置描边颜色
         *
         * @param colorStroke 描边颜色
         * @return
         */
        @Override
        public ICommonBackground colorStroke(int colorStroke) {
            for (ICommonBackground drawable : mInner.mDrawables) {
                drawable.colorStroke(colorStroke);
            }
            return this;
        }

        /**
         * 设置填充位图
         *
         * @param bitmap 填充位图
         * @return
         */
        @Override
        public ICommonBackground bitmap(Bitmap bitmap) {
            for (ICommonBackground drawable : mInner.mDrawables) {
                drawable.bitmap(bitmap);
            }
            return this;
        }

        @Override
        public void showOn(View yourView) {
            if (yourView != null) {
                mInner.showOn(yourView);
            }
        }
    }
}
