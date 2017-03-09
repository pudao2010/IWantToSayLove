package com.bluewhaledt.saylove.ui.register_login.regist;

import com.bluewhaledt.saylove.ui.register_login.login.IRegisterAndLoginBaseView;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.regist
 * @文件名: IRegistView
 * @创建者: YanChao
 * @创建时间: 2016/11/29 19:38
 * @描述： TODO
 */
public interface IRegistView extends IRegisterAndLoginBaseView{
    void onSuccess();
    void resendCodeSuccess();
    void requestCodeFailed(String errorCode, String errorMsg);
}
