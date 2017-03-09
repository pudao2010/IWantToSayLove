package com.bluewhaledt.saylove.base.util;

import android.content.SharedPreferences;

/**
 * Created by zhenai-liliyan on 16/10/27.
 */
public abstract class BasePreferences {

    protected abstract SharedPreferences getSharedPreferences();

    protected String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    protected boolean getBoolean(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    protected int getInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    protected float getFloat(String key, float defaultValue) {
        return getSharedPreferences().getFloat(key, defaultValue);
    }

    protected long getLong(String key, long defaultValue) {
        return getSharedPreferences().getLong(key, defaultValue);
    }

    protected void saveValue(String key, Object value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, Boolean.parseBoolean(value.toString()));
        } else if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Long) {
            editor.putLong(key, Long.parseLong(value.toString()));
        } else if (value instanceof Float) {
            editor.putFloat(key, Float.parseFloat(value.toString()));
        } else if (value instanceof Integer) {
            editor.putInt(key, Integer.parseInt(value.toString()));
        }
        editor.apply();
    }
}
