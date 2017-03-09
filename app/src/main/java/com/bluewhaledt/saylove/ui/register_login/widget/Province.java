package com.bluewhaledt.saylove.ui.register_login.widget;

import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.List;

/**
 * 省份
 *
 * @author yanchao
 * @date 2016/11/27
 */
public class Province extends BaseEntity implements IWheelViewData {
    public int key;
    public String value;
    public List<City> cities; // 二级地区（省份的地级市、直辖市的区）

    @Override
    public String getDisplayName() {
        return value;
    }

    @Override
    public String[] uniqueKey() {
        return new String[]{key + ""};
    }

}
