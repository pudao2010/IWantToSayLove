package com.bluewhaledt.saylove.ui.info.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluewhaledt.saylove.constant.IntentConstants;
import com.bluewhaledt.saylove.photo.BasePhotoFragment;


/**
 * Created by rade.chan on 2016/11/29.
 */

public abstract  class BaseProfileFragment extends BasePhotoFragment implements ISourceFrom , IModifyKey{
    protected int sourceFrom;
    protected View mContentView;
    protected boolean isModify;
    protected String currentValue;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(getContentRes(), container, false);
        }
        if (getArguments() != null) {
            sourceFrom = getArguments().getInt(IntentConstants.MODIFY_FROM);
            currentValue=getArguments().getString(IntentConstants.CURRENT_VALUE);
        }
        return mContentView;
    }

    public abstract int getContentRes();

    @Override
    public void onStartFragment() {
        isModify=false;
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
    public void getPhotoSuccess(String path) {

    }


    @Override
    public void getPhotoFail() {

    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T find(int id) {
        return (T) mContentView.findViewById(id);
    }
}
