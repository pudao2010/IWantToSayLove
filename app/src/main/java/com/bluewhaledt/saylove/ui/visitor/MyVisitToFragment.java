package com.bluewhaledt.saylove.ui.visitor;

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
import com.bluewhaledt.saylove.ui.info.ThirdPartyFragment;
import com.bluewhaledt.saylove.ui.recommend.widget.SwipeRecyclerView;
import com.bluewhaledt.saylove.ui.visitor.adapter.MyVisitListAdapter;
import com.bluewhaledt.saylove.ui.visitor.entiry.Visitor;
import com.bluewhaledt.saylove.ui.visitor.presenter.MyVisitListPresenter;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.widget.linear_view.ILinearBaseView;
import com.bluewhaledt.saylove.widget.linear_view.LinearSwipeRecyclerView;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public class MyVisitToFragment extends BaseFragment implements ILinearBaseView<Visitor> {

    private LinearSwipeRecyclerView recyclerView;

    private MyVisitListPresenter presenter;

    private MyVisitListAdapter adapter;

    private View emptyView;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visitor_mine_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showTitleBar(false);
        recyclerView = (LinearSwipeRecyclerView) getView().findViewById(R.id.lsrv_recycler_view);
        emptyView = getView().findViewById(R.id.empty_view);

        presenter = new MyVisitListPresenter(this);
        recyclerView.setPresenter(presenter);


        adapter = new MyVisitListAdapter(getActivity(), null);

        recyclerView.setAdapter(adapter);

        recyclerView.setRefreshing(true);

        recyclerView.setOnItemClickListener(new SwipeRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Visitor item = adapter.getItemAtPosition(position);
                item.read = true;
                adapter.notifyItemChanged(position);
                Bundle bundle = new Bundle();
                bundle.putString(IntentConstants.USER_ID, item.objectId + "");
                startFragment(ThirdPartyFragment.class, bundle);
            }

            @Override
            public void onLongClickItem(View itemView, int position) {

            }
        });

        EventStatistics.recordLog(ResourceKey.MY_VISIT_TO_PAGE,ResourceKey.MyVisitToPage.MY_VISIT_TO_PAGE);
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
            ToastUtils.toast(getActivity(), msg);
        }

    }

    @Override
    public void totalDataInfo(ResultEntity<Visitor> resultEntity) {

    }
}
