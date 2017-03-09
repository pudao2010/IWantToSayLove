package com.bluewhaledt.saylove.ui.recommend.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.ArrayList;

/**
 * 描述：TODO
 * 作者：shiming_li
 * 时间：2016/12/5 11:27
 * 包名：com.zhenai.saylove_icon.ui.recommend.entity
 * 项目名：SayLove
 */
public class RecommendEntity extends BaseEntity{



        public boolean hasNext;
    
        public ArrayList<ListBean> list;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }


    public  class ListBean extends BaseEntity {
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
        public void setLikeId(int mlikeId){
            likeId=mlikeId;
        }

        @Override
        public String[] uniqueKey() {
            return new String[]{userId + ""};
        }
    }
}
