package com.bluewhaledt.saylove.ui.setting;

import android.content.Context;

import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;

import rx.Observable;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.setting
 * @文件名: LogoutPresenter
 * @创建者: YanChao
 * @创建时间: 2016/12/15 20:19
 * @描述： TODO
 */
public class LogoutPresenter {

    private Context mContext;
    private LogoutService logoutService;

    public LogoutPresenter(Context context){
        mContext = context;
        logoutService = ZARetrofit.getInstance(context).getRetrofit().create(LogoutService.class);
    }

    public void  logout(final boolean isAutoLogin){
        Observable<ZAResponse> logoutObservable = logoutService.logout();
        HttpMethod.toSubscribe(logoutObservable, new BaseSubscriber<ZAResponse>(new ZASubscriberListener<ZAResponse>() {
            @Override
            public void onSuccess(ZAResponse response) {
                PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.AUTO_LOGIN, isAutoLogin);
            }
        }));
    }

}
