package com.bluewhaledt.saylove.ui.register_login.regist;

import android.content.Context;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.register_login.login.RegisterAndLoginBasePresenter;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;

import rx.Observable;

import static com.bluewhaledt.saylove.util.PreferenceUtil.saveValue;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.regist
 * @文件名: RegisterPresenter
 * @创建者: YanChao
 * @创建时间: 2016/11/29 19:36
 * @描述： 注册页数据交互
 */
public class RegisterPresenter extends RegisterAndLoginBasePresenter{
    private IRegistView mIRegistView;
    private RegistService mRegistService;

    public RegisterPresenter(Context context, IRegistView view){
        mContext = context;
        mIRegistView = view;
        mRegistService = ZARetrofit.getInstance(context).getRetrofit().create(RegistService.class);
    }

    public boolean checkIsValid(String phone, String password,String verifyCode) {
        boolean phoneResult = checkPhoneIsValid(phone);
        boolean passwordResult = checkPasswordIsValid(password);
        boolean verifyCodeResult = checkVerifyCodeIsValid(verifyCode);

        if (!phoneResult) {
            ToastUtils.toast(mContext,mContext.getString(R.string.register_phone_tips));
            return false;
        }else if (!passwordResult) {
            ToastUtils.toast(mContext,mContext.getString(R.string.register_password_tips));
            return false;
        }else if(!verifyCodeResult){
            ToastUtils.toast(mContext,mContext.getString(R.string.register_verifyCode_tips));
            return false;
        }
        return true;
    }
    
    public void phoneVerify(String mobile){
        Observable<ZAResponse> zaResponseObservable = mRegistService.phoneVerify(mobile);
        HttpMethod.toSubscribe(zaResponseObservable,new BaseSubscriber<ZAResponse>(new ZASubscriberListener<ZAResponse>() {
            @Override
            public void onSuccess(ZAResponse response) {
                onPhoneVerifySuccess();
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                onPhoneVeriryFail(errorCode,errorMsg);
            }

        }));
    }

    public void regist (final String mobile, String verfycode, int sex, int workcity, int height, int marryState, int salary, final String pwd, String birthday){
        Observable<ZAResponse> zaResponseObservable = mRegistService.regist(  mobile,  verfycode,  sex,  workcity,  height, marryState, salary, pwd, birthday);
        HttpMethod.toSubscribe(zaResponseObservable, new BaseSubscriber<ZAResponse>(new ZASubscriberListener<ZAResponse>() {
            @Override
            public void onSuccess(ZAResponse response) {
                onRegistSuccess();
                saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.LAST_PHONE, mobile);
                saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.LAST_PWD, pwd);
                // TODO: 2016/12/15 注册成功要不要打开自动登录开关 
            }

        }));

    }

    private void onRegistSuccess() {
        mIRegistView.onSuccess();

    }

    private void onPhoneVeriryFail(String errorCode, String errorMsg) {
        mIRegistView.requestCodeFailed(errorCode, errorMsg);
    }

    private void onPhoneVerifySuccess() {
        mIRegistView.resendCodeSuccess();
    }

}