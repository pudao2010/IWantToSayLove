package com.bluewhaledt.saylove.ui.system.model;

import android.content.Context;

import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.im.IMReceiver;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.message.entity.ChatData;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.account.ZAAccount;
import com.bluewhaledt.saylove.widget.linear_view.IBaseMode;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import java.util.List;

/**
 * Created by zhenai-liliyan on 16/12/1.
 */

public class SystemModel implements IBaseMode<ChatData> {

    private IMMessage mAnchor;
    private ZAAccount account = AccountManager.getInstance().getZaAccount();

    @Override
    public void getDataList(Context context, int pageIndex, int pageSize, final BaseSubscriber<ZAResponse<ResultEntity<ChatData>>> subscriber) {
        IMUtil.queryMessageListEx(anchor(), QueryDirectionEnum.QUERY_OLD, 10, new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && exception == null) {
                    if (result.isEmpty()) {
                        ZAResponse<ResultEntity<ChatData>> response = new ZAResponse<>();
                        response.isError = false;
                        response.data = new ResultEntity<>();
                        response.data.list = new ZAArray<>();
                        subscriber.onNext(response);
                        subscriber.onCompleted();
                        return;
                    }
                    ZAArray<ChatData> chats = new ZAArray<>();
                    for (IMMessage message : result) {
                        ChatData chat = new ChatData();
                        chat.message = message;
                        if (message.getDirect() == MsgDirectionEnum.Out) {
                            chat.avatar = account.avator;
                        }

                        chats.add(chat);
                    }
                    mAnchor = result.get(0);
                    ZAResponse<ResultEntity<ChatData>> response = new ZAResponse<>();
                    response.isError = false;
                    response.data = new ResultEntity<>();
                    response.data.list = chats;
                    subscriber.onNext(response);
                    subscriber.onCompleted();

                } else {
                    subscriber.onError(exception);
                    subscriber.onCompleted();
                }
            }
        });
    }

    private IMMessage anchor() {
        if (mAnchor == null) {
            return MessageBuilder.createEmptyMessage(IMReceiver.SYSTEM_NOTIFICATION_ID, SessionTypeEnum.P2P, 0);
        } else {
            return mAnchor;
        }

    }
}
