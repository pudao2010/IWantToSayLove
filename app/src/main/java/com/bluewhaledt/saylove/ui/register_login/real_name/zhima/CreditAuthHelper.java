package com.bluewhaledt.saylove.ui.register_login.real_name.zhima;

import android.app.Activity;

import com.android.moblie.zmxy.antgroup.creditsdk.app.CreditApp;
import com.android.moblie.zmxy.antgroup.creditsdk.app.ICreditListener;
import com.bluewhaledt.saylove.base.util.DebugUtils;

import java.util.Map;

/**
 * 芝麻抽取的接口类
 * Created by befairyliu on 2016/1/30.
 */
public class CreditAuthHelper {
    private static CreditApp mCreditApp;
    private static void createCreditApp(Activity activity){
        // CreditApp类是SDK的主要实现类，开发者可通过CreditApp类访问芝麻信用的授权等API。
        // 传入应用程序的全局context，可通过activity的getApplicationContext方法获取
        mCreditApp = CreditApp.getOrCreateInstance(activity.getApplicationContext());
    }
    public static void creditAuth(final Activity activity, String appid, String params,
                                  String sign, Map<String, String> extParams, ICreditListener listener) {
        //创建credit app
        createCreditApp(activity);
        //请求芝麻信用授权
        mCreditApp.authenticate(activity, appid, params, sign, extParams, listener);
    }
    public static void creditCerify(final Activity activity, String appid,String params,String sign, Map<String , String> extParams, ICreditListener listener){
        createCreditApp(activity);
        // 请求芝麻认证
        //mCreditApp.setCurrEnv("release");
        DebugUtils.d("shiming","芝麻回调的参数"+"appid="+appid+"params"+params+"sign"+sign);
        mCreditApp.cerifyUserInfo(activity,appid,params,sign,null,listener);
    }
}
