package com.bluewhaledt.saylove.ui.message.presenter;

import android.content.Context;

import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.TimeUtils;
import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.im.IMReceiver;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.im.attachment.VideoTopicAttachment;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.message.adapter.MsgAdapter;
import com.bluewhaledt.saylove.ui.message.entity.MessageData;
import com.bluewhaledt.saylove.ui.message.entity.UnreadCount;
import com.bluewhaledt.saylove.ui.message.model.MessageModel;
import com.bluewhaledt.saylove.ui.message.view.MessageViewAction;
import com.bluewhaledt.saylove.widget.linear_view.IBaseMode;
import com.bluewhaledt.saylove.widget.linear_view.LinearBasePresenter;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

/**
 * Created by zhenai-liliyan on 16/11/28.
 */

public class MessagePresenter extends LinearBasePresenter<MessageData> {

    private String TAG = this.getClass().getSimpleName();

    private MessageViewAction<MessageData> messageViewAction;
    private MessageModel model;

    private MsgAdapter msgAdapter;


    public MessagePresenter(MessageViewAction<MessageData> action) {
        super(action);
        messageViewAction = action;
        // 添加联系人改变的监听
        IMUtil.addRecentContactObserver(recentContactObserver);
    }

    @Override
    public IBaseMode<MessageData> createModel() {
        model = new MessageModel();
        return model;
    }

    public void setDataAdapter(MsgAdapter adapter){
        msgAdapter = adapter;
    }

    private Observer<List<RecentContact>> recentContactObserver = new Observer<List<RecentContact>>() {

        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            ZAArray<MessageData> messages = new ZAArray<>();
            String[] accIds = new String[recentContacts.size()];
            boolean needRequest = false;
            int i = 0;
            for (RecentContact contact : recentContacts) {
                MessageData data = new MessageData();
                data.sessionId = contact.getContactId();
                if (data.sessionId.equals(IMReceiver.SYSTEM_NOTIFICATION_ID)){
                    continue;
                }
//                int index = messages.indexOf(data);
                MessageData exitData = msgAdapter.isContains(data);
                if ( exitData != null){
                    data = exitData;
                }else{//此处判断之前是否已经加载过该用户的信息，如果加载过就不需要加载
                    needRequest = true;
                    accIds[i] = data.sessionId;
                }

                if (contact.getMsgType() == MsgTypeEnum.custom && contact.getAttachment() instanceof VideoTopicAttachment) {
                    if (contact.getFromAccount().equals(IMUtil.getLastLoginInfo().getAccount())) {
                        data.message = "你评论了对方的SayLove小视频";
                    } else {
                        data.message = "对方评论了你的SayLove小视频";
                    }
                } else {
                    data.message = contact.getContent();
                }
//                data.message = contact.getContent();
                data.unreadCount = contact.getUnreadCount();
                data.date = TimeUtils.getShowTime(contact.getTime());
                messages.add(data);

//                if (index > 0) {//此处是判断是否收到同一个人发过来的消息，如果有，则更新之前的数据
//                    MessageData exitData = messages.get(index);
//                    exitData.message = contact.getContent();
//                    exitData.unreadCount = contact.getUnreadCount();
//                    exitData.date = TimeUtils.getShowTime(contact.getTime());
//                } else {
//                    data.message = contact.getContent();
//                    data.unreadCount = contact.getUnreadCount();
//                    data.date = TimeUtils.getShowTime(contact.getTime());
//                    messages.add(data);
//                }
//                //此处判断之前是否已经加载过该用户的信息，如果加载过就不需要加载
//                if (!msgAdapter.isContains(data)){
//                    needRequest = true;
//                    accIds[i] = data.sessionId;
//                }

            }

            if (needRequest){
                getChatUsersInfo(ZhenaiApplication.getContext(), accIds, messages, new BaseSubscriber<ZAResponse<ResultEntity<MessageData>>>(new ZASubscriberListener<ZAResponse<ResultEntity<MessageData>>>() {
                    @Override
                    public void onSuccess(ZAResponse<ResultEntity<MessageData>> response) {
                        messageViewAction.onReceiveContact(response.data.list);
                    }
                }));
            }else{
                messageViewAction.onReceiveContact(messages);
            }

        }
    };

    public void destroy() {
        if (recentContactObserver != null) {
            IMUtil.removeRecentContactObserver(recentContactObserver);
        }
    }

    public void getChatUsersInfo(Context context, String[] accIds, final ZAArray<MessageData> subData, final BaseSubscriber<ZAResponse<ResultEntity<MessageData>>> subscriber) {
        model.getChatUsersInfo(context, accIds, subData, subscriber);
    }

    public void getUnReadCount(Context context){
        model.getUnReadCount(context,new BaseSubscriber<ZAResponse<UnreadCount>>(new ZASubscriberListener<ZAResponse<UnreadCount>>() {
            @Override
            public void onSuccess(ZAResponse<UnreadCount> response) {
                messageViewAction.setRedDot(response.data);
            }
        }));
    }


}
