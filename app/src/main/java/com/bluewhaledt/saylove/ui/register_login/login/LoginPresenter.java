package com.bluewhaledt.saylove.ui.register_login.login;

import android.content.Context;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: LoginPresenter
 * @创建者: YanChao
 * @创建时间: 2016/11/28 17:17
 * @描述： 登录特有逻辑
 */
public class LoginPresenter extends LoginBasePresenter {

    private ILoginView mILoginView;

    public LoginPresenter(Context context, ILoginView view) {
        super(context, view);
        mContext = context;
        mILoginView = view;
    }

    public boolean checkIsValid(String phone, String password) {
        boolean phoneResult = checkPhoneIsValid(phone);
        boolean passwordResult = checkPasswordIsValid(password);
        if (!phoneResult) {
            ToastUtils.toast(mContext, mContext.getString(R.string.register_phone_tips));
            return false;
        } else if (!passwordResult) {
            ToastUtils.toast(mContext, mContext.getString(R.string.register_password_tips));
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void loginError(Throwable e) {

    }

    @Override
    protected void loginSuccess() {
        mILoginView.loginSuccess();
    }

    @Override
    protected void loginFailed(String errorCode, String errorMsg) {
        DebugUtils.d("yan", "loginpresenter" + errorCode);
        mILoginView.loginFailed(errorCode, errorMsg);
    }

}
