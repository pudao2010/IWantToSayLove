package com.bluewhaledt.saylove.ui.register_login.Head_portrait.service;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.entity.HeadProtraitEntity;
import com.bluewhaledt.saylove.ui.register_login.Head_portrait.entity.RandomNameEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 描述：service
 * 作者：shiming_li
 * 时间：2016/12/5 11:29
 * 包名：com.zhenai.saylove_icon.ui.recommend.service
 * 项目名：SayLove
 */
public interface HeadProtraitService {
    @FormUrlEncoded
    @POST(Url.COMMIT_AVATOR_NAME)
    Observable<ZAResponse<HeadProtraitEntity>> postNameAndAvatar(@Field("avatar") String array,@Field("nickName") String name);

    @POST(Url.GET_RANDOM_NAME)
    Observable<ZAResponse<RandomNameEntity>> getRandomName();
}
