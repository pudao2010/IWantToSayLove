package com.bluewhaledt.saylove.ui.heartbeat.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public class HeartBeatItem extends BaseEntity{
    //点赞内容详情
    public String detailContent;

    //点赞内容ID
    public long detailId;

    //点赞ID
    public long likeId;

    //点赞时间
    public String likeTime;

    //点赞类型，1心动点赞 2视频点赞
    public int likeType;

    //用户ID
    public long objectId;

    //是否已读，红点
    public boolean read;

    //头像
    public String avatar;

    //昵称
    public String nickName;

    //点赞内容缩略图
    public String detailPhoto;

    //性别 1男 2女
    public int sex;

    public HeartBeatItem(){

    }

    public HeartBeatItem(long objectId){
        this.objectId = objectId;
    }

    @Override
    public String[] uniqueKey() {
        return null;
    }
}
