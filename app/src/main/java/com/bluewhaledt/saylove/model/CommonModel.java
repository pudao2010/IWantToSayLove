package com.bluewhaledt.saylove.model;

import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.entity.CheckChatAvailable;
import com.bluewhaledt.saylove.entity.IMAccount;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.service.CommonService;

import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/12/10.
 *
 */

public class CommonModel {

    public static void checkCanFreeChat(long objectId, BaseSubscriber<ZAResponse<CheckChatAvailable>> subscriber) {
        CommonService service = ZARetrofit.getInstance(ZhenaiApplication.getContext()).getRetrofit().create(CommonService.class);
        Observable<ZAResponse<CheckChatAvailable>> observable = service.checkCanChat(objectId);
        HttpMethod.toSubscribe(observable, subscriber);
    }

    public static void getIMAccount(BaseSubscriber<ZAResponse<IMAccount>> subscriber) {
        CommonService service = ZARetrofit.getInstance(ZhenaiApplication.getContext()).getRetrofit().create(CommonService.class);
        Observable<ZAResponse<IMAccount>> observable = service.getIMAccount();
        HttpMethod.toSubscribe(observable, subscriber);
    }

}
