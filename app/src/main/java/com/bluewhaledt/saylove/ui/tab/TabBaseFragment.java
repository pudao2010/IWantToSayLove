package com.bluewhaledt.saylove.ui.tab;

import com.bluewhaledt.saylove.base.activity.BaseFragment;

/**
 * Created by zhenai-liliyan on 16/11/15.
 */

public abstract class TabBaseFragment extends BaseFragment {

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
        if (isVisibleToUser){
            onResumeFragment();
        }else{
            onPauseFragment();
            onStopFragment();
        }
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
