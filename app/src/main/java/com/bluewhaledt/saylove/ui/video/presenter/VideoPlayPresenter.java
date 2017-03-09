package com.bluewhaledt.saylove.ui.video.presenter;

import android.content.Context;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.service.InfoService;
import com.bluewhaledt.saylove.service.VideoService;
import com.bluewhaledt.saylove.ui.info.entity.ReportData;
import com.bluewhaledt.saylove.ui.recommend.entity.LikeEntity;
import com.bluewhaledt.saylove.ui.recommend.service.RecommendService;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;
import com.bluewhaledt.saylove.ui.video.view.IVideoPlayView;

import rx.Observable;

/**
 * Created by rade.chan on 2016/12/16.
 */

public class VideoPlayPresenter {
    private VideoService mService;
    private IVideoPlayView videoPlayView;
    private Context mContext;


    public VideoPlayPresenter(Context context, IVideoPlayView videoPlayView) {
        this.videoPlayView = videoPlayView;
        mContext = context;
        mService = ZARetrofit.getService(context, VideoService.class);
    }

    public void getReportData(final Context context, long objectId, int type) {
        InfoService infoService = ZARetrofit.getService(mContext, InfoService.class);
        Observable<ZAResponse<ReportData>> observable = infoService.reportUser(objectId, type);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<ReportData>>(new ZASubscriberListener<ZAResponse<ReportData>>() {
            @Override
            public void onSuccess(ZAResponse<ReportData> response) {
                ToastUtils.toast(context, response.data.msg);
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                super.onFail(errorCode, errorMsg);
                ToastUtils.toast(context, errorMsg);
            }
        }));
    }

    public void addPraise(String objId,String videoId) {
        Observable<ZAResponse<LikeEntity>> observable = mService.addVideoPraise(objId,videoId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<LikeEntity>>(new ZASubscriberListener<ZAResponse<LikeEntity>>() {
            @Override
            public void onSuccess(ZAResponse<LikeEntity> response) {
                if(response.data!=null) {
                    videoPlayView.praiseSuccess(response.data);
                }
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                videoPlayView.praiseFail(errorCode,errorMsg);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                videoPlayView.praiseFail(null,null);
            }
        }));
    }

    public void cancelPraise(String likeId){
        RecommendService recommendService =  ZARetrofit.getService(mContext, RecommendService.class);
        Observable<ZAResponse> observable = recommendService.cancelTouchHeart(likeId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse>(new ZASubscriberListener<ZAResponse>() {
            @Override
            public void onSuccess(ZAResponse response) {
                videoPlayView.cancelLikeSuccess();
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                ToastUtils.toast(mContext,errorMsg);
                videoPlayView.cancelLikeFail();

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                videoPlayView.cancelLikeFail();
            }
        }));
    }




    public void deleteVideo(String videoId) {
        Observable<ZAResponse> observable = mService.deleteVideo(videoId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse>(new ZASubscriberListener<ZAResponse>() {
            @Override
            public void onSuccess(ZAResponse response) {
                videoPlayView.deleteVideoSuccess();
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                ToastUtils.toast(mContext, errorMsg);
            }
        }));
    }

    public void addViewNum(String videoId) {
        Observable<ZAResponse> observable = mService.addViewNum(videoId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse>(new ZASubscriberListener<ZAResponse>() {
            @Override
            public void onSuccess(ZAResponse response) {

            }

            @Override
            public void onFail(String errorCode, String errorMsg) {

            }
        }));
    }



    public void getVideoInfo(String videoId) {
        Observable<ZAResponse<VideoIndexEntity>> observable = mService.getVideoInfo(videoId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<VideoIndexEntity>>
                (new ZASubscriberListener<ZAResponse<VideoIndexEntity>>() {
                    @Override
                    public void onSuccess(ZAResponse<VideoIndexEntity> response) {
                        if (response.data != null) {
                            videoPlayView.getVideoInfoSuccess(response.data);
                        }
                    }

                    @Override
                    public void onFail(String errorCode, String errorMsg) {
                        videoPlayView.getVideoFail(errorMsg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        videoPlayView.getVideoFail(mContext.getString(R.string.no_network_connected));
                    }
                }));
    }
}
