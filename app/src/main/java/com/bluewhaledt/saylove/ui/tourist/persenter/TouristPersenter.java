package com.bluewhaledt.saylove.ui.tourist.persenter;

import android.content.Context;

import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.SubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.tourist.entity.TouristEntity;
import com.bluewhaledt.saylove.ui.tourist.model.TouristModel;
import com.bluewhaledt.saylove.ui.tourist.view.ITouristView;

/**
 * 描述：persenter
 * 作者：shiming_li
 * 时间：2016/12/12 14:51
 * 包名：com.zhenai.saylove_icon.ui.tourist.persenter
 * 项目名：SayLove
 */
public class TouristPersenter {
    private Context mContext;
    private  ITouristView mITouristView;
    private final TouristModel mModel;
    private int pageSize=1;//每页10

    public TouristPersenter(Context context, ITouristView view) {
        mContext=context;
        mITouristView=view;
        mModel = new TouristModel(mContext);
    }
    public  void  getTouristRecommend(String sex){
        mModel.getTouristRecommend(sex,pageSize+"",new BaseSubscriber<ZAResponse<TouristEntity>>(new SubscriberListener<ZAResponse<TouristEntity>>() {

            @Override
            public void onSuccess(ZAResponse<TouristEntity> response) {
                if (!response.isError){
                    mITouristView.getTouristDataSuccess(response.data);
                    pageSize++;
                }else {
                    ToastUtils.toast(mContext,response.errorMessage);
                }
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                 ToastUtils.toast(mContext, errorMsg);
                    mITouristView.getTouristDataFail();
            }

            @Override
            public void onError(Throwable e) {
                mITouristView.onError();
            }

            @Override
            public void checkReLogin(String errorCode, String errorMsg) {
                DebugUtils.d("shiming",errorCode+"________"+errorMsg);
            }
        }));
    }
    public  void  getTouristRecommendloadMore(String sex){
        mModel.getTouristRecommend(sex,pageSize+"",new BaseSubscriber<ZAResponse<TouristEntity>>(new SubscriberListener<ZAResponse<TouristEntity>>() {

            @Override
            public void onSuccess(ZAResponse<TouristEntity> response) {
                if (!response.isError){
                    mITouristView.getTouristDataSuccessLoadMore(response.data);
                    pageSize++;
                }else {
                    ToastUtils.toast(mContext,response.errorMessage);
                }
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                    mITouristView.getTouristDataFail(errorCode,errorMsg);
            }

            @Override
            public void onError(Throwable e) {
                mITouristView.onErrorLoadmore();
            }

            @Override
            public void checkReLogin(String errorCode, String errorMsg) {

            }
        }));
    }
}
