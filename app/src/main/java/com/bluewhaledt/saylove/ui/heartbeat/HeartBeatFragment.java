package com.bluewhaledt.saylove.ui.heartbeat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseFragment;
import com.bluewhaledt.saylove.base.widget.tablayout.SegmentTabLayout;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/6.
 */

public class HeartBeatFragment extends BaseFragment {

    private SegmentTabLayout tabLayout;

    private String[] titles;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_heart_beat,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(R.string.fragment_msg_of_heart_beat_title);
        titles = new String[]{getResources().getString(R.string.fragment_heart_beat_tab_1),
                getResources().getString(R.string.fragment_heart_beat_tab_2)};
        tabLayout = (SegmentTabLayout) getView().findViewById(R.id.tabs_container);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HeartBeatToMeFragment());
        fragments.add(new MyHeartBeatRecordFragment());
        tabLayout.setTabData(titles,getActivity(),R.id.fl_container,fragments);
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
}
