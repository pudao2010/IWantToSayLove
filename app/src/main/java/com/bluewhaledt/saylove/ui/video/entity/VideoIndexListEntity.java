package com.bluewhaledt.saylove.ui.video.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.List;

/**
 * Created by rade.chan on 2016/12/13.
 */

public class VideoIndexListEntity extends BaseEntity{
    public int count;
    public boolean hasNext;
    public List<VideoIndexEntity> list;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
