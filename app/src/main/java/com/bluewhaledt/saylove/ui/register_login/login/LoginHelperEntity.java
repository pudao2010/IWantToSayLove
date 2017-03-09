package com.bluewhaledt.saylove.ui.register_login.login;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * @项目名： SayLove
 * @包名： com.zhenai.saylove_icon.ui.register_login.login
 * @文件名: LoginHelperEntity
 * @创建者: YanChao
 * @创建时间: 2016/11/26 14:22
 * @描述： 登录成功的实体返回
 */
public class LoginHelperEntity extends BaseEntity{


    /**
     * avatar :
     * nickName : 会员105129
     * sex : 1
     * uploadAvatar : false
     * userId : 105129
     * verify : false
     * vip : false
     */

    public String avatar;
    public String nickName;
    public int sex;
    public boolean uploadAvatar;
    public int userId;
    public boolean verify;
    public boolean vip;
    public int avatarStatus;
    public boolean popCertificate;
    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
