package org.juice.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHandler {
	
	
	public static String formatDate(Date date){     
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return formatter.format(date);
	}
	
	public static String formatDate(long date){     
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return formatter.format(date);
	}
	
	/**
	* @Description 将Date格式的日期，转换成"yyyy-MM-dd" String格式
	* @param  date  需要进行格式化的日期参数
	* @return String 转换成字符串"yyyy-MM-dd"的日期
	*/
	public static String getFormatDate(Date date){			
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);	
	}
	
	/**
	* @Description 将String格式的日期，转换成Date格式
	* @param  String  需要进行格式化的日期
	* @return Date 转换成日期格式"yyyy-MM-dd"
	* @throws 输入的不是Date格式抛出异常
	*/
	public static Date ParseDate(String date) {		
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(date);			
			} 	
		catch (ParseException e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	* @Description 得到当前日期"yyyy-MM-dd"的字符串
	* @return String 
	*/
	public static String getNowDayString(){		
		return getFormatDate(new Date());
	}
	
	/**
	* @Description 得到date日期，前或后Number天的字符串格式"yyyy-MM-dd"日期
	* @param date 基础日期
	* @param Number 日期增量  负数为之前，正数为之后
	* @return String 
	*/
	public static String getForwardDayString(String date,int Number) {	
		Calendar AddDay = Calendar.getInstance();
		AddDay.setTime(ParseDate(date));
		AddDay.add(Calendar.DATE, Number);
		return getFormatDate(AddDay.getTime());		
	}
	
	
}
