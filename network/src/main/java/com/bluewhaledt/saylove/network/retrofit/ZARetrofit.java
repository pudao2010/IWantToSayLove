package com.bluewhaledt.saylove.network.retrofit;

import android.content.Context;
import android.util.Log;

import com.bluewhaledt.saylove.network.BuildConfig;
import com.bluewhaledt.saylove.network.cookie.PersistentCookieJar;
import com.bluewhaledt.saylove.network.cookie.cache.SetCookieCache;
import com.bluewhaledt.saylove.network.cookie.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author chenzijin
 * @version 1.0.0
 * @date 2016/5/24
 */
public class ZARetrofit {
    private static ZARetrofit instance;
    private static final int DEFAULT_TIMEOUT = 15;
    private Retrofit mRetrofit;
    private Context mContext;
    private static boolean sIsHttps = false;
    public String mServerAddress;
    private OkHttpClient client;

    private ZARetrofit(Context context, boolean isHttps) {
        mContext = context.getApplicationContext();
        sIsHttps = isHttps;
        initRetrofit();
    }

    private void initRetrofit() {
        mServerAddress = BuildConfig.SERVER_ADDRESS_FORMAL;
        String publishEnvironment = BuildConfig.PUBLISH_ENVIRONMENT;
        if (sIsHttps) {
            mServerAddress = BuildConfig.SERVER_ADDRESS_FORMAL_S;
        }
        if ("personal".equals(publishEnvironment)) {
            mServerAddress = BuildConfig.SERVER_ADDRESS_PERSONAL;
        }
        Log.d("yanc", mServerAddress);
        initRetrofit(mServerAddress);
    }

    public final String getHost() {
        return mServerAddress;
    }

    private void initRetrofit(String host) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        /*  shiming tianjia  start */
        //缓存文件夹
        File cacheFile = new File(mContext.getCacheDir(),"cache");
        //缓存大小为10M
        int cacheSize = 10 * 1024 * 1024;
        Cache cache=new Cache( cacheFile,cacheSize);
        /*  shiming tianjia   end*/
        client = new OkHttpClient.Builder().cache(cache)
                .addInterceptor(new HeadersInterceptor(mContext))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext)))
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

    }

    public OkHttpClient getOkHttpClient() {
        return client;
    }

    public static ZARetrofit getInstance(Context context) {
        if (instance == null || sIsHttps) {
            synchronized (ZARetrofit.class) {
                if (instance == null || sIsHttps) {
                    instance = new ZARetrofit(context, true);
                }
            }
        }
        return instance;
    }

    public static ZARetrofit getInstance(Context context, boolean isHttps) {
        if (instance == null || isHttps) {
            synchronized (ZARetrofit.class) {
                if (instance == null || isHttps) {
                    instance = new ZARetrofit(context, isHttps);
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public static <T> T getService(Context context, Class<T> service) {
        return ZARetrofit.getInstance(context).getRetrofit().create(service);
    }

}
