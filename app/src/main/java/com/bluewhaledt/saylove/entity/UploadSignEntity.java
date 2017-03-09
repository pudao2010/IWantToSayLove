package com.bluewhaledt.saylove.entity;


import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by Administrator on 2016/11/18.
 */
public class UploadSignEntity extends BaseEntity {
    public String filePath;
    public String bucket;
    public String sign;
    public String fileName;
    public String singleSign;

    @Override
    public String[] uniqueKey() {
        return null;
    }
}
