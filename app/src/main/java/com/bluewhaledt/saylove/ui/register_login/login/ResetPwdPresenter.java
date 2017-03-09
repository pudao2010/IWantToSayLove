package com.bluewhaledt.saylove.ui.register_login.login;

import android.content.Context;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;

import rx.Observable;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: ResetPwdPresenter
 * @创建者: YanChao
 * @创建时间: 2016/12/2 11:40
 * @描述： 重置密码逻辑
 */
public class ResetPwdPresenter extends RegisterAndLoginBasePresenter{
    private final ResetPwdService mResetPwdService;
    private  IResetPwdView mIResetPwdView;

    public ResetPwdPresenter(Context context, IResetPwdView view){
        mContext = context;
        mIResetPwdView = view;
        mResetPwdService = ZARetrofit.getInstance(context).getRetrofit().create(ResetPwdService.class);
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
    
    public void getResetVerifyCode(String mobile){
        Observable<ZAResponse> pwdVerifyCode = mResetPwdService.pwdVerifyCode(mobile);
        HttpMethod.toSubscribe(pwdVerifyCode, new BaseSubscriber<ZAResponse>(new ZASubscriberListener<ZAResponse>() {
            @Override
            public void onSuccess(ZAResponse response) {
                mIResetPwdView.resendCodeSuccess();
            }

        }));
    }

    public void performReset(String phoneNum, String verifyCode, String newPwd){
        Observable<ZAResponse> resetPwd = mResetPwdService.resetPwd(phoneNum, verifyCode, newPwd);
        HttpMethod.toSubscribe(resetPwd,new BaseSubscriber<ZAResponse>(new ZASubscriberListener<ZAResponse>() {
            @Override
            public void onBegin() {
                super.onBegin();
                mIResetPwdView.showTaskProgress();
            }

            @Override
            public void onSuccess(ZAResponse response) {
                mIResetPwdView.dismissTaskProgress();
                mIResetPwdView.onResetSuccess();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mIResetPwdView.dismissTaskProgress();
            }

        }));
    }
}
