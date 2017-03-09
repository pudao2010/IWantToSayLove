package com.bluewhaledt.saylove.base.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

/**
 * 动画工具类
 *
 * @author yintaibing
 * @date 2016/10/26
 */
public class AnimUtils {

    /**
     * 开始连续多个动画（对重复动画repeatCount>1的支持待完善）
     *
     * @param view                   View对象
     * @param context                Context对象
     * @param animations             动画
     * @param durations              额外指定每个动画的持续时间（若为负数，则采用资源文件中定义的值）
     * @param continuousAnimListener 动画的监听器
     */
    public static void startContinuousAnim(View view, Context context, Animation[] animations,
                                           long[]
                                                   durations, ContinuousAnimListener continuousAnimListener) {
        if (view != null && context != null && animations != null && animations.length > 0) {
            ContinuousAnimationListener listener = new ContinuousAnimationListener(view,
                    animations, durations, continuousAnimListener);
            Animation firstAnim = animations[0];
            if (firstAnim != null) {
                if (durations != null && durations.length > 0 && durations[0] >= 0) {
                    firstAnim.setDuration(durations[0]);
                }
                firstAnim.setAnimationListener(listener);
                view.startAnimation(firstAnim);
            }
        }
    }

    /**
     * 自定义的连续动画监听接口
     */
    public interface ContinuousAnimListener {
        /**
         * 动画开始的回调
         *
         * @param animIndex 第几个动画
         * @param animCount 动画总数
         * @param anim      动画对象
         */
        void onAnimStart(int animIndex, int animCount, Animation anim);

        /**
         * 动画结束的回调
         *
         * @param animIndex 第几个动画
         * @param animCount 动画总数
         * @param anim      动画对象
         */
        void onAnimEnd(int animIndex, int animCount, Animation anim);
    }

    /**
     * 自定义的动画监听器
     */
    private static class ContinuousAnimationListener implements Animation.AnimationListener {
        private View mView;
        private Animation[] mAnimations;
        private long[] mDurations;
        private ContinuousAnimListener mContinuousAnimListener;
        private int mCurrentIndex;

        public ContinuousAnimationListener(View view, Animation[] animations, long[]
                durations, ContinuousAnimListener continuousAnimListener) {
            mView = view;
            mAnimations = animations;
            mDurations = durations;
            mContinuousAnimListener = continuousAnimListener;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (mContinuousAnimListener != null) {
                mContinuousAnimListener.onAnimStart(mCurrentIndex, mAnimations != null ?
                        mAnimations.length : 0, animation);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mContinuousAnimListener != null) {
                mContinuousAnimListener.onAnimEnd(mCurrentIndex, mAnimations != null ?
                        mAnimations.length : 0, animation);
            }

            mCurrentIndex++;
            if (mCurrentIndex < mAnimations.length) {
                Animation nextAnim = mAnimations[mCurrentIndex];
                if (nextAnim != null) {
                    if (mDurations != null && mDurations.length > mCurrentIndex &&
                            mDurations[mCurrentIndex] >= 0) {
                        nextAnim.setDuration(mDurations[mCurrentIndex]);
                    }
                    nextAnim.setAnimationListener(this);
                    mView.startAnimation(nextAnim);
                }
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }
}
