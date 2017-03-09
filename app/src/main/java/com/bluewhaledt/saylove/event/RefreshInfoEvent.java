package com.bluewhaledt.saylove.event;

/**
 * Created by rade.chan on 2016/12/6.
 */

public class RefreshInfoEvent {

    public final static int LOAD_ALL_INFO=1;     //加载全部资料
    public final static int LOAD_BASIC_INFO=2;  //加载基本资料
    public final static int LOAD_PHOTOS=3;      //加载图片
    public final static int LOAD_VERIFY=4;      //加载认证
    public final static int LOAD_VIDEO=5;       //加载小视频

    private int loadType;

    public RefreshInfoEvent(int loadType) {
        this.loadType = loadType;
    }

    public int  getLoadType() {
        return loadType;
    }
}
