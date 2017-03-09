package com.bluewhaledt.saylove.ui.heartbeat.model;

import android.content.Context;

import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.heartbeat.entity.HeartBeatItem;
import com.bluewhaledt.saylove.ui.heartbeat.service.HeartBeatService;
import com.bluewhaledt.saylove.widget.linear_view.IBaseMode;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/12/6.
 */

public class HeartBeatToMeModel implements IBaseMode<HeartBeatItem> {
    @Override
    public void getDataList(Context context, int pageIndex, int pageSize, BaseSubscriber<ZAResponse<ResultEntity<HeartBeatItem>>> subscriber) {
        HeartBeatService service = ZARetrofit.getInstance(context).getRetrofit().create(HeartBeatService.class);
        Observable<ZAResponse<ResultEntity<HeartBeatItem>>> observable = service.getHeartBeatToMeList(pageIndex,pageSize);
        HttpMethod.toSubscribe(observable,subscriber);
    }

}
