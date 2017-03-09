package com.bluewhaledt.saylove.service;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.entity.CheckChatAvailable;
import com.bluewhaledt.saylove.entity.IMAccount;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/12/5.
 */

public interface CommonService {

    @GET(Url.GET_IM_ACCOUNT)
    Observable<ZAResponse<IMAccount>> getIMAccount();

    @FormUrlEncoded
    @POST(Url.CHECK_CAN_CHAT)
    Observable<ZAResponse<CheckChatAvailable>> checkCanChat(@Field("objectId") long objectId);


}
