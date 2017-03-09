package com.bluewhaledt.saylove.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by rade.chan on 2016/12/14.
 */

public class UploadAliSignEntity extends BaseEntity {

    public String accessKeyId;
    public String accessKeySecret;
    public String bucket;
    public String endpoint;
    public String expiration;
    public String targetFileName;
    public String securityToken;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
