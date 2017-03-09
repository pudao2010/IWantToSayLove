package com.bluewhaledt.saylove.ui.message.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public class UnreadCount extends BaseEntity {

    public int likeCount;

    public int viewCount;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }

}
