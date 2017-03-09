package com.bluewhaledt.saylove.ui.tourist;

import android.os.Bundle;

import com.bluewhaledt.saylove.base.activity.BaseActivity;

/**
 * 描述：游客模式
 * 作者：shiming_li
 * 时间：2016/12/16 14:19
 * 包名：com.zhenai.saylove.ui.tourist
 * 项目名：sayLove
 */
public class TouristDetailActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitleBar(false);
        setContentView(new TouristDetailFragment());
    }
}
