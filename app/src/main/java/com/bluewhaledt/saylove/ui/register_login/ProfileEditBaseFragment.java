package com.bluewhaledt.saylove.ui.register_login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login
 * @文件名: ProfileEditBaseFragment
 * @创建者: YanChao
 * @创建时间: 2016/12/8 16:29
 * @描述： 资料编辑页的基类
 */
public abstract class ProfileEditBaseFragment extends RegistAndLoginBaseFragment{

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showTitleBar(true);
        setTitle("完善资料");
        showTitleBarUnderline(false);
    }
}
