package com.bluewhaledt.saylove.ui.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.base.util.ToastUtils;
import com.bluewhaledt.saylove.base.util.ZAArray;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.event.VideoIndexNotifyEvent;
import com.bluewhaledt.saylove.ui.recommend.widget.SwipeRecyclerView;
import com.bluewhaledt.saylove.ui.tab.TabBaseFragment;
import com.bluewhaledt.saylove.ui.video.adapter.VideoIndexAdapter;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;
import com.bluewhaledt.saylove.ui.video.model.VideoIndexModel;
import com.bluewhaledt.saylove.ui.video.view.IVideoIndexView;
import com.bluewhaledt.saylove.util.ActivityRedirectUtil;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.widget.linear_view.LinearSwipeRecyclerView;
import com.bluewhaledt.saylove.widget.linear_view.entity.ResultEntity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 视频列表页
 * Created by rade.chan on 2016/11/28.
 */

public class AVIndexFragment extends TabBaseFragment implements View.OnClickListener, IVideoIndexView<VideoIndexEntity> {

    private LinearSwipeRecyclerView<VideoIndexEntity> mRecyclerView;
    private VideoIndexAdapter mVideoIndexAdapter;
    private ZAArray<VideoIndexEntity> mList = new ZAArray<>();
    private View mRecordBtn;            //录制按钮

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_av_index_layout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onEvent(VideoIndexNotifyEvent notifyEvent) {
        if (notifyEvent != null && mVideoIndexAdapter != null) {
            if (!TextUtils.isEmpty(notifyEvent.getVideoId())) {
                String videoId = notifyEvent.getVideoId();
                int likeId = notifyEvent.getLikeId();
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).videoId.equals(videoId)) {
                        mList.get(i).likeId = likeId;
                        if (likeId > -1) {
                            mList.get(i).likeNum++;
                        } else {
                            mList.get(i).likeNum--;
                        }
                        if (mList.get(i).likeNum < 0) {
                            mList.get(i).likeNum = 0;
                        }
                        break;
                    }
                }
            } else if (notifyEvent.isRefresh()) {
                mRecyclerView.setRefreshing(true);
            }

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showTitleBar(false);
        initView(view);
        initRecyclerView();
        initListener();
    }

    private void initView(View rootView) {
        mRecordBtn = rootView.findViewById(R.id.video_publish_btn);
        mRecyclerView = (LinearSwipeRecyclerView) rootView.findViewById(R.id.video_recycler_view);
    }

    private void initListener() {
        mRecordBtn.setOnClickListener(this);
        mRecyclerView.setOnItemClickListener(new SwipeRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, final int position) {
                VideoIndexEntity entity = mList.get(position);
                entity.viewNum++;
                ActivityRedirectUtil.gotoAvPlayActivity(getActivity(), entity);
            }

            @Override
            public void onLongClickItem(View itemView, int position) {

            }
        });
        mRecyclerView.setReceiveDataCallback(this);
    }


    private void initRecyclerView() {
        mVideoIndexAdapter = new VideoIndexAdapter(getActivity(), mList);
        mRecyclerView.setGridLayoutManager(2);
        mRecyclerView.setAdapter(mVideoIndexAdapter);
        mRecyclerView.setModel(new VideoIndexModel());

    }


    @Override
    public void onStartFragment() {

    }

    @Override
    public void onResumeFragment() {
        if (mVideoIndexAdapter != null) {
            mVideoIndexAdapter.notifyDataSetChanged();
        }
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
    protected void onFirstUserVisible() {
        mRecyclerView.setRefreshing(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_publish_btn:
                EventStatistics.recordLog(ResourceKey.VIDEO_INDEX_PAGE, ResourceKey.VideoIndexPage.VIDEO_INDEX_START_RECORD);
                ActivityRedirectUtil.gotoRecordActivity(getActivity(), ((BaseActivity)getActivity()).mPermissionHelper);
                break;
        }
    }


    @Override
    public void refreshData(ArrayList<VideoIndexEntity> data) {
        mList.clear();
        mList.addAll(data);
        mVideoIndexAdapter.notifyDataSetChanged();

    }

    @Override
    public void loadMoreDate(ArrayList<VideoIndexEntity> data) {
        mList.addAll(data);
        mVideoIndexAdapter.notifyDataSetChanged();
    }

    @Override
    public void emptyData(String msg) {
        ToastUtils.toast(getActivity(), getResources().getString(R.string.no_date));
    }

    @Override
    public void totalDataInfo(ResultEntity<VideoIndexEntity> resultEntity) {
        DebugUtils.d("AVIndexFragment", new Gson().toJson(resultEntity));
    }


}
