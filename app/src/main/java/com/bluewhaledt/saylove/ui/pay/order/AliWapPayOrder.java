package com.bluewhaledt.saylove.ui.pay.order;

import android.app.Activity;
import android.content.Intent;

import com.bluewhaledt.saylove.ui.pay.WapPayWebViewActivity;
import com.bluewhaledt.saylove.ui.pay.entity.PayOrder;
import com.bluewhaledt.saylove.ui.pay.view.IBaseSurePayView;


/**
 * Created by rade.chan on 2016/11/14.
 */

public class AliWapPayOrder extends BaseOrder {
    public AliWapPayOrder(Activity context, IBaseSurePayView surePayView) {
        super(context, surePayView);
    }

    @Override
    public void dealOrder(PayOrder order) {
        String info = order.htmlText;

        Intent mIntent = new Intent(mContext,
                WapPayWebViewActivity.class);
        mIntent.putExtra("photoUrl", info);
        mIntent.putExtra("productId", productId);
        mContext.startActivity(mIntent);
    }
}
