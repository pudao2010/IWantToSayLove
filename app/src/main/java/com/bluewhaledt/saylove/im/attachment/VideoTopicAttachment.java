package com.bluewhaledt.saylove.im.attachment;

import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.google.gson.Gson;

/**
 * Created by zhenai-liliyan on 16/12/19.
 */

public class VideoTopicAttachment extends BaseCustomAttachment{

    public Content content;

    public VideoTopicAttachment() {
        super(CustomMsgType.VIDEO_TOPIC);
    }

    @Override
    public void parseData(String data) {
        content = new Gson().fromJson(data,Content.class);
    }

    @Override
    protected CustomMsgEntity packData() {
        entity.data = new Gson().toJson(content);
        return entity;
    }

    public static class Content extends BaseEntity{
        //话题内容
        public String content;
        //视频图片
        public String pic;
        //视频标题
        public String title;
        //视频id
        public String videoId;

        @Override
        public String[] uniqueKey() {
            return new String[0];
        }
    }
}
