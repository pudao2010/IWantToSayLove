package com.bluewhaledt.saylove.ui.register_login;

import android.os.Bundle;

import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.ui.register_login.login.IRegisterAndLoginBaseView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login
 * @文件名: RegistAndLoginBaseActivity
 * @创建者: YanChao
 * @创建时间: 2016/12/7 16:27
 * @描述： TODO
 */
public class RegistAndLoginBaseActivity extends BaseActivity implements IRegisterAndLoginBaseView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRegistEvent() && !EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegistEvent() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    protected boolean isRegistEvent() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void  onEventMainthread(RegistCloseEvent event){
        if (event != null && event.isClose()) {
            finish();
        }
    }

    @Override
    public void showTaskProgress() {
        showProgress();
    }

    @Override
    public void dismissTaskProgress() {
        dismissProgress();
    }
}
