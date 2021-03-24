package com.yhj.network.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author :杨虎军
 * @date :2020/10/10
 * @desc :
 */
public class TimeUtils {

    public static String timeStamp2Date(String format) {
        long time = System.currentTimeMillis();
        String seconds = String.valueOf(time / 1000);
        if (seconds.isEmpty() || "null".equals(seconds)) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.parseLong(seconds + "000")));
    }

    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
