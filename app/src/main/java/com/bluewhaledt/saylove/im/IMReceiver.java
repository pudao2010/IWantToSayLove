package com.bluewhaledt.saylove.im;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.constant.BroadcastActions;
import com.bluewhaledt.saylove.event.RefreshInfoEvent;
import com.bluewhaledt.saylove.im.entity.NotificationData;
import com.bluewhaledt.saylove.ui.MainActivity;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

/**
 * 云信IM接收广播处理类
 *
 * @author huliang
 * @date 2016/11/05
 */
public class IMReceiver extends BroadcastReceiver {
    public static final String BROADCAST_PUSH_SETTINGS_UPDATED = "broadcast_push_settings_updated";
    private static final String TAG = IMReceiver.class.getSimpleName();

    private final int MIN_HOUR = 1, MAX_HOUR = 24;

    public static String msgReceiveType;

    public static String DATA = "data";
    public static String mAccount = null;

    private boolean mIsPushSettingsLoaded;
    private boolean mIsNoDisturbEnabled;
    private int mNoDisturbStartHour, mNoDisturbEndHour;

    /**
     * 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
     */
    public static final String MSG_CHATTING_ACCOUNT_ALL = "all";
    /**
     * 目前没有与任何人对话，需要状态栏消息通知
     */
    public static final String MSG_CHATTING_ACCOUNT_NONE = null;

    /**
     * 系统通知管理员账号
     */
    public static final String SYSTEM_NOTIFICATION_ID = "admin@zhenai.com";

    /**
     * 关闭当前用户的消息通知
     *
     * @param account
     */
    public static void closeMessageNoticeToUser(String account) {
        msgReceiveType = MSG_CHATTING_ACCOUNT_NONE;
        mAccount = account;
    }

    /**
     * 关闭所有的消息通知
     */
    public static void closeAllMessageNotice() {
        msgReceiveType = MSG_CHATTING_ACCOUNT_ALL;
        mAccount = null;
    }

    /**
     * 打开所有的消息通知
     */
    public static void openMessageNotice() {
        msgReceiveType = MSG_CHATTING_ACCOUNT_NONE;
        mAccount = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String messageAction = context.getPackageName() + NimIntent.ACTION_RECEIVE_MSG;
        String customNotificationAction = context.getPackageName() + NimIntent.ACTION_RECEIVE_CUSTOM_NOTIFICATION;
        if (customNotificationAction.equals(intent.getAction())) {
            // 从 intent 中取出自定义通知， intent 中只包含了一个 CustomNotification 对象
            CustomNotification notification = (CustomNotification)
                    intent.getSerializableExtra(NimIntent.EXTRA_BROADCAST_MSG);
            // 第三方 APP 在此处理自定义通知：存储，处理，展示给用户等
            Log.i("demo", "receive custom notification: " + notification.getContent()
                    + " from :" + notification.getSessionId() + "/" + notification.getSessionType());
            DebugUtils.d("IMReceiver",new Gson().toJson(notification));

            showNotification(context, SYSTEM_NOTIFICATION_ID, context.getString(R.string.app_name), notification.getApnsText(), notification.getContent());
            refreshPage(context,new Gson().fromJson(notification.getContent(), NotificationData.class));

        } else if (messageAction.equals(intent.getAction())) {
            // 从 intent 中取出收到的消息列表
            ArrayList<IMMessage> imMessages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_BROADCAST_MSG);

            for (IMMessage message : imMessages) {
                DebugUtils.d("IMReceiver",new Gson().toJson(message));
                if (message.getFromAccount().equals(SYSTEM_NOTIFICATION_ID)){//如果是系统通知，则显示通知
//                    showNotificationOfMsg(context, message.getFromAccount(), context.getString(R.string.app_name), message.getPushContent(), message.getContent());
                    continue;
                }

                if (MSG_CHATTING_ACCOUNT_ALL.equals(msgReceiveType)) {//如果当前界面已经设置了排除所有通知，则不显示
                    continue;
                }else if ((mAccount != null && message.getFromAccount().equals(mAccount))
                        || !message.getConfig().enablePush ) {//如果当前界面已经设置了排队某一个人的消息通知，则不显示通知
                    continue;
                }
                showNotificationOfMsg(context, message.getFromAccount(), context.getString(R.string.app_name), message.getPushContent(), "{type:0}");//普通聊天消息没有返回type，此处自己写死这个值，方便点击事件处理
            }
        } else if (BROADCAST_PUSH_SETTINGS_UPDATED.equals(intent.getAction())) {
            // 重新加载push设置
            loadNoDisturbTimeRange();
        }
    }

    /**
     * 刷新页面数据
     * @param data
     */
    private void refreshPage(Context context,NotificationData data) {
        switch (data.type){
            case 1:
                break;
            case 2:
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BroadcastActions.SHOW_SYSTEM_MESSAGE_RED_DOT));
                EventBus.getDefault().post(new RefreshInfoEvent(RefreshInfoEvent.LOAD_VERIFY));
                break;
            case 3:
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BroadcastActions.SHOW_SYSTEM_MESSAGE_RED_DOT));
                EventBus.getDefault().post(new RefreshInfoEvent(RefreshInfoEvent.LOAD_VERIFY));

                break;
            case 4:
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BroadcastActions.SHOW_SYSTEM_MESSAGE_RED_DOT));
                EventBus.getDefault().post(new RefreshInfoEvent(RefreshInfoEvent.LOAD_VERIFY));
                break;
            case 5:
            case 6:
            case 7:
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BroadcastActions.SHOW_SYSTEM_MESSAGE_RED_DOT));
                EventBus.getDefault().post(new RefreshInfoEvent(RefreshInfoEvent.LOAD_BASIC_INFO));
                break;
        }
    }

    private int showNotificationOfMsg(Context context, String sessionId, String title, String msg, String data){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.saylove_small);

        // 构造notifyId
        int notifyId;
        try {
            notifyId = Integer.valueOf(sessionId.split("@")[0]);
        } catch (Exception ex) {
            notifyId = new Random().nextInt();
        }
        Intent clickIntent = new Intent(NimIntent.ACTION_RECEIVE_MSG);
        clickIntent.setClass(context, MainActivity.class);
        clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        clickIntent.putExtra("otherSessionId", sessionId);
        if (!TextUtils.isEmpty(data)){
            clickIntent.putExtra(DATA, new Gson().fromJson(data, NotificationData.class));
        }
        Log.d(TAG, "=======>notification notifyId:" + notifyId + ";intentData:" + data);
        mBuilder.setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(PendingIntent.getActivity(context, notifyId, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT)) //设置通知栏点击意图
                .setTicker("您有新的消息")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(false)
                .setSmallIcon(R.mipmap.saylove_small)
                .setLargeIcon(bitmap)
                .setColor(Color.parseColor("#651c83"));
        mNotificationManager.notify(notifyId, mBuilder.build());

        return notifyId;
    }

    private int showNotification(Context context, String sessionId, String title, String msg, String data) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.saylove_small);

        // 构造notifyId
        int notifyId = new Random().nextInt();
