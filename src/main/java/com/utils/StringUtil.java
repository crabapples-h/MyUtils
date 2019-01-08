package com.utils;
/**
 * 
 * @author Wishfor_you@foxmail.com
 * 2018年9月12日 下午3:47:50
 */
public class StringUtil {
	/**
	 * 把回车替换为br标签
	 * @param text
	 * @return
	 */
	public static String Add_Br(String text){
		System.out.println("替换前:"+text);
		if(null!=text) {
			text = text.replaceAll("[\r\n]", "<br/>");
		}
		System.out.println("替换后:"+text);
		return text;
	}
	
	/**
	 * 把br标签替换为回车
	 * @param text
	 * @return
	 */
	public static String Remove_Br(String text){
		System.out.println("替换前:"+text);
		if(null!=text) {
			text = text.replaceAll("<br/>","");
		}
		System.out.println("替换后:"+text);
		return text;
	}
}
