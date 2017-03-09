package com.bluewhaledt.saylove.base.widget.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Html;

/**
 * 通用图文弹框
 * @author hechunshan
 * @date 2016/11/9
 */

public class ProgressDialogHelper {

    private Activity mActivity;
    private ProgressDialog mDialog;

    public ProgressDialogHelper(@NonNull Activity activity) {
        this.mActivity = activity;
        initViews();
    }

    private void initViews() {
        if (!isValidContext()) {
            return;
        }
        mDialog = new ProgressDialog(mActivity);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);
        mDialog.setCanceledOnTouchOutside(false);
    }

    public ProgressDialogHelper setCancelable(boolean cancelable) {
        if (mDialog != null) {
            mDialog.setCancelable(cancelable);
        }
        return this;
    }

    public ProgressDialogHelper setMessage(CharSequence charSequence) {
        if (mDialog != null) {
            mDialog.setMessage(charSequence);
        }
        return this;
    }

    public ProgressDialogHelper setMessage(@StringRes int resId) {
        if (mDialog != null && mActivity != null) {
            mDialog.setMessage(mActivity.getString(resId));
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public ProgressDialogHelper setMessage(String txt) {
        setMessage(Html.fromHtml(txt));
        return this;
    }

    public void show() {
        if (null != mDialog && isValidContext()) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing() && isValidContext()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private boolean isValidContext() {
        return mActivity != null && !mActivity.isFinishing();
    }
}
