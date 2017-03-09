package com.bluewhaledt.saylove.photo.impl;

/**
 * Created by rade.chan on 2016/12/3.
 */

public interface IPhotoType {

    String KEY_PHOTO_NAMES = "photoNames";            //照片名字
    String KEY_PHOTO_TYPE = "type";                         //照片类型        1普通找 2头像 3学历证 4车证 5房产证
    String KEY_ACADEMY = "academy";                   //院校对应的code
    String KEY_EDUCATION = "education";               //学历对应的code
    String KEY_CAR_BRAND = "brand";                   //车品牌名字
    String KEY_HOUSE_CITY = "city";                               //房产证地址对应的城市code


    int PHOTO_TYPE_COMMON = 1;                  //普通照
    int PHOTO_TYPE_AVATAR = 2;                  //头像
    int PHOTO_TYPE_EDUCATION = 3;               //学历
    int PHOTO_TYPE_CAR = 4;                     //车照
    int PHOTO_TYPE_HOUSE = 5;                   //房产
}
