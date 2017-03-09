package com.bluewhaledt.saylove.ui.visitor;

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
import com.bluewhaledt.saylove.ui.info.ThirdPartyFragment;
import com.bluewhaledt.saylove.ui.pay.PayActivity;
import com.bluewhaledt.saylove.ui.pay.constant.PaymentChannel;
import com.bluewhaledt.saylove.ui.recommend.widget.SwipeRecyclerView;
import com.bluewhaledt.saylove.ui.register_login.account.AccountManager;
import com.bluewhaledt.saylove.ui.visitor.adapter.VisitToMeAdapter;
import com.bluewhaledt.saylove.ui.visitor.entiry.Visitor;
import com.bluewhaledt.saylove.ui.visitor.presenter.VisitToMePresenter;
import com.bluewhaledt.saylove.util.DialogUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.widget.linear_view.ILinearBaseView;
import com.bluewhaledt.saylove.widget.linear_view.LinearSwipeRecyclerView;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public class VisitToMeFragment extends BaseFragment implements ILinearBaseView<Visitor> {

    private LinearSwipeRecyclerView recyclerView;

    private VisitToMePresenter presenter;

    private VisitToMeAdapter adapter;

    private View emptyView;

    private boolean isVip;
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visitor_to_me, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showTitleBar(false);
        isVip = AccountManager.getInstance().getZaAccount().isVip;
        recyclerView = (LinearSwipeRecyclerView) getView().findViewById(R.id.lsrv_recycler_view);
        emptyView = getView().findViewById(R.id.empty_view);

        presenter = new VisitToMePresenter(this);
        recyclerView.setPresenter(presenter);

        adapter = new VisitToMeAdapter(getActivity(), null);

        recyclerView.setAdapter(adapter);

        recyclerView.setRefreshing(true);

        recyclerView.setOnItemClickListener(new SwipeRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (isVip) {
                    Visitor data = adapter.getItemAtPosition(position);
                    data.read = true;
                    adapter.notifyItemChanged(position);
                    Bundle bundle = new Bundle();
                    bundle.putString(IntentConstants.USER_ID, data.objectId + "");
                    startFragment(ThirdPartyFragment.class, bundle);
                } else {
                    if (position == 1) {//因为第一项是免费可见的，所以此处排除第一项
                        Visitor data = adapter.getItemAtPosition(position);
                        data.read = true;
                        adapter.notifyItemChanged(position);
                        Bundle bundle = new Bundle();
                        bundle.putString(IntentConstants.USER_ID, data.objectId + "");
                        startFragment(ThirdPartyFragment.class, bundle);
                    } else {
                        if (position == 2){
                            EventStatistics.recordLog(ResourceKey.VISIT_TO_ME_PAGE,ResourceKey.VisitToMePage.PAY_FOR_VIP);
                            Intent intent = new Intent(getActivity(), PayActivity.class);
                            intent.putExtra(PaymentChannel.PAYMENT_CHANNEL_KEY,PaymentChannel.FROM_VISIT_TO_ME_PAGE);
                            startActivity(intent);
                        }else{
                            EventStatistics.recordLog(ResourceKey.VISIT_TO_ME_PAGE,ResourceKey.VisitToMePage.PURCHASE_VIP_DIALOG);
                            DialogUtil.showPurchaseVipDialog(getActivity(),
                                    R.string.fragment_visitor_purchase_vip_title, R.string.fragment_visitor_purchase_vip_tips, PageIndex.VISIT_TO_ME_PAGE);
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

        EventStatistics.recordLog(ResourceKey.VISIT_TO_ME_PAGE,ResourceKey.VisitToMePage.VISIT_TO_ME_PAGE);
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
    public void refreshData(ArrayList<Visitor> data) {
        if (emptyView.getVisibility() == View.VISIBLE)
            emptyView.setVisibility(View.GONE);
        adapter.clearData();
        adapter.addData(data);
    }

    @Override
    public void loadMoreDate(ArrayList<Visitor> data) {
        adapter.addData(data);
    }

    @Override
    public void emptyData(String msg) {
        if (adapter.getItemCount() <= 0){
            emptyView.setVisibility(View.VISIBLE);
        }else{
            ToastUtils.toast(getActivity(), R.string.no_date);
        }
    }

    @Override
    public void totalDataInfo(ResultEntity<Visitor> resultEntity) {
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
