package com.bluewhaledt.saylove.ui.tab;

import com.bluewhaledt.saylove.photo.BasePhotoFragment;

/**
 * Created by rade.chan on 2016/11/29.
 */
public abstract  class TabPhotoBaseFragment extends BasePhotoFragment{

    protected ITabAction mTabAction;

    public void setTabAction(ITabAction tabAction) {
        this.mTabAction = tabAction;
    }

    /**
     * 是否已经准备过
     */
    protected boolean isPrepared;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isPrepared) {
            onFirstUserVisible();
            isPrepared = true;
        }
    }

    /**
     * 当页面第一次可见时调用此方法，可以完成数据加载
     */
    protected abstract void onFirstUserVisible();
}
