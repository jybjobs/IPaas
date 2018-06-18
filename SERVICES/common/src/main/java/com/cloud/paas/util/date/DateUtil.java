package com.cloud.paas.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtil {
    /**
     * 获取当前时间
     * @return 当前时间
     * @throws ParseException
     */
    public Date getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
//            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当前时间
     * @return 当前时间
     * @throws ParseException
     */
    public static String getCurrentTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }


}
