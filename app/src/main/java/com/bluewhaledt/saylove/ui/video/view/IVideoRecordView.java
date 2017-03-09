package com.bluewhaledt.saylove.ui.video.view;

import com.bluewhaledt.saylove.ui.video.entity.VideoTopicEntity;

import java.util.List;

/**
 * Created by rade.chan on 2016/12/14.
 */

public interface IVideoRecordView {
    void getTopicSuccess(List<VideoTopicEntity> list);
    void dismissDialog();
}
