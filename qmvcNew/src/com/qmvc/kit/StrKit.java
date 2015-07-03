package com.qmvc.kit;

/**@author everxs/ez
 * �?��String的工具类，判断为空或者头字母大小写等�?
 * StrKit.
 */
public class StrKit {
	
	/**
	 * 判断首字母大小写
	 * @param str
	 * @return
	 */
	public static boolean firstCharIsUp(String str){
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
		  //如果为大�?
			return true;
		}
		return false;
	}
	
	
	/**
	 * 第一个字母大�?
	 */
	public static String firstCharToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * 第一个字母小�?
	 */
	public static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * 
	 */
	public static boolean isBlank(String str) {
		return str == null || "".equals(str.trim()) ? true : false;
	}
	
	/**
	 * 
	 */
	public static boolean notBlank(String str) {
		return str == null || "".equals(str.trim()) ? false : true;
	}
	
	public static boolean notBlank(String... strings) {
		if (strings == null)
			return false;
		for (String str : strings)
			if (str == null || "".equals(str.trim()))
				return false;
		return true;
	}
	
	public static boolean notNull(Object... paras) {
		if (paras == null)
			return false;
		for (Object obj : paras)
			if (obj == null)
				return false;
		return true;
	}
	
	/**
	 * 返回�?split 分割的字符串的尾�? 例如   abc.txt  返回 txt
	 * @param content
	 * @param split
	 * @return
	 */
	public static String getEndType(String content,String split){
		String ss[] = content.split(split);
		return ss[ss.length-1];
	}
	
	public static void main(String[] args) {
		String name = "abc.txt";
		System.out.println(getEndType(name, "\\."));
	}
}