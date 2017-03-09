package com.bluewhaledt.saylove.im.attachment;

import com.bluewhaledt.saylove.im.parser.CustomAttachParser;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

/**
 * Created by zhenai-liliyan on 16/10/31.
 */

public abstract class BaseCustomAttachment implements MsgAttachment {

    /**
     * 自定义消息内容
     */
    public CustomMsgEntity entity;

    protected BaseCustomAttachment(int type) {
        entity = new CustomMsgEntity();
        entity.type = type;
    }

    @Override
    public String toJson(boolean send) {
        return CustomAttachParser.packData(packData());
    }

    /**
     * 子类的解析和封装接口。
     *
     * @param data 自定义消息的数据
     */
    public abstract void parseData(String data);

    protected abstract CustomMsgEntity packData();

}
