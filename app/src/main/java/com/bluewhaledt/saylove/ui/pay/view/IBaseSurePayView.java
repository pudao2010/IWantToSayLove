package com.bluewhaledt.saylove.ui.pay.view;


import com.bluewhaledt.saylove.ui.pay.entity.PayCategories;

/**
 * Created by Administrator on 2016/11/14.
 */

public interface IBaseSurePayView extends IPayView {
    void getPayTypeSuccess(PayCategories payCategories);
}
