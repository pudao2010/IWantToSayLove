package com.bluewhaledt.saylove.ui.register_login.login;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: ILoginView
 * @创建者: YanChao
 * @创建时间: 2016/11/29 18:00
 * @描述： TODO
 */
public interface ILoginView extends IRegisterAndLoginBaseView {
    void loginSuccess();

    void loginFailed(String errCode, String ErrorMsg);

    void loginError(Throwable e);
}
