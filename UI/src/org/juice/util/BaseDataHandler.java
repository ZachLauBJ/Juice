package org.juice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseDataHandler {
	
	
	
	/**
	 * @Description 将Map转换成String Map中每一个key,value键值对换行显示
	 * @param Map<String,String>
	 * @return
	 */
	public static String transMapToString(Map<String,String> map){
		StringBuffer sb = new StringBuffer();
		for(String s:map.keySet()){
			sb.append(s+" "+map.get(s)+"\n");
		}
		return sb.toString();	
	}
	
	/**
	 * @Description 将List转换成String List中每个值之间换行显示
	 * @param List<String>
	 * @return
	 */
	public static String transListToString(List<String> list){
		StringBuffer sb = new StringBuffer();
		for(String s:list){
			sb.append(s+"\n");
		}
		return sb.toString();	
	}
	
	/**
	* @Description 首字母转大写
	* @param String：待处理的字符串
	* @return String
	*/
	public static String capitalizesTheFirstLetter(String s){
		 if(Character.isUpperCase(s.charAt(0)))
	            return s;
	        else
	            return (new StringBuilder()).append(Character.
	            		toUpperCase(s.charAt(0))).append(s.substring(1)).toString();	 
	}
	
	/**
	* @Description 首字母转小写
	* @param String：待处理的字符串
	* @return String
	*/
	public static String lowercaseTheFirstLetter(String s){
		 if(Character.isLowerCase(s.charAt(0)))
	            return s;
	        else
	            return (new StringBuilder()).append(Character.
	            		toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	
	/**
	* @Description 把字符串数组String[]转成List<String>
	* @param String[]：待处理的字符串数组
	* @return List<String>
	*/
	public static List<String> stringArrayToList(String[] str){
		List<String> list = new ArrayList<String>();
		for(int i=0;i<str.length;i++){
			if(str[i].startsWith("http"))
			list.add(str[i]);
		}
		return list; 
	}
	    
	/**
	* @Description 将字符串text中以startString开头，endString结尾的所有匹配字符放在一个字符串数组中返回
	* @param text：待处理的字符串
	* @param startString：搜索起始字符串
	* @param endString：搜索结尾字符串
	* @return String[] 符合条件的字符串数组
	*/
	public static List<String> subTextString(String text, String startString, String endString){
		List<String> Text = stringArrayToList(text.split(startString));
		List<String> finalText = new ArrayList<String>();
		for (int i=1;i<Text.size();i++){		
			finalText.add(i-1, Text.get(i).substring(0,  Text.get(i).indexOf(endString)).trim());
		}	
		return finalText;
	}
	
	/**
	* @Description 截取字符串startString首次出现之后的内容(不包括startString)
	* @param text：待处理的字符串
	* @param startString：搜索起始字符串
	* @return String 处理后的字符串
	*/
	public static String subTextString(String text, String startString){
		int index = text.indexOf(startString)+startString.length();
		return  text.substring(index);
	}
	
	
	/**
	 * 判断String是否为空
	 * 
	 * @param s
	 * @return 如果字符串为空或者字符串去除首尾空格为空字符串则返回true,反之返回false
	 */
	public static boolean isEmpty(String s) {
		if (s == null || s.trim().length() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断map是否为空
	 * 
	 * @param map
	 * @return 如果map==null或者map.size()==0则返回true,反之返回false
	 */
	@SuppressWarnings("all")
	public static boolean isEmpty(Map map) {
		if (map == null || map.size() == 0) {
			return true;
		}
		return false;
	}

	/***
	 * 判断list是否为空
	 * 
	 * @param list
	 * @return 如果list==null或者list.size==则返回true,反之返回false
	 */
	@SuppressWarnings("all")
	public static boolean isEmpty(List list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

}
