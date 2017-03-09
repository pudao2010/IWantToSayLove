package com.bluewhaledt.saylove.ui.video.model;

import android.content.Context;

import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.service.VideoService;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;
import com.bluewhaledt.saylove.widget.linear_view.IBaseMode;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/12/16.
 */

public class VideoIndexModel implements IBaseMode<VideoIndexEntity> {

    @Override
    public void getDataList(Context context, int pageIndex, int pageSize, BaseSubscriber<ZAResponse<ResultEntity<VideoIndexEntity>>> subscriber) {
        VideoService mService = ZARetrofit.getService(context, VideoService.class);
        Observable<ZAResponse<ResultEntity<VideoIndexEntity>>> observable = mService.getVideoIndexList(pageIndex);
        HttpMethod.toSubscribe(observable, subscriber);
    }

}
