package com.bluewhaledt.saylove.ui.message.model;

import android.content.Context;

import com.bluewhaledt.saylove.base.util.TimeUtils;
import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.entity.ChatUserList;
import com.bluewhaledt.saylove.im.IMReceiver;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.im.attachment.VideoTopicAttachment;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.message.entity.MessageData;
import com.bluewhaledt.saylove.ui.message.entity.UnreadCount;
import com.bluewhaledt.saylove.ui.message.service.MessageService;
import com.bluewhaledt.saylove.widget.linear_view.IBaseMode;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/11/30.
 */

public class MessageModel implements IBaseMode<MessageData> {

    private ArrayList<MessageData> recentContacts = new ArrayList<>();

    private int pageIndex = 1;

    private int pageSize = 10;

    public MessageModel() {
    }

    @Override
    public void getDataList(Context context, int pageIndex, int pageSize, final BaseSubscriber<ZAResponse<ResultEntity<MessageData>>> subscriber) {
        if (recentContacts.isEmpty()) {
            this.pageIndex = 1;
            getRecentContact(context, subscriber);
        } else {
            this.pageIndex = pageIndex;
            getChatUsersInfo(context, subscriber);
        }
    }

    private void getRecentContact(final Context context, final BaseSubscriber<ZAResponse<ResultEntity<MessageData>>> subscriber) {
        IMUtil.queryRecentContacts(new RequestCallbackWrapper<List<RecentContact>>() {
            @Override
            public void onResult(int code, List<RecentContact> result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && exception == null) {
                    recentContacts.clear();
                    if (result.size() > 0) {
                        for (int i = 0; i < result.size(); i++) {
                            RecentContact contact = result.get(i);
                            if (contact.getFromAccount().equals(IMReceiver.SYSTEM_NOTIFICATION_ID) ||
                                    contact.getContactId().equals(IMReceiver.SYSTEM_NOTIFICATION_ID)) {
                                continue;
                            }
                            MessageData data = new MessageData();
                            if (contact.getMsgType() == MsgTypeEnum.custom && contact.getAttachment() instanceof VideoTopicAttachment) {
                                if (contact.getFromAccount().equals(IMUtil.getLastLoginInfo().getAccount())) {
                                    data.message = "你评论了对方的SayLove小视频";
                                } else {
                                    data.message = "对方评论了你的SayLove小视频";
                                }
                            } else {
                                data.message = contact.getContent();
                            }

                            data.unreadCount = contact.getUnreadCount();
                            data.date = TimeUtils.formatTime(contact.getTime());
                            data.sessionId = contact.getContactId();
                            recentContacts.add(data);
                        }
                        getChatUsersInfo(context, subscriber);
                        return;
                    }
                }
                ZAResponse<ResultEntity<MessageData>> response = new ZAResponse<>();
                response.isError = false;
                response.data = new ResultEntity<>();
                response.data.list = new ZAArray<>();
                subscriber.onNext(response);
                subscriber.onCompleted();
            }
        });
    }

    private void getChatUsersInfo(Context context, final BaseSubscriber<ZAResponse<ResultEntity<MessageData>>> subscriber) {
        final ZAArray<MessageData> subData = new ZAArray<>();
        if (pageIndex == 1) {
            subData.addAll(recentContacts.subList(0, recentContacts.size() > pageSize ? pageSize : recentContacts.size()));
        } else {
            subData.addAll(recentContacts.subList(recentContacts.size(), recentContacts.size() > ((pageIndex + 1) * pageSize) ? ((pageIndex + 1) * pageSize) : recentContacts.size()));
        }
        if (subData.isEmpty()) {
            ZAResponse<ResultEntity<MessageData>> response = new ZAResponse<>();
            response.isError = false;
            response.data = new ResultEntity<>();
            response.data.list = new ZAArray<>();
            subscriber.onNext(response);
            subscriber.onCompleted();
            return;
        }
        String[] accIds = new String[subData.size()];
        for (int i = 0; i < subData.size(); i++) {
            accIds[i] = subData.get(i).sessionId;
        }
        getChatUsersInfo(context, accIds, subData, subscriber);

    }

    public void getChatUsersInfo(Context context, String[] accIds, final ZAArray<MessageData> subData, final BaseSubscriber<ZAResponse<ResultEntity<MessageData>>> subscriber) {
        MessageService service = ZARetrofit.getInstance(context).getRetrofit().create(MessageService.class);
        JSONArray array = new JSONArray();
        for (String accId : accIds) {
            array.put(accId);
        }
        Observable<ZAResponse<ChatUserList>> observable = service.getChatUserInfo(array.toString());
        HttpMethod.toSubscribe(observable, new BaseSubscriber<ZAResponse<ChatUserList>>(new ZASubscriberListener<ZAResponse<ChatUserList>>() {
            @Override
            public void onSuccess(ZAResponse<ChatUserList> response) {
                pageIndex++;
                int index = 0;
                for (ChatUserList.ChatUser user : response.data.list) {
                    if (user != null) {
                        subData.get(index).user = user;
                    } else {
                        subData.remove(index);
                    }

                    index++;
                }
                ZAResponse<ResultEntity<MessageData>> responseNew = new ZAResponse<>();
                responseNew.data = new ResultEntity<>();
                responseNew.data.list = subData;
                responseNew.isError = response.isError;
                responseNew.errorCode = response.errorCode;
                responseNew.errorMessage = response.errorMessage;
                subscriber.onNext(responseNew);
                subscriber.onCompleted();

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                subscriber.onError(e);
            }
        }));
    }

    public void getUnReadCount(Context context, BaseSubscriber<ZAResponse<UnreadCount>> subscriber) {
        MessageService service = ZARetrofit.getInstance(context).getRetrofit().create(MessageService.class);
        Observable<ZAResponse<UnreadCount>> observable = service.getUnReadCount();
        HttpMethod.toSubscribe(observable, subscriber);
    }
}
