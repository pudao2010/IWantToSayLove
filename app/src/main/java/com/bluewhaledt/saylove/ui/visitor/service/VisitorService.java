package com.bluewhaledt.saylove.ui.visitor.service;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.visitor.entiry.Visitor;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public interface VisitorService {

    @FormUrlEncoded
    @POST(Url.GET_VISIT_ME_LIST)
    Observable<ZAResponse<ResultEntity<Visitor>>> getVisitMeList(@Field("page") int page,@Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST(Url.GET_MY_VISIT_TO_LIST)
    Observable<ZAResponse<ResultEntity<Visitor>>> getMyVisitToList(@Field("page") int page,@Field("pageSize") int pageSize);

}
