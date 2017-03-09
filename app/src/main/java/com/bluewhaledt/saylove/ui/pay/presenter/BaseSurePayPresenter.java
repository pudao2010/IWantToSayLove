package com.bluewhaledt.saylove.ui.pay.presenter;

import android.app.Activity;
import android.content.Intent;

import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.pay.constant.PayType;
import com.bluewhaledt.saylove.ui.pay.entity.PayCategories;
import com.bluewhaledt.saylove.ui.pay.entity.PayOrder;
import com.bluewhaledt.saylove.ui.pay.order.AliPayOrder;
import com.bluewhaledt.saylove.ui.pay.order.AliWapPayOrder;
import com.bluewhaledt.saylove.ui.pay.order.BaseOrder;
import com.bluewhaledt.saylove.ui.pay.order.UnionPayOrder;
import com.bluewhaledt.saylove.ui.pay.order.WeChatOrder;
import com.bluewhaledt.saylove.ui.pay.service.PayService;
import com.bluewhaledt.saylove.ui.pay.view.IBaseSurePayView;

import rx.Observable;


/**
 * Created by rade.chan on 2016/11/10.
 */

public class BaseSurePayPresenter {

    private PayService payService;
    private IBaseSurePayView mBaseSurePayView;
    private Activity mActivity;
    private BaseOrder order;
    private final int ANDROID_PLATFORM = 1;

    public BaseSurePayPresenter(Activity context, IBaseSurePayView view) {
        this.mActivity = context;
        this.mBaseSurePayView = view;
        payService = ZARetrofit.getService(context, PayService.class);
    }


    public void getPayType() {
        Observable<ZAResponse<PayCategories>> observable = payService.getPayType();
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<PayCategories>>
                (new ZASubscriberListener<ZAResponse<PayCategories>>() {

                    @Override
                    public void onBegin() {
                        super.onBegin();

                    }

                    @Override
                    public void onSuccess(ZAResponse<PayCategories> response) {

                        if (response.data != null) {
                            mBaseSurePayView.getPayTypeSuccess(response.data);
                        }
                    }

                    @Override
                    public void onFail(String errorCode, String errorMsg) {
                        super.onFail(errorCode, errorMsg);

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                    }
                }));
    }

    public void createOrder(final PayType payType, int paymentChannel, int productId) {
        createOrder(payType, paymentChannel,1,productId,0, "");
    }

    public void createOrder(final PayType payType, int paymentChannel,int count, int productId,int flagBit, String ext) {
        Observable<ZAResponse<PayOrder>> observable = payService.createOrder(payType.getValue(), paymentChannel, count,productId, flagBit,ANDROID_PLATFORM,ext);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<PayOrder>>
                (new ZASubscriberListener<ZAResponse<PayOrder>>() {

                    @Override
                    public void onBegin() {
                        super.onBegin();
                        if (mActivity instanceof BaseActivity) {
                            ((BaseActivity) mActivity).showProgress();
                        }

                    }

                    @Override
                    public void onSuccess(ZAResponse<PayOrder> response) {
                        if (mActivity instanceof BaseActivity) {
                            ((BaseActivity) mActivity).dismissProgress();
                        }
                        PayOrder payOrder = response.data;
                        if (payOrder != null) {

                            switch (payType) {
                                case AliPay:
                                    order = new AliPayOrder(mActivity, mBaseSurePayView);
                                    break;
                                case WeChatPay:
                                    order = new WeChatOrder(mActivity, mBaseSurePayView);
                                    break;
                                case UnionPay:
                                    order = new UnionPayOrder(mActivity, mBaseSurePayView);
                                    break;
                                case AliWapPay:
                                    order = new AliWapPayOrder(mActivity, mBaseSurePayView);
                            }
                            if (order != null) {
                                order.dealOrder(payOrder);
                            }
                        }

                    }

                    @Override
                    public void onFail(String errorCode, String errorMsg) {
                        super.onFail(errorCode, errorMsg);
                        if (mActivity instanceof BaseActivity) {
                            ((BaseActivity) mActivity).dismissProgress();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mActivity instanceof BaseActivity) {
                            ((BaseActivity) mActivity).dismissProgress();
                        }

                    }
                }));
    }

    public void dealUnionPayResult(Intent intent) {
        if (order != null && order instanceof UnionPayOrder) {
            ((UnionPayOrder) order).dealTclResult(intent);
        }
    }
}
