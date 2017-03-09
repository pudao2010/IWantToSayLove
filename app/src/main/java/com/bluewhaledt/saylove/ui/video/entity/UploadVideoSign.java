package com.bluewhaledt.saylove.ui.video.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by rade.chan on 2016/12/12.
 */

public class UploadVideoSign extends BaseEntity {
    public String path;
    public String sign;
    public String bucket;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
