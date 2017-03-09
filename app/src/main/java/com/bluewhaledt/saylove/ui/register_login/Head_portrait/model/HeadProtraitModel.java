package com.bluewhaledt.saylove.ui.register_login.Head_portrait.model;

import android.content.Context;

import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.entity.HeadProtraitEntity;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.entity.RandomNameEntity;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.service.HeadProtraitService;

import rx.Observable;

/**
 * 描述：model
 * 作者：shiming_li
 * 时间：2016/12/5 11:31
 * 包名：com.zhenai.saylove_icon.ui.recommend.model
 * 项目名：SayLove
 */
public class HeadProtraitModel {

    private HeadProtraitService mService;

    public HeadProtraitModel(Context context) {
        mService = ZARetrofit.getInstance(context).getRetrofit().create(HeadProtraitService.class);
    }
    public void getRecommendEntity(String array,String name,BaseSubscriber infos){
        Observable<ZAResponse<HeadProtraitEntity>> zaResponseObservable = mService.postNameAndAvatar(array, name);
        HttpMethod.toSubscribe(zaResponseObservable,infos);
    }
    public void getRandomNameEntity(BaseSubscriber infos){
        Observable<ZAResponse<RandomNameEntity>> randomName = mService.getRandomName();
        HttpMethod.toSubscribe(randomName,infos);
    }

}
