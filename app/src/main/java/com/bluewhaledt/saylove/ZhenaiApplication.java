package com.bluewhaledt.saylove;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.bluewhaledt.saylove.base.util.DebugUtils;
import com.bluewhaledt.saylove.im.IMUtil;
import com.bluewhaledt.saylove.util.FileConfig;

import java.util.List;

/**
 * @author hechunshan
 * @date 2016/11/9
 */
public class ZhenaiApplication extends Application {

    public static BDLocation mBDLocation;
    public static int mCityCode = 0;
    private String TAG = "ZhenaiApplication";

//    public static String otherSessionId = "54261@zhenai-saylove_icon";
//    public static String token = "21eea12efc84a95bc5fd339f0f712f01";
//    public static String otherSessionId = "54264@zhenai-saylove_icon";
//    public static String otherToken = "687413ab98e677a477d29f2c3879afe6";
//    public static boolean isMe = false;
//    public static String otherSessionId;
//    public static String token = "687413ab98e677a477d29f2c3879afe6";
//    public static String otherSessionId = "54261@zhenai-saylove_icon";
//    public static String otherToken = "21eea12efc84a95bc5fd339f0f712f01";


    private static ZhenaiApplication mInstance;

    private int screenWidth;
    private int screenHeight;

    public static ZhenaiApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        DebugUtils.setDebug(BuildConfig.DEBUG);
        FileConfig.initFolder();
        getScreenDimen();

        IMUtil.initIM(this);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (isMainProcessType()){
            MultiDex.install(this);
        }
    }


    private boolean isMainProcessType() {
        String processName = getCurrentProcessName();
        if (getPackageName().equals(processName)) {
            return true;
        } else {
            return false;
        }
    }

    private String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        if (infos != null && infos.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo appProcess : infos) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return "";
    }

    private void getScreenDimen() {
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

}
