package com.bluewhaledt.saylove.util;

import android.os.SystemClock;

import java.io.File;

import rx.Observable;
import rx.Single;
import rx.Single.Transformer;
import rx.SingleSubscriber;
import rx.functions.Func1;

public class RxUtil {

    /**
     * rxjava递归查询内存中的视频文件
     * @param f
     * @return
     */
    public static Observable<File> listFiles(final File f){
        if(f.isDirectory()){
            return Observable.from(f.listFiles()).flatMap(new Func1<File, Observable<File>>() {
                @Override
                public Observable<File> call(File file) {
                    /**如果是文件夹就递归**/
                    return listFiles(file);
                }
            });
        } else {
            /**filter操作符过滤,是文件就通知观察者**/
                    SystemClock.sleep(10);
            return Observable.just(f).filter(new Func1<File, Boolean>() {
                @Override
                public Boolean call(File file) {
                    return f.exists() && f.canRead();
                }
            });
        }
    }

    int add(int a, int b){
        return a+b;
    }

    public  void example(){
        Single.just(add(1,2)).compose(new Transformer<Integer, String>(){

            @Override
            public Single<String> call(Single<Integer> integerSingle) {
                return integerSingle.map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return String.valueOf(integer + 3);
                    }
                });
            }
        }).subscribe(new SingleSubscriber<String>() {
            @Override
            public void onSuccess(String value) {
                System.out.println(value);
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

}