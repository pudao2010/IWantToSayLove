package com.bluewhaledt.saylove.util;

import com.baidu.location.BDLocation;

/**
 * Created by rade.chan on 2016/11/21.
 */

public class LocationEvent {

    private BDLocation bdLocation;

    public LocationEvent(BDLocation location) {
        this.bdLocation = location;
    }

    public BDLocation getBdLocation() {
        return bdLocation;
    }
}
