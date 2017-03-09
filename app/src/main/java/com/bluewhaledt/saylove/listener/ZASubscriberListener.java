package com.bluewhaledt.saylove.listener;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.activity.ActivityManager;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.network.retrofit.SubscriberListener;
import com.bluewhaledt.saylove.ui.register_login.login.LoginActivity;
import com.bluewhaledt.saylove.video_record.upload.UploadService;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Stack;

import retrofit2.adapter.rxjava.HttpException;

/**
 * @author chenzijin
 * @version 1.0.0
 * @date 2016/10/27
 */
public abstract class ZASubscriberListener<T> extends SubscriberListener<T> {
    @Override
    public void checkReLogin(String errorCode, String msg) {
        ActivityManager manager = ActivityManager.getInstance();
        BaseActivity currentActivity = manager.getCurrentActivity();
        if (currentActivity != null
                && !(currentActivity instanceof LoginActivity)) {  //不是在登录页
            currentActivity.startActivity(new Intent(currentActivity, LoginActivity.class));
            Stack<BaseActivity> activityStack = manager.getActivityStack();//遍历activity栈，清除loginActivity以外的所有activity
            for (BaseActivity baseActivity : activityStack) {
                if (!(baseActivity instanceof LoginActivity)) {
                    baseActivity.finish();
                }
            }
        }
        IMUtil.logout();
        if(currentActivity!=null) {
            Intent intent = new Intent(currentActivity, UploadService.class);
            currentActivity.stopService(intent);
        }
        if (ZhenaiApplication.getContext() != null && !TextUtils.isEmpty(msg)) {
            ToastUtils.toast(ZhenaiApplication.getContext(), msg, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onFail(String errorCode, String errorMsg) {
        if (!TextUtils.isEmpty(errorMsg)) {
            ToastUtils.toast(ZhenaiApplication.getContext(), errorMsg, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException
                || e instanceof SocketException
                || e instanceof HttpException) {
            e.printStackTrace();
            if (ZhenaiApplication.getContext() != null)
                ToastUtils.toast(ZhenaiApplication.getContext(), R.string.no_network_connected, Toast.LENGTH_SHORT);
        } else if (e instanceof SocketTimeoutException) {
            e.printStackTrace();
            if (ZhenaiApplication.getContext() != null)
                ToastUtils.toast(ZhenaiApplication.getContext(), R.string.no_network_connected, Toast.LENGTH_SHORT);
        } else {
            if (ZhenaiApplication.getContext() != null)
                ToastUtils.toast(ZhenaiApplication.getContext(), R.string.no_network_connected, Toast.LENGTH_SHORT);
        }
    }
}
