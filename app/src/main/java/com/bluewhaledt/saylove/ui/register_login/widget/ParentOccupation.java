package com.bluewhaledt.saylove.ui.register_login.widget;

import java.util.List;

/**
 * Created by rade.chan on 2016/11/11.
 */

public class ParentOccupation implements IWheelViewData {
    public int key;
    public String value;
    public List<ChildOccupation> childOccupations;
    @Override
    public String getDisplayName() {
        return value;
    }
}
