package com.bluewhaledt.saylove.ui.message.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by zhenai-liliyan on 16/12/1.
 */

public class ExtendedData extends BaseEntity {

    public String otherSessionId;
    public String otherAvatar;
    public long otherMemberId;
    public String nikeName;
    /**
     * 我还可用的免费次数
     */
    public int myFreeChatCount;

    public boolean canChat;

    public boolean isLockMessage;

    /**
     * 是否是从视频页面跳转过来的
     */
    public boolean isFromVideoPage;

    public String videoPic;

    public String videoTitle;

    public String videoMessage;

    public String videoId;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
