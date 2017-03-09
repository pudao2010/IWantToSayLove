package com.bluewhaledt.saylove.ui.pay.order;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.ui.pay.entity.PayOrder;
import com.bluewhaledt.saylove.ui.pay.entity.Result;
import com.bluewhaledt.saylove.ui.pay.view.IBaseSurePayView;

import java.net.URLEncoder;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 阿里支付
 * Created by rade.chan on 2016/11/14.
 */

public class AliPayOrder extends BaseOrder {


    public AliPayOrder(Activity context, IBaseSurePayView surePayView) {
        super(context, surePayView);
    }

    @Override
    public void dealOrder(PayOrder data) {
        final String info = data.signData + "&sign=" + "\""
                + URLEncoder.encode(data.sign) + "\""
                + "&" + getSignType();
        // 极简支付
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                PayTask aliPay = new PayTask(mContext);
                // 调用支付接口
                String result = aliPay.pay(info, true);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                DebugUtils.d("AliPay",e.getMessage());
            }

            @Override
            public void onNext(String result) {
                Result resultObj = new Result(result);
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultObj.resultStatus, "9000")) {
                    mSurePayView.paySuccess();
                    ToastUtils.toast(mContext,"支付成功");
                } else if (TextUtils.equals(resultObj.resultStatus, "8000")) {
                    ToastUtils.toast(mContext,"支付结果确认中");
                } else {
                    mSurePayView.payFailed("支付失败");
                    ToastUtils.toast(mContext,"支付失败");
                }
            }
        });
    }


    private static String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
