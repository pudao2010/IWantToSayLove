package com.bluewhaledt.saylove.ui.setting;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;

import retrofit2.http.POST;
import rx.Observable;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.setting
 * @文件名: LogoutService
 * @创建者: YanChao
 * @创建时间: 2016/12/15 19:53
 * @描述： TODO
 */

public interface LogoutService {
    @POST(Url.LOGOUT)
    Observable<ZAResponse> logout();
}
