package com.tt.wechatlooper.util.sp;

import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.tt.wechatlooper.App;

/**
 * Author: Beta-Tan
 * CreateTime: 2017/3/28
 * Description:
 */
public class SPBase {


    private static final String PRE_NAME = "WECHAT_SETTINS";

    /**
     */
    private static SharedPreferences getSharePreferences() {
        return App.getInstance().getSharedPreferences(PRE_NAME, 0);
    }

    private static SharedPreferences.Editor getEditor() {
        SharedPreferences preferences = getSharePreferences();
        return preferences.edit();
    }


    static void putInt(String name, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(name, value);
        editor.commit();
    }

    public static void putBoolean(String name, boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static void putLong(String name, Long value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putLong(name, value);
        editor.commit();
    }

    public static void putFloat(String name, float value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putFloat(name, value);
        editor.commit();
    }


    public static float getFloat(String name) {
        return getSharePreferences().getFloat(name, 0f);
    }

    public static void putString(String name, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(name, value);
        editor.commit();
    }


    public static boolean getBoolean(String name, boolean defaultValue) {
        return getSharePreferences().getBoolean(name, defaultValue);
    }

    public static String getString(String name) {
        return getSharePreferences().getString(name, "");
    }

    public static String getString(String name, String defValue) {
        return getSharePreferences().getString(name, defValue);
    }


    public static long getLong(String name) {
        return getSharePreferences().getLong(name, 0L);
    }

    public static int getInt(String name) {
        return getSharePreferences().getInt(name, 0);
    }

    public static <T> T getObject(String name, Class<T> clz) {
        String json = getString(name, "");
        if (json.length() == 0) {
            return null;
        }
        return JSON.parseObject(json, clz);
    }

    public static void putObject(String name, Object object) {
        String jsonStr = "";
        if (object != null) {
            jsonStr = JSON.toJSONString(object);
        }
        putString(name, jsonStr);
    }
}
