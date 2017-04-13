package com.tt.wechatlooper.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: Beta-Tan
 * CreateTime: 2017/3/10
 * Description:
 */
public class DateUtil {
    public static final DateFormat HHMMSS = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);


    public static String format(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return "";
        }
        return dateFormat.format(date);
    }
}
