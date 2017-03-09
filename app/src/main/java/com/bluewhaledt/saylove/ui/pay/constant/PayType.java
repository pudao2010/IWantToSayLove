package com.bluewhaledt.saylove.ui.pay.constant;

/**
 * Created by zhenai-liliyan on 16/11/15.
 */

public enum PayType {
    AliPay(1),AliWapPay(2),WeChatPay(7),UnionPay(4),EasyPay(5);
    private int value;
    PayType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PayType typeOfValue(int value) {
        for (PayType e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }
        return AliPay;
    }
}
