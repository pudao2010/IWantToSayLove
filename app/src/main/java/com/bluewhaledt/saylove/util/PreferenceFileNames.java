package com.bluewhaledt.saylove.util;

/**
 * Created by zhenai-liliyan on 16/11/19.
 */

public enum PreferenceFileNames {
    APP_CONFIG("app_config"),
    USER_CONFIG("user_config"),
    APP_BUSINESS_CONFIG("app_business_config");


    private String value;

    PreferenceFileNames(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PreferenceFileNames typeOfValue(String value) {
        for (PreferenceFileNames e : values()) {
            if (value.equals(e.getValue())) {
                return e;
            }
        }
        return APP_CONFIG;
    }

}
