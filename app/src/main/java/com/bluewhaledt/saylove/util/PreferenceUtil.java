package com.bluewhaledt.saylove.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.bluewhaledt.saylove.ZhenaiApplication;

/**
 * Created by zhenai-liliyan on 16/10/27.
 */

public class PreferenceUtil {

    public static String getString(PreferenceFileNames fileNames, PreferenceKeys key, String defaultValue) {
        SharedPreferences sp = ZhenaiApplication.getInstance().getSharedPreferences(fileNames.getValue(), Context.MODE_PRIVATE);
        return sp.getString(key.getValue(), defaultValue);
    }

    public static String getString(PreferenceFileNames fileNames, PreferenceKeys key,String additional, String defaultValue) {
        SharedPreferences sp = ZhenaiApplication.getInstance().getSharedPreferences(fileNames.getValue() + additional, Context.MODE_PRIVATE);
        return sp.getString(key.getValue(), defaultValue);
    }

    public static boolean getBoolean(PreferenceFileNames fileNames, PreferenceKeys key, boolean defaultValue) {
        SharedPreferences sp = ZhenaiApplication.getInstance().getSharedPreferences(fileNames.getValue(), Context.MODE_PRIVATE);
        return sp.getBoolean(key.getValue(), defaultValue);
    }

    public static int getInt(PreferenceFileNames fileNames, PreferenceKeys key, int defaultValue) {
        SharedPreferences sp = ZhenaiApplication.getInstance().getSharedPreferences(fileNames.getValue(), Context.MODE_PRIVATE);
        return sp.getInt(key.getValue(), defaultValue);
    }

    public static float getFloat(PreferenceFileNames fileNames, PreferenceKeys key, float defaultValue) {
        SharedPreferences sp = ZhenaiApplication.getInstance().getSharedPreferences(fileNames.getValue(), Context.MODE_PRIVATE);
        return sp.getFloat(key.getValue(), defaultValue);
    }

    public static long getLong(PreferenceFileNames fileNames, PreferenceKeys key, long defaultValue) {
        SharedPreferences sp = ZhenaiApplication.getInstance().getSharedPreferences(fileNames.getValue(), Context.MODE_PRIVATE);
        return sp.getLong(key.getValue(), defaultValue);
    }


    public static void saveValue(PreferenceFileNames fileNames, PreferenceKeys key, Object value) {
        SharedPreferences sp = ZhenaiApplication.getInstance().getSharedPreferences(fileNames.getValue(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key.getValue(), Boolean.parseBoolean(value.toString()));
        } else if (value instanceof String) {
            editor.putString(key.getValue(), value.toString());
        } else if (value instanceof Long) {
            editor.putLong(key.getValue(), Long.parseLong(value.toString()));
        } else if (value instanceof Float) {
            editor.putFloat(key.getValue(), Float.parseFloat(value.toString()));
        } else if (value instanceof Integer) {
            editor.putInt(key.getValue(), Integer.parseInt(value.toString()));
        }
        editor.apply();
    }

    public static void saveValue(PreferenceFileNames fileNames, PreferenceKeys key,String additional, Object value) {
        SharedPreferences sp = ZhenaiApplication.getInstance().getSharedPreferences(fileNames.getValue() + additional, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key.getValue(), Boolean.parseBoolean(value.toString()));
        } else if (value instanceof String) {
            editor.putString(key.getValue(), value.toString());
        } else if (value instanceof Long) {
            editor.putLong(key.getValue(), Long.parseLong(value.toString()));
        } else if (value instanceof Float) {
            editor.putFloat(key.getValue(), Float.parseFloat(value.toString()));
        } else if (value instanceof Integer) {
            editor.putInt(key.getValue(), Integer.parseInt(value.toString()));
        }
        editor.apply();
    }
}
