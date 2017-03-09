package com.bluewhaledt.saylove.ui.register_login.regist;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.regist
 * @文件名: RegistService
 * @创建者: YanChao
 * @创建时间: 2016/11/29 19:43
 * @描述： TODO
 */
public interface RegistService {
    @FormUrlEncoded
    @POST(Url.PHONE_VERIFY)
    Observable<ZAResponse> phoneVerify(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST(Url.REGISTER_USER)
    Observable<ZAResponse> regist(@Field("mobile") String mobile,
                                          @Field("verfycode") String verfycode,
                                          @Field("sex") int sex,
                                          @Field("workcity") int workcity,
                                          @Field("height") int height,
                                          @Field("marryState") int marryState,
                                          @Field("salary") int salary,
                                          @Field("pwd") String pwd,
                                          @Field("birthday") String birthday);
}
