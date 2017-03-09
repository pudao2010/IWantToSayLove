package com.bluewhaledt.saylove.ui.pay.presenter;

import android.content.Context;

import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.pay.entity.PurchasePageData;
import com.bluewhaledt.saylove.ui.pay.service.PayService;
import com.bluewhaledt.saylove.ui.pay.view.IProductPageViewAction;

import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/12/6.
 */

public class ProductPagePresenter {

    private IProductPageViewAction viewAction;

    public ProductPagePresenter(IProductPageViewAction viewAction){
        this.viewAction = viewAction;
    }

    public void getProductList(Context context){
        PayService service = ZARetrofit.getInstance(context).getRetrofit().create(PayService.class);
        Observable<ZAResponse<PurchasePageData>> observable = service.getProductList();
        HttpMethod.toSubscribe(observable,new BaseSubscriber<ZAResponse<PurchasePageData>>(new ZASubscriberListener<ZAResponse<PurchasePageData>>() {
            @Override
            public void onSuccess(ZAResponse<PurchasePageData> response) {
                if (response.data != null){
                    viewAction.onProductDataReceived(response.data);
                }
            }

            @Override
            public void onBegin() {
                super.onBegin();
                viewAction.showLoadProgress();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                viewAction.dismissLoadProgress();
            }
        }));

    }


}
