package com.bluewhaledt.saylove.service;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.entity.UploadAliSignEntity;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.video.entity.VideoAskForUploadEntity;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexListEntity;
import com.bluewhaledt.saylove.ui.video.entity.VideoTopicListEntity;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by rade.chan on 2016/12/12.
 */

public interface VideoService {


    @FormUrlEncoded
    @POST(Url.SAVE_VIDEO_FILE_INFO)
    Observable<ZAResponse> saveVideoInfo(@Field("topicId") String topicId, @Field("fileName") String fileName, @Field("shield") int shield);

    @FormUrlEncoded
    @POST(Url.GET_VIDEO_INDEX_LIST)
    Observable<ZAResponse<ResultEntity<VideoIndexEntity>>> getVideoIndexList(@Field("page") int page);


    @POST(Url.GET_RANDOM_TOPIC)
    Observable<ZAResponse<VideoTopicListEntity>> getVideoRandomTopic();

    @POST(Url.GET_ALI_VIDEO_SIGN)
    Observable<ZAResponse<UploadAliSignEntity>> getAliVideoSign();

    @FormUrlEncoded
    @POST(Url.GET_SIMILAR_VIDEO)
    Observable<ZAResponse<VideoIndexListEntity>> getSimilarVideo(@Field("topicId") String topicId);

    @FormUrlEncoded
    @POST(Url.ADD_VIEW_NUM)
    Observable<ZAResponse> addViewNum(@Field("videoId") String videoId);

    @FormUrlEncoded
    @POST(Url.GET_VIDEO_INFO)
    Observable<ZAResponse<VideoIndexEntity>> getVideoInfo(@Field("videoId") String videoId);


    @FormUrlEncoded
    @POST(Url.DELETE_VIDEO)
    Observable<ZAResponse> deleteVideo(@Field("videoId") String  videoId);

    @FormUrlEncoded
    @POST(Url.VIDEO_ADD_PRAISE)
    Observable<ZAResponse<LikeEntity>> addVideoPraise(@Field("objectId") String  objectId, @Field("detailId") String  detailId);



    @POST(Url.CHECK_CAN_UPLOAD_VIDEO)
    Observable<ZAResponse<VideoAskForUploadEntity>> checkCanUploadVideo();


}
