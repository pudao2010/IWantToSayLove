package com.bluewhaledt.saylove.base.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.base.R;

/**
 * 通用图文弹框
 *
 * @author hechunshan
 * @date 2016/11/9
 */
public class CommonDialogHelper implements OnClickListener {

    private Activity mActivity;
    private Dialog mDialog;

    private LinearLayout mContentLayout;
    private ImageView mImageView;
    private TextView mPrimaryTextView;
    private TextView mSecondaryTextView;

    private Button mTopButton;
    private Button mBottomButton;
    private Button mLeftButton;
    private Button mRightButton;
    private ImageView mCloseBtn;

    private OnClickListener mImageClickListener;
    private boolean mImageAutoDismiss = true;

    private OnClickListener mPrimaryTextViewClickListener;
    private boolean mPrimaryTextAutoDismiss = true;
    private OnClickListener mSecondaryTextViewClickListener;
    private boolean mSecondaryTextAutoDismiss = true;

    private OnClickListener mTopBtnClickListener;
    private boolean mTopBtnAutoDismiss = true;
    private OnClickListener mBottomBtnClickListener;
    private boolean mBottomBtnAutoDismiss = true;

    private OnClickListener mLeftBtnClickListener;
    private boolean mLeftBtnAutoDismiss = true;
    private OnClickListener mRightBtnClickListener;
    private boolean mRightBtnAutoDismiss = true;

    private OnClickListener mCloseBtnClickListener;
    private boolean mCloseAutoDismiss = true;

    public CommonDialogHelper(@NonNull Activity activity) {
        this.mActivity = activity;
        initViews();
    }

