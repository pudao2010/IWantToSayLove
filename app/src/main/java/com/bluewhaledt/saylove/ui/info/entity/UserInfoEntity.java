package com.bluewhaledt.saylove.ui.info.entity;

import android.text.TextUtils;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.bluewhaledt.saylove.util.DataDictionaryHelper;
import com.bluewhaledt.saylove.util.DictionaryUtil;

/**
 * Created by rade.chan on 2016/12/1.
 */

public class UserInfoEntity extends BaseEntity {

    private int invalidValue = Constants.INFO_VALUE_INVALID;

    public int drinkState;                       //是否喝酒code
    public int height;                              //身高code
    public int hometown;                            //籍贯code
    public int marryState;                         //婚姻状况code
    public int memberStatus;                       //会员状态code
    public int occupation;                          //职业code
    public int salary;                              //月薪code
    public int sex;                                 //性别，1男，2女
    public int smokeState = invalidValue;                          //是否吸烟 code
    public String userId;                            //用户id
    public int workcity;                            //工作地code
    public boolean me;                                //是否自己
    public int verifyAvatarStatus;                   //头像审核状态1审核中，2通过，3不通过

    public String drinkDesc;                        //喝酒描述
    public String haveChildDesc;                    //是否有小孩描述
    public String heightDesc;                       //身高描述
    public String hometownDesc;                     //家乡描述
    public String marryDesc;                        //婚姻描述
    public String occupationDesc;                   //职业描述
    public String salaryDesc;                       //薪资描述
    public String sexDesc;                          //性别描述
    public String workcityDesc;                     //工作地描述

    public String birthday;                         //生日
    public String introduce;                        //自我介绍
    public String avatarUrl;                        //头像地址

    public String nickName;                                                     //昵称
    public boolean vip;                             //是否vip
    public String imSessionId;                      //聊天sessionId
    public int haveChild;                           //是否有小孩code -1未选择 1有 2没有
    public String age;                              //岁数如：18岁
    public long likeId;                             //心动id，如果有点赞就有返回
    public boolean praise;                         //true赞过 false未赞
    public String finishedRate;                      //资料完善百分比
    @Override
    public String[] uniqueKey() {
        return new String[0];
    }


    public String getSmoking() {
        if (smokeState > invalidValue) {
            String smokeValue = DictionaryUtil.getSingleValueByKey(DataDictionaryHelper.SMOKING, smokeState);
            if (!TextUtils.isEmpty(smokeValue)) {
                return smokeValue;
            }
        }
        return getDefaultText();
    }

    private String getDefaultText() {
        return ZhenaiApplication.getInstance().getString(R.string.select_nothing);
    }
}
