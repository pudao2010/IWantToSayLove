package com.bluewhaledt.saylove.ui.message.entity;

import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.im.util.MessageUtils;
import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by zhenai-liliyan on 16/11/29.
 */

public class ChatData extends BaseEntity {

    private final String LOCAL_DATE = "local_date";

    private final String IS_READ = "is_read";

    public String avatar;

    public boolean isAnimPlaying;


    public IMMessage message;

    public void setDate(String date){
        MessageUtils.setLocalExtValue(message,LOCAL_DATE,date);
        IMUtil.updateIMMessage(message);
    }

    public String getDate(){
        return (String) MessageUtils.getLocalExtValue(message,LOCAL_DATE);
    }

    public void setReadStatus(boolean isRead){
        MessageUtils.setLocalExtValue(message,IS_READ,isRead);
        IMUtil.updateIMMessage(message);
    }

    public boolean getReadStatus(){
        Object result = MessageUtils.getLocalExtValue(message,IS_READ);
        if (result != null){
            return (boolean) result;
        }else{
            return false;
        }

    }





    @Override
    public String[] uniqueKey() {
        return new String[]{message.getUuid()};
    }

}
