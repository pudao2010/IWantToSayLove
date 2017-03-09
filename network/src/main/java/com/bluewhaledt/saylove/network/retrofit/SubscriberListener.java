package com.bluewhaledt.saylove.network.retrofit;

/**
 * Created by liukun on 16/3/10.
 */
public abstract class SubscriberListener<T> {
    public abstract void onSuccess(T response);

    public abstract void onFail(String errorCode,String errorMsg);

    public abstract void onError(Throwable e);

    public void onCompleted() {
    }

    public void onBegin() {
    }

    public abstract void checkReLogin(String errorCode,String errorMsg);
}
