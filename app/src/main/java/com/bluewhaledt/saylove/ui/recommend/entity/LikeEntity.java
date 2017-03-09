package com.bluewhaledt.saylove.ui.recommend.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * 描述：心动返回的likeid
 * 作者：shiming_li
 * 时间：2016/12/9 17:47
 * 包名：com.zhenai.saylove_icon.ui.recommend.entity
 * 项目名：SayLove
 */
public class LikeEntity extends BaseEntity {
    public int likeId;
    public String msg;
    @Override
    public String[] uniqueKey() {
        return new String[]{likeId+""};
    }

}
