package com.bluewhaledt.saylove.widget.linear_view.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/7.
 */

public class ResultEntity<T> extends BaseEntity {

    public int count;

    public String accessKeyID;

    public String accessKeySecret;

    public boolean hasNext;

    public ArrayList<T> list;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
