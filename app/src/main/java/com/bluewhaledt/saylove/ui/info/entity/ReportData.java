package com.bluewhaledt.saylove.ui.info.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by zhenai-liliyan on 16/12/14.
 */

public class ReportData extends BaseEntity {

    public String msg;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
