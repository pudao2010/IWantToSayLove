package com.bluewhaledt.saylove.ui.register_login.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.MainActivity;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.HeadPortraitActivity;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.account.ZAAccount;
import com.bluewhaledt.saylove.ui.register_login.real_name.RealNameActivity;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;

import rx.Observable;
import rx.functions.Func1;

import static com.bluewhaledt.saylove.util.PreferenceUtil.saveValue;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: LoginBasePresenter
 * @创建者: YanChao
 * @创建时间: 2016/11/26 14:24
 * @描述： 登录主要逻辑
 */
public abstract class LoginBasePresenter extends RegisterAndLoginBasePresenter {

    private LoginService loginService;
    private IRegisterAndLoginBaseView mIBaseView;
    private LoginHelperEntity entity;

    public LoginBasePresenter(Context context, IRegisterAndLoginBaseView view) {
        mContext = context;
        loginService = ZARetrofit.getService(context, LoginService.class);
        mIBaseView = view;
    }
    public void login(final String phoneNum, final String passWord, final boolean isAutoLogin) {
        Observable<ZAResponse> phoneLoginObservable = loginService.phoneLogin(phoneNum, passWord);
        Observable<ZAResponse<LoginHelperEntity>> loginHelperObservable =phoneLoginObservable.flatMap(
                        new Func1<ZAResponse, Observable<ZAResponse<LoginHelperEntity>>>() {
                            @Override
                            public Observable<ZAResponse<LoginHelperEntity>> call(final ZAResponse zaResponse) {
                                if (!zaResponse.isError) {
                                    return loginService.getLoginHelper();
                                }
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mIBaseView.dismissTaskProgress();
                                        loginFailed(zaResponse.errorCode, zaResponse.errorMessage);
                                    }
                                });

                                return null;
                            }
                        });

        HttpMethod.toSubscribe(loginHelperObservable,
                new BaseSubscriber<ZAResponse<LoginHelperEntity>>(
                        new ZASubscriberListener<ZAResponse<LoginHelperEntity>>() {

                            @Override
                            public void onBegin() {
                                super.onBegin();
                                if (!isAutoLogin) {
                                mIBaseView.showTaskProgress();
                                }
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                                mIBaseView.dismissTaskProgress();
                            }

                            @Override
                            public void onFail(String errorCode, String errorMsg) {
                                super.onFail(errorCode, errorMsg);
                                loginFailed(errorCode, errorMsg);
                            }

                            @Override
                            public void onSuccess(ZAResponse<LoginHelperEntity> response) {
                                mIBaseView.dismissTaskProgress();
                                saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.LAST_PHONE, phoneNum);
                                saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.LAST_PWD, passWord);
                                PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.AUTO_LOGIN, true);
                                performResponse(response);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                loginError(e);
                            }
                        }
                ));

    }

    protected abstract void loginError(Throwable e);


    private void identify() {
        mContext.startActivity(new Intent(mContext, RealNameActivity.class));
    }

    private void uploadAvator(){
        mContext.startActivity(new Intent(mContext,HeadPortraitActivity.class));
    }

    private void gotoMainActivity() {
        mContext.startActivity(new Intent(mContext, MainActivity.class));
    }

    private void performResponse(ZAResponse<LoginHelperEntity> response) {
        entity = response.data;
        if (entity != null) {
            ZAAccount zaAccount = new ZAAccount();
            zaAccount.uploadAvator = entity.uploadAvatar;
            zaAccount.uid = entity.userId;
            zaAccount.verify = entity.verify;
            zaAccount.isVip = entity.vip;
            zaAccount.nickName = entity.nickName;
            zaAccount.sex = entity.sex;
            zaAccount.avator = entity.avatar;
            zaAccount.avatarStatus = entity.avatarStatus;
            zaAccount.popCertificate = entity.popCertificate;
            AccountManager.getInstance().setAccount(zaAccount);
            DebugUtils.d("yan", "id = "+entity.userId + "verify=" + entity.verify + "vip=" + entity.vip + "upload=" + entity.uploadAvatar + "name=" + entity.nickName + "sex=" + entity.sex + "avatar=" + entity.avatar);
            DebugUtils.d("yan", "******popCertificate*******" + AccountManager.getInstance().getZaAccount().popCertificate + "");
        }

        if (!entity.verify) {
            identify();
        } else if (!entity.uploadAvatar) {
            uploadAvator();
        } else {
            gotoMainActivity();
        }
            loginSuccess();
    }

    protected abstract void loginSuccess();

    protected abstract void loginFailed(String errorCode, String errorMsg);

}
