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
 * @文件名: LoginService
 * @创建者: YanChao
 * @创建时间: 2016/11/26 14:14
 * @描述： 登录相关网络请求
 */
public interface LoginService {
    @FormUrlEncoded
    @POST(Url.PHONE_LOGIN)
    Observable<ZAResponse> phoneLogin(@Field("mobile") String phone, @Field("pwd") String password);

    @POST(Url.LOGIN_HELPER)
    Observable<ZAResponse<LoginHelperEntity>> getLoginHelper();

    @POST(Url.LOGOUT)
    Observable<ZAResponse> logout();
}
