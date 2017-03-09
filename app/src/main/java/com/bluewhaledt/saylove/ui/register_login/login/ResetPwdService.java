package com.bluewhaledt.saylove.ui.register_login.login;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: ResetPwdService
 * @创建者: YanChao
 * @创建时间: 2016/12/2 11:28
 * @描述： TODO
 */
public interface ResetPwdService {
    @FormUrlEncoded
    @POST(Url.PWD_VERIFY_CODE)
    Observable<ZAResponse> pwdVerifyCode(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST(Url.RESET_PWD)
    Observable<ZAResponse> resetPwd(@Field("mobile") String mobile,@Field("verifyCode") String verifyCode,@Field("newPwd") String newPwd);
}
