package com.MissX.utils;

public class Enum {
	/**
	 * 枚举类(用来定义是否抛出异常)
	 * 2019年2月1日 下午4:40:48
	 * @author H
	 * Admin
	 */
	public enum EXCEPTION{
		EXCEPTION,UN_EXCEPTION;
		public static boolean check(EXCEPTION e){
			if(e==EXCEPTION) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	/**
	 * 枚举类(用来定义是否打印验证过程)
	 * 2019年2月1日 下午4:41:23
	 * @author H
	 * Admin
	 */
	public enum PRINT{
		PRINT,UN_PRINT;
		public static boolean check(PRINT e){
			if(e==PRINT) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	public static enum OrderBy{
		ASC,DESC;
		public static boolean isASC(OrderBy order){
			if(order==ASC) {
				return true;
			}
			return false;
		}
		public static boolean isDESC(OrderBy order){
			if(order==DESC) {
				return true;
			}
			return false;
		}
	}
}
