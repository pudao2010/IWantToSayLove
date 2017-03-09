package com.bluewhaledt.saylove.network.retrofit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * @author chenzijin
 * @version 1.0.0
 * @date 2016/6/15
 */
public class HttpMethod {
    public static <T extends ZAResponse> void toSubscribe(Observable<T> o, final BaseSubscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .retryWhen(new RetryWhenHandler(1, 5))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        s.onBegin();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    public static <T> Observable.Transformer<T, T> defaultHandler() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .retryWhen(new RetryWhenHandler(1, 5))
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
