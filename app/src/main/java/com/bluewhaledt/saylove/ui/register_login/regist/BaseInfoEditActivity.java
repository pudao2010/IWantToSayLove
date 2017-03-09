package com.bluewhaledt.saylove.ui.register_login.regist;

import android.os.Bundle;

import com.bluewhaledt.saylove.ui.register_login.RegistAndLoginBaseActivity;


/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: BaseInfoEditActivity
 * @创建者: YanChao
 * @创建时间: 2016/11/28 19:54
 * @描述： TODO
 */
public class BaseInfoEditActivity extends RegistAndLoginBaseActivity{

    private GenderSelectFragment genderSelectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitleBar(false);
        genderSelectFragment = new GenderSelectFragment();
        setContentView(genderSelectFragment);
    }

}
