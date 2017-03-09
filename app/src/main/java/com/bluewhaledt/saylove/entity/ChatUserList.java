package com.bluewhaledt.saylove.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/5.
 */

public class ChatUserList extends BaseEntity {

    public boolean hasNext;

    public ArrayList<ChatUser> list;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }

    public static class ChatUser extends BaseEntity{

        public int age;

        public String avatar;

        public String nickname;

        public boolean readable;

        public String salary;

        public long userId;

        public boolean vip;

        public String workCity;

        public int sex;

        @Override
        public String[] uniqueKey() {
            return new String[]{userId + ""};
        }
    }
}
