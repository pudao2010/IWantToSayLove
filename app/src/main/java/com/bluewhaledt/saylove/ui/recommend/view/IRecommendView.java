package com.bluewhaledt.saylove.ui.recommend.view;

import android.support.v7.widget.RecyclerView;

import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.recommend.entity.RecommendEntity;

/**
 * 描述：view
 * 作者：shiming_li
 * 时间：2016/12/5 11:31
 * 包名：com.zhenai.saylove_icon.ui.recommend.view
 * 项目名：SayLove
 */
public interface IRecommendView {
    void getRecommendEntity(RecommendEntity entity);
    void getRecommendLoadMoreEntity(RecommendEntity entity);
    void getRecommendFail(String errorCode, String errorMsg);
    void getRecommendLoadMoreFail(String errorCode, String errorMsg);
    void onError();
    void onErrorLoadmore();
    void likeSuccess(RecommendEntity.ListBean bean, LikeEntity i, RecyclerView.ViewHolder viewHolder);
    void LikeFail(String errorCode, String errorMsg);
    void cancleLikeSuccess(RecommendEntity.ListBean bean,ZAResponse zaResponse, RecyclerView.ViewHolder viewHolder);

}
