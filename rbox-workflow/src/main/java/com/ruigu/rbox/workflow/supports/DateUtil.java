package com.ruigu.rbox.workflow.supports;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author alan.zhao
 */
public class DateUtil {
    static Map<Integer, String> map;
    static Map<Integer, String> map1;

    static {
        map = new HashMap<Integer, String>();
        map1 = new HashMap<Integer, String>();
        map.put(1, "一");
        map.put(2, "二");
        map.put(3, "三");
        map.put(4, "四");
        map.put(5, "五");
        map.put(6, "六");
        map.put(7, "日");
        map1.put(1, "星期一");
        map1.put(2, "星期二");
        map1.put(3, "星期三");
        map1.put(4, "星期四");
        map1.put(5, "星期五");
        map1.put(6, "星期六");
        map1.put(7, "星期日");
    }

    /**
     * 解析将时间默认格式 ="yyyy-MM-dd",转换为星期几
     */
    public static String parseDateStringToWeek(String date, boolean shorter) {
        return parseWeekLabel(shorter, parseDateStringToWeek(date));
    }

    /**
     * 解析将时间默认格式 ="yyyy-MM-dd",转换为星期几
     */
    public static Integer parseDateStringToWeek(String date) {
        return DateUtil.getWeekOfDate(parseStringToDate(date));
    }

    /**
     * 解析将时间默认格式 ="yyyy-MM-dd",转换为星期几
     */
    public static Integer parseDateToWeek(Date date) {
        return DateUtil.getWeekOfDate(date);
    }

    public static String parseWeekLabel(boolean shorter, int index) {
        if (shorter) {
            return map.get(index);
        } else {
            return map1.get(index);
        }
    }

