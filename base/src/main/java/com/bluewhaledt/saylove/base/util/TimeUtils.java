package com.bluewhaledt.saylove.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期通用方法类
 * 
 * @version 1.0.0
 * @date 2013-3-26
 * @author S.Kei.Cheung
 */
public class TimeUtils {

	/**
	 * 返回当前日期
	 */
	public static String dateOfNow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new java.util.Date());
	}

	/**
	 * 返回当前日期格式为20070921
	 */
	public static String dateOfNowOnlyNumber() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new java.util.Date());
	}

	/**
	 * 返回当前时间
	 */
	public static String timeOfNow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar curcal = Calendar.getInstance();
		return sdf.format(curcal.getTime());
	}

	/**
	 * 返回指定格式时间
	 */
	public static String timeOfNow(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date == null)
			return "";
		return sdf.format(date);
	}

	/**
	 * 返回指定格式时间
	 */
	public static String dateOfNow(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null)
			return "";
		return sdf.format(date);
	}

	/**
	 * 返回指定格式时间
	 */
	public static String timeofnowFormat(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (date == null)
			return "";
		return sdf.format(date);
	}

	public static String timeofnowFromStr(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (dateStr == null) {
			return "";
		}
		try {
			return timeOfNow(sdf.parse(dateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 给指定时间加上一个数值
	 * 
	 * @param time1
	 *            要加上一数值的时间，为null即为当前时间，格式为yyyy-MM-dd HH:mm:ss
	 * @param addpart
	 *            要加的部分：年月日时分秒分别为：YMDHFS
	 * @param num
	 *            要加的数值
	 * @return 新时间，格式为yyyy-MM-dd HH:mm:ss
	 */
	public static String addTime(String time1, String addpart, int num) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now = sdf.format(new Date());
			time1 = (time1 == null) ? now : time1;
			if (time1.length() < 19) {
				time1 += " 00:00:00";
			}
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(sdf.parse(time1));
			if (addpart.equalsIgnoreCase("Y")) {
				cal.add(Calendar.YEAR, num);
			} else if (addpart.equalsIgnoreCase("M")) {
				cal.add(Calendar.MONTH, num);
			} else if (addpart.equalsIgnoreCase("D")) {
				cal.add(Calendar.DATE, num);
			} else if (addpart.equalsIgnoreCase("H")) {
				cal.add(Calendar.HOUR, num);
			} else if (addpart.equalsIgnoreCase("F")) {
				cal.add(Calendar.MINUTE, num);
			} else if (addpart.equalsIgnoreCase("S")) {
				cal.add(Calendar.SECOND, num);
			}
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 给指定时间加上一个数值
	 * 
	 * @param time1
	 *            要加上一数值的时间，为null即为当前时间，格式为格式可自定义传入
	 * @param addpart
	 *            要加的部分：年月日时分秒分别为：YMDHFS
	 * @param num
	 *            要加的数值
	 * @return 新时间，格式可自定义传入
	 */
	public static String addTimeFormat(String time1, String addpart, int num,
									   String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			String now = sdf.format(new Date());
			time1 = (time1 == null) ? now : time1;
			if (time1.length() < 19) {
				time1 += " 00:00:00";
			}
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(sdf.parse(time1));
			if (addpart.equalsIgnoreCase("Y")) {
				cal.add(Calendar.YEAR, num);
			} else if (addpart.equalsIgnoreCase("M")) {
				cal.add(Calendar.MONTH, num);
			} else if (addpart.equalsIgnoreCase("D")) {
				cal.add(Calendar.DATE, num);
			} else if (addpart.equalsIgnoreCase("H")) {
				cal.add(Calendar.HOUR, num);
			} else if (addpart.equalsIgnoreCase("F")) {
				cal.add(Calendar.MINUTE, num);
			} else if (addpart.equalsIgnoreCase("S")) {
				cal.add(Calendar.SECOND, num);
			}
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 给指定日期加上一个数值
	 * 
	 * @param date1
	 *            要加上一数值的日期，为null即为当前日期，格式为yyyy-MM-dd
	 * @param addpart
	 *            要加的部分：年月日分别为：YMD
	 * @param num
	 *            要加的数值
	 * @return 新日期，格式为yyyy-MM-dd
	 */
	public static String addDate(String date1, String addpart, int num) {
		return addTime(date1, addpart, num).substring(0, 10);
	}

	/**
	 * 传入当前字符串时间 yyyy-mm-dd HH:MM:SS 返回今天 HH:MM:SS,昨天 HH:MM:SS,前天 HH:MM:SS,年-月-日
	 * HH:MM:SS
	 * 
	 * @param strDate
	 *            字符串日期时间
	 * @return 返回今天 HH:MM:SS,昨天 HH:MM:SS,前天 HH:MM:SS,年-月-日 HH:MM:SS
	 */
	public static String getTime_yyyymmdd_hhmmss(String strDate) {
		return getTime(strDate, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 传入当前字符串时间 yyyy-mm-dd HH:MM 返回今天 HH:MM,昨天 HH:MM,前天 HH:MM,年-月-日 HH:MM
	 * 
	 * @param strDate
	 *            字符串日期时间
	 * @return 返回今天 HH:MM,昨天 HH:MM,前天 HH:MM,年-月-日 HH:MM
	 */
	public static String getTime_yyyymmdd_hhmm(String strDate) {
		return getTime(strDate, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 传入当前字符串时间 yyyy-mm-dd HH:MM:SS 返回今天 HH:MM:SS,昨天 HH:MM:SS,前天 HH:MM:SS,年-月-日
	 * HH:MM:SS
	 * 
	 * @param strDate
	 *            字符串日期时间
	 * @return 返回今天 HH:MM:SS,昨天 HH:MM:SS,前天 HH:MM:SS,年-月-日 HH:MM:SS
	 */
	public static String getTime(String strDate, String format) {
		String todySDF = "今天 HH:mm";
		String yesterDaySDF = "昨天 HH:mm";
		String beforeYesterDaySDF = "前天 HH:mm";
		String otherSDF = "yyyy-MM-dd HH:mm";
		SimpleDateFormat sfd = null;
		String time = "";
		Calendar dateCalendar = Calendar.getInstance();
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date;
		try {
			date = simpleDateFormat.parse(strDate);
			dateCalendar.setTime(date);
			Date now = new Date();
			Calendar targetCalendar = Calendar.getInstance();
			targetCalendar.setTime(now);
			targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
			targetCalendar.set(Calendar.MINUTE, 0);
			if (dateCalendar.after(targetCalendar)) {
				sfd = new SimpleDateFormat(todySDF);
				time = sfd.format(date);
				return time;
			} else {
				targetCalendar.add(Calendar.DATE, -1);
				if (dateCalendar.after(targetCalendar)) {
					sfd = new SimpleDateFormat(yesterDaySDF);
					time = sfd.format(date);
					return time;
				} else {
					targetCalendar.add(Calendar.DATE, -2);
					if (dateCalendar.after(targetCalendar)) {
						sfd = new SimpleDateFormat(beforeYesterDaySDF);
						time = sfd.format(date);
						return time;
					}
				}
			}
			sfd = new SimpleDateFormat(otherSDF);
			time = sfd.format(date);
			return time;
		} catch (ParseException e) {
			// 当天时间
			if (strDate.length() == 5)
				strDate = "今天 " + strDate;
		}
		return strDate;
	}

	/**
	 * 获取两个日期之间的间隔天数
	 * 
	 * @return 两个日期之间的间隔天数
	 */
	public static int getGapCount(Date startDate, Date endDate) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime()
				.getTime()) / (1000 * 60 * 60 * 24));
	}

	/**
	 * 判断两个日期是否在指定时间长度内
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param hours
	 *            时间间隔
	 * @return 时间间隔在指定小时内，返回true；在指定小时外返回false
	 */
	public static boolean betweenHours(Date startDate, Date endDate, int hours) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (toCalendar.getTime().getTime()
				- fromCalendar.getTime().getTime() < 1000 * 60 * 60 * hours);
	}

	/**
	 * 时间转化为时间戳
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm:ss格式的时间字符串
	 * @return
	 */
	public static long getTime(String time) {
		long time_stamp = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d;
		try {
			d = sdf.parse(time);
			time_stamp = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time_stamp;
	}

	/**
	 * 时间转化为时间戳
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm格式的时间字符串
	 * @return
	 */
	public static long getTimeStamp(String time) {
		long time_stamp = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d;
		try {
			d = sdf.parse(time);
			time_stamp = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time_stamp;
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
		String hour = "";
		if (h2 < 10) {
			hour = "0" + h2;
		} else {
			hour = String.valueOf(h2);
		}
		String min = "";
		if (m < 10) {
			min = "0" + m;
		} else {
			min = String.valueOf(m);
		}
		String secord = "";
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

//		long d = millisecond / (24 * 60 * 60 * 1000);// 天
		long h2 = millisecond / (60 * 60 * 1000);// 小时
		long h = millisecond / (60 * 60 * 1000);// 小时
		long m = millisecond / (60 * 1000) - h * 60;// 分钟前
		long s = millisecond / 1000 - h * 60 * 60 - m * 60;// 秒前

//		String day = "";
//		if (d > 0) {
//			day = String.valueOf(d) + "天 ";
//		}
		String hour = "";
		if (h2 < 10) {
			hour = "0" + h2;
		} else {
			hour = String.valueOf(h2);
		}
		String min = "";
		if (m < 10) {
			min = "0" + m;
		} else {
			min = String.valueOf(m);
		}
		String secord = "";
		if (s < 10) {
			secord = "0" + s;
		} else {
			secord = String.valueOf(s);
		}
		timeStr = hour + ":" + min + ":" + secord;
		return timeStr;
	}

	/**
	 * @param millisecond
	 *            单位:毫秒
	 * @return 返回格式 _:_
	 */
	public static String getMinuteSecondFromStamp(Long millisecond) {
		if (millisecond <= 0) {
			return "";
		}
		String timeStr = "";

		long m = millisecond / (60 * 1000);// 分钟前
		long s = millisecond / 1000 - m * 60;// 秒前

		String min = "";
		if (m < 10) {
			min = "0" + m;
		} else {
			min = String.valueOf(m);
		}
		String secord = "";
		if (s < 10) {
			secord = "0" + s;
		} else {
			secord = String.valueOf(s);
		}
		timeStr = min + ":" + secord;
		return timeStr;
	}

	public static String getShowTime(long time){
		long currentTime= System.currentTimeMillis();
		long duration=(currentTime-time)/1000;		//秒
		if(duration<60){
			return "刚刚";
		}else if(duration>60 && duration<60*60){	//1分钟到1小时
			return ((int)duration/60)+"分钟前";
		}else if(duration>60*60 && duration<60*60*24){
			return ((int)duration/(60*60))+"小时前";
		}else{
			return ((int)duration/(60*60*24))+"天前";
		}
	}

	public static String getShowTime2(long time){
		long duration=time/1000;		//秒
		if(duration<60){
			return "刚刚";
		}else if(duration>60 && duration<60*60){	//1分钟到1小时
			return ((int)duration/60)+"分钟前";
		}else if(duration>60*60 && duration<60*60*24){
			return ((int)duration/(60*60))+"小时前";
		}else{
			return ((int)duration/(60*60*24))+"天前";
		}
	}

	//毫秒转秒
	public static String long2String(long time){

		//毫秒转秒
		int sec = (int) time / 1000 ;
		int min = sec / 60 ;	//分钟
		sec = sec % 60 ;		//秒
		if(min < 10){	//分钟补0
			if(sec < 10){	//秒补0
				return "0"+min+":0"+sec;
			}else{
				return "0"+min+":"+sec;
			}
		}else{
			if(sec < 10){	//秒补0
				return min+":0"+sec;
			}else{
				return min+":"+sec;
			}
		}

	}

	/**
	 * 返回当前时间的格式为 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String  getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(System.currentTimeMillis());
	}

	public static String formatTime(long times){
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(times);
		String date;
		Calendar now = Calendar.getInstance();
		if (calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH)){
			date = format2.format(new Date(times));
		} else if (calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
			date = format.format(new Date(times));
		} else {
			date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
		}
		return date;
	}
}
