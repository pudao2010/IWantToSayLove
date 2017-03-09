package com.bluewhaledt.saylove.ui.info.entity;

import android.text.TextUtils;

import com.bluewhaledt.saylove.R;
import com.bluewhaledt.saylove.ZhenaiApplication;
import com.bluewhaledt.saylove.constant.Constants;
import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by rade.chan on 2016/12/1.
 */

public class RequireInfoEntity extends BaseEntity {

    private int invalidValue = Constants.INFO_VALUE_INVALID;


    public int minAge = invalidValue;                               //年龄下限
    public int maxAge = invalidValue;                               //年龄上限
    public int minHeight = invalidValue;                            //身高下限
    public int maxHeight = invalidValue;                            //身高上限
    public int salary;                                            //年收入
    public int workCity;                                            //工作城市
    public int hometown;                                          //籍贯
    public int marryState;                                         //婚姻状况

    public String salaryDesc;                                       //薪资描述
    public String workCityDesc;                                     //工作地描述
    public String hometownDesc;                                   //籍贯描述
    public String marryDesc;                                        //婚姻状态描述

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }


    public String getAge() {
        if (minAge == -1 && maxAge == -1) {
            return getDefaultText();
        } else if (minAge == -1) {
            return maxAge + "岁以下";
        } else if (maxAge == -1) {
            return minAge + "岁以上";
        } else {
            return minAge + "-" + maxAge + ZhenaiApplication.getInstance().getString(R.string.age_unit);
        }

    }

    public String getHeight() {
        if (minHeight == -1 && maxHeight == -1) {
            return getDefaultText();
        } else if (minHeight == -1) {
            return maxHeight + "cm以下";
        } else if (maxHeight == -1) {
            return minHeight + "cm以上";
        } else {
            return minHeight + "-" + maxHeight + ZhenaiApplication.getInstance().getString(R.string.height_unit);
        }
    }

    public String getSalary() {
//        if (minSalary > invalidValue) {
//            String salaryValue = DictionaryUtil.getSingleValueByKey(DataDictionaryHelper.SALARY, minSalary);
//            if (!TextUtils.isEmpty(salaryValue)) {
//                return salaryValue;
//            }
//        }
        if (!TextUtils.isEmpty(salaryDesc)) {
            return salaryDesc;
        }
        return getDefaultText();
    }

    public String getWorkCity() {
//        if (workCity > invalidValue) {
//            String cityValue = DictionaryUtil.getLocationByCode(workCity);
//            if (!TextUtils.isEmpty(cityValue)) {
//                return cityValue;
//            }
//        }
        if (!TextUtils.isEmpty(workCityDesc)) {
            return workCityDesc;
        }
        return getDefaultText();
    }

    public String getNativePlace() {
//        if (nativeCity > invalidValue) {
//            String nativeValue = DictionaryUtil.getLocationByCode(nativeCity);
//            if (!TextUtils.isEmpty(nativeValue)) {
//                return nativeValue;
//            }
//        }
        if (!TextUtils.isEmpty(hometownDesc)) {
            return hometownDesc;
        }
        return getDefaultText();
    }


    public String getMarryState() {
//        if (marryState > invalidValue) {
//            String marryValue = DictionaryUtil.getSingleValueByKey(DataDictionaryHelper.MARRIAGE, marryState);
//            if (!TextUtils.isEmpty(marryValue)) {
//                return marryValue;
//            }
//        }
        if (!TextUtils.isEmpty(marryDesc)) {
            return marryDesc;
        }
        return getDefaultText();
    }

    public String getDefaultText() {
        return ZhenaiApplication.getInstance().getString(R.string.select_nothing);
    }
}
