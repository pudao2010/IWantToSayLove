package com.bluewhaledt.saylove.ui.message.view;

import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.bluewhaledt.saylove.ui.message.entity.ChatData;
import com.bluewhaledt.saylove.ui.message.entity.FreeChatMsg;
import com.bluewhaledt.saylove.widget.linear_view.ILinearBaseView;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/1.
 */

public interface IChatDetailViewAction<E extends BaseEntity> extends ILinearBaseView<E> {

    void onReceiveData(ArrayList<ChatData> imMessages);
    void onReceiptData(ChatData imMessages);
    void onAttachmentProgressObserver(String uuid, float progress);
    void showFreeMsgLayout(FreeChatMsg msg);
}
