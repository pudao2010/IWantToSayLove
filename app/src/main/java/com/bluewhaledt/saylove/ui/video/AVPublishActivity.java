package com.bluewhaledt.saylove.ui.video;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.ui.video.adapter.VideoIndexAdapter;
import com.bluewhaledt.saylove.ui.video.entity.VideoIndexEntity;
import com.bluewhaledt.saylove.ui.video.presenter.VideoPublishPresenter;
import com.bluewhaledt.saylove.ui.video.view.IVideoPublishView;
import com.bluewhaledt.saylove.util.ActivityRedirectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rade.chan on 2016/12/14.
 */

public class AVPublishActivity extends BaseActivity implements IVideoPublishView ,View.OnClickListener{

    private RecyclerView mRecommendView;
    private VideoIndexAdapter mVideoIndexAdapter;
    private List<VideoIndexEntity> mList = new ArrayList<>();
    private String topicText;
    private String topicId;
    private VideoPublishPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_av_publish_layout);
        setTitleBarLeftBtnImage(R.drawable.icon_video_black_close_selector);
        setTitleBarLeftBtnListener(this);
        setTitle(getString(R.string.av_publish_title));
        topicText = getIntent().getStringExtra(IntentConstants.VIDEO_TOPIC);
        topicId = getIntent().getStringExtra(IntentConstants.VIDEO_TOPIC_ID);
        initView();
        initRecyclerView();
        initData();

    }

    private void initData() {
        mPresenter = new VideoPublishPresenter(this, this);
        mPresenter.getVideoIndexList(topicId);
    }

    private void initView() {
        mRecommendView = (RecyclerView) findViewById(R.id.recommend_recycle_view);
    }



    private void initRecyclerView() {
        mVideoIndexAdapter = new VideoIndexAdapter(this, mList, topicText);
        mVideoIndexAdapter.addListener(new VideoIndexAdapter.OnVideoItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                VideoIndexEntity entity = mList.get(position);
                ActivityRedirectUtil.gotoAvPlayActivity(AVPublishActivity.this, entity);
            }
        });

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mVideoIndexAdapter.isHeader(position) ? layoutManager.getSpanCount() : 1;
            }
        });
        mRecommendView.setLayoutManager(layoutManager);
        mRecommendView.setAdapter(mVideoIndexAdapter);
    }


    @Override
    public void showSimilarVideos(List<VideoIndexEntity> list) {
        mList.clear();
        mList.addAll(list);
        mVideoIndexAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.zhenai_library_slide_out_bottom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zhenai_lib_titlebar_left_text:
                finish();
                break;
        }
    }
}
