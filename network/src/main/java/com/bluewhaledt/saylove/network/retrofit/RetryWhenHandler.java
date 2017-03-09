package com.bluewhaledt.saylove.network.retrofit;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author hechunshan
 * @date 2016/10/20
 */
public class RetryWhenHandler implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private int mCount = 3;
    private long mDelay = 3; //s

    private int counter = 0;

    public RetryWhenHandler() {
    }

    public RetryWhenHandler(int count) {
        this.mCount = count;
    }

    public RetryWhenHandler(int count, long delay) {
        this(count);
        this.mCount = count;
        this.mDelay = delay;
    }

    @Override
    public Observable<?> call(final Observable<? extends Throwable> observable) {
        return observable
                .flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        if (counter < mCount && (throwable instanceof UnknownHostException
                                || throwable instanceof SocketException
                                || throwable instanceof HttpException)) {
                            counter++;
                            return Observable.timer(mDelay, TimeUnit.SECONDS);
                        }
                        return Observable.error(throwable);
                    }
                });
    }

}
