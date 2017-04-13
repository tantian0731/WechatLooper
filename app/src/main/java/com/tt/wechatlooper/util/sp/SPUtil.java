package com.tt.wechatlooper.util.sp;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.HashSet;

/**
 * Author: Beta-Tan
 * CreateTime: 2017/2/25
 * Description:
 */
public class SPUtil extends SPBase {


    public static final String SPEED_FACTOR = "SPEED_FACTOR";
    private static final String LOG_STATE = "LOG_STATE";
    private static final String ACCOUNTS_NAME = "ACCOUNTS_NAME";
    private static final String RECENT_ACCOUNT = "RECENT_ACCOUNT";
    private static final String CLOSE_HOUR = "CLOSE_HOUR";
    private static final String OPEN_HOUR = "OPEN_HOUR";

    public static float getSpeedFactor() {
        float time = SPUtil.getFloat(SPEED_FACTOR);
        if (time == 0) {
            time = 1;
        }
        return time;
    }

    public static boolean getSwitchState() {
        return getBoolean(LOG_STATE, true);
    }

    public static void setSwitchState(boolean state) {
        putBoolean(LOG_STATE, state);
        Log.e("setSwitchState", "" + state);
    }

    public static void setAccountsName(HashSet<String> accountsName) {
        String result = JSON.toJSONString(accountsName);
        putString(ACCOUNTS_NAME, result);
    }

    public static HashSet<String> getAccountsName() {
        String json = getString(ACCOUNTS_NAME);
        if (!"".equals(json)) {
            HashSet<String> result = JSON.parseObject(json, HashSet.class);
            return result;
        }

        return null;
    }

    public static String getRecentAccount() {
        return getString(RECENT_ACCOUNT);
    }

    public static void setRecentAccount(String recentAccount) {
        putString(RECENT_ACCOUNT, recentAccount);
    }

    public static int getOpenHour() {
        return getInt(OPEN_HOUR);
    }

    public static void setOpenHour(int openHour) {
        putInt(OPEN_HOUR, openHour);
    }

    public static int getCloseHour() {
        return getInt(CLOSE_HOUR);
    }

    public static void setCloseHour(int closeHour) {
        putInt(CLOSE_HOUR, closeHour);
    }
}
