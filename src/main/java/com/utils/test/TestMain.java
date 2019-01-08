package com.utils.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;


public class TestMain {
	public int CONN_TIMEOUT = 50000;	//链接超时时间
	public int READ_TIMEOUT = 50000;	//响应超时时间
	public static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6799.400 QQBrowser/10.3.2908.400";	//请求头
	
	
	public static void main(String [] args) throws IOException, SQLException, ClassNotFoundException{
//			SqlSessionFactoryBuilder builder = new	SqlSessionFactoryBuilder();
//			SqlSessionFactory factory = builder.build(null, null);
//			SqlSession session = factory.openSession();
//			session.insert("");
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		String url = "jdbc:mysql://132.232.237.157:3306/kudikeji?useUnicode=true&serverTimezone=GMT%2B8&useSSL=false&allowPublicKeyRetrieval=true";
//		Connection conn = DriverManager.getConnection(url, "kdkj", "kudikeji2018.");
//		Statement st = conn.createStatement();
//		ResultSet rs = st.executeQuery("select * from kd_user");
//		while(rs.next()) {
//			  String id = rs.getString(1);
//			  System.out.println(id);
//		}
		Map<String,String> map = new LinkedHashMap<String, String>();
		map.put("username", "18768772932");
		map.put("password", "062410140402JF");
		map.put("appid", "otn");
		map.put("answer", "172,47");
		String login = "https://kyfw.12306.cn/passport/web/login";
		String check = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand&0.32534953888081053";
		String param = readlyParams(map);
		sendRequest(check, param, map);
		sendRequest1(login, param, map);
	}
	
	/**
	 * URLConnection 发送请求get
	 * @param str
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static void sendRequest(String str,String data,Map<String,String> map) throws IOException {
		File file = new File("d:/1.jpg");
		URL url = new URL(str);
		URLConnection conn = url.openConnection();
		conn.setDoInput(true);
		conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
		conn.setRequestProperty("User-Agent", userAgent);
		InputStream in = conn.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(in);
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte [] b = new byte[8];
		while((bis.read(b)!=-1)) {
			bos.write(b);
		}
		bos.flush();
		bos.close();
		bis.close();
	}
	@SuppressWarnings("unused")
	public static String sendRequest1(String str,String data,Map<String,String> map) throws IOException {
		String param = new Gson().toJson(map);
		str+="?"+data;
		System.out.println("请求地址:"+str);
		System.out.println("请求参数:"+data);
		System.out.println("请求参数1:"+param);
		URL url = new URL(str);
		URLConnection conn = url.openConnection();
//		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		conn.setRequestProperty("Origin", "https://kyfw.12306.cn");
		conn.setRequestProperty("Referer", "https://kyfw.12306.cn/otn/resources/login.html");
		conn.setRequestProperty("User-Agent", userAgent);
//		OutputStream os = conn.getOutputStream();
//		OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
//		PrintWriter pw = new PrintWriter(osw);
//		pw.println(data);
		InputStream in = conn.getInputStream();
		String result = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		while(true) {
			result += br.readLine();
			if((result.indexOf("网络可能存在问题")!=-1)) {
				System.err.println(result);
				break;
			}else if(result==null) {
				break;
			}
		}
//		pw.close();
		in.close();
		return result;
	}
	
	/**
	 * 拼接请求参数
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String readlyParams(Map<String,String> params) throws IOException {
		String data = "";
		Set<Entry<String, String>> entrys = params.entrySet();
		for (Entry<String, String> entry : entrys) {
			data += entry.getKey()+"="+entry.getValue()+"&";
		}
		return data.substring(0,data.length()-1);
	}
}