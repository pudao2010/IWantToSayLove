package com.bluewhaledt.saylove.im.util;

import com.bluewhaledt.saylove.im.attachment.VideoTopicAttachment;
import com.google.gson.Gson;

/**
 * Attachment帮助类
 */
public class AttachmentUtils {

    public static VideoTopicAttachment genVideoTopicAttachment(String content, String myMsgContent) {
        VideoTopicAttachment mAttachment = new VideoTopicAttachment();
        mAttachment.content = new Gson().fromJson(content, VideoTopicAttachment.Content.class);
        return mAttachment;
    }

}
