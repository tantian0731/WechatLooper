package com.tt.wechatlooper.util;

import android.view.KeyEvent;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Author: xtc
 * CreateTime: 2017/3/23
 * Description:
 */
public class AdbUtil {


    /**
     * 结束进程,执行操作调用即可
     */
    public static void kill(String packageName) {
        String cmd = "adb shell \n am force-stop " + packageName + " \n";
        execShellCmd(cmd);
    }

    /**
     * 点击事件
     *
     * @param x
     * @param y
     */
    public static void tap(int x, int y) {
        String cmd = "adb shell \n input tap " + x + " " + y + " \n";
        execShellCmd(cmd);
    }

    /**
     * 系统返回键
     */
    public static void back() {
        String cmd = "adb shell \n input keyevent " + KeyEvent.KEYCODE_BACK + " \n";
        execShellCmd(cmd);
    }


    public static void swipe(int fromX, int fromY, int toX, int toY) {
        String cmd = "adb shell \n input swipe " + fromX + " " + fromY + " " + toX + " " + toY + " \n";
        execShellCmd(cmd);
    }


    private static void execShellCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream out = process.getOutputStream();
            out.write(cmd.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void killWechat() {
        kill("com.tencent.mm");
    }
}
