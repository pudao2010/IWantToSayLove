package com.bluewhaledt.saylove.ui.heartbeat.service;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.heartbeat.entity.HeartBeatItem;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/12/7.
 */

public interface HeartBeatService {

    @FormUrlEncoded
    @POST(Url.GET_PASSIVE_LIKE)
    Observable<ZAResponse<ResultEntity<HeartBeatItem>>> getHeartBeatToMeList(@Field("page")int page, @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST(Url.GET_MY_HEART_HEAT_RECORD)
    Observable<ZAResponse<ResultEntity<HeartBeatItem>>> getMyHeartBeatList(@Field("page")int page, @Field("pageSize") int pageSize);
}
