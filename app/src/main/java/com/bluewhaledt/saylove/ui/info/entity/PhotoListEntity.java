package com.bluewhaledt.saylove.ui.info.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.List;

/**
 * Created by rade.chan on 2016/12/4.
 */

public class PhotoListEntity extends BaseEntity{

    public int count;

    public boolean hasNext;

    public List<PhotoEntity> list;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
