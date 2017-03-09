package com.bluewhaledt.saylove.im;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.DensityUtils;
import com.bluewhaledt.saylove.base.util.ObjectStringConverter;
import com.bluewhaledt.saylove.entity.IMAccount;
import com.bluewhaledt.saylove.im.parser.CustomAttachParser;
import com.bluewhaledt.saylove.im.util.MessageUtils;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.model.CommonModel;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by zhenai-liliyan on 16/10/27.
 */

public class IMUtil {

    private static String TAG = "NIMUtil";
    public static boolean isLogin = false;
    public static String nickname = "";

    public static void initIM(Context context) {
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(context, null, options(context));
    }

    public static void login() {
        login("", "", null);
    }

    public static void login(ILoginIMCallback loginCallback) {
        login("", "", loginCallback);
    }

    public static void login(String account, String token) {
        login(account, token, null);
    }

    public static synchronized void login(String account, String token, final ILoginIMCallback loginCallback) {
        if (!isLogin) {
            LoginInfo info = getLastLoginInfo();
            DebugUtils.d(TAG, "getLastLoginInfo:" + new Gson().toJson(info));
            if (info == null || (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token) &&
                    (!account.equals(info.getAccount()) || !token.equals(info.getToken())))) {
                info = new LoginInfo(account, token);
            }
            // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
            RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo param) {
                    Log.d(TAG, "IM login success");
                    try {
                        saveLastLoginInfo(param);

                        // 开启/关闭通知栏消息提醒（内置通知功能）
                        NIMClient.toggleNotification(false);
                        // 注册自定义消息的监听器
                        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
                        IMUtil.openMessageNotice();

                        isLogin = true;
                        if (loginCallback != null) {
                            loginCallback.loginSuccess();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (loginCallback != null) {
                            loginCallback.loginFailed();
                        }
                    }
                }

                @Override
                public void onFailed(int code) {
                    Log.d(TAG, "IM login failed:" + code);
                    if (loginCallback != null) {
                        loginCallback.loginFailed();
                    }
                }

                @Override
                public void onException(Throwable exception) {
                    Log.d(TAG, "IM login failed:" + exception.toString());
                    if (loginCallback != null) {
                        loginCallback.loginFailed();
                    }
                }
            };
            DebugUtils.d(TAG, new Gson().toJson(info));
            NIMClient.getService(AuthService.class)
                    .login(info)
                    .setCallback(callback);
        }
    }

    /**
     * 发送文本消息
     *
     * @param sessionId   聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
     * @param sessionType 聊天类型，单聊或群组
     * @param content     文本内容
     * @param data        扩展字段Map，开发者需要保证此Map能够转换为JsonObject
     * @param config      消息配置
     * @return InvocationFuture 可以设置回调函数。消息发送完成后才会调用，如果出错，会有具体的错误代码。
     */
    public static InvocationFuture<Void> sendTextMessage(String sessionId, SessionTypeEnum sessionType,
                                                         String content, Map<String, Object> data, CustomMessageConfig config) {
        IMMessage message = MessageBuilder.createTextMessage(
                sessionId,
                sessionType,
                content
        );
        if (data != null) {
            message.setRemoteExtension(data);
        }
        if (config != null) {
            message.setConfig(config);
        }
        // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        return NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

    public static IMMessage sendTextMessageReturnIMMessage(String sessionId, SessionTypeEnum sessionType,
                                                           String content, Map<String, Object> data, CustomMessageConfig config) {
        IMMessage message = MessageBuilder.createTextMessage(
                sessionId,
                sessionType,
                content
        );
        if (data != null) {
            message.setRemoteExtension(data);
        }
        if (config != null) {
            message.setConfig(config);
        }

        message.setPushContent("【" + AccountManager.getInstance().getZaAccount().nickName + "】" + "给你发送了一条信息");
        // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        NIMClient.getService(MsgService.class).sendMessage(message, false);
        return message;
    }

    /**
     * 向某个用户发消息
     *
     * @param sessionId 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
     * @param content   文本内容
     * @param data      扩展字段Map，开发者需要保证此Map能够转换为JsonObject
     * @return InvocationFuture 可以设置回调函数。消息发送完成后才会调用，如果出错，会有具体的错误代码。
     */
    public static InvocationFuture<Void> sendTextMessageToUser(String sessionId, String content, Map<String, Object> data) {
        return sendTextMessage(sessionId, SessionTypeEnum.P2P, content, data, null);
    }

    /***
     * 向某个用户发消息
     *
     * @param sessionId 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
     * @param content   文本内容
     * @return InvocationFuture 可以设置回调函数。消息发送完成后才会调用，如果出错，会有具体的错误代码。
     */
    public static InvocationFuture<Void> sendTextMessageToUser(String sessionId, String content) {
        return sendTextMessage(sessionId, SessionTypeEnum.P2P, content, null, null);
    }

    public static IMMessage sendTextMessageToUserReturnIMMessage(String sessionId, String content) {
        return sendTextMessageReturnIMMessage(sessionId, SessionTypeEnum.P2P, content, null, null);
    }

    /**
     * 向某个群发消息
     *
     * @param sessionId 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
     * @param content   文本内容
     * @param data      扩展字段Map，开发者需要保证此Map能够转换为JsonObject
     * @return InvocationFuture 可以设置回调函数。消息发送完成后才会调用，如果出错，会有具体的错误代码。
     */
    public static InvocationFuture<Void> sendTextMessageToChatRoom(String sessionId, String content, Map<String, Object> data) {
        return sendTextMessage(sessionId, SessionTypeEnum.ChatRoom, content, data, null);
    }

    /**
     * 向某个群发消息
     *
     * @param sessionId 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
     * @param content   文本内容
     * @return InvocationFuture 可以设置回调函数。消息发送完成后才会调用，如果出错，会有具体的错误代码。
     */
    public static InvocationFuture<Void> sendTextMessageToChatRoom(String sessionId, String content) {
        return sendTextMessage(sessionId, SessionTypeEnum.ChatRoom, content, null, null);
    }

    /***
     * 发送地理位置消息
     *
     * @param sessionId   聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
     * @param sessionType 聊天类型，单聊或群组
     * @param latitude    纬度
     * @param longitude   经度
     * @param address     地址信息描述
     */
    public static void sendLocationMessage(String sessionId, SessionTypeEnum sessionType, double latitude,
                                           double longitude, String address, Map<String, Object> data, CustomMessageConfig config) {
        IMMessage message = MessageBuilder.createLocationMessage(
                sessionId,
                sessionType,
                latitude,
                longitude,
                address
        );
        message.setRemoteExtension(data);
        message.setConfig(config);
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

    /***
     * 发送图片消息
     *
     * @param sessionId   聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
     * @param sessionType 聊天类型，单聊或群组
     * @param file        图片文件对象
     * @param displayName 文件显示名字，如果第三方 APP 不关注，可以为 null
     */
    public static void sendPhotoMessage(String sessionId, SessionTypeEnum sessionType, File file,
                                        String displayName, Map<String, Object> data, CustomMessageConfig config) {
        IMMessage message = MessageBuilder.createImageMessage(
                sessionId,
                sessionType,
                file,
                displayName
        );
        message.setRemoteExtension(data);
        message.setConfig(config);
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

    public static IMMessage sendAudioMessage(String sessionId, File file, long duration) {
        return sendAudioMessage(sessionId, SessionTypeEnum.P2P, file, duration, null, null);
    }

    /***
     * 发送音频消息
     *
     * @param sessionId   聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
     * @param sessionType 聊天类型，单聊或群组
     * @param file        音频文件
     * @param duration    音频持续时间，单位是ms
     */
    public static IMMessage sendAudioMessage(String sessionId, SessionTypeEnum sessionType, File file,
                                             long duration, Map<String, Object> data, CustomMessageConfig config) {
        IMMessage message = MessageBuilder.createAudioMessage(
                sessionId,
                sessionType,
                file,
                duration
        );
        message.setPushContent("【" + AccountManager.getInstance().getZaAccount().nickName + "】给你发了一段语音");
        message.setContent("给你发了一段语音");
//        message.setRemoteExtension(intentData);
//        message.setConfig(config);
        NIMClient.getService(MsgService.class).sendMessage(message, false);
        return message;
    }

    /***
     * 发送视频消息
     *
     * @param sessionId   聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
     * @param sessionType 聊天类型，单聊或群组
     * @param file        视频文件
     * @param duration    视频持续时间
     * @param width       视频宽度
     * @param height      视频高度
     * @param displayName 视频显示名，可为空
     */
    public static void sendVideoMessage(String sessionId, SessionTypeEnum sessionType,
                                        File file, long duration, int width, int height,
                                        String displayName, Map<String, Object> data, CustomMessageConfig config) {
        IMMessage message = MessageBuilder.createVideoMessage(
                sessionId,
                sessionType,
                file,
                duration,
                width,
                height,
                displayName
        );
        message.setRemoteExtension(data);
        message.setConfig(config);
        NIMClient.getService(MsgService.class).sendMessage(message, false);

    }

    /***
     * 发送提醒消息
     *
     * @param sessionId   聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
     * @param sessionType 聊天类型，单聊或群组
     */
    public static void sendTipMessage(String sessionId, SessionTypeEnum sessionType) {
        IMMessage message = MessageBuilder.createTipMessage(
                sessionId,
                sessionType
        );
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

    /**
     * 自定义推送属性
     *
     * @param sessionId 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
     * @param pushTitle 收到通知时的提示信息
     * @param data      本条推送所带的字段信息
     */
    public static void push(String sessionId, String pushTitle, Map<String, Object> data) {
        CustomMessageConfig config = new CustomMessageConfig();
        config.enablePush = true;
        config.enableUnreadCount = false;
        IMMessage msg = MessageBuilder.createCustomMessage(sessionId, SessionTypeEnum.System, null);
        msg.setPushContent(pushTitle);
        msg.setConfig(config);
        msg.setPushPayload(data);
        NIMClient.getService(MsgService.class).sendMessage(msg, false);
    }

    /**
     * 创建一条APP自定义类型消息, 同时提供描述字段，可用于推送以及状态栏消息提醒的展示。
     *
     * @param sessionId   聊天对象ID
     * @param sessionType 会话类型
     * @param content     消息简要描述，可通过IMMessage#getContent()获取，主要用于用户推送展示。
     * @param attachment  消息附件对象
     * @param config      自定义消息配置
     * @return 自定义消息
     */
    public static void sendCustomMessage(String sessionId, SessionTypeEnum sessionType, String content, MsgAttachment attachment, CustomMessageConfig config) {
        IMMessage message = MessageBuilder.createCustomMessage(sessionId, sessionType, content, attachment, config);
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

    /**
     * 创建一条到指定用户的自定义类型消息, 同时提供描述字段，可用于推送以及状态栏消息提醒的展示。
     *
     * @param sessionId  聊天对象ID
     * @param /isObjVip  聊天对象是否为vip
     * @param content    消息简要描述，可通过IMMessage#getContent()获取，主要用于用户推送展示。
     * @param attachment 消息附件对象
     * @return 自定义消息
     */
    public static IMMessage sendCustomMessageToUser(String sessionId, /*boolean isObjVip,*/ String content, MsgAttachment attachment) {
        IMMessage message = MessageBuilder.createCustomMessage(sessionId, SessionTypeEnum.P2P, content, attachment, null);
//        String pushContent = ChatDataTransformUtil.transformReceivedMsgToTxt(attachment, MessageUtils.mMySessionId, isObjVip, true);
//        pushContent = pushContent.replace("TA", nickname);
        message.setPushContent(content);
        message.setContent("对视频做了评论");
        NIMClient.getService(MsgService.class).sendMessage(message, false);
        return message;
    }

    /**
     * 创建一条到指定用户的自定义类型消息, 同时提供描述字段，可用于推送以及状态栏消息提醒的展示。
     *
     * @param sessionId  聊天对象ID
     * @param 'isObjVip  聊天对象是否为vip
     * @param content    消息简要描述，可通过IMMessage#getContent()获取，主要用于用户推送展示。
     * @param attachment 消息附件对象
     * @param enablePush 该消息是否要消息提醒
     * @return 自定义消息
     */
    public static IMMessage sendCustomMessageToUser(String sessionId, /*boolean isObjVip,*/ String content, MsgAttachment attachment, boolean enablePush) {
        CustomMessageConfig config = new CustomMessageConfig();
        config.enablePush = enablePush;
        IMMessage message = MessageBuilder.createCustomMessage(sessionId, SessionTypeEnum.P2P, content, attachment, null);
//        String pushContent = ChatDataTransformUtil.transformReceivedMsgToTxt(attachment, MessageUtils.mMySessionId, isObjVip, true);
//        pushContent = pushContent.replace("TA", nickname);
//        message.setPushContent(TextUtils.isEmpty(pushContent) ? content : pushContent);
        message.setConfig(config);
        NIMClient.getService(MsgService.class).sendMessage(message, false);
        return message;
    }

    /**
     * 发送message
     *
     * @param message
     */
    public static void sendMessage(IMMessage message) {
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

    /**
     * 重新发送消息
     *
     * @param message
     */
    public static void resendMessage(IMMessage message) {
        NIMClient.getService(MsgService.class).sendMessage(message, true);
    }

    /**
     * 创建一条到指定聊天室的自定义类型消息, 同时提供描述字段，可用于推送以及状态栏消息提醒的展示。
     *
     * @param sessionId  聊天对象ID
     * @param content    消息简要描述，可通过IMMessage#getContent()获取，主要用于用户推送展示。
     * @param attachment 消息附件对象
     * @return 自定义消息
     */
    public static void sendCustomMessageToChatRoom(String sessionId, String content, MsgAttachment attachment) {
        IMMessage message = MessageBuilder.createCustomMessage(sessionId, SessionTypeEnum.ChatRoom, content, attachment, null);
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }


    /**
     * 监听消息发送状态的变化通知
     *
     * @param observer 此observer内有 Observer.onEvent回调方法：参数为有状态发生改变的消息对象，其 msgStatus 和 attachStatus 均为最新状态。
     *                 发送消息和接收消息的状态监听均可以通过此接口完成。
     */
    public static void addMsgStatusObserver(Observer<IMMessage> observer) {
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(observer, true);
    }

    /**
     * 注销消息发送状态的变化通知
     *
     * @param observer 此observer内有 Observer.onEvent回调方法：参数为有状态发生改变的消息对象，其 msgStatus 和 attachStatus 均为最新状态。
     *                 发送消息和接收消息的状态监听均可以通过此接口完成。
     */
    public static void removeMsgStatusObserver(Observer<IMMessage> observer) {
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(observer, false);
    }

    /**
     * 发送的多媒体文件消息时的监听文件的上传/下载进度。
     *
     * @param observer 此observer内有 Observer.onEvent回调方法： 参数为附件的传输进度，可根据 attachmentProgress 中的 uuid 查找具体的消息对象，更新 UI
     *                 上传附件和下载附件的进度监听均可以通过此接口完成。
     */
    public static void addAttachmentProgressObserver(Observer<AttachmentProgress> observer) {
        NIMClient.getService(MsgServiceObserve.class).observeAttachmentProgress(observer, true);
    }

    /**
     * 注销多媒体文件消息时的监听文件的上传进度。
     *
     * @param observer 此observer内有 Observer.onEvent回调方法： 参数为附件的传输进度，可根据 attachmentProgress 中的 uuid 查找具体的消息对象，更新 UI
     *                 上传附件和下载附件的进度监听均可以通过此接口完成。
     */
    public static void removeAttachmentProgressObserver(Observer<AttachmentProgress> observer) {
        NIMClient.getService(MsgServiceObserve.class).observeAttachmentProgress(observer, false);
    }

    /**
     * 接收消息的监听
     *
     * @param incomingMessageObserver
     */
    public static void addIncomingMsgObserve(Observer<List<IMMessage>> incomingMessageObserver) {
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
    }

    /**
     * 注销接收消息的监听
     *
     * @param incomingMessageObserver
     */
    public static void removeIncomingMsgObserve(Observer<List<IMMessage>> incomingMessageObserver) {
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);
    }

    /**
     * 保存消息到本地，保存后在im消息接收的监听中会收到此消息,但不计入未读数中
     *
     * @param msg 消息
     */
    public static void saveMessageToLocal(IMMessage msg) {
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableUnreadCount = false;
        msg.setConfig(config);
        NIMClient.getService(MsgService.class).saveMessageToLocal(msg, true);
    }

    /***
     * 获取最近会话列表(本地数据)
     */
    public static void queryRecentContacts(RequestCallbackWrapper<List<RecentContact>> callback) {
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(callback);
    }

    /**
     * 从最近联系人列表中删除一项。
     * 调用这个接口删除数据后，不会引发观察者通知。
     *
     * @param recent 待删除的最近联系人项
     */
    public static void deleteRecentContact(RecentContact recent) {
        NIMClient.getService(MsgService.class).deleteRecentContact(recent);
    }

    /**
     * 查询消息历史
     *
     * @param anchor    IMMessage 查询锚点
     * @param direction QueryDirectionEnum 查询方向
     * @param limit     int 查询结果的条数限制
     * @return 调用跟踪，可设置回调函数，接收查询结果
     */
    public static void queryMessageListEx(IMMessage anchor, QueryDirectionEnum direction, int limit, RequestCallbackWrapper<List<IMMessage>> callback) {
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, direction, limit, true).setCallback(callback);
    }

    /**
     * 查询消息历史
     *
     * @param anchor    IMMessage 查询锚点
     * @param direction QueryDirectionEnum 查询方向
     * @param limit     int 查询结果的条数限制
     * @param asc       boolean 查询结果的排序规则，如果为 true，结果按照时间升级排列，如果为 false，按照时间降序排列
     * @return 调用跟踪，可设置回调函数，接收查询结果
     */
    public static void queryMessageListEx(IMMessage anchor, QueryDirectionEnum direction, int limit, boolean asc, RequestCallbackWrapper<List<IMMessage>> callback) {
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, direction, limit, asc).setCallback(callback);
    }

    /**
     * 通过消息类型从本地消息数据库中查询消息历史。查询范围由msgTypeEnum参数和anchor的sessionId决定
     * 该接口查询方向为从后往前。以锚点anchor作为起始点（不包含锚点），往前查询最多limit条消息。
     *
     * @param msgTypeEnum 消息类型
     * @param anchor      搜索的消息锚点
     * @param limit       搜索结果的条数限制
     * @return
     */
    public static void queryMessageListByType(MsgTypeEnum msgTypeEnum, IMMessage anchor, int limit, RequestCallbackWrapper<List<IMMessage>> callback) {
        NIMClient.getService(MsgService.class).queryMessageListByType(msgTypeEnum, anchor, limit).setCallback(callback);
    }

    /***
     * 删除单条消息
     *
     * @param message
     */
    public static void deleteChattingHistory(IMMessage message) {
        NIMClient.getService(MsgService.class).deleteChattingHistory(message);
    }

    /**
     * 清除与指定用户的所有消息记录
     *
     * @param account     用户帐号
     * @param sessionType 聊天类型
     */
    public static void clearChattingHistory(String account, SessionTypeEnum sessionType) {
        NIMClient.getService(MsgService.class).clearChattingHistory(account, sessionType);
    }

    /**
     * 监听最近会话变更
     *
     * @param messageObserver
     */
    public static void addRecentContactObserver(Observer<List<RecentContact>> messageObserver) {
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, true);
    }

    /**
     * 注销监听最近会话变更
     *
     * @param messageObserver
     */
    public static void removeRecentContactObserver(Observer<List<RecentContact>> messageObserver) {
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, false);
    }

    /**
     * 获取会话未读数总数
     *
     * @return 未读总数
     */
    public static int getTotalUnreadCount() {
        return NIMClient.getService(MsgService.class).getTotalUnreadCount();
    }

    /**
     * 发送消息已读回执 在会话界面中调用发送已读回执的接口并传入最后一条消息，即表示这之前的消息都已读，对端将收到此回执。
     *
     * @param sessionId 会话ID（聊天对象账号）
     * @param message   已读的消息(一般是当前接收的最后一条消息）
     */
    public static void sendMessageReceipt(String sessionId, IMMessage message) {
        NIMClient.getService(MsgService.class).sendMessageReceipt(sessionId, message);
    }

    /**
     * 注册监听已读回执
     *
     * @param messageReceiptObserver 已读消息的观察者
     */
    public static void addObserveMessageReceipt(Observer<List<MessageReceipt>> messageReceiptObserver) {
        NIMClient.getService(MsgServiceObserve.class).observeMessageReceipt(messageReceiptObserver, true);
    }

    /**
     * 注销监听已读回执
     *
     * @param messageReceiptObserver 已读消息的观察者
     */
    public static void removeObserveMessageReceipt(Observer<List<MessageReceipt>> messageReceiptObserver) {
        NIMClient.getService(MsgServiceObserve.class).observeMessageReceipt(messageReceiptObserver, false);
    }

    /**
     * 清空本地所有消息记录
     */
    public static void clearMsgDatabase() {
        NIMClient.getService(MsgService.class).clearMsgDatabase(true);
    }

    public static void clearChattingHistory(String account) {
        NIMClient.getService(MsgService.class).clearChattingHistory(account, SessionTypeEnum.P2P);
    }

    /**
     * 监听用户在线状态
     */
    public static void addUserIMAuthObserver() {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode>() {
                    public void onEvent(StatusCode status) {
                        Log.i("tag", "ChatUser status changed to: " + status);
                        if (status.wontAutoLogin()) {
                            // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
//                            Intent intent = new Intent(context, LoginActivity.class);
//                            context.startActivity(intent);
                        } else if (status.shouldReLogin()) {
                            LoginInfo loginInfo = getLastLoginInfo();
                            login(loginInfo.getAccount(), loginInfo.getToken());
                        }
                    }
                }, true);
    }


    /***
     * 数据同步状态通知
     */
    public static void addUserLoginSynData() {
        NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(new Observer<LoginSyncStatus>() {
            @Override
            public void onEvent(LoginSyncStatus status) {
                if (status == LoginSyncStatus.BEGIN_SYNC) {
                    Log.i(TAG, "login sync data begin");
                } else if (status == LoginSyncStatus.SYNC_COMPLETED) {
                    Log.i(TAG, "login sync data completed");
                }
            }
        }, true);
    }

    public static void logout() {
        clear();
        NIMClient.getService(AuthService.class).logout();

    }

    public static void clear() {
        isLogin = false;
        MessageUtils.mMySessionId = null;
        nickname = "";
        clearLastLoginInfo();
    }

    /**
     * 切换用户的离线数据
     *
     * @param account
     */
    public static void openUserOfflineData(String account) {
        NIMClient.getService(AuthService.class).openLocalCache(account);
    }

    /**
     * 更新消息。目前只能更新本地扩展字段LocalExtension
     *
     * @param message 待更新的消息记录
     */
    public static void updateIMMessage(IMMessage message) {
        NIMClient.getService(MsgService.class).updateIMMessage(message);
    }

    /**
     * 关闭当个用户的消息通知
     *
     * @param account
     */
    public static void closeMessageNoticeToUser(String account) {
        // 进入聊天界面，建议放在onResume中
        NIMClient.getService(MsgService.class).setChattingAccount(account, SessionTypeEnum.P2P);
        IMReceiver.closeMessageNoticeToUser(account);
    }

    /**
     * 关闭所有的消息通知
     */
    public static void closeAllMessageNotice() {
        // 进入最近联系人列表界面，建议放在onResume中
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        IMReceiver.closeAllMessageNotice();
    }

    /**
     * 打开所有的消息通知
     */
    public static void openMessageNotice() {
        // 退出聊天界面或离开最近联系人列表界面，建议放在onPause中
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        IMReceiver.openMessageNotice();
    }


    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    public static LoginInfo getLastLoginInfo() {
        try {
            String loginInfo = PreferenceUtil.getString(PreferenceFileNames.APP_BUSINESS_CONFIG, PreferenceKeys.IM_LOGIN_INFO, "");
            if (TextUtils.isEmpty(loginInfo)) {
                return null;
            }
            Object object = ObjectStringConverter.stringToObject(loginInfo);
            if (object != null) {
                return (LoginInfo) object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveLastLoginInfo(LoginInfo param) {
        String loginInfoStr = ObjectStringConverter.objectToString(param);
        PreferenceUtil.saveValue(PreferenceFileNames.APP_BUSINESS_CONFIG, PreferenceKeys.IM_LOGIN_INFO, loginInfoStr);
    }

    public static void clearLastLoginInfo() {
        PreferenceUtil.saveValue(PreferenceFileNames.APP_BUSINESS_CONFIG, PreferenceKeys.IM_LOGIN_INFO, "");
    }


    // 如果返回值为 null，则全部使用默认参数。
    private static SDKOptions options(Context context) {
        SDKOptions options = new SDKOptions();
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = DensityUtils.getScreenWidth(context) / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.mipmap.saylove_icon;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
    }

    public static void loginIMByGetAccount() {
        CommonModel.getIMAccount(new BaseSubscriber<ZAResponse<IMAccount>>(new ZASubscriberListener<ZAResponse<IMAccount>>() {
            @Override
            public void onSuccess(ZAResponse<IMAccount> response) {
                if (response.data != null) {
                    IMAccount account = response.data;
                    IMUtil.login(account.imAccount, account.token);
                }
            }
        }));
    }
}
