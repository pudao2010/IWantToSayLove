package com.bluewhaledt.saylove.ui.message.view;

import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.bluewhaledt.saylove.ui.message.entity.MessageData;
import com.bluewhaledt.saylove.ui.message.entity.UnreadCount;
import com.bluewhaledt.saylove.widget.linear_view.ILinearBaseView;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/11/30.
 */

public interface MessageViewAction<E extends BaseEntity> extends ILinearBaseView<E> {

//    void getRecentContact(List<RecentContact> recentContacts);

    void onReceiveContact(ArrayList<MessageData> recentContacts);

    void setRedDot(UnreadCount count);
}
