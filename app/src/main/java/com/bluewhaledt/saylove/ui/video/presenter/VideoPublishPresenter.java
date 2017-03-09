package com.bluewhaledt.saylove.ui.video.presenter;

import android.content.Context;

import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.service.VideoService;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexListEntity;
import com.bluewhaledt.saylove.ui.video.view.IVideoPublishView;

import rx.Observable;

/**
 * Created by rade.chan on 2016/12/16.
 */

public class VideoPublishPresenter {
    private VideoService mService;
    private IVideoPublishView publishView;

    public VideoPublishPresenter(Context context, IVideoPublishView view) {
        this.publishView = view;
        mService = ZARetrofit.getService(context, VideoService.class);
    }

    public void getVideoIndexList(String topicId) {
        Observable<ZAResponse<VideoIndexListEntity>> observable = mService.getSimilarVideo(topicId);
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<VideoIndexListEntity>>
                (new ZASubscriberListener<ZAResponse<VideoIndexListEntity>>() {

                    @Override
                    public void onSuccess(ZAResponse<VideoIndexListEntity> response) {
                        if (response.data != null && response.data.list != null) {
                            publishView.showSimilarVideos(response.data.list);
                        }
                    }

                }));
    }

}
