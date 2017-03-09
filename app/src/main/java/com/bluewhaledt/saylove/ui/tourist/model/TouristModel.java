package com.bluewhaledt.saylove.ui.tourist.model;

import android.content.Context;

import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.tourist.entity.TouristEntity;
import com.bluewhaledt.saylove.ui.tourist.service.TouristService;

import rx.Observable;

/**
 * 描述：model
 * 作者：shiming_li
 * 时间：2016/12/12 14:49
 * 包名：com.zhenai.saylove_icon.ui.tourist.model
 * 项目名：SayLove
 */
public class TouristModel {

    private final TouristService mService;

    public TouristModel(Context context) {
        mService = ZARetrofit.getInstance(context).getRetrofit().create(TouristService.class);
    }
    public void getTouristRecommend(String sex, String pagesize, BaseSubscriber info){
        Observable<ZAResponse<TouristEntity>> touristRecommend = mService.getTouristRecommend(sex, pagesize);
        HttpMethod.toSubscribe(touristRecommend,info);
    }
}
