package com.bluewhaledt.saylove.ui.register_login.others;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.network.utils.ChannelUtils;
import com.bluewhaledt.saylove.ui.register_login.RegistAndLoginBaseActivity;
import com.bluewhaledt.saylove.ui.register_login.regist.EntranceActivity;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: SplashActivity
 * @创建者: YanChao
 * @创建时间: 2016/11/28 17:02
 * @描述： 闪屏页
 */
public class SplashActivity extends RegistAndLoginBaseActivity implements ISplashView {

    private SplashPresenter mSplashPresenter;
    private ImageView splashView;
    private String UM_APP_ID = "5864af2807fe6542d50018ec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitleBar(false);
        setContentView(R.layout.activity_splash_layout);
        String channel = ChannelUtils.getChannel(this);
        // 设置友盟渠道
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, UM_APP_ID, channel);
        MobclickAgent.startWithConfigure(config);
        Log.i("SplashActivity", "icg-channel : " + channel);
        splashView = (ImageView) findViewById(R.id.iv_splash);
        mSplashPresenter = new SplashPresenter(this, this);
//        startAlphaAnimation();
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        String phone = PreferenceUtil.getString(PreferenceFileNames.USER_CONFIG, PreferenceKeys.LAST_PHONE, "");
                        String password = PreferenceUtil.getString(PreferenceFileNames.USER_CONFIG, PreferenceKeys.LAST_PWD, "");
                        boolean auto_login = PreferenceUtil.getBoolean(PreferenceFileNames.USER_CONFIG, PreferenceKeys.AUTO_LOGIN, false);
                        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password) && auto_login) {
                            login(phone, password);
                        } else {
                            gotoEnterPage();
                        }
                    }
                });
    }

    /**
     * 手机登录
     *
     * @param phone
     * @param password
     */
    public void login(String phone, String password) {
        mSplashPresenter.login(phone, password, true);
    }

    private void gotoEnterPage() {
        PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.AUTO_LOGIN, false);
        Intent intent = new Intent(this, EntranceActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginSuccess() {
        loginIM();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashView.clearAnimation();
    }

    private void loginIM() {
        IMUtil.loginIMByGetAccount();
    }

    @Override
    public void loginFail(String errorCode, String errorMsg) {
        gotoEnterPage();
    }

    @Override
    public void loginError(Throwable e) {
        gotoEnterPage();
    }

}
