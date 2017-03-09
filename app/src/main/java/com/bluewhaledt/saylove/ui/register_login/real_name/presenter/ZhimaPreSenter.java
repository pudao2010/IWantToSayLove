package com.bluewhaledt.saylove.ui.register_login.real_name.presenter;

import android.content.Context;
import android.widget.Toast;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.pay.entity.VerifyProduct;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.VerifyTipsEntity;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.ZhimaEntity;
import com.bluewhaledt.saylove.ui.register_login.real_name.model.ZhimaModel;
import com.bluewhaledt.saylove.ui.register_login.real_name.view.IZhimaView;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 描述：persenter
 * 作者：shiming_li
 * 时间：2016/12/5 10:25
 * 包名：com.zhenai.saylove_icon.ui.register_login.real_name.presenter
 * 项目名：SayLove
 */
public class ZhimaPreSenter {
    private Context mContext;
    private IZhimaView mView;
    private final ZhimaModel mModel;


    public ZhimaPreSenter(Context context, IZhimaView view) {
        mContext = context;
        mView = view;
        mModel = new ZhimaModel(context);
    }

    public void setZhimaConfig(String name, String id) {
        mModel.getZhimaEntityData(name, id, new BaseSubscriber<ZAResponse<ZhimaEntity>>(new ZASubscriberListener<ZAResponse<ZhimaEntity>>() {
            @Override
            public void onSuccess(ZAResponse<ZhimaEntity> response) {
                mView.getZhimaData(response);
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                //失败了到这里来验证  第一次不提醒我们的用户没有认证次数，以后都提醒
                boolean mFlag = PreferenceUtil.getBoolean(PreferenceFileNames.APP_CONFIG, PreferenceKeys.IS_HAS_CHANCE_AUTHENTICATE, false);
//                DebugUtils.d("shimng",mFlag+"");
//                if (!mFlag) {
//                    PreferenceUtil.saveValue(PreferenceFileNames.APP_CONFIG, PreferenceKeys.IS_HAS_CHANCE_AUTHENTICATE, true);
//                }else {
//                    ToastUtils.toast(mContext, errorMsg);
//                }
                if(mFlag){
                    ToastUtils.toast(mContext, errorMsg);
                }
                mView.goToRealNameGoMoneyActivity(errorCode, errorMsg);
            }
//            @Override
//            public void onError(Throwable e) {
//                ToastUtils.toast(mContext, e.toString() + "");
//            }
        }));
    }

    public void postZhimaCallBack(String params, String sign) {
        mModel.postZhimaCallbackSing(params, sign, new BaseSubscriber<ZAResponse<Void>>(new ZASubscriberListener<ZAResponse<Void>>() {
            @Override
            public void onSuccess(ZAResponse<Void> response) {
                if (!response.isError) {
                    ToastUtils.toast(mContext, "认证成功");
                    mView.ZhimaCallBackSuccess();
                } else {
                    ToastUtils.toast(mContext, "认证失败");
                }
            }

            //{"data":{},"errorCode":"-55005","errorMessage":"芝麻认证失败","isError":true}
            // "errorMessage": "芝麻认证token鉴权失败",  "er  rorCode": "-55008",
            @Override
            public void onFail(String errorCode, String errorMsg) {
                mView.ZhimaCallBackFail(errorCode, errorMsg);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException
                        || e instanceof SocketException
                        || e instanceof HttpException) {
                    e.printStackTrace();
                    if (ZhenaiApplication.getContext() != null)
                        ToastUtils.toast(ZhenaiApplication.getContext(), R.string.no_network_connected_real_name, Toast.LENGTH_SHORT);
                } else if (e instanceof SocketTimeoutException) {
                    e.printStackTrace();
                    if (ZhenaiApplication.getContext() != null)
                        ToastUtils.toast(ZhenaiApplication.getContext(), R.string.no_network_connected_real_name, Toast.LENGTH_SHORT);
                } else {
                    if (ZhenaiApplication.getContext() != null)
                        ToastUtils.toast(ZhenaiApplication.getContext(), R.string.no_network_connected_real_name, Toast.LENGTH_SHORT);
                }
            }
        }));
    }

    public void getVerifyProduct() {
        mModel.getPrice(new BaseSubscriber<ZAResponse<VerifyProduct>>(new ZASubscriberListener<ZAResponse<VerifyProduct>>() {
            @Override
            public void onSuccess(ZAResponse<VerifyProduct> response) {
                if (!response.isError) {
                    mView.getVerifyProduct(response.data);
                } else {
                    ToastUtils.toast(mContext, response.errorMessage);
                }
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                super.onFail(errorCode, errorMsg);
                ToastUtils.toast(mContext, errorMsg);
            }
        }));
    }
    public void getVerifyTips(){
        mModel.getVerifyTips(new BaseSubscriber<ZAResponse<VerifyTipsEntity>>(new ZASubscriberListener<ZAResponse<VerifyTipsEntity>>() {
            @Override
            public void onSuccess(ZAResponse<VerifyTipsEntity> response) {
                if (!response.isError){
//                    DebugUtils.d("shiming",response.data.tips);
                    mView.getVerifyTips(response.data);
                }else{
                    ToastUtils.toast(mContext, response.errorMessage);
                }
            }
        }));
    }
}
