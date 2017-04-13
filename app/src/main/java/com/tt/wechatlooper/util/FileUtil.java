package com.tt.wechatlooper.util;

import android.os.Environment;
import android.util.Log;

import com.tt.wechatlooper.LoopService;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

/**
 * Author: Beta-Tan
 * CreateTime: 2017/3/10
 * Description: 读写文件的工具类
 */

public class FileUtil {
    private static final String NORMAL_LOG = "NormalLog";
    private static final String ERROR_LOG = "ErrorLog";
    public static String HOME_PATH = "/JFWechatHelper";
    private static String LOG_PATH = HOME_PATH + "/log/";
    /**
     * 日志名称
     */
    private static String LOG_NAME = "log.txt";
    private static String ERROR_NAME = "error.txt";
    /**
     * 离线位置名称
     */

    /**
     * 保存错误信息到文件中
     */
    public static void saveLog(String log) {
        if (LoopService.switchState) {
            writeFile(LOG_NAME, log);
        }
        Log.e(NORMAL_LOG, log);
    }

    public static void errorLog(String log) {
        if (LoopService.switchState) {
            writeFile(ERROR_NAME, log);
        }
        Log.e(ERROR_LOG, log);
    }


    /**
     * 将数据写入文件
     *
     * @param fileName 文件名
     * @param content  内容
     */
    private static void writeFile(String fileName, String content) {
        final Date now = new Date();
        StringBuilder builder = new StringBuilder();
        builder.append(DateUtil.format(now, DateUtil.HHMMSS)).append("_").append(content).append("\n");
        try {
            if (isSDCardEnable()) {
                File uploadLogFile = new File(getLogPath() + fileName);
                if (!uploadLogFile.getParentFile().exists()) {
                    uploadLogFile.getParentFile().mkdirs();
                }
                FileWriter writer = new FileWriter(uploadLogFile, true);
                writer.write(builder.toString());
                writer.close();
            }
        } catch (Exception ignored) {
        }
    }


    private static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private static String getLogPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + LOG_PATH;
    }
}
