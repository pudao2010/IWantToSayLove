package com.bluewhaledt.saylove.ui.video.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by rade.chan on 2016/12/25.
 */

public class VideoAskForUploadEntity extends BaseEntity {

    public String message;  //对应的提示
    public boolean state;  //true 正常 false - 超过限制

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
