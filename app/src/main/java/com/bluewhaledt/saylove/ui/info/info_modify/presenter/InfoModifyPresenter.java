package com.bluewhaledt.saylove.ui.info.info_modify.presenter;

import android.content.Context;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.service.InfoService;
import com.bluewhaledt.saylove.ui.info.entity.RequireInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.UserInfoEntity;
import com.bluewhaledt.saylove.ui.info.info_modify.view.IModifyView;

import java.util.Map;

import rx.Observable;

/**
 * Created by rade.chan on 2016/12/2.
 */

public class InfoModifyPresenter {
    private InfoService mService;
    private IModifyView modifyView;
    private Context mContext;

    public InfoModifyPresenter(Context context, IModifyView view) {
        this.mContext=context;
        this.modifyView = view;
        mService = ZARetrofit.getService(context, InfoService.class);
    }

    public void getRequireInfo(String objId) {
        Observable<ZAResponse<RequireInfoEntity>> observable = mService.getRequireConditions(objId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<RequireInfoEntity>>
                (new ZASubscriberListener<ZAResponse<RequireInfoEntity>>() {

                    @Override
                    public void onSuccess(ZAResponse<RequireInfoEntity> response) {
                        if(response.data!=null){
                            modifyView.showItemInfo(response.data);
                        }
                    }

                }));
    }

    public void modifyRequireInfo(Map<String,String> params) {
        Observable<ZAResponse> observable = mService.modifyMateConditions(params);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse>
                (new ZASubscriberListener<ZAResponse>() {

                    @Override
                    public void onSuccess(ZAResponse response) {
                        ToastUtils.toast(mContext,mContext.getString(R.string.save_success));
                        modifyView.modifySuccess();
                    }

                }));
    }

    public void getUserInfo(String objId) {
        Observable<ZAResponse<UserInfoEntity>> observable = mService.getUserInfo(objId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<UserInfoEntity>>
                (new ZASubscriberListener<ZAResponse<UserInfoEntity>>() {

                    @Override
                    public void onSuccess(ZAResponse<UserInfoEntity> response) {
                        if(response.data!=null){
                            modifyView.showItemInfo(response.data);
                        }
                    }

                }));
    }

    public void modifyMyInfo(Map<String,String> params) {
        Observable<ZAResponse> observable = mService.modifyMyInfo(params);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse>
                (new ZASubscriberListener<ZAResponse>() {

                    @Override
                    public void onSuccess(ZAResponse response) {
                        ToastUtils.toast(mContext,mContext.getString(R.string.save_success));
                        modifyView.modifySuccess();
                    }

                }));
    }

}
