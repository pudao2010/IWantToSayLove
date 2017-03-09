package com.bluewhaledt.saylove.service;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.entity.MsgEntity;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.info.entity.PhotoListEntity;
import com.bluewhaledt.saylove.ui.info.entity.ReportData;
import com.bluewhaledt.saylove.ui.info.entity.RequireInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.UserInfoEntity;
import com.bluewhaledt.saylove.ui.info.entity.VerifyInfoEntity;
import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexListEntity;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by rade.chan on 2016/12/1.
 */

public interface InfoService {

    @FormUrlEncoded
    @POST(Url.GET_INFO)
    Observable<ZAResponse<UserInfoEntity>> getUserInfo(@Field("objectId") String objectId);

    @FormUrlEncoded
    @POST(Url.GET_MATE_REQUIRE_CONDITIONS)
    Observable<ZAResponse<RequireInfoEntity>> getRequireConditions(@Field("objectId") String objectId);

    @FormUrlEncoded
    @POST(Url.EDIT_MATE_REQUIRE_CONDITIONS)
    Observable<ZAResponse> modifyMateConditions(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(Url.EDIT_MY_INFO)
    Observable<ZAResponse> modifyMyInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(Url.GET_PHOTOS)
    Observable<ZAResponse<PhotoListEntity>> getUserPhotos(@Field("pageNo") int pageNo, @Field("pageSize") int pageSize, @Field("objectId") String objectId);

    @FormUrlEncoded
    @POST(Url.TOUCH_HEART)
    Observable<ZAResponse<LikeEntity>> addPraise(@Field("objectId") String objectId);

    @FormUrlEncoded
    @POST(Url.GET_VERIFY_INFO)
    Observable<ZAResponse<VerifyInfoEntity>> getVerifyInfo(@Field("objectId") String objectId);

    @FormUrlEncoded
    @POST(Url.DELETE_PHOTO)
    Observable<ZAResponse<MsgEntity>> deletePhoto(@Field("photoId") String photoId);

    @FormUrlEncoded
    @POST(Url.REPORT_USER)
    Observable<ZAResponse<ReportData>> reportUser(@Field("objectId") long objectId, @Field("type") int type);

    @FormUrlEncoded
    @POST(Url.GET_OTHER_VIDEO_LIST)
    Observable<ZAResponse<VideoIndexListEntity>> getOtherVideoList(@Field("userId") String userId, @Field("page") int page);

    @FormUrlEncoded
    @POST(Url.GET_MY_VIDEO_LIST)
    Observable<ZAResponse<VideoIndexListEntity>> getMyVideoList(@Field("page") int page);


    @FormUrlEncoded
    @POST(Url.QUERY_VERIFY_DETAIL)
    Observable<ZAResponse<VerifyInfoEntity>> queryVerifyDetail(@Field("type") int type);

}
