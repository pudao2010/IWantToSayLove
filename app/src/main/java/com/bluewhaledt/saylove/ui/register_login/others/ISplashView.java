package com.bluewhaledt.saylove.ui.register_login.others;

import com.bluewhaledt.saylove.ui.register_login.login.IRegisterAndLoginBaseView;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: ISplashView
 * @创建者: YanChao
 * @创建时间: 2016/11/28 17:32
 * @描述： TODO
 */

public interface ISplashView extends IRegisterAndLoginBaseView {
    void loginSuccess();

    void loginFail(String errorCode, String errorMsg);

    void loginError(Throwable e);
}
