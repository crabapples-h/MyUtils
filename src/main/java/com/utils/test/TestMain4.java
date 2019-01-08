package com.utils.test;

import java.io.IOException;

public class TestMain4 {
	public static void main(String [] args) throws IOException, InterruptedException {
//		String url="";
//		Map<String,String> param = new HashMap<String,String>();
//		HttpUtil.SendHttpRequest(url, param, HttpUtil.POST);
		String str ="<p>dddd<img src=\"http://1811k7132h.iask.in:80/file/ALW/2019-01-04_/19-29-43-1546601383990.jpg\" style=\"background-color: rgb(255, 255, 255); max-width: 100%;\">465465465<img src=\"http://1811k7132h.iask.in:80/file/ALW/2019-01-04_/19-29-43-1546601383990.jpg\" style=\"background-color: rgb(255, 255, 255); max-width: 100%;\"><p>3</p></p>";
		while(str.indexOf("style=")!=-1) {
			int start = str.indexOf("style=");
			String str2 = str.substring(start);
			int end = str2.indexOf(">");
			str2 = str2.substring(0,end);
			str = str.replace(str2,"/");
			str = str.replace(" /","/></p><p");
			System.out.println(str);
		}
		System.out.println(str);
		System.out.println(str.replaceAll("(<p>)+", "<p>"));
//		test1(str);
	}
	
	public static String test1(String str){
		int start = str.indexOf("src=\"")+5;
		String str1 = str.substring(0, start);
		String str2 = str.replace(str1, "");
		int end = str2.indexOf(">");
		String str3 = str.substring(start,end+start-2);
		System.out.println(str3);
		return str3;
	}
}