package com.bluewhaledt.saylove.ui.message.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by zhenai-liliyan on 16/12/14.
 */

public class FreeChatMsg extends BaseEntity {

    public String msg;

    //免费次数
    public int residueCount;

    //免费天数
    public int residueDay;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
