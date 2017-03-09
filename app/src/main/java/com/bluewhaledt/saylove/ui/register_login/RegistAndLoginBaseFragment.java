package com.bluewhaledt.saylove.ui.register_login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluewhaledt.saylove.base.activity.BaseFragment;
import com.bluewhaledt.saylove.ui.register_login.login.IRegisterAndLoginBaseView;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login
 * @文件名: RegistAndLoginBaseFragment
 * @创建者: YanChao
 * @创建时间: 2016/12/7 16:56
 * @描述： TODO
 */
public abstract class RegistAndLoginBaseFragment extends BaseFragment implements IRegisterAndLoginBaseView{

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResouces(),container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    protected abstract int getLayoutResouces();

    @Override
    public void onStartFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onStopFragment() {

    }

    @Override
    public void onDestroyFragment() {

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
