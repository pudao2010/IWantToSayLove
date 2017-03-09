package com.bluewhaledt.saylove.ui.visitor.entiry;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * Created by zhenai-liliyan on 16/12/9.
 */

public class Visitor extends BaseEntity {

    public int age;

    public String nickName;

    public long objectId;

    public boolean read;

    public String salary;

    public String workCity;

    public String avatar;

    public String viewTime;

    //性别 1男 2女
    public int sex;

    public Visitor(){

    }

    public Visitor(long objectId){
        this.objectId = objectId;
    }

    @Override
    public String[] uniqueKey() {
        return new String[]{objectId + ""};
    }
}
