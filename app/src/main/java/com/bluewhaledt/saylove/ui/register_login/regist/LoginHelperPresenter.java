package com.bluewhaledt.saylove.ui.register_login.regist;

import android.content.Context;

import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.account.ZAAccount;
import com.bluewhaledt.saylove.ui.register_login.login.LoginHelperEntity;
import com.bluewhaledt.saylove.ui.register_login.login.LoginService;
import com.bluewhaledt.saylove.ui.register_login.login.RegisterAndLoginBasePresenter;

import rx.Observable;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.regist
 * @文件名: LoginHelperPresenter
 * @创建者: YanChao
 * @创建时间: 2016/12/14 9:59
 * @描述： 拉取最新的个人基本信息
 */
public class LoginHelperPresenter extends RegisterAndLoginBasePresenter{
    private LoginService loginService;

    public LoginHelperPresenter(Context context){
        loginService = ZARetrofit.getInstance(context).getRetrofit().create(LoginService.class);
    }

    public void getLoginHelper(){
        Observable<ZAResponse<LoginHelperEntity>> loginHelperObserver = loginService.getLoginHelper();
        HttpMethod.toSubscribe(loginHelperObserver, new BaseSubscriber<ZAResponse<LoginHelperEntity>>(new ZASubscriberListener<ZAResponse<LoginHelperEntity>>() {
            @Override
            public void onSuccess(ZAResponse<LoginHelperEntity> response) {
                LoginHelperEntity entity = response.data;
                if (entity != null) {
                    ZAAccount zaAccount = new ZAAccount();
                    zaAccount.uploadAvator = entity.uploadAvatar;
                    zaAccount.uid = entity.userId;
                    zaAccount.verify = entity.verify;
                    zaAccount.isVip = entity.vip;
                    zaAccount.nickName = entity.nickName;
                    zaAccount.sex = entity.sex;
                    zaAccount.avator = entity.avatar;
                    AccountManager.getInstance().setAccount(zaAccount);
                    DebugUtils.d("yanc", "loginHelper"+entity.userId + "==" + entity.vip + "==" + entity.verify + "==" + entity.vip + "==" + entity.uploadAvatar + "==" + entity.nickName + "==" + entity.sex + "==" + entity.avatar);
                    DebugUtils.d("yanc", AccountManager.getInstance().getZaAccount().uid + "");
                }
            }
        }));
    }
}
