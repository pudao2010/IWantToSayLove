package com.bluewhaledt.saylove.ui.register_login.real_name.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * 描述：TODO
 * 作者：shiming_li
 * 时间：2016/12/23 15:12
 * 包名：com.zhenai.saylove.ui.register_login.real_name.entity
 * 项目名：sayLove
 */
public class VerifyTipsEntity extends BaseEntity{
    public String tips;
    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
