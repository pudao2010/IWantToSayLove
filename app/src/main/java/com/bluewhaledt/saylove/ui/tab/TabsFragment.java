package com.bluewhaledt.saylove.ui.tab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseFragment;
import com.bluewhaledt.saylove.base.widget.tablayout.CommonTabLayout;
import com.bluewhaledt.saylove.base.widget.tablayout.listener.CustomTabEntity;
import com.bluewhaledt.saylove.base.widget.tablayout.listener.OnTabSelectListener;
import com.bluewhaledt.saylove.constant.BroadcastActions;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.entity.TabEntity;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.ui.info.MyInfoFragment;
import com.bluewhaledt.saylove.ui.message.MessageFragment;
import com.bluewhaledt.saylove.ui.recommend.RecommendFragment;
import com.bluewhaledt.saylove.ui.system.SystemMessageActivity;
import com.bluewhaledt.saylove.ui.video.AVIndexFragment;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenai-liliyan on 16/11/10.
 */

public class TabsFragment extends BaseFragment implements ITabAction {

    public ViewPager mViewPager;
    public CommonTabLayout mTabLayout;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"推荐", "说爱", "消息", "我的"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] mIconSelectIds = {
            R.mipmap.icon_tab_home_selected, R.mipmap.icon_tab_video_selected,
            R.mipmap.icon_tab_msg_selected, R.mipmap.icon_tab_me_selected};
    private int[] mIconUnselectIds = {
            R.mipmap.icon_tab_home_noselected, R.mipmap.icon_tab_video_noselected,
            R.mipmap.icon_tab_msg_noselected, R.mipmap.icon_tab_me_noselected};

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IMUtil.addRecentContactObserver(recentContactObserver);

        showTitleBarLeftBtn(false);
        mViewPager = (ViewPager) getView().findViewById(R.id.view_container);
        mTabLayout = (CommonTabLayout) getView().findViewById(R.id.tabs_container);
        mViewPager.setOffscreenPageLimit(mTitles.length);
        for (int i = 0; i < mTitles.length; i++) {
            switch (i) {
                case 0:
                    mFragments.add(new RecommendFragment());
                    break;
                case 1:
                    mFragments.add(new AVIndexFragment());
                    break;
                case 2:
                    mFragments.add(new MessageFragment());
                    break;
                case 3:
                    MyInfoFragment myInfoFragment = new MyInfoFragment();
                    mFragments.add(myInfoFragment);
                    myInfoFragment.setTabAction(this);
                    break;
            }
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }


        mViewPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));

        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setTitle(mTitles[position]);
                showTitleBar(true);
                if (position == 0) {
                    EventStatistics.recordLog(ResourceKey.RECOMMEND_PAGE, ResourceKey.RecommendDetailPage.RECOMMEND_PAGE);
                    setMidelImgShow(true);
                } else {
                    setMidelImgShow(false);
                }

                if (position == 1) {
                    EventStatistics.recordLog(ResourceKey.VIDEO_INDEX_PAGE, ResourceKey.VIDEO_INDEX_PAGE);
                } else if (position == 2) {
                    IMUtil.login();
                    EventStatistics.recordLog(ResourceKey.MESSAGE_PAGE, ResourceKey.MessagePage.MESSAGE_PAGE);
                    setTitleBarRightBtnImage(R.mipmap.fragment_msg_title_bar_right);
                    setTitleBarRightBtnListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showRedDot(false);
                            Intent intent = new Intent(getActivity(), SystemMessageActivity.class);
                            startActivity(intent);
                        }
                    });
                    if (systemRedDot) {
                        showRedDot(true);
                    }

                } else if (position == 3) {
                    EventStatistics.recordLog(ResourceKey.MY_INFO_PAGE, ResourceKey.MY_INFO_PAGE);
                    showTitleBar(false);
                } else {
                    setTitleBarRightBtnImage(null);
                    showRedDot(false);
                }
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //设置未读消息红点
        updateMsgTabUnReadCount();

        mViewPager.setCurrentItem(0);
        EventStatistics.recordLog(ResourceKey.RECOMMEND_PAGE, ResourceKey.RecommendDetailPage.RECOMMEND_PAGE);
        setMidelImgShow(true);

        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(receiver, new IntentFilter(BroadcastActions.SHOW_SYSTEM_MESSAGE_RED_DOT));

    }


    private void updateMsgTabUnReadCount() {
        int count = IMUtil.getTotalUnreadCount();
        if (count > 0) {
            showRedDotCount(2, count);
        } else {
            hindRedDot(2);
        }

    }

    /**
     * 监听是否有未读消息并展示未读消息数
     */
    private Observer<List<RecentContact>> recentContactObserver = new Observer<List<RecentContact>>() {

        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            updateMsgTabUnReadCount();
        }
    };

    @Override
    public void showRedDot(int tabPosition) {
        mTabLayout.showDot(tabPosition);
    }

    @Override
    public void showRedDotCount(int tabPosition, int count) {
        mTabLayout.showMsg(tabPosition, count);
    }

    @Override
    public void hindRedDot(int tabPosition) {
        mTabLayout.hideMsg(tabPosition);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
    }

    @Override
    public void onStopFragment() {
    }

    @Override
    public void onDestroyFragment() {
        if (recentContactObserver != null) {
            IMUtil.removeRecentContactObserver(recentContactObserver);
        }
        if (receiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        }
    }

    @Override
    public void onStartFragment() {
    }

    private boolean systemRedDot;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BroadcastActions.SHOW_SYSTEM_MESSAGE_RED_DOT.equals(intent.getAction())) {
                if (mViewPager.getCurrentItem() == 2) {
                    showRedDot(true);
                } else {
                    systemRedDot = true;
                }
            }
        }
    };

}
