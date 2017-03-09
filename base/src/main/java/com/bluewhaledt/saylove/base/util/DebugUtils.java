package com.bluewhaledt.saylove.base.util;

import android.util.Log;

/**
 * 调试信息工具类
 */
public class DebugUtils {

    private static final String TAG_DEBUG = "zhenai_app";
    
    private static boolean mDebug;

    public static void setDebug(boolean debug) {
        mDebug = debug;
    }

    /**
     * 打印调试信息.
     *
     * @param msg
     */
    public static void debug(String msg) {
        if (mDebug) {
            Log.d(TAG_DEBUG, msg);
        }
    }

    /**
     * 打印警告信息.
     *
     * @param msg
     */
    public static void warn(String msg) {
        if (mDebug) {
            Log.w(TAG_DEBUG, msg);
        }
    }

    /**
     * 打印提示信息.
     *
     * @param msg
     */
    public static void info(String msg) {
        if (mDebug) {
            Log.i(TAG_DEBUG, msg);
        }
    }

    /**
     * 打印错误信息.
     *
     * @param msg
     */
    public static void error(String msg, Exception e) {
        if (mDebug) {
            Log.w(TAG_DEBUG, msg, e);
//            printLog(Log.ERROR, tag, msg, e);
        }
    }

    public static void i(String tag, String info) {
        if (mDebug) {
            Log.i(tag, info);
        }
    }

    public static void w(String tag, String info) {
        if (mDebug) {
            Log.w(tag, info);
        }
    }

    public static void d(String tag, String info) {
        if (mDebug) {
            Log.d(tag, info);
//            printLog(Log.DEBUG, tag, info, null);
        }
    }

    public static void e(String tag, String info) {
        if (mDebug) {
            Log.e(tag, info);
//            printLog(Log.ERROR, tag, info, null);
        }
    }


    private static void printLog(int logType, String tag, String msg, Throwable e) {
        String tagStr = (tag == null) ? TAG_DEBUG : tag;
        if (mDebug) {
            if (logType < Log.INFO) {
                return;
            }
            String msgStr =
                    (e == null) ? msg : (msg + "\n" + Log.getStackTraceString(e));

            switch (logType) {
                case Log.ERROR:
                    Log.e(tagStr, msgStr);

                    break;
                case Log.WARN:
                    Log.w(tagStr, msgStr);

                    break;
                case Log.INFO:
                    Log.i(tagStr, msgStr);

                    break;
                default:
                    break;
            }

        } else {
            StringBuilder msgStr = new StringBuilder();

            if (false || true || true) {
                Thread currentThread = Thread.currentThread();

                if (false) {
                    msgStr.append("<");
                    msgStr.append(currentThread.getName());
                    msgStr.append("> ");
                }

                if (true) {
                    StackTraceElement ste = currentThread.getStackTrace()[4];
                    String className = ste.getClassName();
                    msgStr.append("[");
                    msgStr.append(className == null ? null
                            : className.substring(className.lastIndexOf('.') + 1));
                    msgStr.append("--");
                    msgStr.append(ste.getMethodName());
                    msgStr.append("] ");
                }

                if (true) {
                    StackTraceElement ste = currentThread.getStackTrace()[4];
                    msgStr.append("[");
                    msgStr.append(ste.getFileName());
                    msgStr.append("--");
                    msgStr.append(ste.getLineNumber());
                    msgStr.append("] ");
                }
            }

            msgStr.append(msg);
            if (e != null && true) {
                msgStr.append('\n');
                msgStr.append(Log.getStackTraceString(e));
            }

            switch (logType) {
                case Log.ERROR:
                    Log.e(tagStr, msgStr.toString());

                    break;
                case Log.WARN:
                    Log.w(tagStr, msgStr.toString());

                    break;
                case Log.INFO:
                    Log.i(tagStr, msgStr.toString());

                    break;
                case Log.DEBUG:
                    Log.d(tagStr, msgStr.toString());

                    break;
                case Log.VERBOSE:
                    Log.v(tagStr, msgStr.toString());

                    break;
                default:
                    break;
            }
        }
    }

}
