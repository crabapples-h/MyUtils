package com.MissX.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.MissX.utils.Enum.EXCEPTION;
import com.MissX.utils.Enum.PRINT;
import com.MissX.utils.check.EmptyCheckUtil;
import com.MissX.utils.check.EmptyCheckUtil.CheckException;

/**
 * 网络请求工具类
 * 2019年1月8日 下午2:57:52
 * @author H
 * TODO 发起网络请求时使用
 * Admin
 */
public class HttpUtil {
	public enum RequestMethod {
		POST,GET;
	}
	private static final Map<String,String> HEADER = new TreeMap<String,String>();
	private static final int CONN_TIMEOUT = 30000;	//链接超时时间
	private static final int READ_TIMEOUT = 30000;	//响应超时时间
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6799.400 QQBrowser/10.3.2908.400";	//请求头
	static {
		HEADER.put("Accept", "*/*");
		HEADER.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		HEADER.put("User-Agent", USER_AGENT);
	}
	
	/**
	 * 发送Http请求(默认请求头)
	 * @param url 发送Http请求
	 * @param param 请求参数
	 * @return 返回响应结果
	 */
	public static String SendHttpRequest(String url,String param,RequestMethod method) {
		String result = sendRequest(url, param, null,method);
		return result;
	}
	
	/**
	 * 发送Http请求(自定义请求头)
	 * @param url 发送Http请求
	 * @param param 请求参数
	 * @param header 请求头(若不设置则使用默认请求头)
	 * @return 返回响应结果
	 */
	public static String SendHttpRequest(String url,String param,Map<String,String> header,RequestMethod method) {
		String result = sendRequest(url, param, header, method);
		return result;
	}
	
	/**
	 * 设置请求头
	 * @param conn传入需要设置请求头的HttpURLConnection
	 * @return 返回设置完毕之后的HttpURLConnection
	 */
	private static HttpURLConnection setRequest(HttpURLConnection conn,Map<String,String> header) {
		if(header==null) {
			Set<String> keySet = HEADER.keySet();
			for (String key : keySet) {
				conn.setRequestProperty(key, HEADER.get(key));
			}
			return conn;
		}else {
			Set<String> keySet = header.keySet();
			for (String key : keySet) {
				conn.setRequestProperty(key, header.get(key));
			}
			return conn;
		}
	}

	/**
	 * 发送http请求
	 * @param link 链接地址
	 * @param params 参数
	 * @param header 请求头
	 * @return 响应消息
	 */
	private static String sendRequest(String link,String params,Map<String,String> header,RequestMethod method) {
		PrintWriter print = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if(method==RequestMethod.POST) {
				conn.setRequestMethod("POST");
			}else {
				conn.setRequestMethod("GET");
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			setRequest(conn,header);
			conn.setConnectTimeout(CONN_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
			print = new PrintWriter(osw);
			print.print(params);
			print.flush();
			print.close();
			InputStream in = conn.getInputStream();
			String result = "";
			reader = new BufferedReader(new InputStreamReader(in));
			while(true) {
				String str = reader.readLine();
				if(null == str) {
					break;
				}
				result += str;
			}
			in.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "-1";
	}
	
	/**
	 * 将请求参数map转换为字符串
	 * @param params 需要发送的参数
	 * @return	转换之后的字符串
	 */
	public static String readlyParams(TreeMap<String,String> params) {
		try {
			if(null != params) {
				Map<String,Object> map = new HashMap<String,Object>(params);
				if(EmptyCheckUtil.EmptyCheckMapByParamsIsAll(map, EXCEPTION.UN_EXCEPTION, PRINT.UN_PRINT)) {
					String data = "";
					Set<Entry<String, String>> entrys = params.entrySet();
					for (Entry<String, String> entry : entrys) {
						data += entry.getKey()+"="+entry.getValue()+"&";
					}
					return data.substring(0,data.length()-1);
				}
			}
		} catch (CheckException e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}
}