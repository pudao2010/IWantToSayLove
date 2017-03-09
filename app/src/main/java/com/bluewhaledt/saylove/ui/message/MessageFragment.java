package com.bluewhaledt.saylove.ui.message;

import android.app.usage.UsageEvents;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.BroadcastActions;
import com.bluewhaledt.saylove.constant.PageIndex;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.entity.CheckChatAvailable;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.listener.ZASubscriberListener;
import com.bluewhaledt.saylove.model.CommonModel;
import com.bluewhaledt.saylove.network.retrofit.BaseSubscriber;
import com.bluewhaledt.saylove.network.retrofit.ZAResponse;
import com.bluewhaledt.saylove.ui.heartbeat.HeartBeatFragment;
import com.bluewhaledt.saylove.ui.message.adapter.MsgAdapter;
import com.bluewhaledt.saylove.ui.message.entity.ExtendedData;
import com.bluewhaledt.saylove.ui.message.entity.MessageData;
import com.bluewhaledt.saylove.ui.message.entity.UnreadCount;
import com.bluewhaledt.saylove.ui.message.presenter.MessagePresenter;
import com.bluewhaledt.saylove.ui.message.view.MessageViewAction;
import com.bluewhaledt.saylove.ui.recommend.widget.SwipeRecyclerView;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.register_login.account.ZAAccount;
import com.bluewhaledt.saylove.ui.tab.TabBaseFragment;
import com.bluewhaledt.saylove.ui.visitor.VisitorFragment;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.widget.linear_view.LinearSwipeRecyclerView;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/11/28.
 */

public class MessageFragment extends TabBaseFragment implements MessageViewAction<MessageData> {

    private String TAG = this.getClass().getSimpleName();
    private LinearSwipeRecyclerView mMessageListSRV;
    private MessagePresenter mPresenter;

    private MsgAdapter mAdapter;

    private ZAAccount account;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showTitleBar(false);
        account = AccountManager.getInstance().getZaAccount();
        initView();

        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(receiver, new IntentFilter(BroadcastActions.PAY_VIP_SUCCESS));

    }

    private void initView() {
        mMessageListSRV = (LinearSwipeRecyclerView) getView().findViewById(R.id.srv_messages);
        mMessageListSRV.setLoadMoreEnable(true);
        mPresenter = new MessagePresenter(this);
        mMessageListSRV.setPresenter(mPresenter);
        mAdapter = new MsgAdapter(this, null);
        mPresenter.setDataAdapter(mAdapter);
        mMessageListSRV.setAdapter(mAdapter);
        mMessageListSRV.setOnItemClickListener(new SwipeRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, final int position) {
                if (position < 2) {
                    if (position == 0) {
                        mAdapter.updateHeartBeatRedDot(0);
                        startFragment(HeartBeatFragment.class, null);
                    } else {
                        mAdapter.updateVisitorRedDot(0);
                        startFragment(VisitorFragment.class, null);
                    }
                    return;
                }
                int adjPosition = position - 2;
                final MessageData data = mAdapter.getItemAtPosition(adjPosition);

                if (account.isVip) {
                    Bundle args = new Bundle();
                    ExtendedData info = new ExtendedData();
                    info.otherSessionId = data.sessionId;
                    info.otherAvatar = data.user.avatar;
                    info.otherMemberId = data.user.userId;
                    info.nikeName = data.user.nickname;
                    info.isLockMessage = false;
                    args.putSerializable(ChatDetailFragment.EXTENDED_DATA, info);
                    Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
                    intent.putExtras(args);
                    startActivity(intent);
                } else {//如果不是vip，则调用接口检查用户是否有三次免费次数
                    CommonModel.checkCanFreeChat(data.user.userId, new BaseSubscriber<ZAResponse<CheckChatAvailable>>(new ZASubscriberListener<ZAResponse<CheckChatAvailable>>() {
                        @Override
                        public void onSuccess(ZAResponse<CheckChatAvailable> response) {
                            if (!response.data.canChat) {
                                EventStatistics.recordLog(ResourceKey.MESSAGE_PAGE, ResourceKey.MessagePage.PURCHASE_VIP_DIALOG);
                                DialogUtil.showExtended3FreeTimeChatPurchaseVipDialog(getActivity(), PageIndex.MESSAGE_PAGE);
                            } else {
                                Bundle args = new Bundle();
                                ExtendedData info = new ExtendedData();
                                info.otherSessionId = data.sessionId;
                                info.otherAvatar = data.user.avatar;
                                info.otherMemberId = data.user.userId;
                                info.nikeName = data.user.nickname;
                                info.myFreeChatCount = response.data.residueCount;
                                info.canChat = response.data.canChat;
                                info.isLockMessage = false;
                                args.putSerializable(ChatDetailFragment.EXTENDED_DATA, info);
                                Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
                                intent.putExtras(args);
                                startActivity(intent);
                                data.user.readable = true;
                                mAdapter.notifyItemChanged(position);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            ToastUtils.toast(getActivity(), R.string.no_network_connected);
                        }

                        @Override
                        public void onFail(String errorCode, String errorMsg) {
                            super.onFail(errorCode, errorMsg);
                            ToastUtils.toast(getActivity(), errorMsg);
                        }
                    }));
                }

            }

            @Override
            public void onLongClickItem(View itemView, final int position) {

            }
        });
    }

    @Override
    protected void onFirstUserVisible() {
        mMessageListSRV.setRefreshing(true);
    }

    @Override
    public void onStartFragment() {

    }

    @Override
    public void onResumeFragment() {
        mPresenter.getUnReadCount(getActivity());
        IMUtil.closeAllMessageNotice();
    }

    @Override
    public void onPauseFragment() {
        IMUtil.openMessageNotice();
    }

    @Override
    public void onStopFragment() {

    }

    @Override
    public void onDestroyFragment() {
        mPresenter.destroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public void refreshData(ArrayList<MessageData> data) {
        mAdapter.clearData();
        mAdapter.addData(data);
        mMessageListSRV.setRefreshEnable(false);
    }

    @Override
    public void loadMoreDate(ArrayList<MessageData> data) {
        mAdapter.addData(data);
    }

    @Override
    public void emptyData(String msg) {
        if (mAdapter == null || mAdapter.getItemCount() <= 0){
            EventStatistics.recordLog(ResourceKey.MESSAGE_PAGE, ResourceKey.MessagePage.MESSAGE_PAGE_EMPTY);
        }

        ToastUtils.toast(getActivity(), msg);
    }

    @Override
    public void totalDataInfo(ResultEntity<MessageData> resultEntity) {

    }


    @Override
    public void onReceiveContact(ArrayList<MessageData> recentContacts) {
        mAdapter.addAndUpdateData(recentContacts);
    }

    @Override
    public void setRedDot(UnreadCount entity) {
        mAdapter.updateHeartBeatRedDot(entity.likeCount);
        mAdapter.updateVisitorRedDot(entity.viewCount);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastActions.PAY_VIP_SUCCESS)) {
                mMessageListSRV.setRefreshEnable(true);
                mMessageListSRV.setRefreshing(true);
            }
        }
    };
}
