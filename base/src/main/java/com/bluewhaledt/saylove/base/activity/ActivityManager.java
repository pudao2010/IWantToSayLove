package com.bluewhaledt.saylove.base.activity;

import java.util.Stack;

public class ActivityManager {
    private Stack<BaseActivity> activityStack;
    private static ActivityManager activityManager;

    private ActivityManager() {

    }

    public static synchronized ActivityManager getInstance() {
        if (activityManager == null) {
            activityManager = new ActivityManager();
        }
        return activityManager;
    }

    public void addActivity(BaseActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<BaseActivity>();
        }
        activityStack.add(activity);
    }

    public void removeActivity(BaseActivity activity) {
        if (activityStack == null) {
            return;
        }
        activityStack.remove(activity);
        if (!activity.isFinishing()) {
            activity.finish();
        }
    }

    public BaseActivity getCurrentActivity() {
        return activityStack.lastElement();
    }

    public void closeAllActivity(boolean isFullExit) {
        if (activityStack != null) {
            for (int i = 0, j = activityStack.size(); i < j; i++) {
                if (activityStack.get(i) != null) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
        if (isFullExit)
            System.exit(0);
    }

    public Stack<BaseActivity> getActivityStack(){
        return activityStack;
    }

    public int getCount() {
        return activityStack.size();
    }


}
