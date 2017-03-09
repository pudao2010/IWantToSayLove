package com.bluewhaledt.saylove.ui.info.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by rade.chan on 2016/11/30.
 */

public class VerifyItemInfoEntity extends BaseEntity {

    public String verifyTitle;
    public String showText;
    public int iconRes;
    public String showDetailText;
    public String verifyTime;
    public boolean isVerifySuccess;
    public int verifyStatus;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
