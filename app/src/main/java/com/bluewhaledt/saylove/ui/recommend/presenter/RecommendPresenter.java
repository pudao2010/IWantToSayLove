package com.bluewhaledt.saylove.ui.recommend.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.recommend.entity.RecommendEntity;
import com.bluewhaledt.saylove.ui.recommend.model.RecommendModel;
import com.bluewhaledt.saylove.ui.recommend.view.IRecommendView;

/**
 * 描述：presenter
 * 作者：shiming_li
 * 时间：2016/12/5 11:33
 * 包名：com.zhenai.saylove_icon.ui.recommend.presenter
 * 项目名：SayLove
 */
public class RecommendPresenter {
    private IRecommendView mView;
    Context mContext;
    private final RecommendModel mRecommendModel;

    public RecommendPresenter(Context context, IRecommendView view) {
        mContext=context;
        mView=view;
        mRecommendModel = new RecommendModel(context);

    }
    public void getRecomendEntity(){
        mRecommendModel.getRecommendEntity(new BaseSubscriber<ZAResponse<RecommendEntity>>(new ZASubscriberListener<ZAResponse<RecommendEntity>>() {
            @Override
            public void onSuccess(ZAResponse<RecommendEntity> response) {
                if (!response.isError){
                    mView.getRecommendEntity(response.data);
                }else{
                    ToastUtils.toast(mContext,response.errorMessage);
                }
            }
            @Override
            public void onFail(String errorCode, String errorMsg) {
                mView.getRecommendFail(errorCode,errorMsg);
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mView.onError();
            }
        }));
    }

    public void getRecomendLoadMoreEntity(){
        mRecommendModel.getRecommendEntity(new BaseSubscriber<ZAResponse<RecommendEntity>>(new ZASubscriberListener<ZAResponse<RecommendEntity>>() {
            @Override
            public void onSuccess(ZAResponse<RecommendEntity> response) {
                if (!response.isError){
                    mView.getRecommendLoadMoreEntity(response.data);
                }else{
                    ToastUtils.toast(mContext,response.errorMessage);
                }
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                mView.getRecommendLoadMoreFail(errorCode,errorMsg);
            }

            @Override
            public void onError(Throwable e) {
               super.onError(e);
                mView.onErrorLoadmore();
//                DebugUtils.e("shiming",e.toString()+"onError");
            }

        }));
    }
    public void setHotRead(final RecommendEntity.ListBean bean, String id, final RecyclerView.ViewHolder viewHolder){
        mRecommendModel.setHotRead(id,new BaseSubscriber<ZAResponse<LikeEntity>>(new ZASubscriberListener<ZAResponse<LikeEntity>>() {
            @Override
            public void onSuccess(ZAResponse<LikeEntity> response) {
               if (!response.isError){
                   ToastUtils.toast(mContext,response.data.msg);
                   mView.likeSuccess(bean,response.data,viewHolder);
               }else{
                   ToastUtils.toast(mContext,response.errorMessage);
               }
            }

            /**
             * {
             "intentData": { },
             "errorCode": "-56007",
             "errorMessage": "会员点赞次数太多",
             "isError": true
             }  是-56007
             * */
            @Override
            public void onFail(String errorCode, String errorMsg) {
                mView.LikeFail(errorCode,  errorMsg);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        }));





        }
    public void cancleLike(final RecommendEntity.ListBean bean, String liekId, final RecyclerView.ViewHolder viewHolder){
        mRecommendModel.cancelHotRead(liekId,new BaseSubscriber<ZAResponse>(new ZASubscriberListener<ZAResponse>() {
            @Override
            public void onSuccess(ZAResponse response) {
                if (!response.isError){
                    ToastUtils.toast(mContext,"取消心动成功");
                    mView.cancleLikeSuccess(bean,response,viewHolder);
                }else{
                    ToastUtils.toast(mContext,response.errorMessage);
                }
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                    ToastUtils.toast(mContext,errorMsg);
            }

        }));
    }

}
