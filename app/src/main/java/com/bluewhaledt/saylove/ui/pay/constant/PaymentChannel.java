package com.bluewhaledt.saylove.ui.pay.constant;

/**
 * Created by zhenai-liliyan on 16/12/7.
 */

public class PaymentChannel {

    public static final String PAYMENT_CHANNEL_KEY = "payment_channel_key";

    public static final int DEFAULT = 1000;

    /**
     * 心动拦截
     */
    public static final int FROM_HEART_BEAT = 1001;

    /**
     * 聊天拦截
     */
    public static final int FROM_CHAT_PAGE_SEND_MSG = 1002;

    /**
     * 消息页看信拦截
     */
    public static final int FROM_MESSAGE_PAGE_READ_MSG = 1003;

    /**
     * 谁心动我拦截
     */
    public static final int FROM_HEART_BEAT_TO_ME_PAGE = 1004;

    /**
     * 谁看过我拦截
     */
    public static final int FROM_VISIT_TO_ME_PAGE = 1005;

}
