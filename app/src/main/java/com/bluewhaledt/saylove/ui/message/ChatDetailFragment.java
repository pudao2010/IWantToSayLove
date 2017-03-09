package com.bluewhaledt.saylove.ui.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseFragment;
import com.bluewhaledt.saylove.base.util.DensityUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.widget.ObservableRelativeLayout;
import com.bluewhaledt.saylove.constant.BroadcastActions;
import com.bluewhaledt.saylove.constant.PageIndex;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.entity.CheckChatAvailable;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.im.attachment.VideoTopicAttachment;
import com.bluewhaledt.saylove.imageloader.ImageLoaderFactory;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.model.CommonModel;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.message.adapter.ChatAdapter;
import com.bluewhaledt.saylove.ui.message.entity.ChatData;
import com.bluewhaledt.saylove.ui.message.entity.ExtendedData;
import com.bluewhaledt.saylove.ui.message.entity.FreeChatMsg;
import com.bluewhaledt.saylove.ui.message.presenter.ChatDetailPresenter;
import com.bluewhaledt.saylove.ui.message.view.IChatDetailViewAction;
import com.bluewhaledt.saylove.ui.message.view.InputPanel;
import com.bluewhaledt.saylove.ui.pay.PayActivity;
import com.bluewhaledt.saylove.ui.pay.constant.PaymentChannel;
import com.bluewhaledt.saylove.ui.recommend.widget.SwipeRecyclerView;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.account.ZAAccount;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;
import com.bluewhaledt.saylove.widget.linear_view.LinearSwipeRecyclerView;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhenai-liliyan on 16/11/29.
 */

public class ChatDetailFragment extends BaseFragment implements View.OnClickListener, IChatDetailViewAction<ChatData>/*, IEmoticonSelectedListener*/ {

    private LinearSwipeRecyclerView mRefreshView;

    private ChatAdapter mAdapter;

    private View videoTipsLL;

    private ObservableRelativeLayout keyboardObservableView;

    private LinearLayoutManager layoutManager;

    private ExtendedData extendedData;

    private ChatDetailPresenter mPresenter;

    private boolean isFirstPage = true;

    private boolean isFirstSendMsg = true;

    private CanChatStatus canChat = CanChatStatus.NORMAL;

    public static String EXTENDED_DATA = "extendedData";

    private InputPanel inputPanel;

