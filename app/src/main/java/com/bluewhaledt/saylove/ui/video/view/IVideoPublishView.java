package com.bluewhaledt.saylove.ui.video.view;

import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;

import java.util.List;

/**
 * Created by rade.chan on 2016/12/16.
 */

public interface IVideoPublishView {
    void showSimilarVideos(List<VideoIndexEntity> list);
}
