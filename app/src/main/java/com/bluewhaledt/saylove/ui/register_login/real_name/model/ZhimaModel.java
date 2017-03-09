package com.bluewhaledt.saylove.ui.register_login.real_name.model;

import android.content.Context;

import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.pay.entity.VerifyProduct;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.VerifyTipsEntity;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.ZhimaEntity;
import com.bluewhaledt.saylove.ui.register_login.real_name.service.ZhimaService;

import rx.Observable;

/**
 * 描述：model
 * 作者：shiming_li
 * 时间：2016/12/5 10:21
 * 包名：com.zhenai.saylove_icon.ui.register_login.real_name.model
 * 项目名：SayLove
 */
public class ZhimaModel {

    private  ZhimaService mService;

    public ZhimaModel(Context context) {
        mService = ZARetrofit.getInstance(context).getRetrofit().create(ZhimaService.class);
    }
    public void getZhimaEntityData(String name,String id,BaseSubscriber infos){
        Observable<ZAResponse<ZhimaEntity>> zhimaInfo = mService.getZhimaInfo(name, id);
        HttpMethod.toSubscribe(zhimaInfo,infos);
    }
    public void postZhimaCallbackSing(String params,String  sign,BaseSubscriber infos){
        Observable<ZAResponse<Void>> zaResponseObservable = mService.postZhimaCallBack(params, sign);
        HttpMethod.toSubscribe(zaResponseObservable,infos);
    }
    public void getPrice(BaseSubscriber info){
        Observable<ZAResponse<VerifyProduct>> verifyProduct = mService.getVerifyProduct();
        HttpMethod.toSubscribe(verifyProduct,info);
    }
    public void getVerifyTips(BaseSubscriber info){
        Observable<ZAResponse<VerifyTipsEntity>> verifyTips = mService.getVerifyTips();
        HttpMethod.toSubscribe(verifyTips,info);
    }

}
