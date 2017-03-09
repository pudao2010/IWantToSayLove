package com.bluewhaledt.saylove.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by zhenai-liliyan on 16/12/5.
 */

public class IMAccount extends BaseEntity {

    public String imAccount;

    public String token;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
