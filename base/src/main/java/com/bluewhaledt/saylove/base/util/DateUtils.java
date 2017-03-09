package com.bluewhaledt.saylove.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 */
public class DateUtils {
    public static final String FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_ONLY = "yyyy-MM-dd";
    public static final String FORMAT_TIME_ONLY = "HH:mm:ss";

    /**
     * 返回当前日期
     */
    public static String dateofnow() {
        return format( new java.util.Date(), FORMAT_DATE_ONLY );
    }

    /**
     * 返回当前时间
     */
    public static String timeofnow() {
        return format( new java.util.Date() );
    }

    /**
     * 当前日期
     *
     * @return 当前日期
     */
    public static Date now() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 格式化日期（默认格式）
     *
     * @param date 日期对象
     * @return 格式化结果
     */
    public static String format(Date date) {
        DateFormat formatter = new SimpleDateFormat(FORMAT_DEFAULT, Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 格式化日期（指定格式）
     *
     * @param date   日期对象
     * @param format 格式化结果
     * @return
     */
    public static String format(Date date, String format) {
        DateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 从字符串解析日期对象
     *
     * @param dateStr 日期字符串
     * @param format  格式
     * @return 解析得到的日期对象
     */
    public static Date parse(String dateStr, String format) {
        try {
            DateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 某日期是星期几
     *
     * @param date 日期对象
     * @return 星期几（1-7）
     */
    public static int dayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            day = 7;
        } else {
            day = day - 1;
        }
        return day;
    }

    /**
     * @param millisecond
     *            单位:毫秒
     * @return 返回格式 _天 _:_:_
     */
    public static String getTimeFromStamp(Long millisecond) {
        if (millisecond <= 0) {
            return "";
        }
        String timeStr = "";

        long d = millisecond / (24 * 60 * 60 * 1000);// 天
        long h2 = millisecond / (60 * 60 * 1000) - d * 24;// 小时
        long h = millisecond / (60 * 60 * 1000);// 小时
        long m = millisecond / (60 * 1000) - h * 60;// 分钟前
        long s = millisecond / 1000 - h * 60 * 60 - m * 60;// 秒前

        String day = "";
        if (d > 0) {
            day = String.valueOf(d) + "天 ";
        }
        String hour;
        if (h2 < 10) {
            hour = "0" + h2;
        } else {
            hour = String.valueOf(h2);
        }
        String min;
        if (m < 10) {
            min = "0" + m;
        } else {
            min = String.valueOf(m);
        }
        String secord;
        if (s < 10) {
            secord = "0" + s;
        } else {
            secord = String.valueOf(s);
        }
        timeStr = day + hour + ":" + min + ":" + secord;
        return timeStr;
    }

    /**
     * @param millisecond
     *            单位:毫秒
     * @return 返回格式 _:_:_
     */
    public static String getTimeFromStamp2(Long millisecond) {
        if (millisecond <= 0) {
            return "";
        }
        String timeStr = "";

        long h2 = millisecond / (60 * 60 * 1000);// 小时
        long h = millisecond / (60 * 60 * 1000);// 小时
        long m = millisecond / (60 * 1000) - h * 60;// 分钟前
        long s = millisecond / 1000 - h * 60 * 60 - m * 60;// 秒前

        String hour;
        if (h2 < 10) {
            hour = "0" + h2;
        } else {
            hour = String.valueOf(h2);
        }
        String min;
        if (m < 10) {
            min = "0" + m;
        } else {
            min = String.valueOf(m);
        }
        String secord;
        if (s < 10) {
            secord = "0" + s;
        } else {
            secord = String.valueOf(s);
        }
        timeStr = hour + ":" + min + ":" + secord;
        return timeStr;
    }

    /**
     * 获取指定年月的天数
     *
     * @param year  年
     * @param month 月（1-12）
     * @return 该年月的天数
     */
    public static int getDayCountInMonth(int year, int month) {
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else if (month == 2) {
            if (year % 400 == 0) {
                // 四百年再闰
                return 29;
            }
            if (year % 100 == 0) {
                // 百年不闰
                return 28;
            }
            if (year % 4 == 0) {
                // 四年一闰
                return 29;
            }
            return 28;
        } else {
            return 31;
        }
    }
}
