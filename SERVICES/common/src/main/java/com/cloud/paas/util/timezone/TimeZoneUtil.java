package com.cloud.paas.util.timezone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Author: srf
 * @desc: TimeZoneUtil对象
 * @Date: Created in 2018-04-10 19-19
 * @Modified By:
 */
public class TimeZoneUtil {
    public static String changeToUTC(String cstTime) throws ParseException {
        String utcTime;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date date = df.parse(cstTime);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        utcTime = df.format(date);
        utcTime = utcTime.substring(0, utcTime.length() - 2) + "00";
        return utcTime;
    }
    public static String changeToCST(String utcTime) throws ParseException {
        String cstTime;
        utcTime = utcTime.substring(0, utcTime.length() - 1);
        utcTime = utcTime.replace("T"," ");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = df.parse(utcTime);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        cstTime = df.format(date);
        cstTime = cstTime.substring(0, cstTime.length() - 2) + "00";
        return cstTime;
    }
}
