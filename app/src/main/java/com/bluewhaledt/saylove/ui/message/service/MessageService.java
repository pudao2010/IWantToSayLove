package com.bluewhaledt.saylove.ui.message.service;

import com.bluewhaledt.saylove.constant.Url;
import com.bluewhaledt.saylove.entity.ChatUserList;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.message.entity.FreeChatMsg;
import com.bluewhaledt.saylove.ui.message.entity.UnreadCount;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhenai-liliyan on 16/12/5.
 */

public interface MessageService {

    @FormUrlEncoded
    @POST(Url.GET_CHAT_USER_INFO)
    Observable<ZAResponse<ChatUserList>> getChatUserInfo(@Field("accIds")String accIds);

    @POST(Url.GET_MESSAGE_LIST_UN_READ_COUNT)
    Observable<ZAResponse<UnreadCount>> getUnReadCount();

    @POST(Url.GET_FREE_COUNT)
    Observable<ZAResponse<FreeChatMsg>> getFreeChatStatus();
}
