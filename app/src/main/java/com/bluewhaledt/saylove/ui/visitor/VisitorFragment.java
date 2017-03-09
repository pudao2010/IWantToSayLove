package com.bluewhaledt.saylove.ui.visitor;

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
 * Created by zhenai-liliyan on 16/12/9.
 */

public class VisitorFragment extends BaseFragment {

    private SegmentTabLayout tabLayout;

    private String[] titles;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visitor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(R.string.fragment_visitor_title);

        titles = new String[]{getResources().getString(R.string.fragment_visitor_tab1),
                getResources().getString(R.string.fragment_visitor_tab2)};

        tabLayout = (SegmentTabLayout) getView().findViewById(R.id.tabs_container);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new VisitToMeFragment());
        fragments.add(new MyVisitToFragment());
        tabLayout.setTabData(titles, getActivity(), R.id.fl_container, fragments);
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
