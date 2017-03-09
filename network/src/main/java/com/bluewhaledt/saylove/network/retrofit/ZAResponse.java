package com.bluewhaledt.saylove.network.retrofit;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * @author chenzijin
 * @version 1.0.0
 * @date 2016/5/18
 */
public class ZAResponse<T> extends BaseEntity {

//    public T data;
//    public String msg;
//    public int status;
//    public String newStatus;           //服务端定义的额外状态值
    public T data;
    public String errorCode;
    public String errorMessage;
    public boolean isError ;
    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
