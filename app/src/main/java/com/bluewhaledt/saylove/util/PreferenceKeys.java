package com.bluewhaledt.saylove.util;


/**
 * Created by zhenai-liliyan on 16/11/19.
 */

public enum PreferenceKeys {

    APP_DEFAULT("app_default"),

    IM_LOGIN_INFO("im_login_info"),

    LAST_PHONE("last_phone"),

    LAST_PWD("last_pwd"),

    AUTO_LOGIN("auto_login"),

    GENDER("gender"),

    WORK_CITY("work_city"),

    AGE("age"),

    HEIGHT("height"),

    MARITAL("marital"),

    SALARY("salary"),

    REGISTED_PHONE("registed_phone"),

    USER_INFO("user_info"),

    EXTENDED_FREE_TIME_FIRST("extended_free_time_first"),

    SHOW_FREE_CHAT_TIPS("show_free_chat_tips"),

    IS_HAS_CHANCE_AUTHENTICATE("is_has_chance_authenticate"),

    TOURIST_SEX("tourist_sex"),

    OPEN_RECORD_AUDIO_PERMISSION("open_record_audio_permission"),

    VIDEO_IS_FIRST_GUIDE("video_is_first_guide");

    private String value;

    PreferenceKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PreferenceKeys typeOfValue(String value) {
        for (PreferenceKeys e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }
        return APP_DEFAULT;
    }
}
