package com.MissX.utils.random;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 随机数生成工具类
 * Time 2018年9月28日 上午9:38:43
 * Admin
 */
public class CreateRandomUtil {
	private static final String [] chars = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	public static final String [] rand = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","0","1","2","3","4","5","6","7","8","9"};
	/**
	 * 根据时间生成14位数字和三位随机字母后缀
	 * @param name 需要添加的前缀
	 * @return 生成的ID格式:name+时间+3位随机字母
	 */
	public static String createIdByTime(String name) {
		String random = "";
		for (int i = 0; i < 3; i++) {
			random += chars[new Random().nextInt(26)];
		}
		name += "_" + new Date().getTime() + random;
		return name;
	}
	/**
	 * 根据时间生成X加14位数字和五位随机字母后缀
	 * @return 生成的ID格式:X+时间+5位随机字母
	 */
	public static String createNumberByTime(char c){
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
		String number = "X"+c+sdf.format(new Date());
		for (int i = 0; i < 5; i++) {
			number += chars[new Random().nextInt(26)];
		}
		return number;
	}
	/**
	 * 获取随机数字和字母的随机组合(大写),长度为参数
	 * @param length 需要获取的随机数长度
	 * @return 随机字符串，长度为length
	 */
	public static String getRandom(int length){
		Random ran = new Random();
		String str = "";
		for (int i = 0; i < length; i++) {
			str += (rand[ran.nextInt(36)]);
		}
		return str;
	}
}
