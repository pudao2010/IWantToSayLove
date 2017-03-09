package com.bluewhaledt.saylove.ui.register_login.Head_portrait.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.ArrayList;

/**
 * 描述：随即昵称
 * 作者：shiming_li
 * 时间：2016/12/7 21:26
 * 包名：com.zhenai.saylove_icon.ui.register_login.Head_portrait.entity
 * 项目名：SayLove
 */
public class RandomNameEntity extends BaseEntity{

    public ArrayList<String> names;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
