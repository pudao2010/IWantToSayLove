package com.bluewhaledt.saylove.ui.register_login.login;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: LoginEntity
 * @创建者: YanChao
 * @创建时间: 2016/12/2 18:12
 * @描述： TODO
 */
public class LoginEntity extends BaseEntity{


    /**
     * msg : 登录成功
     */

    public String msg;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
