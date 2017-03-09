package com.bluewhaledt.saylove.ui.video.presenter;

import android.content.Context;

import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.service.VideoService;
import com.bluewhaledt.saylove.ui.video.entity.VideoAskForUploadEntity;
import com.bluewhaledt.saylove.ui.video.entity.VideoTopicListEntity;
import com.bluewhaledt.saylove.ui.video.view.IVideoRecordView;

import rx.Observable;

/**
 * Created by rade.chan on 2016/12/14.
 */

public class VideoRecordPresenter {

    private VideoService mService;
    private IVideoRecordView publishView;

    public VideoRecordPresenter(Context context, IVideoRecordView view) {
        this.publishView = view;
        mService = ZARetrofit.getService(context, VideoService.class);
    }

    public void getRandomTopic() {
        Observable<ZAResponse<VideoTopicListEntity>> observable = mService.getVideoRandomTopic();
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<VideoTopicListEntity>>
                (new ZASubscriberListener<ZAResponse<VideoTopicListEntity>>() {

                    @Override
                    public void onSuccess(ZAResponse<VideoTopicListEntity> response) {
                        if(response.data!=null && response.data.list!=null){

                            publishView.getTopicSuccess(response.data.list);
                        }
                    }

                }));
    }


    public void checkIsCanUpload(final boolean isRetry) {
        if(!isRetry){
            publishView.dismissDialog();
        }
        Observable<ZAResponse<VideoAskForUploadEntity>> observable = mService.checkCanUploadVideo();
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<VideoAskForUploadEntity>>
                (new ZASubscriberListener<ZAResponse<VideoAskForUploadEntity>>() {
                    @Override
                    public void onSuccess(ZAResponse<VideoAskForUploadEntity> response) {
                        if(response.data!=null){
                           // publishView.checkIsCanUpload(isRetry,response.data);
                        }
                        publishView.dismissDialog();
                    }

                    @Override
                    public void onFail(String errorCode, String errorMsg) {
                        publishView.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        publishView.dismissDialog();
                    }
                }));
    }



}
