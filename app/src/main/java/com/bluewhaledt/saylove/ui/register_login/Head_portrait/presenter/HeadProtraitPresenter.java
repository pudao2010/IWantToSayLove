package com.bluewhaledt.saylove.ui.register_login.Head_portrait.presenter;

import android.content.Context;

import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.entity.HeadProtraitEntity;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.entity.RandomNameEntity;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.model.HeadProtraitModel;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.view.IHeadProtraitView;

/**
 * 描述：presenter
 * 作者：shiming_li
 * 时间：2016/12/5 11:33
 * 包名：com.zhenai.saylove_icon.ui.recommend.presenter
 * 项目名：SayLove
 */
public class HeadProtraitPresenter {
    private IHeadProtraitView mView;
    private Context mContext;
    private final HeadProtraitModel mRecommendModel;

    public HeadProtraitPresenter(Context context, IHeadProtraitView view) {
        mContext=context;
        mView=view;
        mRecommendModel = new HeadProtraitModel(context);

    }
    public void getProtraitEntity(String array,String name){
        mRecommendModel.getRecommendEntity(array,name,new BaseSubscriber<ZAResponse<HeadProtraitEntity>>(new ZASubscriberListener<ZAResponse<HeadProtraitEntity>>() {
            @Override
            public void onSuccess(ZAResponse<HeadProtraitEntity> response) {
                if (!response.isError){
                    mView.getHeadPRotraitEntity(response.data);
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
    public void getRandomNameEntity(){
        mRecommendModel.getRandomNameEntity(new BaseSubscriber<ZAResponse<RandomNameEntity>>(new ZASubscriberListener<ZAResponse<RandomNameEntity>>() {
            @Override
            public void onSuccess(ZAResponse<RandomNameEntity> response) {

                if (!response.isError){
                    mView.getRanDomName(response.data);

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