    private ZAAccount account;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        extendedData = (ExtendedData) getArguments().getSerializable(EXTENDED_DATA);
        initView();
        initDate();


    }

    private void initDate() {
        account = AccountManager.getInstance().getZaAccount();
        if (!account.isVip) {
            if (extendedData.canChat) {
                canChat = CanChatStatus.CAN;
            }
            mPresenter.getFreeChatStatus();
        }
        mRefreshView.setRefreshing(true);


        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(receiver, new IntentFilter(BroadcastActions.PAY_VIP_SUCCESS));
    }

    private void initView() {
        setTitle(extendedData.nikeName);
        inputPanel = (InputPanel) getView().findViewById(R.id.ip_input_panel);
        inputPanel.setActionCallback(new InputPanel.IActionCallback() {
            @Override
            public void sendBtnClick(final String content) {
                if (account.isVip || canChat == CanChatStatus.CAN) {
                    IMMessage sendMsg;
                    if (extendedData.isFromVideoPage && videoTipsLL.getVisibility() == View.VISIBLE) {
                        extendedData.isFromVideoPage = false;//此处发送只发一条
                        VideoTopicAttachment attachment = new VideoTopicAttachment();
                        VideoTopicAttachment.Content attachmentContent = new VideoTopicAttachment.Content();
                        attachmentContent.content = content;
                        attachmentContent.pic = extendedData.videoPic;
                        attachmentContent.title = extendedData.videoMessage;
                        attachmentContent.videoId = extendedData.videoId;
                        attachment.content = attachmentContent;
                        sendMsg = IMUtil.sendCustomMessageToUser(extendedData.otherSessionId, content, attachment);
                        videoTipsLL.setVisibility(View.GONE);
                    } else {
                        sendMsg = IMUtil.sendTextMessageToUserReturnIMMessage(extendedData.otherSessionId, content);
                    }
                    if (sendMsg != null) {
                        ChatData chat = new ChatData();
                        chat.message = sendMsg;
                        chat.avatar = account.avator;
                        mAdapter.addData(chat);
                    }

                    scrollToBottom();
                } else {

                    if (isFirstSendMsg) {//如果是第一次点击发送，则调用接口判断用户是否还有免费聊天次数
                        isFirstSendMsg = false;
                        CommonModel.checkCanFreeChat(extendedData.otherMemberId, new BaseSubscriber<ZAResponse<CheckChatAvailable>>(new ZASubscriberListener<ZAResponse<CheckChatAvailable>>() {
                            @Override
                            public void onSuccess(ZAResponse<CheckChatAvailable> response) {
                                if (response.data.canChat) {
                                    canChat = CanChatStatus.CAN;
                                    IMMessage sendMsg;
                                    if (extendedData.isFromVideoPage && videoTipsLL.getVisibility() == View.VISIBLE) {
                                        extendedData.isFromVideoPage = false;//此处发送只发一条
                                        VideoTopicAttachment attachment = new VideoTopicAttachment();
                                        VideoTopicAttachment.Content attachmentContent = new VideoTopicAttachment.Content();
                                        attachmentContent.content = content;
                                        attachmentContent.pic = extendedData.videoPic;
                                        attachmentContent.title = extendedData.videoMessage;
                                        attachmentContent.videoId = extendedData.videoId;
                                        attachment.content = attachmentContent;
                                        sendMsg = IMUtil.sendCustomMessageToUser(extendedData.otherSessionId, content, attachment);
                                        videoTipsLL.setVisibility(View.GONE);
                                    } else {
                                        sendMsg = IMUtil.sendTextMessageToUserReturnIMMessage(extendedData.otherSessionId, content);
                                    }
                                    if (sendMsg != null) {
                                        ChatData chat = new ChatData();
                                        chat.message = sendMsg;
                                        chat.avatar = account.avator;
                                        mAdapter.addData(chat);
                                    }
                                    scrollToBottom();
                                } else {
                                    canChat = CanChatStatus.CAN_NOT;
                                    if (response.data.hasExpired) {
                                        canChat = CanChatStatus.HAS_EXPIRED;
                                        EventStatistics.recordLog(ResourceKey.CHAT_PAGE, ResourceKey.ChatPage.PURCHASE_VIP_DIALOG_SHOW_BY_CLICK_SEND_BTN);
                                        DialogUtil.showExtended3FreeTimeChatPurchaseVipDialog(getActivity(), PageIndex.CHAT_PAGE);
                                    } else {
                                        EventStatistics.recordLog(ResourceKey.CHAT_PAGE, ResourceKey.ChatPage.PURCHASE_VIP_DIALOG_SHOW_BY_CLICK_SEND_BTN);
                                        DialogUtil.showPurchaseVipDialog(getActivity(),
                                                R.string.fragment_chat_detail_dialog_purchase_title, R.string.fragment_chat_detail_dialog_purchase_tips, PageIndex.CHAT_PAGE);

                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                isFirstSendMsg = true;
                                ToastUtils.toast(getActivity(), R.string.no_network_connected);
                            }

                            @Override
                            public void onFail(String errorCode, String errorMsg) {
                                super.onFail(errorCode, errorMsg);
                                isFirstSendMsg = true;
                                ToastUtils.toast(getActivity(), R.string.no_network_connected);
                            }
                        }));
                    } else if (canChat == CanChatStatus.CAN_NOT) {
                        EventStatistics.recordLog(ResourceKey.CHAT_PAGE, ResourceKey.ChatPage.PURCHASE_VIP_DIALOG_SHOW_BY_CLICK_SEND_BTN);
                        DialogUtil.showPurchaseVipDialog(getActivity(), R.string.fragment_chat_detail_dialog_purchase_title, R.string.fragment_chat_detail_dialog_purchase_tips, PageIndex.CHAT_PAGE);
                    } else if (canChat == CanChatStatus.HAS_EXPIRED) {
                        EventStatistics.recordLog(ResourceKey.CHAT_PAGE, ResourceKey.ChatPage.PURCHASE_VIP_DIALOG_SHOW_BY_CLICK_SEND_BTN);
                        DialogUtil.showExtended3FreeTimeChatPurchaseVipDialog(getActivity(), PageIndex.CHAT_PAGE);
                    }

                }
            }

            @Override
            public void onRecordAudioSuccess(final String filePath, final long length) {
                if (account.isVip || canChat == CanChatStatus.CAN) {
                    if (length / 1000 < 3) {
                        DialogUtil.showRecordAudioMsgFailDialog(getActivity());
                    } else {
                        IMMessage message = IMUtil.sendAudioMessage(extendedData.otherSessionId, new File(filePath), length);
                        ChatData chat = new ChatData();
                        chat.avatar = account.avator;
                        chat.message = message;
                        mAdapter.addData(chat);
                        scrollToBottom();
                    }
                } else if (isFirstSendMsg) {//如果是第一次点击发送，则调用接口判断用户是否还有免费聊天次数
                    isFirstSendMsg = false;
                    CommonModel.checkCanFreeChat(extendedData.otherMemberId, new BaseSubscriber<ZAResponse<CheckChatAvailable>>(new ZASubscriberListener<ZAResponse<CheckChatAvailable>>() {
                        @Override
                        public void onSuccess(ZAResponse<CheckChatAvailable> response) {
                            if (response.data.canChat) {
                                canChat = CanChatStatus.CAN;
                                if (length / 1000 < 3) {
                                    DialogUtil.showRecordAudioMsgFailDialog(getActivity());
                                } else {
                                    IMMessage message = IMUtil.sendAudioMessage(extendedData.otherSessionId, new File(filePath), length);
                                    ChatData chat = new ChatData();
                                    chat.avatar = account.avator;
                                    chat.message = message;
                                    mAdapter.addData(chat);
                                    scrollToBottom();
                                }
                            } else {
                                canChat = CanChatStatus.CAN_NOT;
                                if (response.data.hasExpired) {
                                    canChat = CanChatStatus.HAS_EXPIRED;
                                    EventStatistics.recordLog(ResourceKey.CHAT_PAGE, ResourceKey.ChatPage.PURCHASE_VIP_DIALOG_SHOW_BY_CLICK_SEND_BTN);
                                    DialogUtil.showExtended3FreeTimeChatPurchaseVipDialog(getActivity(), PageIndex.CHAT_PAGE);
                                } else {
                                    EventStatistics.recordLog(ResourceKey.CHAT_PAGE, ResourceKey.ChatPage.PURCHASE_VIP_DIALOG_SHOW_BY_CLICK_SEND_BTN);
                                    DialogUtil.showPurchaseVipDialog(getActivity(),
                                            R.string.fragment_chat_detail_dialog_purchase_title, R.string.fragment_chat_detail_dialog_purchase_tips, PageIndex.CHAT_PAGE);

                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            isFirstSendMsg = true;
                            ToastUtils.toast(getActivity(), R.string.no_network_connected);
                        }

                        @Override
                        public void onFail(String errorCode, String errorMsg) {
                            super.onFail(errorCode, errorMsg);
                            isFirstSendMsg = true;
                            ToastUtils.toast(getActivity(), R.string.no_network_connected);
                        }
                    }));
                } else if (canChat == CanChatStatus.CAN_NOT) {
                    EventStatistics.recordLog(ResourceKey.CHAT_PAGE, ResourceKey.ChatPage.PURCHASE_VIP_DIALOG_SHOW_BY_CLICK_SEND_BTN);
                    DialogUtil.showPurchaseVipDialog(getActivity(), R.string.fragment_chat_detail_dialog_purchase_title, R.string.fragment_chat_detail_dialog_purchase_tips, PageIndex.CHAT_PAGE);
                } else if (canChat == CanChatStatus.HAS_EXPIRED) {
                    EventStatistics.recordLog(ResourceKey.CHAT_PAGE, ResourceKey.ChatPage.PURCHASE_VIP_DIALOG_SHOW_BY_CLICK_SEND_BTN);
                    DialogUtil.showExtended3FreeTimeChatPurchaseVipDialog(getActivity(), PageIndex.CHAT_PAGE);
                }


            }

            @Override
            public void switchTextAndVoiceBtn(boolean isText) {
                if (isText && extendedData.isFromVideoPage) {
                    videoTipsLL.setVisibility(View.VISIBLE);
                } else {
                    videoTipsLL.setVisibility(View.GONE);
                }
            }

            @Override
            public void startRecord(boolean start) {
                if (start) {
                    mAdapter.stopPlay();
                }
            }
        });
        keyboardObservableView = (ObservableRelativeLayout) getView().findViewById(R.id.orl_observable_keyboard);
        keyboardObservableView.setLayoutChangeListener(new ObservableRelativeLayout.LayoutChangeListener() {
            @Override
            public void onKeyboardShow() {
                scrollToBottom();

            }

            @Override
            public void onKeyboardHind() {

            }
        });


        videoTipsLL = getView().findViewById(R.id.ll_safe_tips);
        if (extendedData.isFromVideoPage) {
            showVideoTips();
        }

        mRefreshView = (LinearSwipeRecyclerView) getView().findViewById(R.id.rl_refresh);
        mRefreshView.setLoadMoreEnable(false);
        layoutManager = (LinearLayoutManager) mRefreshView.getRecyclerView().getLayoutManager();
        mPresenter = new ChatDetailPresenter(this, extendedData);
        mRefreshView.setPresenter(mPresenter);
        mAdapter = new ChatAdapter(this, null, extendedData.isLockMessage);
        mAdapter.setOtherSideInfo(extendedData);
        mRefreshView.setAdapter(mAdapter);

        mRefreshView.setOnItemClickListener(new SwipeRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                inputPanel.resetView();
            }

            @Override
            public void onLongClickItem(View itemView, int position) {
            }
        });

    }

    private void scrollToBottom() {
        if (mAdapter.getItemCount() > 0) {
//            layoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
            layoutManager.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, -DensityUtils.getScreenHeight(getActivity()));
        }

    }

    private void scrollTo(int position) {
        if (mAdapter.getItemCount() > 0)
            layoutManager.scrollToPosition(position);
    }


    @Override
    public void onStartFragment() {

    }

    @Override
    public void onResumeFragment() {
        IMUtil.closeMessageNoticeToUser(extendedData.otherSessionId);
    }


    @Override
    public void onPauseFragment() {
        IMUtil.openMessageNotice();
    }

    @Override
    public void onStopFragment() {
        if (mAdapter != null) {
            mAdapter.stopPlay();
        }
    }

    @Override
    public void onDestroyFragment() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video_tips_close:
                videoTipsLL.setVisibility(View.GONE);
                break;
            case R.id.ll_safe_tips:

                break;

        }
    }

    private void showVideoTips() {
        videoTipsLL.clearAnimation();
        AnimationSet animationSet = (AnimationSet) AnimationUtils
                .loadAnimation(getActivity(), R.anim.fragment_chat_detail_safe_tips_anim);

        videoTipsLL.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                videoTipsLL.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                videoTipsLL.clearAnimation();
//                videoTipsLL.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ImageLoaderFactory
                .getImageLoader()
                .with(getActivity())
                .load(extendedData.otherAvatar)
                .into((ImageView) videoTipsLL.findViewById(R.id.iv_video_pic));

        ((TextView) videoTipsLL.findViewById(R.id.tv_title)).setText(extendedData.videoTitle);
        ((TextView) videoTipsLL.findViewById(R.id.tv_msg)).setText(extendedData.videoMessage);
        videoTipsLL.findViewById(R.id.iv_video_tips_close).setOnClickListener(this);
//        videoTipsLL.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void refreshData(ArrayList<ChatData> data) {
//        emptyView.setVisibility(View.GONE);
        int last = layoutManager.findLastCompletelyVisibleItemPosition();
        mAdapter.addDataHead(data);
        if (mAdapter.getItemCount() > 0) {
            EventStatistics.recordLog(ResourceKey.CHAT_PAGE, ResourceKey.ChatPage.NOT_EMPTY_DATA);
        }
        if (isFirstPage) {
            isFirstPage = false;
            scrollToBottom();

        } else {
            scrollTo(data.size() + last);
        }

    }

    @Override
    public void loadMoreDate(ArrayList<ChatData> data) {
    }

    @Override
    public void onReceiveData(ArrayList<ChatData> chatDatas) {
        mAdapter.addData(chatDatas);
        scrollToBottom();
    }

    @Override
    public void onReceiptData(ChatData chatData) {

        if (chatData.message.getMsgType() == MsgTypeEnum.audio) {
            if (chatData.message.getAttachStatus() == AttachStatusEnum.transferred) {
                int position = mAdapter.findChatById(chatData);
                mAdapter.updateAttachmentMessage(position);
            } else {
                mAdapter.updateChatStatus(chatData);
            }

        } else {
            mAdapter.updateChatStatus(chatData);
        }


    }

    @Override
    public void emptyData(String msg) {
        if (mAdapter == null || mAdapter.getItemCount() <= 0){
            EventStatistics.recordLog(ResourceKey.CHAT_PAGE, ResourceKey.ChatPage.EMPTY_DATA);
        }

    }

    @Override
    public void totalDataInfo(ResultEntity<ChatData> resultEntity) {

    }


    @Override
    public void onAttachmentProgressObserver(String uuid, float progress) {
        int index = mAdapter.findChatById(uuid);
        mAdapter.updateAttachmentMessage(index);
    }

    @Override
    public void showFreeMsgLayout(FreeChatMsg msg) {//收到三天免费提示信息的数据

        String date = PreferenceUtil.getString(PreferenceFileNames.APP_BUSINESS_CONFIG, PreferenceKeys.SHOW_FREE_CHAT_TIPS, extendedData.otherMemberId + "", "");
        if (!TextUtils.isEmpty(date)) {
            Date date1 = new Date(date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String lastTime = sdf.format(date1);
            String nowTime = sdf.format(new Date());
            if (nowTime.equals(lastTime)) {
                return;
            }
        }

        final View freeChatContainer = getView().findViewById(R.id.ll_free_tips_container);
        freeChatContainer.setVisibility(View.VISIBLE);
        freeChatContainer.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceUtil.saveValue(PreferenceFileNames.APP_BUSINESS_CONFIG, PreferenceKeys.SHOW_FREE_CHAT_TIPS, extendedData.otherMemberId + "", new Date().toString());
                freeChatContainer.setVisibility(View.GONE);
            }
        });

        TextView freeTipsTV = (TextView) getView().findViewById(R.id.tv_free_tips);
        StringBuilder tips = new StringBuilder(msg.msg);
        tips.append("前往开通");

        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(tips.toString());
        SpannableString ss = new SpannableString(tips);
        while (m.find()) {
            if (!"".equals(m.group())) {
                ss.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.color_d46e22)),
                        m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        ss.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.color_d46e22)),
                tips.length() - 4, tips.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new UnderlineSpan(), tips.length() - 4, tips.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        freeTipsTV.setText(ss);
        freeTipsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PayActivity.class);
                intent.putExtra(PaymentChannel.PAYMENT_CHANNEL_KEY, PaymentChannel.FROM_CHAT_PAGE_SEND_MSG);
                startActivity(intent);
            }
        });
        scrollToBottom();
    }

    private enum CanChatStatus {
        NORMAL, CAN, CAN_NOT, HAS_EXPIRED
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastActions.PAY_VIP_SUCCESS)) {
                account = AccountManager.getInstance().getZaAccount();

                //隐藏三天免费的提示信息
                View freeChatContainer = getView().findViewById(R.id.ll_free_tips_container);
                if (freeChatContainer.isShown()) {
                    freeChatContainer.setVisibility(View.GONE);
                }
                //刷新数据源
                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
