package com.bluewhaledt.saylove.im.parser;

import android.text.TextUtils;

import com.bluewhaledt.saylove.im.attachment.BaseCustomAttachment;
import com.bluewhaledt.saylove.im.attachment.CustomMsgEntity;
import com.bluewhaledt.saylove.im.attachment.CustomMsgType;
import com.bluewhaledt.saylove.im.attachment.VideoTopicAttachment;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by zhenai-liliyan on 16/10/31.
 */

public class CustomAttachParser implements MsgAttachmentParser {

    @Override
    public MsgAttachment parse(String json) {
        BaseCustomAttachment attachment = null;
        try {
            JSONObject jsonObject = new JSONObject(json);

            CustomMsgEntity entity = new CustomMsgEntity();
            entity.type = jsonObject.optInt("type", CustomMsgType.DEFAULT);
            entity.data = jsonObject.optString("data", "");
            String ext = jsonObject.optString("ext", "");
            if (!TextUtils.isEmpty(ext)) {
                JSONObject extJson = new JSONObject(ext);
                Iterator<String> iterator = extJson.keys();
                entity.ext = new HashMap<>();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    entity.ext.put(key, extJson.optString(key, ""));
                }

            }
            switch (entity.type) {
                case CustomMsgType.VIDEO_TOPIC:
                    attachment = new VideoTopicAttachment();
                    break;
            }
            if (attachment != null && !TextUtils.isEmpty(entity.data)) {
                attachment.entity = entity;
                attachment.parseData(entity.data);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return attachment;
    }

    public static String packData(CustomMsgEntity entity) {
        return new Gson().toJson(entity);
    }
}
