package com.bluewhaledt.saylove.ui.system;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.DensityUtils;
import com.bluewhaledt.saylove.base.widget.ObservableRelativeLayout;
import com.bluewhaledt.saylove.im.IMReceiver;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.ui.message.entity.ChatData;
import com.bluewhaledt.saylove.ui.message.entity.FreeChatMsg;
import com.bluewhaledt.saylove.ui.message.view.IChatDetailViewAction;
import com.bluewhaledt.saylove.ui.message.view.InputPanel;
import com.bluewhaledt.saylove.ui.recommend.widget.SwipeRecyclerView;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.system.adapter.SystemAdapter;
import com.bluewhaledt.saylove.ui.system.presenter.SystemPresenter;
import com.bluewhaledt.saylove.widget.linear_view.LinearSwipeRecyclerView;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/23.
 */

public class SystemMessageActivity extends BaseActivity implements View.OnClickListener, IChatDetailViewAction<ChatData> {

    private LinearSwipeRecyclerView mRefreshView;

    private InputPanel inputPanel;

    private SystemAdapter mAdapter;

    private ObservableRelativeLayout keyboardObservableView;

    private LinearLayoutManager layoutManager;

    private SystemPresenter mPresenter;

    private boolean isFirstPage = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        initView();


    }

    private void initView() {
        setTitle(R.string.system_title);
        inputPanel = (InputPanel) findViewById(R.id.ip_input_panel);
        inputPanel.setOnlyTextModel();
        inputPanel.setActionCallback(new InputPanel.IActionCallback() {
            @Override
            public void sendBtnClick(final String content) {
                IMMessage sendMsg;
                sendMsg = IMUtil.sendTextMessageToUserReturnIMMessage(IMReceiver.SYSTEM_NOTIFICATION_ID, content);
                ChatData chat = new ChatData();
                chat.message = sendMsg;
                chat.avatar = AccountManager.getInstance().getZaAccount().avator;
                mAdapter.addData(chat);
                scrollToBottom();
            }

            @Override
            public void onRecordAudioSuccess(final String filePath, final long length) {

            }

            @Override
            public void switchTextAndVoiceBtn(boolean isText) {

            }

            @Override
            public void startRecord(boolean start) {

            }
        });
        keyboardObservableView = (ObservableRelativeLayout) findViewById(R.id.orl_observable_keyboard);
        keyboardObservableView.setLayoutChangeListener(new ObservableRelativeLayout.LayoutChangeListener() {
            @Override
            public void onKeyboardShow() {
                scrollToBottom();

            }

            @Override
            public void onKeyboardHind() {

            }
        });

        mRefreshView = (LinearSwipeRecyclerView) findViewById(R.id.rl_refresh);
        mRefreshView.setLoadMoreEnable(false);
        layoutManager = (LinearLayoutManager) mRefreshView.getRecyclerView().getLayoutManager();
        mPresenter = new SystemPresenter(this);
        mRefreshView.setPresenter(mPresenter);
        mAdapter = new SystemAdapter(this, null);
        mRefreshView.setAdapter(mAdapter);

        mRefreshView.setOnItemClickListener(new SwipeRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                hideSoftInput();
            }

            @Override
            public void onLongClickItem(View itemView, int position) {
                hideSoftInput();
            }
        });

        mRefreshView.setRefreshing(true);
    }

    private void scrollToBottom() {
        if (mAdapter.getItemCount() > 0) {
            layoutManager.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, -DensityUtils.getScreenHeight(this));
        }

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onReceiveData(ArrayList<ChatData> imMessages) {

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
    public void onAttachmentProgressObserver(String uuid, float progress) {

    }

    @Override
    public void showFreeMsgLayout(FreeChatMsg msg) {

    }

    @Override
    public void refreshData(ArrayList<ChatData> data) {
        int last = layoutManager.findLastCompletelyVisibleItemPosition();
        mAdapter.addDataHead(data);
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
    public void emptyData(String msg) {

    }

    @Override
    public void totalDataInfo(ResultEntity<ChatData> resultEntity) {

    }

    private void scrollTo(int position) {
        if (mAdapter.getItemCount() > 0)
            layoutManager.scrollToPosition(position);
    }
}
