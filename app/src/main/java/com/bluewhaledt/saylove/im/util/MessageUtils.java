package com.bluewhaledt.saylove.im.util;

import android.text.TextUtils;

import com.bluewhaledt.saylove.im.IMUtil;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * 云信im消息帮助类
 */
public class MessageUtils {
    public static String mMySessionId;

    public static Object getLocalExtValue(IMMessage message, String key) {
        Map<String, Object> localExtension = message.getLocalExtension();
        if (localExtension != null) {
            return localExtension.get(key);
        }
        return null;
    }

    public static void setLocalExtValue(IMMessage message, String key, Object value) {
        Map<String, Object> localExtension = message.getLocalExtension();
        if (localExtension == null) {
            localExtension = new HashMap<>();
        }
        localExtension.put(key, value);
        message.setLocalExtension(localExtension);
    }

    /**
     * 是否为对方的消息
     *
     * @param sessionId
     * @return
     */
    public static boolean isObjMessage(String sessionId) {
        if (TextUtils.isEmpty(mMySessionId)) {
            LoginInfo loginInfo = IMUtil.getLastLoginInfo();
            if (loginInfo != null) {
                mMySessionId = loginInfo.getAccount();
            }
            if (mMySessionId == null) {
                mMySessionId = "";
            }
        }
        return !mMySessionId.equals(sessionId);
    }
}
