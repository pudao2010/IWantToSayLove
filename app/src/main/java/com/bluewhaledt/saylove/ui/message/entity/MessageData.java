package com.bluewhaledt.saylove.ui.message.entity;

import com.bluewhaledt.saylove.entity.ChatUserList;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

/**
 * Created by zhenai-liliyan on 16/11/28.
 */

public class MessageData extends ResultEntity{

    public int unreadCount;

    public String message;

    public String date;

    public String sessionId;


    public ChatUserList.ChatUser user;


    @Override
    public String[] uniqueKey() {
        return new String[]{sessionId};
    }
}
