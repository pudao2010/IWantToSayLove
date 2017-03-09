package com.bluewhaledt.saylove.ui.pay.order;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.bluewhaledt.saylove.ui.pay.entity.PayOrder;
import com.bluewhaledt.saylove.ui.pay.utils.UnionpayUtils;
import com.bluewhaledt.saylove.ui.pay.view.IBaseSurePayView;

/**
 * Created by Administrator on 2016/11/14.
 */

public class UnionPayOrder extends BaseOrder {
    public UnionPayOrder(Activity context, IBaseSurePayView surePayView) {
        super(context, surePayView);
    }

    @Override
    public void dealOrder(PayOrder order) {
        String submitUrl = order.orderId;
        UnionpayUtils.pay(mContext, submitUrl,
                UnionpayUtils.MODE_PRODUCT);
    }

    public void dealTclResult(Intent data) {
        if (data == null) {
            return;
        }
        String str = data.getExtras().getString("pay_result");
        if (!TextUtils.isEmpty(str)) {
            if (str.equalsIgnoreCase("success")) {
                mSurePayView.paySuccess();
            } else if (str.equalsIgnoreCase("fail")
                    || str.equalsIgnoreCase("cancel")) {
                mSurePayView.payFailed("");
            }

        }

    }
}
