package com.bluewhaledt.saylove.ui.pay.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/11/15.
 */

public class PayCategories extends BaseEntity {

    /** 是否隐藏除支付宝支付的其他支付（1表示隐藏，0表示不隐藏），version>=3.4.3有效 */
    public int hideOther;

    public ArrayList<PayCategory> payTypes = new ArrayList<PayCategory>();

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
