package com.bluewhaledt.saylove.ui.system.presenter;

import com.bluewhaledt.saylove.im.IMReceiver;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.ui.message.entity.ChatData;
import com.bluewhaledt.saylove.ui.message.view.IChatDetailViewAction;
import com.bluewhaledt.saylove.ui.system.model.SystemModel;
import com.bluewhaledt.saylove.widget.linear_view.IBaseMode;
import com.bluewhaledt.saylove.widget.linear_view.LinearBasePresenter;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenai-liliyan on 16/12/1.
 */

public class SystemPresenter extends LinearBasePresenter<ChatData> {

    private IChatDetailViewAction<ChatData> mActionView;

    public SystemPresenter(IChatDetailViewAction<ChatData> actionView) {
        super(actionView);
        mActionView = actionView;
        IMUtil.addIncomingMsgObserve(incomingMessageObserver);
        IMUtil.addMsgStatusObserver(messageStatusObserver);
    }

    @Override
    public IBaseMode<ChatData> createModel() {
        return new SystemModel();
    }

    public void destroy() {
        IMUtil.removeIncomingMsgObserve(incomingMessageObserver);
        IMUtil.removeMsgStatusObserver(messageStatusObserver);
    }


    private Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            // 1、根据sessionId判断是否是自己的消息
            // 2、更改内存中消息的状态
            // 3、刷新界面
            if (imMessages == null || imMessages.isEmpty()) {
                return;
            }

            ArrayList<ChatData> chats = new ArrayList<>();
            for (IMMessage message : imMessages) {
                if (message == null) {
                    continue;
                }
                if (message.getFromAccount().equals(IMReceiver.SYSTEM_NOTIFICATION_ID) &&
                        message.getMsgType() != MsgTypeEnum.notification) {
                    ChatData chat = new ChatData();
                    chat.message = message;
                    chats.add(chat);
                }
            }
            if (!chats.isEmpty()) {
                mActionView.onReceiveData(chats);
            }

        }
    };

    /**
     * 发送消息状态监听
     */
    private Observer<IMMessage> messageStatusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            if (isMyMessage(message)) {
                ChatData chat = new ChatData();
                chat.message = message;
                mActionView.onReceiptData(chat);
            }
        }
    };

    public boolean isMyMessage(IMMessage message) {
        if (message.getSessionType() == SessionTypeEnum.P2P) {
            if (message.getSessionId() == null) {
                return false;
            }
            if (message.getSessionId().equals(IMReceiver.SYSTEM_NOTIFICATION_ID) || message.getSessionId().equals(IMUtil.getLastLoginInfo().getAccount())) {
                return true;
            }
        }
        return false;

    }

}
