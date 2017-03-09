package com.bluewhaledt.saylove.util;

/**
 * Created by zhenai-liliyan on 16/12/26.
 */

public class NumberUtil {

    public static String changeNumber(float num){
        int temp = (int) num;
        if (num == temp){
            return temp + "";
        }else {
            return num + "";
        }
    }
}
