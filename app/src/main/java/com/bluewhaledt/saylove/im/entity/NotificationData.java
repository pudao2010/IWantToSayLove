package com.bluewhaledt.saylove.im.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by zhenai-liliyan on 16/12/21.
 */

public class NotificationData extends BaseEntity {


    /**
     * 通知类型
     * 0:普通聊天消息
     * 1：心动
     * 2：学历审核未通过
     * 3：房产审核未通过
     * 4：车辆审核未通过
     * 5：头像审核未通过
     * 6：昵称审核未通过
     * 7：自我介绍审核未通过
     */
    public int type;

    /**
     * 学历，房产，车辆审核id
     */
    public long verifyId;


    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
