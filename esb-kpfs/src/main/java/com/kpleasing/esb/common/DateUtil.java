package com.kpleasing.esb.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	
	
	/**
	 * 当前日历，这里用中国时间表示
	 *
	 * @return 以当地时区表示的系统当前日历
	 */
	public static Calendar getCalendar() {
		return Calendar.getInstance();
	}
	
	/**
	 * 当前日期
	 *
	 * @return 系统当前时间
	 */
	public static Date getDate() {
		return getCalendar().getTime();
	}
	
	
	/**
	 * 日期转换为字符串
	 *
	 * @param date
	 *            日期
	 * @param format
	 *            日期格式
	 * @return 字符串
	 */
	public static String getCurrentDate(SimpleDateFormat date_sdf) {
		Date date = getDate();
		if (null == date) {
			return null;
		}
		return date_sdf.format(date);
	}
	
	public static void main(String[] argc) {
		System.out.println(getCurrentDate(DateUtil.yyyyMMddHHmmss));;
	}
	

}
