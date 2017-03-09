package com.bluewhaledt.saylove.event;

/**
 * Created by rade.chan on 2016/12/22.
 */

public class CameraOpenFailEvent {
    private boolean isOpenFail;

    public CameraOpenFailEvent(boolean isOpenFail) {
        this.isOpenFail = isOpenFail;
    }

    public boolean isOpenFail() {
        return isOpenFail;
    }
}
