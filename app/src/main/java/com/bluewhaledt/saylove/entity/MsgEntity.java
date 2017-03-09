package com.bluewhaledt.saylove.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by Administrator on 2016/12/2.
 */

public class MsgEntity extends BaseEntity {
    public String msg;
    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