//        try {
//            notifyId = new Random().nextInt();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        Intent clickIntent = new Intent(NimIntent.EXTRA_BROADCAST_MSG);
        clickIntent.setClass(context, MainActivity.class);
        clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        clickIntent.putExtra("otherSessionId", sessionId);
        if (!TextUtils.isEmpty(data)){
            clickIntent.putExtra(DATA, new Gson().fromJson(data, NotificationData.class));
        }

        Log.d(TAG, "=======>notification notifyId:" + notifyId + ";intentData:" + data);
        mBuilder.setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(PendingIntent.getActivity(context, notifyId, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT)) //设置通知栏点击意图
                .setTicker("您有新的消息")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(false)
                .setSmallIcon(R.mipmap.saylove_small)
                .setLargeIcon(bitmap)
                .setColor(Color.parseColor("#651c83"));
        mNotificationManager.notify(notifyId, mBuilder.build());

        return notifyId;

    }

    public static void clearNotification(Context context, String sessionId) {
        int notifyId;
        try {
            notifyId = Integer.valueOf(sessionId.split("@")[0]);
        } catch (Exception ex) {
            return;
        }
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notifyId);
    }

    /**
     * 是否“不”处于勿扰模式
     * @return
     */
//    private boolean isInNoDisturbMode() {
//        if (!mIsPushSettingsLoaded) {
//            loadNoDisturbTimeRange();
//        }
//        return mIsNoDisturbEnabled && isInHoursInterval();
//    }

    private boolean isInHoursInterval() {
        int nowHour = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.HOUR_OF_DAY)
                + 1;
        if (nowHour == 0) {
            nowHour = MAX_HOUR;
        }

        if (mNoDisturbEndHour > mNoDisturbStartHour) {
            // 在同一天
            return nowHour >= mNoDisturbStartHour && nowHour < mNoDisturbEndHour;
        } else {
            // 跨天
            return (nowHour >= mNoDisturbStartHour && nowHour <= MAX_HOUR) ||
                    (nowHour >= MIN_HOUR && nowHour < mNoDisturbEndHour);
        }
    }

    /**
     * 加载push设置
     */
    private void loadNoDisturbTimeRange() {
//        mIsNoDisturbEnabled = PushSettingsPreference.getInstance().getEnabled();
//        mNoDisturbStartHour = PushSettingsPreference.getInstance().getStartHour();
//        mNoDisturbEndHour = PushSettingsPreference.getInstance().getEndHour();
        mIsPushSettingsLoaded = true;
    }
}
