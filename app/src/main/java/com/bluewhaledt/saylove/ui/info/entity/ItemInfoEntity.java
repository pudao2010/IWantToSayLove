package com.bluewhaledt.saylove.ui.info.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by rade.chan on 2016/11/30.
 */

public class ItemInfoEntity extends BaseEntity{
    public String title;
    public String content;
    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
