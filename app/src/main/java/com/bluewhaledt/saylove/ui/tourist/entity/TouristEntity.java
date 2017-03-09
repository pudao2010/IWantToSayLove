package com.bluewhaledt.saylove.ui.tourist.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.ArrayList;

/**
 * 描述：entity
 * 作者：shiming_li
 * 时间：2016/12/12 14:41
 * 包名：com.zhenai.saylove_icon.ui.tourist.entity
 * 项目名：SayLove
 */
public class TouristEntity extends BaseEntity {
       public boolean hasNext;
    public ArrayList<TouristEntityListBean> list;
    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
    public  class TouristEntityListBean extends BaseEntity{
        public int age;
        public String avatar;
        public String degree;
        public String height;
        public String imAccId;
        public boolean isCar;
        public boolean isDegree;
        public boolean isHouse;
        public boolean isVIP;
        public boolean isZM;
        public int likeId;
        public String nickName;
        public String salary;
        public int sex;
        public int userId;
        public String workcity;

        @Override
        public String[] uniqueKey() {
            return new String[]{userId+""};
        }
    }
}
