package com.bluewhaledt.saylove.ui.register_login.regist;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login.Fragment
 * @文件名: IncomeFragment
 * @创建者: YanChao
 * @创建时间: 2016/11/29 13:49
 * @描述： TODO
 */
public class IncomeFragment extends ProfileEditBaseFragment implements View.OnClickListener {

    private TextView mTvIncome;
    private Button mSalary10w;
    private Button mSalary20w;
    private Button mSalary30w;
    private Button mSalary50w;
    private Button mSalary100w;
    private Button mSalaryMax;
    private HashMap<Integer, Button> buttonHashMap;
    private Bundle arguments;
    private Intent intent;
    private int salary = 0;
    private Set<Map.Entry<Integer, Button>> entries;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventStatistics.recordLog(ResourceKey.REGISTER_AND_LOGIN_PAGE, ResourceKey.RegisterAndLoginPage.SALARY_SELECT_PAGE);
    }

    @Override
    protected int getLayoutResouces() {
        return R.layout.fragment_income_layout;
    }

    @Override
    protected void initView() {
        assignViews();
    }

    protected void initData() {
        arguments = getArguments();
        buttonHashMap = new HashMap<Integer,Button>();
        buttonHashMap.put(R.id.salary_10w,mSalary10w);
        buttonHashMap.put(R.id.salary_20w,mSalary20w);
        buttonHashMap.put(R.id.salary_30w,mSalary30w);
        buttonHashMap.put(R.id.salary_50w,mSalary50w);
        buttonHashMap.put(R.id.salary_100w,mSalary100w);
        buttonHashMap.put(R.id.salary_max,mSalaryMax);
        entries = buttonHashMap.entrySet();
    }

    protected void initListener() {
        mSalary10w.setOnClickListener(this);
        mSalary20w.setOnClickListener(this);
        mSalary30w.setOnClickListener(this);
        mSalary50w.setOnClickListener(this);
        mSalary100w.setOnClickListener(this);
        mSalaryMax.setOnClickListener(this);
    }

    private void assignViews() {
        View view = getView();
        mTvIncome = (TextView) view.findViewById(R.id.tv_income);
        mSalary10w = (Button) view.findViewById(R.id.salary_10w);
        mSalary20w = (Button) view.findViewById(R.id.salary_20w);
        mSalary30w = (Button) view.findViewById(R.id.salary_30w);
        mSalary50w = (Button) view.findViewById(R.id.salary_50w);
        mSalary100w = (Button) view.findViewById(R.id.salary_100w);
        mSalaryMax = (Button) view.findViewById(R.id.salary_max);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        selectButton(id);
        switch (id) {
            case R.id.salary_10w:
                salary = 10;
                break;
            case R.id.salary_20w:
                salary = 20;
                break;
            case R.id.salary_30w:
                salary = 30;
                break;
            case R.id.salary_50w:
                salary = 50;
                break;
            case R.id.salary_100w:
                salary = 100;
                break;
            case R.id.salary_max:
                salary = 101;
                break;
        }
        selectButton(id);
        arguments.putInt(Constants.SALARY, salary);
        PreferenceUtil.saveValue(PreferenceFileNames.USER_CONFIG, PreferenceKeys.SALARY, salary);
        startFragment(RegisterFragment.class, arguments);
    }

    private void selectButton(int id) {
        for (Map.Entry<Integer, Button> entry : entries) {
            if (entry.getKey() == id) {
                entry.getValue().setSelected(true);
                continue;
            }
            entry.getValue().setSelected(false);
        }
    }
}
