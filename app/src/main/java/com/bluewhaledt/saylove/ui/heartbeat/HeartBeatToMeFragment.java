package com.bluewhaledt.saylove.ui.heartbeat;

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
import com.bluewhaledt.saylove.base.activity.BaseFragment;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.BroadcastActions;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.constant.PageIndex;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.ui.heartbeat.adapter.HeartBeatToMeAdapter;
import com.bluewhaledt.saylove.ui.heartbeat.entity.HeartBeatItem;
import com.bluewhaledt.saylove.ui.heartbeat.presenter.HeartBeatToMePresenter;
import com.bluewhaledt.saylove.ui.heartbeat.view.IHeartBeatToMeViewAction;
import com.bluewhaledt.saylove.ui.info.ThirdPartyFragment;
import com.bluewhaledt.saylove.ui.pay.PayActivity;
import com.bluewhaledt.saylove.ui.pay.constant.PaymentChannel;
import com.bluewhaledt.saylove.ui.recommend.widget.SwipeRecyclerView;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.widget.linear_view.LinearSwipeRecyclerView;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/6.
 */

public class HeartBeatToMeFragment extends BaseFragment implements IHeartBeatToMeViewAction {

    private LinearSwipeRecyclerView recyclerView;

    private HeartBeatToMePresenter presenter;

    private HeartBeatToMeAdapter adapter;

    private View emptyView;

    private boolean isVip;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_heart_beat_to_me, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showTitleBar(false);
        isVip = AccountManager.getInstance().getZaAccount().isVip;
        recyclerView = (LinearSwipeRecyclerView) getView().findViewById(R.id.lsrv_recycler_view);
        emptyView = getView().findViewById(R.id.empty_view);
        presenter = new HeartBeatToMePresenter(this);
        recyclerView.setPresenter(presenter);

        adapter = new HeartBeatToMeAdapter(getActivity(), null);

        recyclerView.setAdapter(adapter);

        recyclerView.setRefreshing(true);

        recyclerView.setOnItemClickListener(new SwipeRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (isVip) {
                    HeartBeatItem data = adapter.getItemAtPosition(position);
                    data.read = true;
                    adapter.notifyItemChanged(position);
                    Bundle bundle = new Bundle();
                    bundle.putString(IntentConstants.USER_ID, data.objectId + "");
                    startFragment(ThirdPartyFragment.class, bundle);
                } else {
                    if (position == 1) {//因为第一项是免费可见的，所以此处排除第一项
                        HeartBeatItem data = adapter.getItemAtPosition(position);
                        data.read = true;
                        adapter.notifyItemChanged(position);
                        Bundle bundle = new Bundle();
                        bundle.putString(IntentConstants.USER_ID, data.objectId + "");
                        startFragment(ThirdPartyFragment.class, bundle);

                    } else {
                        if (position == 2){
                            EventStatistics.recordLog(ResourceKey.HEART_BEAT_TO_ME_PAGE,ResourceKey.HeartBeatToMePage.PAY_FOR_VIP);
                            Intent intent = new Intent(getActivity(), PayActivity.class);
                            intent.putExtra(PaymentChannel.PAYMENT_CHANNEL_KEY,PaymentChannel.FROM_HEART_BEAT_TO_ME_PAGE);
                            startActivity(intent);

                        }else{
                            EventStatistics.recordLog(ResourceKey.HEART_BEAT_TO_ME_PAGE,ResourceKey.HeartBeatToMePage.PURCHASE_VIP_DIALOG);
                            DialogUtil.showPurchaseVipDialog(getActivity(),
                                    R.string.fragment_heart_beat_purchase_vip_title, R.string.fragment_heart_beat_purchase_vip_tips, PageIndex.HEART_BEAT_TO_ME_PAGE);
                        }
                    }
                }

            }

            @Override
            public void onLongClickItem(View itemView, int position) {

            }
        });

        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(receiver, new IntentFilter(BroadcastActions.PAY_VIP_SUCCESS));
        EventStatistics.recordLog(ResourceKey.HEART_BEAT_TO_ME_PAGE,ResourceKey.HeartBeatToMePage.HEART_BEAT_TO_ME_PAGE);

    }

    @Override
    public void onStartFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onStopFragment() {

    }

    @Override
    public void onDestroyFragment() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public void refreshData(ArrayList<HeartBeatItem> data) {
        if (emptyView.getVisibility() == View.VISIBLE)
            emptyView.setVisibility(View.GONE);
        adapter.clearData();
        adapter.addData(data);
    }

    @Override
    public void loadMoreDate(ArrayList<HeartBeatItem> data) {
        adapter.addData(data);
    }

    @Override
    public void emptyData(String msg) {
        if (adapter.getItemCount() <= 0) {
            emptyView.setVisibility(View.VISIBLE);
        }else{
            ToastUtils.toast(getActivity(), msg);
        }
    }

    @Override
    public void totalDataInfo(ResultEntity resultEntity) {
        adapter.setTotalMsgCount(resultEntity.count);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastActions.PAY_VIP_SUCCESS)) {
                isVip = AccountManager.getInstance().getZaAccount().isVip;
                adapter.updateVipStatus();
            }
        }
    };
}