    private void initViews() {
        if (!isValidContext()) {
            return;
        }
        mDialog = new Dialog(mActivity, R.style.Dialog_Fullscreen);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.zhenai_library_common_dialog_layout, null);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);

        mContentLayout = (LinearLayout) view.findViewById(R.id.view_dialog_content);

        mImageView = (ImageView) view.findViewById(R.id.iv_image);

        mPrimaryTextView = (TextView) view.findViewById(R.id.tv_primary_text);
        mSecondaryTextView = (TextView) view.findViewById(R.id.tv_secondary_text);

        mTopButton = (Button) view.findViewById(R.id.btn_top);
        mBottomButton = (Button) view.findViewById(R.id.btn_bottom);

        mLeftButton = (Button) view.findViewById(R.id.btn_left);
        mRightButton = (Button) view.findViewById(R.id.btn_right);

        mCloseBtn = (ImageView) view.findViewById(R.id.btn_close);

        mTopButton.setOnClickListener(this);
        mBottomButton.setOnClickListener(this);

        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);

        mCloseBtn.setOnClickListener(this);
    }

    public CommonDialogHelper setCancelable(boolean cancelable) {
        if (mDialog != null) {
            mDialog.setCancelable(cancelable);
        }
        return this;
    }

    public CommonDialogHelper setBackgroundResource(@DrawableRes int resId) {
        if (mContentLayout != null) {
            mContentLayout.setBackgroundResource(resId);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setBackgroundColor(@ColorRes int resId) {
        if (mContentLayout != null && mActivity != null) {
            mContentLayout.setBackgroundColor(mActivity.getResources().getColor(resId));
        }
        return this;
    }

    public CommonDialogHelper setImageVisibility(int visibility) {
        if (mImageView != null) {
            mImageView.setVisibility(visibility);
        }
        return this;
    }

    public CommonDialogHelper setImage(@DrawableRes int resId) {
        if (mImageView != null) {
            setImageVisibility(View.VISIBLE);
            mImageView.setImageResource(resId);
        }
        return this;
    }

    public CommonDialogHelper setImage(Bitmap bitmap) {
        if (mImageView != null) {
            setImageVisibility(View.VISIBLE);
            mImageView.setImageBitmap(bitmap);
        }
        return this;
    }

    public CommonDialogHelper setImageClickListener(OnClickListener listener) {
        if (mImageView != null) {
            mImageView.setOnClickListener(this);
            mImageClickListener = listener;
        }
        return this;
    }

    public CommonDialogHelper setImageAutoDismiss(boolean autoDismiss) {
        mImageAutoDismiss = autoDismiss;
        return this;
    }

    public CommonDialogHelper setPrimaryTextVisibility(int visibility) {
        if (mPrimaryTextView != null) {
            mPrimaryTextView.setVisibility(visibility);
        }
        return this;
    }

    public CommonDialogHelper setPrimaryText(CharSequence charSequence) {
        if (mPrimaryTextView != null) {
            setPrimaryTextVisibility(View.VISIBLE);
            mPrimaryTextView.setText(charSequence);
        }
        return this;
    }

    public CommonDialogHelper setPrimaryText(@StringRes int resId) {
        if (mPrimaryTextView != null) {
            setPrimaryTextVisibility(View.VISIBLE);
            mPrimaryTextView.setText(resId);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setPrimaryText(String txt) {
        setPrimaryText(Html.fromHtml(txt));
        return this;
    }

    public CommonDialogHelper setPrimaryTextGravity(int gravity) {
        if (mPrimaryTextView != null) {
            mPrimaryTextView.setGravity(gravity);
        }
        return this;
    }

    public CommonDialogHelper setPrimaryTextColor(String color) {
        if (mPrimaryTextView != null) {
            mPrimaryTextView.setTextColor(Color.parseColor(color));
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setPrimaryTextColor(@ColorRes int colorId) {
        if (mPrimaryTextView != null && mActivity != null) {
            mPrimaryTextView.setTextColor(mActivity.getResources().getColor(colorId));
        }
        return this;
    }

    public CommonDialogHelper setPrimaryTextSize(int sp) {
        if (mPrimaryTextView != null) {
            mPrimaryTextView.setTextSize(sp);
        }
        return this;
    }

    public CommonDialogHelper setPrimaryTextClickListener(OnClickListener listener) {
        if (mPrimaryTextView != null) {
            mPrimaryTextView.setOnClickListener(this);
            mPrimaryTextViewClickListener = listener;
        }
        return this;
    }

    public CommonDialogHelper setPrimaryTextAutoDismiss(boolean autoDismiss) {
        mPrimaryTextAutoDismiss = autoDismiss;
        return this;
    }

    public CommonDialogHelper setSecondaryTextVisibility(int visibility) {
        if (mSecondaryTextView != null) {
            mSecondaryTextView.setVisibility(visibility);
        }
        return this;
    }

    public CommonDialogHelper setSecondaryText(CharSequence charSequence) {
        if (mSecondaryTextView != null) {
            setSecondaryTextVisibility(View.VISIBLE);
            mSecondaryTextView.setText(charSequence);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setSecondaryText(String txt) {
        setSecondaryText(Html.fromHtml(txt));
        return this;
    }

    public CommonDialogHelper setSecondaryText(@StringRes int resId) {
        if (mSecondaryTextView != null) {
            setSecondaryTextVisibility(View.VISIBLE);
            mSecondaryTextView.setText(resId);
        }
        return this;
    }

    public CommonDialogHelper setSecondaryTextGravity(int gravity) {
        if (mSecondaryTextView != null) {
            mSecondaryTextView.setGravity(gravity);
        }
        return this;
    }

    public CommonDialogHelper setSecondaryTextColor(String color) {
        if (mSecondaryTextView != null) {
            mSecondaryTextView.setTextColor(Color.parseColor(color));
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setSecondaryTextColor(@ColorRes int colorId) {
        if (mSecondaryTextView != null && mActivity != null) {
            mSecondaryTextView.setTextColor(mActivity.getResources().getColor(colorId));
        }
        return this;
    }

    public CommonDialogHelper setSecondaryTextSize(int sp) {
        if (mSecondaryTextView != null) {
            mSecondaryTextView.setTextSize(sp);
        }
        return this;
    }

    public CommonDialogHelper setSecondaryTextOnClickListener(OnClickListener listener) {
        if (mSecondaryTextView != null) {
            mSecondaryTextView.setOnClickListener(this);
            mSecondaryTextViewClickListener = listener;
        }
        return this;
    }

    public CommonDialogHelper setSecondaryTextAutoDismiss(boolean autoDismiss) {
        mSecondaryTextAutoDismiss = autoDismiss;
        return this;
    }

    public CommonDialogHelper setTopBtnVisibility(int visibility) {
        if (mTopButton != null) {
            mTopButton.setVisibility(visibility);
        }
        return this;
    }

    public CommonDialogHelper setTopBtnBackground(@DrawableRes int resId) {
        if (mTopButton != null) {
            setTopBtnVisibility(View.VISIBLE);
            mTopButton.setBackgroundResource(resId);
        }
        return this;
    }

    public CommonDialogHelper setTopBtnBackgroundColor(@ColorRes int resId) {
        if (mTopButton != null && mActivity != null) {
            setTopBtnVisibility(View.VISIBLE);
            mTopButton.setBackgroundColor(mActivity.getResources().getColor(resId));
        }
        return this;
    }

    public CommonDialogHelper setTopBtnText(CharSequence charSequence) {
        if (mTopButton != null) {
            setTopBtnVisibility(View.VISIBLE);
            mTopButton.setText(charSequence);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setTopBtnText(String txt) {
        setTopBtnText(Html.fromHtml(txt));
        return this;
    }

    public CommonDialogHelper setTopBtnText(@StringRes int resId) {
        if (mTopButton != null) {
            setTopBtnVisibility(View.VISIBLE);
            mTopButton.setText(resId);
        }
        return this;
    }

    public CommonDialogHelper setTopBtnTextColor(String color) {
        if (mTopButton != null) {
            setTopBtnVisibility(View.VISIBLE);
            mTopButton.setTextColor(Color.parseColor(color));
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setTopBtnTextColor(@ColorRes int resId) {
        if (mTopButton != null && mActivity != null) {
            setTopBtnVisibility(View.VISIBLE);
            mTopButton.setTextColor(mActivity.getResources().getColor(resId));
        }
        return this;
    }

    public CommonDialogHelper setTopBtnTextSize(int sp) {
        if (mTopButton != null) {
            setTopBtnVisibility(View.VISIBLE);
            mTopButton.setTextSize(sp);
        }
        return this;
    }

    public CommonDialogHelper setTopBtnClickListener(OnClickListener listener) {
        mTopBtnClickListener = listener;
        return this;
    }

    public CommonDialogHelper setTopBtnAutoDismiss(boolean autoDismiss) {
        mTopBtnAutoDismiss = autoDismiss;
        return this;
    }

    public CommonDialogHelper setBottomBtnVisibility(int visibility) {
        if (mBottomButton != null) {
            setBottomBtnVisibility(View.VISIBLE);
            mBottomButton.setVisibility(visibility);
        }
        return this;
    }

    public CommonDialogHelper setBottomBtnBackground(@DrawableRes int resId) {
        if (mBottomButton != null) {
            setBottomBtnVisibility(View.VISIBLE);
            mBottomButton.setBackgroundResource(resId);
        }
        return this;
    }

    public CommonDialogHelper setBottomBtnBackgroundColor(@ColorRes int resId) {
        if (mBottomButton != null && mActivity != null) {
            setBottomBtnVisibility(View.VISIBLE);
            mBottomButton.setBackgroundColor(mActivity.getResources().getColor(resId));
        }
        return this;
    }

    public CommonDialogHelper setBottomBtnText(CharSequence charSequence) {
        if (mBottomButton != null) {
            setBottomBtnVisibility(View.VISIBLE);
            mBottomButton.setText(charSequence);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setBottomBtnText(String txt) {
        setBottomBtnText(Html.fromHtml(txt));
        return this;
    }

    public CommonDialogHelper setBottomBtnText(@StringRes int resId) {
        if (mBottomButton != null) {
            setBottomBtnVisibility(View.VISIBLE);
            mBottomButton.setText(resId);
        }
        return this;
    }

    public CommonDialogHelper setBottomBtnTextColor(String color) {
        if (mBottomButton != null) {
            setBottomBtnVisibility(View.VISIBLE);
            mBottomButton.setTextColor(Color.parseColor(color));
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setBottomBtnTextColor(@ColorRes int resId) {
        if (mBottomButton != null && mActivity != null) {
            setBottomBtnVisibility(View.VISIBLE);
            mBottomButton.setTextColor(mActivity.getResources().getColor(resId));
        }
        return this;
    }

    public CommonDialogHelper setBottomBtnTextSize(int sp) {
        if (mBottomButton != null) {
            setBottomBtnVisibility(View.VISIBLE);
            mBottomButton.setTextSize(sp);
        }
        return this;
    }

    public CommonDialogHelper setBottomBtnClickListener(OnClickListener listener) {
        mBottomBtnClickListener = listener;
        return this;
    }

    public CommonDialogHelper setBottomBtnAutoDismiss(boolean autoDismiss) {
        mBottomBtnAutoDismiss = autoDismiss;
        return this;
    }

    public CommonDialogHelper setLeftBtnVisibility(int visibility) {
        if (mLeftButton != null) {
            mLeftButton.setVisibility(visibility);
        }
        return this;
    }

    public CommonDialogHelper setLeftBtnBackground(@DrawableRes int resId) {
        if (mLeftButton != null) {
            setLeftBtnVisibility(View.VISIBLE);
            mLeftButton.setBackgroundResource(resId);
        }
        return this;
    }

    public CommonDialogHelper setLeftBtnBackgroundColor(@ColorRes int resId) {
        if (mLeftButton != null && mActivity != null) {
            setLeftBtnVisibility(View.VISIBLE);
            mLeftButton.setBackgroundColor(mActivity.getResources().getColor(resId));
        }
        return this;
    }

    public CommonDialogHelper setLeftBtnText(CharSequence charSequence) {
        if (mLeftButton != null) {
            setLeftBtnVisibility(View.VISIBLE);
            mLeftButton.setText(charSequence);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setLeftBtnText(String txt) {
        setLeftBtnText(Html.fromHtml(txt));
        return this;
    }

    public CommonDialogHelper setLeftBtnText(@StringRes int resId) {
        if (mLeftButton != null) {
            setLeftBtnVisibility(View.VISIBLE);
            mLeftButton.setText(resId);
        }
        return this;
    }

    public CommonDialogHelper setLeftBtnTextColor(String color) {
        if (mLeftButton != null) {
            setLeftBtnVisibility(View.VISIBLE);
            mLeftButton.setTextColor(Color.parseColor(color));
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setLeftBtnTextColor(@ColorRes int resId) {
        if (mLeftButton != null && mActivity != null) {
            setLeftBtnVisibility(View.VISIBLE);
            mLeftButton.setTextColor(mActivity.getResources().getColor(resId));
        }
        return this;
    }

    public CommonDialogHelper setLeftBtnTextSize(int sp) {
        if (mLeftButton != null) {
            setLeftBtnVisibility(View.VISIBLE);
            mLeftButton.setTextSize(sp);
        }
        return this;
    }

    public CommonDialogHelper setLeftBtnClickListener(OnClickListener listener) {
        mLeftBtnClickListener = listener;
        return this;
    }

    public CommonDialogHelper setLeftBtnAutoDismiss(boolean autoDismiss) {
        mLeftBtnAutoDismiss = autoDismiss;
        return this;
    }

    public CommonDialogHelper setRightBtnVisibility(int visibility) {
        if (mRightButton != null) {
            mRightButton.setVisibility(visibility);
        }
        return this;
    }

    public CommonDialogHelper setRightBtnBackground(@DrawableRes int resId) {
        if (mRightButton != null) {
            setRightBtnVisibility(View.VISIBLE);
            mRightButton.setBackgroundResource(resId);
        }
        return this;
    }

    public CommonDialogHelper setRightBtnBackgroundColor(@ColorRes int resId) {
        if (mRightButton != null && mActivity != null) {
            setRightBtnVisibility(View.VISIBLE);
            mRightButton.setBackgroundColor(mActivity.getResources().getColor(resId));
        }
        return this;
    }

    public CommonDialogHelper setRightBtnText(CharSequence charSequence) {
        if (mRightButton != null) {
            setRightBtnVisibility(View.VISIBLE);
            mRightButton.setText(charSequence);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setRightBtnText(String txt) {
        setRightBtnText(Html.fromHtml(txt));
        return this;
    }

    public CommonDialogHelper setRightBtnText(@StringRes int resId) {
        if (mRightButton != null) {
            setRightBtnVisibility(View.VISIBLE);
            mRightButton.setText(resId);
        }
        return this;
    }

    public CommonDialogHelper setRightBtnTextColor(String color) {
        if (mRightButton != null) {
            setRightBtnVisibility(View.VISIBLE);
            mRightButton.setTextColor(Color.parseColor(color));
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public CommonDialogHelper setRightBtnTextColor(@ColorRes int resId) {
        if (mRightButton != null && mActivity != null) {
            setRightBtnVisibility(View.VISIBLE);
            mRightButton.setTextColor(mActivity.getResources().getColor(resId));
        }
        return this;
    }

    public CommonDialogHelper setRightBtnTextSize(int sp) {
        if (mRightButton != null) {
            setRightBtnVisibility(View.VISIBLE);
            mRightButton.setTextSize(sp);
        }
        return this;
    }

    public CommonDialogHelper setRightBtnClickListener(OnClickListener listener) {
        mRightBtnClickListener = listener;
        return this;
    }

    public CommonDialogHelper setRightBtnAutoDismiss(boolean autoDismiss) {
        mRightBtnAutoDismiss = autoDismiss;
        return this;
    }

    public CommonDialogHelper setCloseBtnVisibility(int visibility) {
        if (mCloseBtn != null) {
            mCloseBtn.setVisibility(visibility);
        }
        return this;
    }

    public CommonDialogHelper setCloseBtnImage(@DrawableRes int resId) {
        if (mCloseBtn != null) {
            setCloseBtnVisibility(View.VISIBLE);
            mCloseBtn.setImageResource(resId);
        }
        return this;
    }

    public CommonDialogHelper setCloseBtnImage(Bitmap bitmap) {
        if (mCloseBtn != null) {
            setCloseBtnVisibility(View.VISIBLE);
            mCloseBtn.setImageBitmap(bitmap);
        }
        return this;
    }

    public CommonDialogHelper setCloseBtnClickListener(OnClickListener listener) {
        mCloseBtnClickListener = listener;
        return this;
    }

    public CommonDialogHelper setCloseBtnAutoDismiss(boolean autoDismiss) {
        mCloseAutoDismiss = autoDismiss;
        return this;
    }

    public void show() {
        if (null != mDialog && isValidContext()) {
            try {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                Window win = mDialog.getWindow();
                if (win != null) {
                    win.setWindowAnimations(R.style.Dialog_Animation);
                    win.getDecorView().setPadding(0, 0, 0, 0);
                    WindowManager.LayoutParams lp = win.getAttributes();
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    win.setAttributes(lp);
                }

                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing() && isValidContext()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_image) {
            if (mImageAutoDismiss) {
                dismiss();
            }
            if (mImageClickListener != null) {
                mImageClickListener.onClick(v);
            }
        } else if (id == R.id.tv_primary_text) {
            if (mPrimaryTextAutoDismiss) {
                dismiss();
            }
            if (mPrimaryTextViewClickListener != null) {
                mPrimaryTextViewClickListener.onClick(v);
            }
        } else if (id == R.id.tv_secondary_text) {
            if (mSecondaryTextAutoDismiss) {
                dismiss();
            }
            if (mSecondaryTextViewClickListener != null) {
                mSecondaryTextViewClickListener.onClick(v);
            }
        } else if (id == R.id.btn_top) {
            if (mTopBtnAutoDismiss) {
                dismiss();
            }
            if (mTopBtnClickListener != null) {
                mTopBtnClickListener.onClick(v);
            }
        } else if (id == R.id.btn_bottom) {
            if (mBottomBtnAutoDismiss) {
                dismiss();
            }
            if (mBottomBtnClickListener != null) {
                mBottomBtnClickListener.onClick(v);
            }
        } else if (id == R.id.btn_left) {
            if (mLeftBtnAutoDismiss) {
                dismiss();
            }
            if (mLeftBtnClickListener != null) {
                mLeftBtnClickListener.onClick(v);
            }
        } else if (id == R.id.btn_right) {
            if (mRightBtnAutoDismiss) {
                dismiss();
            }
            if (mRightBtnClickListener != null) {
                mRightBtnClickListener.onClick(v);
            }
        } else if (id == R.id.btn_close) {
            if (mCloseAutoDismiss) {
                dismiss();
            }
            if (mCloseBtnClickListener != null) {
                mCloseBtnClickListener.onClick(v);
            }
        }
    }

    private boolean isValidContext() {
        return mActivity != null && !mActivity.isFinishing();
    }
}
