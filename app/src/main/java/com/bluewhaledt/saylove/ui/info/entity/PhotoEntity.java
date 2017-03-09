package com.bluewhaledt.saylove.ui.info.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by rade.chan on 2016/11/28.
 */

public class PhotoEntity extends BaseEntity {
    public static final int TYPE_PHOTO = 0;
    public static final int TYPE_VIDEO = 1;

    public String photoId;
    public String photoUrl;          //照片地址
    public int verifyStatus;        //审核状态，1审核中 2通过 3未通过
    public int type;
    public int shiedId;             //视频遮罩  //1-没有遮罩，2-遮罩1（以后可能会有其他遮罩）

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
