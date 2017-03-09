package com.bluewhaledt.saylove.ui.pay.view;

import com.bluewhaledt.saylove.ui.pay.entity.PurchasePageData;

/**
 * Created by zhenai-liliyan on 16/12/6.
 */

public interface IProductPageViewAction {

    void onProductDataReceived(PurchasePageData data);

    void showLoadProgress();

    void dismissLoadProgress();


}
