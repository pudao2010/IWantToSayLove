package com.bluewhaledt.saylove.ui.pay.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by zhenai-liliyan on 16/12/6.
 */

public class VerifyProduct extends BaseEntity {

    public int productId;

    public String initPrice;

    public String price;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
