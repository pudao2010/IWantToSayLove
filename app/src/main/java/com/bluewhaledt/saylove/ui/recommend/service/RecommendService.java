package com.bluewhaledt.saylove.ui.recommend.service;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.recommend.entity.RecommendEntity;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：service
 * 作者：shiming_li
 * 时间：2016/12/5 11:29
 * 包名：com.zhenai.saylove_icon.ui.recommend.service
 * 项目名：SayLove
 */
public interface RecommendService {

    @POST(Url.GET_RECOMMEND)
    Observable<ZAResponse<RecommendEntity>> getRecommend();


    @GET(Url.TOUCH_HEART)
    Observable<ZAResponse<LikeEntity>> touchHeart(@Query("objectId") String objectId);


    @GET(Url.CANCEL_TOUCH_HEART)
    Observable<ZAResponse> cancelTouchHeart(@Query("likeId") String likeId);
}
