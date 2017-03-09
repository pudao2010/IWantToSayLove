package com.bluewhaledt.saylove.ui.tourist.service;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.tourist.entity.TouristEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：service
 * 作者：shiming_li
 * 时间：2016/12/12 14:38
 * 包名：com.zhenai.saylove_icon.ui.tourist.service
 * 项目名：SayLove
 */
public interface TouristService {
    @GET(Url.TOURIST_RECOMMEDN)
    Observable<ZAResponse<TouristEntity>> getTouristRecommend(@Query("sex") String sex, @Query("page") String page);
}
