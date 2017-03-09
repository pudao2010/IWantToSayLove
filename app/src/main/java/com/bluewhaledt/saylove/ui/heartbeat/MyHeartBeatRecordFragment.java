package com.bluewhaledt.saylove.ui.heartbeat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseFragment;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.ui.heartbeat.adapter.MyHeartBeatRecordAdapter;
import com.bluewhaledt.saylove.ui.heartbeat.entity.HeartBeatItem;
import com.bluewhaledt.saylove.ui.heartbeat.presenter.MyHeartBeatRecordPresenter;
import com.bluewhaledt.saylove.ui.heartbeat.view.IMyHeartBeatRecordViewAction;
import com.bluewhaledt.saylove.ui.info.ThirdPartyFragment;
import com.bluewhaledt.saylove.ui.recommend.widget.SwipeRecyclerView;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.widget.linear_view.LinearSwipeRecyclerView;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public class MyHeartBeatRecordFragment extends BaseFragment implements IMyHeartBeatRecordViewAction {

    private LinearSwipeRecyclerView recyclerView;

    private MyHeartBeatRecordPresenter presenter;

    private MyHeartBeatRecordAdapter adapter;

    private View emptyView;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_heart_my_record, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showTitleBar(false);
        recyclerView = (LinearSwipeRecyclerView) getView().findViewById(R.id.lsrv_recycler_view);
        emptyView = getView().findViewById(R.id.empty_view);
        presenter = new MyHeartBeatRecordPresenter(this);
        recyclerView.setPresenter(presenter);

        adapter = new MyHeartBeatRecordAdapter(getActivity(), null);

        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshing(true);

        recyclerView.setOnItemClickListener(new SwipeRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                HeartBeatItem item = adapter.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString(IntentConstants.USER_ID, item.objectId + "");
                startFragment(ThirdPartyFragment.class, bundle);
            }

            @Override
            public void onLongClickItem(View itemView, int position) {

            }
        });

        EventStatistics.recordLog(ResourceKey.MY_HEART_BEAT_TO_PAGE,ResourceKey.MyHeartBeatToPage.MY_HEART_BEAT_TO_PAGE);
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

    }

    @Override
    public void refreshData(ArrayList<HeartBeatItem> data) {
        if (emptyView.getVisibility() == View.VISIBLE){
            emptyView.setVisibility(View.GONE);
        }
        adapter.clearData();
        adapter.addData(data);
    }

    @Override
    public void loadMoreDate(ArrayList<HeartBeatItem> data) {
        adapter.addData(data);
    }

    @Override
    public void emptyData(String msg) {
        if (adapter.getItemCount() <= 0){
            emptyView.setVisibility(View.VISIBLE);
        }else{
            ToastUtils.toast(getActivity(), msg);
        }
    }

    @Override
    public void totalDataInfo(ResultEntity<HeartBeatItem> resultEntity) {

    }

}
