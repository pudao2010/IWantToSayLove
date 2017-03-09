package com.bluewhaledt.saylove.network.retrofit;

import android.util.Log;

import com.bluewhaledt.saylove.network.constant.TaskResult;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by chenzijin on 16/3/10.
 */
public class BaseSubscriber<T extends ZAResponse> extends Subscriber<T> {

    private static final String TAG = "BaseSubscriber";

    private SubscriberListener mSubscriberOnNextListener;

    public BaseSubscriber(SubscriberListener mSubscriberOnNextListener) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
    }



    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    public void onBegin() {
        Log.i(TAG, "onBegin");
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onBegin();
        }
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        Log.i(TAG, "onCompleted");
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onCompleted();
        }
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        Log.i(TAG, "onError:"+e.getMessage());
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onError(e);
        }
        onCompleted();
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param response 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T response) {
        Log.i(TAG, "onNext");
        if (mSubscriberOnNextListener != null) {
            if (!response.isError) {
//                if (TaskResult.STATUS_RE_LOGIN.equals(response.errorCode)) {//判断是否需要重新登录
//                    mSubscriberOnNextListener.checkReLogin(response.errorCode, response.errorMessage);
//                } else {
                    mSubscriberOnNextListener.onSuccess(response);
//                }
            } else {
                if (TaskResult.STATUS_RE_LOGIN.equals(response.errorCode) || TaskResult.STATUS_NO_LOGIN.equals(response.errorCode)) {//判断是否需要重新登录
                    mSubscriberOnNextListener.checkReLogin(response.errorCode, response.errorMessage);
                } else {
//                    mSubscriberOnNextListener.onSuccess(response);
                    mSubscriberOnNextListener.onFail(response.errorCode, response.errorMessage);
                }
            }
        }
    }
}