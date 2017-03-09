package com.bluewhaledt.saylove.ui.register_login.regist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
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
 * @文件名: GenderSelectFragment
 * @创建者: YanChao
 * @创建时间: 2016/11/28 20:16
 * @描述： 性别选择页
 */
public class GenderSelectFragment extends ProfileEditBaseFragment implements View.OnClickListener {

    private TextView mTvGender;
    private ImageView mTvMale;
    private ImageView mTvFemale;
    private Bundle bundle;
    private int gender = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.RegisterAndLoginPage.GENDER_SELECT_PAGE);
    }

    @Override
    protected int getLayoutResouces() {
        return R.layout.fragment_gender_layout;
    }

    @Override
    protected void initView() {
        assignViews();
    }

    protected void initData() {
        bundle = new Bundle();
        PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.GENDER,gender);
    }

    protected void initListener() {
        mTvMale.setOnClickListener(this);
        mTvFemale.setOnClickListener(this);
    }

    private void assignViews() {
        View view = getView();
        mTvGender = (TextView) view.findViewById(R.id.tv_gender);
        mTvMale = (ImageView)  view.findViewById(R.id.iv_male);
        mTvFemale = (ImageView)  view.findViewById(R.id.iv_female);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.iv_male:
                gender = 1;
                mTvMale.setSelected(true);
                mTvFemale.setSelected(false);
                break;
            case R.id.iv_female:
                gender = 2;
                mTvFemale.setSelected(true);
                mTvMale.setSelected(false);
                break;
        }
                bundle.putInt(Constants.GENDER, gender);
                startFragment(CitySelectFragment.class,bundle);
                PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.GENDER,gender);
    }
}