    public static Integer getWeekOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        Integer week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) {
            week = 7;
        }
        return week;
    }

    public static int daysBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String CTS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final String DATE_TIME_FORMAT_14 = "yyyyMMddHHmmss";
    public static final String UTC_DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZ yyyy";

    /**
     * 获得系统当前日期时间，以默认格式显示
     *
     * @return e.g.2006-10-12 10:55:06
     */
    public static String getCurrentFormatDateTime() {
        Date currentDate = getCurrentDate();
        SimpleDateFormat dateFormator = new SimpleDateFormat(DATE_TIME_FORMAT);
        return dateFormator.format(currentDate);
    }

    /**
     * 获得系统的当前时间
     *
     * @return e.g.Thu Oct 12 10:25:14 CST 2006
     */
    public static Date getCurrentDate() {
        return new Date(getCurrentTimeMillis());
    }

    /**
     * 获得系统的当前时间，毫秒.
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 输入日期，按照指定格式返回
     *
     * @param date
     * @param pattern e.g.DATE_FORMAT_8 = "yyyyMMdd"; DATE_TIME_FORMAT_14 =
     *                "yyyyMMddHHmmss"; 或者类似于二者的格式,e.g."yyyyMMddHH"，"yyyyMM"
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if (checkPara(pattern) || checkPara(date)) {
            return "";
        }
        SimpleDateFormat dateFormator = new SimpleDateFormat(pattern);

        return dateFormator.format(date);
    }

    /**
     * 将时间字符串按照默认格式DATE_TIME_FORMAT ="yyyy-MM-dd HH:mm:ss",转换为Date
     *
     * @param dateStr
     * @return
     */
    public static Date parseStrToDateTime(String dateStr) {
        if (checkPara(dateStr)) {
            return null;
        }
        SimpleDateFormat dateFormator = new SimpleDateFormat(
                DATE_TIME_FORMAT);
        Date resDate = dateFormator
                .parse(dateStr, new ParsePosition(0));

        return resDate;
    }

    /**
     * 将时间字符串按照默认格式DATE_TIME_FORMAT ="yyyy-MM-dd",转换为Date
     *
     * @param dateStr
     * @return
     */
    public static Date parseStringToDate(String dateStr) {
        if (checkPara(dateStr)) {
            return null;
        }
        SimpleDateFormat dateFormator = new SimpleDateFormat(
                DATE_FORMAT);
        Date resDate = dateFormator
                .parse(dateStr, new ParsePosition(0));

        return resDate;
    }

    public static Date parseStringToDateNew(String dateStr) {
        if (checkPara(dateStr)) {
            return null;
        }
        SimpleDateFormat dateFormator = new SimpleDateFormat(
                DATE_TIME_FORMAT);

        try {
            return dateFormator.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析utc时间字符串为DATE类型
     *
     * @param utcString e.g. Mon Jan 27 00:00:00 UTC+0800 2014
     * @return
     * @throws ParseException
     * @throws ParseException
     */
    public static Date parseUtcStringToDate(String utcString)
            throws ParseException {

        if (checkPara(utcString)) {
            return null;
        }
        utcString = utcString.replace("UTC", "");
        utcString = utcString.replace("GMT", "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                UTC_DATE_FORMAT, Locale.US);
        Date date = null;
        try {
            date = simpleDateFormat.parse(utcString);
        } catch (ParseException e) {
            simpleDateFormat = new SimpleDateFormat(
                    "EEE MMM dd yyyy HH:mm:ss ZZZ",
                    Locale.US);
            date = simpleDateFormat.parse(utcString);
        }

        return date;
    }

    /**
     * 解析CTS时间字符串为DATE类型
     *
     * @param ctsString e.g. Wed Sep 07 14:57:28 CST 2011
     * @return
     * @throws ParseException
     */
    public static Date parseCtsStringToDate(String ctsString)
            throws ParseException {
        if (checkPara(ctsString)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                CTS_DATE_FORMAT, Locale.US);
        Date date = simpleDateFormat.parse(ctsString);

        return date;
    }

    /**
     * 解析时间字符串为DATE类型
     *
     * @param str 格式yyyyMMddHHmmss e.g. 20111010084617
     * @return
     * @throws ParseException
     */
    public static Date str2Date(String str, String format)
            throws ParseException {
        if (checkPara(str)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = simpleDateFormat.parse(str);

        return date;
    }

    public static Date str2Date(String str) throws ParseException {
        if (checkPara(str)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                DATE_TIME_FORMAT_14);
        Date date = simpleDateFormat.parse(str);

        return date;
    }

    /**
     * 判断参数是否等于null或者空
     *
     * @param para
     * @return
     */
    private static boolean checkPara(Object para) {
        if (null == para || "".equals(para)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取两个时间之间的时间差(时分秒时间) author:徐国飞
     *
     * @param oldDate
     * @param newDate
     * @return
     */
    public static String getTimeDifference(Date oldDate, Date newDate) {
        // 除以1000是为了转换成秒
        long between = (newDate.getTime() - oldDate.getTime()) / 1000;
        long day1 = between / (24 * 3600);
        long hour1 = between % (24 * 3600) / 3600;
        long minute1 = between % 3600 / 60;
        long second1 = between % 60;
        return "" + day1 + "天" + hour1 + "小时" + minute1 + "分" + second1
                + "秒";
    }

    /**
     * 按类型获取日期 1，本周第一天 2，上周第一天 3，上周最后一天 4，上月第一天 5，上月最后一天 6，本月第一天
     **/
    public static String getDate(int type) {
        Calendar c = Calendar.getInstance();

        switch (type) {
            case 1:
                c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                return formatDate(c.getTime(), "yyyy-MM-dd");
            case 2:
                c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                c.add(Calendar.WEEK_OF_MONTH, -1);
                return formatDate(c.getTime(), "yyyy-MM-dd");
            case 3:
                c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                c.add(Calendar.WEEK_OF_MONTH, -1);
                c.add(Calendar.DAY_OF_WEEK, 6);
                return formatDate(c.getTime(), "yyyy-MM-dd");
            case 4:
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.add(Calendar.DAY_OF_MONTH, -1);
                c.set(Calendar.DAY_OF_MONTH, 1);
                return formatDate(c.getTime(), "yyyy-MM-dd");
            case 5:
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.add(Calendar.DAY_OF_MONTH, -1);
                return formatDate(c.getTime(), "yyyy-MM-dd");
            case 6:
                c.set(Calendar.DAY_OF_MONTH, 1);
                return formatDate(c.getTime(), "yyyy-MM-dd");
            default:
                break;
        }

        return formatDate(new Date(), "yyyy-MM-dd");
    }

    /**
     * 得到两个日期的天数差(date1-date2)
     * @param date1
     * @param date2
     * @return
     */
    public static int getDayDifference(Date date1, Date date2) {
        long between = (date1.getTime() - date2.getTime()) / 1000;
        long day = between / (24 * 3600);
        if(day<0){
            day=0;
        }
        return (int)day;
    }

    /**
     * 给日期增加天数(计算截止时间)
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+day);
        return calendar.getTime();
    }


}
