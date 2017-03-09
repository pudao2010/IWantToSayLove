package com.bluewhaledt.saylove.ui.pay.view;

/**
 * Created by zhenai-liliyan on 16/8/1.
 */
public interface IPayView {

    void showHasPayIn24HoursDialog(String windowContent);

    void paySuccess();

    void payFailed(String msg);


}
