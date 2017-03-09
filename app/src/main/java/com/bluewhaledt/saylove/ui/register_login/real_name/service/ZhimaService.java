package com.bluewhaledt.saylove.ui.register_login.real_name.service;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.pay.entity.VerifyProduct;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.VerifyTipsEntity;
import com.bluewhaledt.saylove.ui.register_login.real_name.entity.ZhimaEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;


/**
 * 描述：service
 * 作者：shiming_li
 * 时间：2016/12/5 10:08
 * 包名：com.zhenai.saylove_icon.ui.register_login.real_name.service
 * 项目名：SayLove
 */
public interface ZhimaService {
    @FormUrlEncoded
    @POST(Url.ZHIMA_POST_NAME)
    Observable<ZAResponse<ZhimaEntity>> getZhimaInfo(@Field("userName") String  username,
                                                         @Field("certNo") String certNo);

    @FormUrlEncoded
    @POST(Url.ZHIMA_CALL_BACK)
    Observable<ZAResponse<Void>> postZhimaCallBack(@Field("params") String params,@Field("sign") String sign);

    @POST(Url.GET_VERIFY_PRODUCT)
    Observable<ZAResponse<VerifyProduct>> getVerifyProduct();


    @GET(Url.GET_VERIFYTIPS_CONTENT)
    Observable<ZAResponse<VerifyTipsEntity>> getVerifyTips();

}
