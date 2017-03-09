package com.bluewhaledt.saylove.ui.register_login.others;

import android.content.Context;

import com.bluewhaledt.saylove.ui.register_login.login.LoginBasePresenter;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: SplashPresenter
 * @创建者: YanChao
 * @创建时间: 2016/11/28 17:31
 * @描述： 闪屏页的数据逻辑
 */
public class SplashPresenter extends LoginBasePresenter {
    private ISplashView mISplashView;

    public SplashPresenter(Context context, ISplashView view) {
        super(context, view);
        mISplashView = view;
        mContext = context;
    }

    @Override
    protected void loginError(Throwable e) {
        mISplashView.loginError(e);
    }

    @Override
    protected void loginSuccess() {
        mISplashView.loginSuccess();
    }

    @Override
    protected void loginFailed(String errorCode, String errorMsg) {
        mISplashView.loginFail(errorCode, errorMsg);
    }

}
