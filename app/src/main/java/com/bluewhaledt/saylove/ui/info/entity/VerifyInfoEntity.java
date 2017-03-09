package com.bluewhaledt.saylove.ui.info.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by rade.chan on 2016/12/5.
 */

public class VerifyInfoEntity extends BaseEntity{

    public boolean me;                                        //是否自己 0否 1是
    public boolean verifiedIdentity;                       //是否认证身份证 true认证 false否
    public String idcardNo="";                                 //身份证号码
    public String name ="";                                     //名字
    public boolean verifiedDegree;                         //是否已认证学历
    public String academy="";                                  //学校名称
    public int degreeVerifyStatus;                         //学历审核状态 学历审核状态1审核中 2通过 3不通过
    public String degreeVerifyStatusDesc="";                  //学历审核状态描述
    public boolean verifiedCar;                             //是否已认证车
    public String brand="";                                     //车品牌
    public int carVerifyStatus;                             //车审核状态
    public String carVerifyStatusDesc="";                      //是否已认证描述
    public boolean verifiedHouse;                           //是否已认证房产证
    public String house="";                                     //房产证的地址
    public int houseVerifyStatus;                           //审核房产证状态
    public String houseVerifyStatusDesc="";                    //审核房产证状态描述
    public String identityTime="";                             //身份证认证时间
    public String degreeTime="";                               //学历认证时间
    public String carTime="";                                  //车证明认证时间
    public String houseTime="";                                //房产证认证时间
    public long carId; //车认证信息id
    public long houseId;//房产证认证信息id
    public long degreeId;//学历认证信息id
    public String carPicUrl="";//车认证照片url
    public String degreePicUrl="";//学历认证照片url
    public String housePicUrl="";//房产证认证照url

    public String educationDesc="";//学历描述
    public int education;//学历code
    public int city;//城市code




    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
