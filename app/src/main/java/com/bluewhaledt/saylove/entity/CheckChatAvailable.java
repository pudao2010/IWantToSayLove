package com.bluewhaledt.saylove.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by zhenai-liliyan on 16/12/10.
 */

public class CheckChatAvailable extends BaseEntity {

    /**
     * 是否还有免费聊天次数
     */
    public boolean canChat;

    /**
     * 剩余可用免费次数
     */
    public int residueCount;

    /**
     * 是否超过3天的免费期
     */
    public boolean hasExpired;



    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
