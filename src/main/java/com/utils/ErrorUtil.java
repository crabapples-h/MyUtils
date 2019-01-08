package com.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class ErrorUtil {
	private static final Gson GSON = new Gson();
	/**
	 * 生成错误信息
	 * @param request
	 * @return
	 */
	public static String CreateErrorMessage() {
		Map<String,String> errorMap = new HashMap<String,String>();
		errorMap.put("status", "-1");
		errorMap.put("message", "参数为空");
		String errorMessage = GSON.toJson(errorMap);
		return errorMessage;
	}
	
	/**
	 * 获取错误信息
	 * @param exception
	 * @return
	 */
	public static String GetErrorMessage(Exception exception) {
		Map<String,String> errorMap = new HashMap<String,String>();
		errorMap.put("status", "-1");
		errorMap.put("message", exception.getMessage());
		String errorMessage = GSON.toJson(errorMap);
		return errorMessage;
	}
}