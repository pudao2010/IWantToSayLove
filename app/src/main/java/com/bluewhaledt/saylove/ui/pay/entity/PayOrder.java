package com.bluewhaledt.saylove.ui.pay.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;
import com.google.gson.annotations.SerializedName;

public class PayOrder extends BaseEntity {
    /**
     * 订单时间
     */
    public String orderDate;
    /**
     * 要提交到tcl unionpay的请求报文
     */
    public String submitUrl;

    public String order_id;

    public String mEasyPayRequestPay;
    /**
     * 请求结果: 1>>成功; -1>>失败; -11>>异常
     */
    public int status;

    /**
     * 提示信息 ，没有可以为空
     */
    public String msg;

    /**
     * 未签名前订单
     */
    public String signData;

    /**
     * 签名后订单
     */
    public String sign;
    public String info;
    public String htmlText;

    public String orderId;

    public boolean hasPayIn24Hours;//是否24小时内购买了珍心会员 true=是 false=否
    public String windowContent;//弹窗内容

    public String appid;
    public String appkey;
    public String partnerid;
    public String prepayid;
    public String noncestr;
    public String timestamp;
    @SerializedName("package")
    public String packageValue;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
