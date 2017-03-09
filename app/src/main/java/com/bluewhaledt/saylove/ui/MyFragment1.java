package com.bluewhaledt.saylove.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.ui.tab.TabBaseFragment;

/**
 * Created by zhenai-liliyan on 16/11/9.
 */

public class MyFragment1 extends TabBaseFragment {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showTitleBar(false);
        Log.d(TAG, "========createView");
        TextView tv = new TextView(getActivity());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        tv.setBackgroundColor(Color.WHITE);
        tv.setText(TAG);
        tv.setTextSize(80);
        tv.setTextColor(Color.BLACK);
        tv.setLayoutParams(lp);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        return tv;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "========onActivityResult:resultCode=" + resultCode + ";intentData = " + data);
    }

    @Override
    protected void onFirstUserVisible() {
        DebugUtils.d(TAG, "========loadData");
    }

    @Override
    public void onPauseFragment() {
        Log.d(TAG, "========onPause");
    }

    @Override
    public void onResumeFragment() {
        Log.d(TAG, "========onResume");
    }

    @Override
    public void onStopFragment() {
        Log.d(TAG, "========onStop");
    }

    @Override
    public void onDestroyFragment() {
        Log.d(TAG, "========onDestroy");
    }

    @Override
    public void onStartFragment() {
        Log.d(TAG, "========onStart");
    }

}
