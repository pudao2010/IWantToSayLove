package com.bluewhaledt.saylove.ui.video.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.List;

/**
 * Created by rade.chan on 2016/12/14.
 */

public class VideoTopicListEntity extends BaseEntity{

    public List<VideoTopicEntity> list;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
