package com.bluewhaledt.saylove.ui.register_login.widget;

/**
 * Created by rade.chan on 2016/11/11.
 */

public class ChildOccupation implements IWheelViewData {
    public int key;
    public String value;
    @Override
    public String getDisplayName() {
        return value;
    }
}
