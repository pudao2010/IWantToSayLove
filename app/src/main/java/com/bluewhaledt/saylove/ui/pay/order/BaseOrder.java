package com.bluewhaledt.saylove.ui.pay.order;

import android.app.Activity;

import com.bluewhaledt.saylove.ui.pay.entity.PayOrder;
import com.bluewhaledt.saylove.ui.pay.view.IBaseSurePayView;

/**
 * Created by rade.chan on 2016/11/14.
 */

public abstract class BaseOrder {
    protected Activity mContext;
    protected IBaseSurePayView mSurePayView;
    protected int productId;



    public BaseOrder(Activity context, IBaseSurePayView surePayView) {
        this.mContext = context;
        this.mSurePayView = surePayView;

    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public abstract void dealOrder(PayOrder order);


}
