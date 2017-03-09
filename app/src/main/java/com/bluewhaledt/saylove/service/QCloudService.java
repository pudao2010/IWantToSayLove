package com.bluewhaledt.saylove.service;


import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.entity.UploadSignEntity;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 腾讯云Service
 *
 * @author yintaibing
 * @date 2016/11/18
 */
public interface QCloudService {

    @FormUrlEncoded
    @POST(Url.GET_QCLOUD_SIGN)
    Observable<ZAResponse<UploadSignEntity>> getQCloudSign(@Field("suffix") String suffix);

    /**
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Url.SAVE_PICTURE)
    Observable<ZAResponse<Void>> savePicNames(@FieldMap Map<String,String> params);
}
