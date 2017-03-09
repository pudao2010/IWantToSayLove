package com.bluewhaledt.saylove.ui.video.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by rade.chan on 2016/12/14.
 */

public class VideoTopicEntity extends BaseEntity{

    public String content;
    public int topicId;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
