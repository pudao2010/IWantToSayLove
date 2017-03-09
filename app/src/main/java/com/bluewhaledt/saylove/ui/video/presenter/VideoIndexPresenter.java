package com.bluewhaledt.saylove.ui.video.presenter;

import android.content.Context;

import com.bluewhaledt.saylove.service.VideoService;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;
import com.bluewhaledt.saylove.ui.video.view.IVideoIndexView;
import com.bluewhaledt.saylove.widget.linear_view.IBaseMode;
import com.bluewhaledt.saylove.widget.linear_view.LinearBasePresenter;

/**
 * Created by rade.chan on 2016/12/2.
 */

public class VideoIndexPresenter extends LinearBasePresenter<VideoIndexEntity> {
    private VideoService mService;
    private IVideoIndexView modifyView;
    private Context mContext;

    public VideoIndexPresenter(Context context,IVideoIndexView<VideoIndexEntity> actionView) {
        super(actionView);
        this.mContext = context;
        this.modifyView = actionView;
    }

    @Override
    public IBaseMode<VideoIndexEntity> createModel() {
        return null;
    }

//    @Override
//    public IBaseMode<ResultEntity<VideoIndexEntity>> createModel() {
//        return new VideoIndexModel();
//    }

//    public VideoIndexPresenter(Context context, IVideoIndexView view) {
//        this.mContext = context;
//        this.modifyView = view;
//        mService = ZARetrofit.getService(context, VideoService.class);
//    }

//    public void getVideoIndexList(int page) {
//        Observable<ZAResponse<VideoIndexListEntity>> observable = mService.getVideoIndexList(page);
//        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<VideoIndexListEntity>>
//                (new ZASubscriberListener<ZAResponse<VideoIndexListEntity>>() {
//
//                    @Override
//                    public void onSuccess(ZAResponse<VideoIndexListEntity> response) {
//                        if (response.data != null && response.data.list != null) {
//                            modifyView.showIndexList(response.data.list);
//                        }
//                    }
//
//                }));
//    }


//    @Override
//    public IBaseMode<VideoIndexEntity> createModel() {
//        return new VideoIndexModel();
//    }
}
