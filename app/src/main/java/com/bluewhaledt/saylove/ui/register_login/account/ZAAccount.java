package com.bluewhaledt.saylove.ui.register_login.account;


import java.io.Serializable;

public class ZAAccount implements Serializable {
    public int uid;                   //用户id
    public boolean isVip;                   //是否为再婚会员
    public boolean uploadAvator ;                      //头像

    public boolean verify;

    public String avator;
    public String nickName;
    public int sex;//性别 1男 2女

    public int avatarStatus;//头像状态 1审核中 2通过 3不通过
    public boolean popCertificate;//是否开启认证引导弹窗
}
