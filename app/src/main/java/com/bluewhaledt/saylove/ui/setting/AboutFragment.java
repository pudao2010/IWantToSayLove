package com.bluewhaledt.saylove.ui.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.base.activity.BaseFragment;

/**
 * Created by rade.chan on 2016/12/2.
 */

public class AboutFragment extends BaseFragment {


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_app_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(R.string.about_say_love);

        TextView versionView = (TextView) view.findViewById(R.id.version_view);

        try {
            String versionName = ZhenaiApplication.getContext()
                    .getPackageManager().getPackageInfo(
                            ZhenaiApplication.getContext().getPackageName(), 0).versionName;
            versionView.setText(getString(R.string.current_version) + "    v" + versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
