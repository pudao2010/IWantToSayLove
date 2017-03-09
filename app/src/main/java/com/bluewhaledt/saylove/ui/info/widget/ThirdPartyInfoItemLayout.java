package com.bluewhaledt.saylove.ui.info.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ui.info.adapter.ThirdPartyInfoAdapter;
import com.bluewhaledt.saylove.ui.info.entity.ItemInfoEntity;
import com.bluewhaledt.saylove.ui.recommend.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ThirdPartyInfoItemLayout extends FrameLayout {

    private TextView mTitleView;
    private RecyclerView mRecyclerView;
    private ThirdPartyInfoAdapter mThirdPartyInfoAdapter;
    private List<ItemInfoEntity> mList = new ArrayList<>();
    private Context mContext;

    public ThirdPartyInfoItemLayout(Context context) {
        this(context, null);
    }

    public ThirdPartyInfoItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ThirdPartyInfoItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_third_party_info_layout, this);
        initView(view);
    }

    private void initView(View rootView) {
        mTitleView = (TextView) rootView.findViewById(R.id.title_view);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.info_recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(mContext.getResources().getDimensionPixelSize(R.dimen.info_item_between_dimen)));
        mThirdPartyInfoAdapter = new ThirdPartyInfoAdapter(mContext, mList);

    }

    public void addItem(List<ItemInfoEntity> list, String title) {
        mList.clear();
        mList.addAll(list);
        if (TextUtils.isEmpty(title)) {
            mTitleView.setVisibility(View.GONE);
        } else {
            mTitleView.setText(title);
        }
        mRecyclerView.setAdapter(mThirdPartyInfoAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
    }


}
