package com.bluewhaledt.saylove.ui.register_login.widget;

/**
 * 二级地区（省份的地级市、直辖市的区）
 *
 * @author yintaibing
 * @date 2016/11/3
 */
public class City implements IWheelViewData {
    public int key;
    public String value;

    @Override
    public String getDisplayName() {
        return value;
    }
}
