package com.bluewhaledt.saylove.ui.register_login.regist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.constant.ResourceKey;
import com.bluewhaledt.saylove.ui.register_login.ProfileEditBaseFragment;
import com.bluewhaledt.saylove.util.EventStatistics;
import com.bluewhaledt.saylove.util.PreferenceFileNames;
import com.bluewhaledt.saylove.util.PreferenceKeys;
import com.bluewhaledt.saylove.util.PreferenceUtil;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login.Fragment
 * @文件名: MaritalStatusFragment
 * @创建者: YanChao
 * @创建时间: 2016/11/29 12:54
 * @描述： TODO
 */
public class MaritalStatusFragment extends ProfileEditBaseFragment implements View.OnClickListener {

    private TextView mTvMarital;
    private Button mMaritalNo;
    private Button mDivoce;
    private Button mWidowed;
    private Bundle arguments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.RegisterAndLoginPage.MARITAL_SELECT_PAGE);
    }

    @Override
    protected int getLayoutResouces() {
        return R.layout.fragment_marital_layout;
    }

    @Override
    protected void initView() {
        assignViews();
    }

    protected void initListener() {
        mMaritalNo.setOnClickListener(this);
        mDivoce.setOnClickListener(this);
        mWidowed.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        arguments = getArguments();
    }


    private void assignViews() {
        View view = getView();
        mTvMarital = (TextView) view.findViewById(R.id.tv_marital);
        mMaritalNo = (Button) view.findViewById(R.id.marital_no);
        mDivoce = (Button) view.findViewById(R.id.divoce);
        mWidowed = (Button) view.findViewById(R.id.widowed);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        int marital = -1;
        switch (id) {
            case R.id.marital_no:
                mMaritalNo.setSelected(true);
                mDivoce.setSelected(false);
                mWidowed.setSelected(false);
                marital = 1;
                break;
            case R.id.divoce:
                mMaritalNo.setSelected(false);
                mDivoce.setSelected(true);
                mWidowed.setSelected(false);
                marital = 3;
                break;
            case R.id.widowed:
                mMaritalNo.setSelected(false);
                mDivoce.setSelected(false);
                mWidowed.setSelected(true);
                marital = 4;
                break;
        }

        arguments.putInt(Constants.MARITAL, marital);
        PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.MARITAL,marital);
        startFragment(IncomeFragment.class,arguments );
    }
}
