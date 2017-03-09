package com.bluewhaledt.saylove.ui.tourist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.NetworkUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.ui.info.ThirdPartyFragment;
import com.bluewhaledt.saylove.ui.recommend.widget.SwipeRecyclerView;
import com.bluewhaledt.saylove.ui.register_login.regist.BaseInfoEditActivity;
import com.bluewhaledt.saylove.ui.tab.TabBaseFragment;
import com.bluewhaledt.saylove.ui.tourist.entity.TouristEntity;
import com.bluewhaledt.saylove.ui.tourist.persenter.TouristPersenter;
import com.bluewhaledt.saylove.ui.tourist.view.ITouristView;
import com.bluewhaledt.saylove.util.EventStatistics;

import java.util.ArrayList;

/**
 * 描述：逛一逛的详情fragment
 * 作者：shiming_li
 * 时间：2016/12/12 13:57
 * 包名：com.zhenai.saylove_icon.ui.tourist
 * 项目名：SayLove
 */
public class TouristDetailFragment extends TabBaseFragment implements ITouristView, View.OnClickListener {
    private View mView;
    private SwipeRecyclerView mSwipeRecyclerView;
    private Button mGotoRegist;
    private Button mGotoRegistAlgin;
    private TouristDetailAdapter mAdapter;
    private TouristPersenter mPersenter;
    private String mWhichMale;
    private LinearLayout mHasNetWorksContainer;
    private RelativeLayout mNoNetWorksContainer;
    private Button mTryNetWorks;
    private boolean mNetworkConnected;
    private BaseActivity mActivity;
    private boolean isFristGetData=true;
    @Override
    protected void onFirstUserVisible() {

    }
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tourist_detail_layout, container, false);
        return mView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        mActivity.showTitleBar(false);

        showTitleBar(true);
        setTitle(R.string.recommend_tourist);
        showTitleBarLeftBtn(true);

        Bundle arguments = getArguments();
        if (arguments!=null) {
            mWhichMale = arguments.getString("tourist_which_male");
        }
        mNetworkConnected = NetworkUtils.isNetworkConnected(getActivity());
        mPersenter = new TouristPersenter(getActivity(), this);
        initView();
        initListener();
        findHasNetWork();
        EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_PAGE,ResourceKey.TOURIST_DETAIL_PAGE);

    }

    private void findHasNetWork() {
       if (!mNetworkConnected){
           mHasNetWorksContainer.setVisibility(View.GONE);
           mNoNetWorksContainer.setVisibility(View.VISIBLE);
       }else {
           mHasNetWorksContainer.setVisibility(View.VISIBLE);
           mNoNetWorksContainer.setVisibility(View.GONE);
       }
    }

    private void initListener() {
        mGotoRegist.setOnClickListener(this);
        mNoNetWorksContainer.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mSwipeRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new TouristDetailAdapter(getActivity(), new ArrayList<TouristEntity.TouristEntityListBean>());
        mSwipeRecyclerView.setAdapter(mAdapter);
        mSwipeRecyclerView.setOnLoadListener(new SwipeRecyclerView.OnLoadListener() {
            @Override
            public void onRefresh() {
                mPersenter.getTouristRecommend(mWhichMale);
            }

            @Override
            public void onLoadMore() {
                mPersenter.getTouristRecommendloadMore(mWhichMale);
            }
        });
        mSwipeRecyclerView.setRefreshing(true);
        mSwipeRecyclerView.setOnItemClickListener(new SwipeRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                ArrayList<TouristEntity.TouristEntityListBean>  list = mAdapter.getArraylist();
                Bundle bundle = new Bundle();
                bundle.putString(IntentConstants.USER_ID,list.get(position).userId+"");
                bundle.putBoolean(IntentConstants.FROM_TOURIST_FRAGMENT,true);
                startFragment(ThirdPartyFragment.class,bundle);
            }
            @Override
            public void onLongClickItem(View itemView, int position) {

            }
        });
        mTryNetWorks.setOnClickListener(this);
        mGotoRegistAlgin.setOnClickListener(this);
    }
    private void initView() {
        mSwipeRecyclerView = (SwipeRecyclerView) mView.findViewById(R.id.fragment_recommend_layout_lv);
        mGotoRegist = (Button) mView.findViewById(R.id.fragment_tourist_detail_btn_gotonext);
        mGotoRegistAlgin = (Button) mView.findViewById(R.id.fragment_tourist_detail_btn_gotonext_algin);
        mHasNetWorksContainer = (LinearLayout) mView.findViewById(R.id.fragment_tourist_detail_hasnet_work_container);
        mNoNetWorksContainer = (RelativeLayout) mView.findViewById(R.id.fragment_tourist_detail_no_net_work_container);
        mTryNetWorks = (Button) mView.findViewById(R.id.fragment_head_prortrait_btn_try_again);
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
    public void getTouristDataSuccess(TouristEntity entity) {
//        if (isFristGetData){
//            entity.list.clear();
//        }
        if (entity!=null&&entity.list.size()>0) {
            mNoNetWorksContainer.setVisibility(View.GONE);
            mHasNetWorksContainer.setVisibility(View.VISIBLE);
            mAdapter.clearRecommendData();
            mAdapter.addRecommendData(entity.list);
            mSwipeRecyclerView.complete();
            isFristGetData=false;
        }else  if (entity!=null&&entity.list.size()==0&&isFristGetData){
            mHasNetWorksContainer.setVisibility(View.GONE);
            mNoNetWorksContainer.setVisibility(View.VISIBLE);
        }else {
            ToastUtils.toast(getActivity(), R.string.recommend_no_person);
            mSwipeRecyclerView.complete();
        }
        dismissProgress();
    }
    @Override
    public void getTouristDataFail() {
        if (mSwipeRecyclerView!=null){
            //隐藏布局
            mHasNetWorksContainer.setVisibility(View.GONE);
            mNoNetWorksContainer.setVisibility(View.VISIBLE);
            mSwipeRecyclerView.complete();
        }
    }
    @Override
    public void getTouristDataSuccessLoadMore(TouristEntity entity) {
        if (entity!=null && entity.list.size() > 0) {
            boolean hasLoadMore = mAdapter.addRecommendData(entity.list);
            mSwipeRecyclerView.stopLoadingMore();
            if (hasLoadMore) {
                ToastUtils.toast(getActivity(), R.string.recommend_no_person);
            }
        } else{
            ToastUtils.toast(getActivity(), R.string.recommend_no_person);
            mSwipeRecyclerView.onNoMore(R.string.recommend_show_no_view + "");
            mSwipeRecyclerView.stopLoadingMore();
        }
    }

    @Override
    public void getTouristDataFail(String errorCode, String errorMsg) {
        ToastUtils.toast(getActivity(), errorMsg);
        if (mSwipeRecyclerView!=null){
            mSwipeRecyclerView.stopLoadingMore();
        }
    }

    @Override
    public void onError() {
        if (mSwipeRecyclerView!=null){
            mSwipeRecyclerView.complete();
        }
    }

    @Override
    public void onErrorLoadmore() {
        if (mSwipeRecyclerView!=null){
            mSwipeRecyclerView.stopLoadingMore();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_tourist_detail_btn_gotonext_algin:
            case R.id.fragment_tourist_detail_btn_gotonext:
                EventStatistics.recordLog(ResourceKey.TOURIST_DETAIL_PAGE,ResourceKey.TouristDetailPage.TOURIST_DETAIL_PAGE_RIGEST);
                startActivity(new Intent(getActivity(),BaseInfoEditActivity.class));
                finish();
                break;
            case R.id.fragment_head_prortrait_btn_try_again:
                showProgress();
                if (NetworkUtils.isNetworkConnected(getActivity())){
                    mSwipeRecyclerView.setRefreshing(true);
//                    mHasNetWorksContainer.setVisibility(View.VISIBLE);
//                    mNoNetWorksContainer.setVisibility(View.GONE);
                }else {
                    ToastUtils.toast(getActivity(),R.string.recommend_tourist_is_net_work);
                    dismissProgress();
                }
                break;
            case R.id.zhenai_lib_titlebar_left_text:
                finish();
                break;
        }
    }
}
