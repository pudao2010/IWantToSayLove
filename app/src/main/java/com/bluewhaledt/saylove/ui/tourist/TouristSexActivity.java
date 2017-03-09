package com.bluewhaledt.saylove.ui.tourist;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.base.activity.BaseActivity;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.util.EventStatistics;

/**
 * 描述：逛一逛
 * 作者：shiming_li
 * 时间：2016/12/12 12:04
 * 包名：com.zhenai.saylove_icon.ui.tourist
 * 项目名：SayLove
 */
public class TouristSexActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mIv_male;
    private ImageView mIv_female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tourist_sex_layout);
        showTitleBar(true);
        setTitleBarLeftBtnListener(this);
        setTitle(R.string.recommend_tourist);
        initView();
        initListener();
        EventStatistics.recordLog(ResourceKey.TOURIST_CHOOSE_SEX_PAGE,ResourceKey.TouristChooseSexPage.TOURIST_CHOOSE_SEX_PAGE);

    }
    private void initListener() {
        mIv_male.setOnClickListener(this);
        mIv_female.setOnClickListener(this);
    }
    private void initView() {
        //nan
        mIv_male = (ImageView) findViewById(R.id.iv_male);
        mIv_female = (ImageView) findViewById(R.id.iv_female);
    }

    @Override
    public void onClick(View view) {
        Bundle arguments = new Bundle();
        switch (view.getId()){
            case R.id.iv_female:
                EventStatistics.recordLog(ResourceKey.TOURIST_CHOOSE_SEX_PAGE,ResourceKey.TouristChooseSexPage.TOURIST_SEX_NV);
                arguments.putString("tourist_which_male","2");
                startFragment(TouristDetailFragment.class,arguments);
//                finish();
                break;
            case R.id.iv_male://男
                EventStatistics.recordLog(ResourceKey.TOURIST_CHOOSE_SEX_PAGE,ResourceKey.TouristChooseSexPage.TOURIST_SEX_NAN);
                arguments.putString("tourist_which_male","1");
                startFragment(TouristDetailFragment.class,arguments);
//                finish();
                break;
            case R.id.zhenai_lib_titlebar_left_text:
                finish();
                break;
        }
    }
}
