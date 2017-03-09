package com.bluewhaledt.saylove.ui.pay.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.ArrayList;

/**
 * Created by zhenai-liliyan on 16/12/6.
 */

public class PurchasePageData extends BaseEntity {

    public String nickName;

    public String avatar;

    public boolean isVip;

    /**
     * VIP服务期
     */
    public String vipServiceTime;

    /**
     * 性别，1：男； 2：女
     */
    public int sex;

    /**
     * 产品列表
     */
    public ArrayList<Product> products;

    /**
     * 特权列表
     */
    public ArrayList<Privilege> privileges;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }

    public static class Product extends BaseEntity{

        /**
         * 产品ID
         */
        public int productId;

        /**
         * 产品名称
         */
        public String productName;

        /**
         * 价格
         */
        public float money;

        /**
         * 日均价格
         */
        public String dayPrice;

        /**
         * 数量或者月份
         */
        public int amount;

        /**
         * 单位：如 个月
         */
        public String uint;

        /**
         * 苹果产品ID
         */
        public String iosProductId;


        @Override
        public String[] uniqueKey() {
            return new String[0];
        }
    }

    public static class Privilege extends BaseEntity{

        /**
         * 特权名称
         */
        public String name;

        /**
         * 特权
         */
        public String desc;

        /**
         * 图标URL
         */
        public String icon;


        @Override
        public String[] uniqueKey() {
            return new String[0];
        }
    }
}
