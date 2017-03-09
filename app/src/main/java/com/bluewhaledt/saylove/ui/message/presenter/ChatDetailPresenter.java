package com.bluewhaledt.saylove.ui.message.presenter;

import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.HttpMethod;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.network.retrofit.ZARetrofit;
import com.bluewhaledt.saylove.ui.message.entity.ChatData;
import com.bluewhaledt.saylove.ui.message.entity.ExtendedData;
import com.bluewhaledt.saylove.ui.message.entity.FreeChatMsg;
import com.bluewhaledt.saylove.ui.message.model.ChatDetailModel;
import com.bluewhaledt.saylove.ui.message.service.MessageService;
import com.bluewhaledt.saylove.ui.message.view.IChatDetailViewAction;
import com.bluewhaledt.saylove.widget.linear_view.IBaseMode;
import com.bluewhaledt.saylove.widget.linear_view.LinearBasePresenter;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenai-liliyan on 16/12/1.
 */

public class ChatDetailPresenter extends LinearBasePresenter<ChatData> {

    private IChatDetailViewAction<ChatData> mActionView;

    private ChatDetailModel mModel;

    private ExtendedData mOtherSideInfo;

    public ChatDetailPresenter(IChatDetailViewAction<ChatData> actionView, ExtendedData otherInfo) {
        super(actionView);
        mActionView = actionView;
        mOtherSideInfo = otherInfo;
        mModel.setOtherSideInfo(mOtherSideInfo);

        IMUtil.addIncomingMsgObserve(incomingMessageObserver);
        IMUtil.addMsgStatusObserver(messageStatusObserver);
        IMUtil.addAttachmentProgressObserver(attachmentProgressObserver);
    }

    @Override
    public IBaseMode<ChatData> createModel() {
        mModel = new ChatDetailModel();
        return mModel;
    }

    public void destroy() {
        IMUtil.removeIncomingMsgObserve(incomingMessageObserver);
        IMUtil.removeMsgStatusObserver(messageStatusObserver);
        IMUtil.removeAttachmentProgressObserver(attachmentProgressObserver);
    }

    private Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
        @Override
        public void onEvent(AttachmentProgress progress) {
            float value = (float) progress.getTransferred() / (float) progress.getTotal();
            mActionView.onAttachmentProgressObserver(progress.getUuid(),value);
            DebugUtils.d("progress","=======>progress:" + progress.getTransferred() + ";total:" + progress.getTotal());
        }
    };

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
                if (message.getFromAccount().equals(mOtherSideInfo.otherSessionId) &&
                        message.getMsgType() != MsgTypeEnum.notification ){
                    ChatData chat = new ChatData();
                    chat.message = message;
                    chat.avatar = mOtherSideInfo.otherAvatar;
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
            if (isMyMessage(message)){
                ChatData chat = new ChatData();
                chat.message = message;
                mActionView.onReceiptData(chat);
            }
        }
    };

    public boolean isMyMessage(IMMessage message) {
        if (message.getSessionType() == SessionTypeEnum.P2P){
            if (message.getSessionId() == null){
                return false;
            }
            if (message.getSessionId().equals(mOtherSideInfo.otherSessionId) || message.getSessionId().equals(IMUtil.getLastLoginInfo().getAccount()) ){
                return true;
            }
        }
        return false;

    }

    public void getFreeChatStatus(){
        MessageService service = ZARetrofit.getInstance(ZhenaiApplication.getContext()).getRetrofit().create(MessageService.class);
        HttpMethod.toSubscribe(service.getFreeChatStatus(),new BaseSubscriber<ZAResponse<FreeChatMsg>>(new ZASubscriberListener<ZAResponse<FreeChatMsg>>() {
            @Override
            public void onSuccess(ZAResponse<FreeChatMsg> response) {
                mActionView.showFreeMsgLayout(response.data);
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {

            }
        }));
    }
}
