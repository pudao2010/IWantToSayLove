package com.bluewhaledt.saylove.ui.recommend.model;

import android.content.Context;

import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.recommend.entity.RecommendEntity;
import com.bluewhaledt.saylove.ui.recommend.service.RecommendService;

import rx.Observable;

/**
 * 描述：推荐的model
 * 作者：shiming_li
 * 时间：2016/12/5 11:31
 * 包名：com.zhenai.saylove_icon.ui.recommend.model
 * 项目名：SayLove
 */
public class RecommendModel {

    private final RecommendService mService;

    public RecommendModel(Context context) {
        mService = ZARetrofit.getInstance(context).getRetrofit().create(RecommendService.class);
    }
    public void getRecommendEntity(BaseSubscriber infos){
        Observable<ZAResponse<RecommendEntity>> recommend = mService.getRecommend();
        HttpMethod.toSubscribe(recommend,infos);
    }
    public void setHotRead(String id,BaseSubscriber infos){
        Observable<ZAResponse<LikeEntity>> zaResponseObservable = mService.touchHeart(id);
        HttpMethod.toSubscribe(zaResponseObservable,infos);

//        HttpMethod.toSubscribe(zaResponseObservable, new BaseSubscriber<ZAResponse>
//                (new ZASubscriberListener<ZAResponse>() {
//
//                    @Override
//                    public void onSuccess(ZAResponse response) {
//                        DebugUtils.d("shiming","成功了啊点赞");
//                        if (response.intentData != null) {
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        DebugUtils.d("shimingonError","成功了啊点赞");
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        super.onCompleted();
//                        DebugUtils.d("shimingonCompleted","成功了啊点赞");
//                    }
//
//                    @Override
//                    public void onFail(String errorCode, String errorMsg) {
//                        super.onFail(errorCode, errorMsg);
//                        DebugUtils.d("shimingonFail","成功了啊点赞");
//                    }
//                }));
    }

    public void cancelHotRead(String liekid, BaseSubscriber infos){
        Observable<ZAResponse> zaResponseObservable = mService.cancelTouchHeart(liekid);
        HttpMethod.toSubscribe(zaResponseObservable,infos);

    }
}
