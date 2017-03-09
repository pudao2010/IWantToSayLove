package com.bluewhaledt.saylove.base.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 发短信、打电话工具类
 */

public class PhoneUtils {

    /**
     * 发短信
     *
     * @param context Context
     * @param content 短信内容
     */
    public static void sendSms(Context context, String content) {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }

    /**
     * 打电话
     *
     * @param context     Context
     * @param phoneNumber 电话号码
     * @return 是否打电话成功（能否打开拨号界面）
     */
    public static boolean call(Context context, String phoneNumber) throws SecurityException {
        if (PermissionHelper.hasPermissions(context, Manifest.permission.CALL_PHONE)) {
            Uri uri = Uri.parse("tel:" + phoneNumber);
            context.startActivity(new Intent(Intent.ACTION_CALL, uri));
            return true;
        }
        return false;
    }
}
