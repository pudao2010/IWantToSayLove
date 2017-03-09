package com.bluewhaledt.saylove.ui.register_login.real_name.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

/**
 * 描述：zhima
 * 作者：shiming_li
 * 时间：2016/12/5 10:17
 * 包名：com.zhenai.saylove_icon.ui.register_login.real_name.entity
 * 项目名：SayLove
 */
public class ZhimaEntity extends BaseEntity{


    public String sign;
    public String token;
    public int resultType;
    public String app_id;
    public String params;

//    public ZhimaEntity(JSONObject json) {
//        if (json != null) {
//            //url看不到明显的地址
////            otherMemberId = json.optString("otherMemberId");
//            resultType = json.optInt("resultType",-1);
//            token = json.optString("token","");
//            try{
//                sign = URLDecoder.decode(json.optString("sign"),"UTF-8");
//                app_id = URLDecoder.decode(json.optString("app_id"),"UTF-8");
//                params = URLDecoder.decode(json.optString("params"),"UTF-8");
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//
//
//        }
//    }
    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
