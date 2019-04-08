package com.MissX.utils.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.MissX.utils.Enum.PRINT;
import com.MissX.utils.net.HttpUtil.RequestMethod;
import com.MissX.utils.random.CreateRandomUtil;

/**
 *  网络请求工具类
 * 2019年2月22日 下午7:05:49
 * @author H
 * TODO 发起网络请求时使用（默认为POST请求）
 * Admin
 */
public class HttpUtil2 {
	private static final Map<String,String> HEADER = new TreeMap<String,String>();
	private static final int CONN_TIMEOUT = 30000;	//链接超时时间
	private static final int READ_TIMEOUT = 30000;	//响应超时时间
	private static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)";	//请求头
	private static final String boundary = CreateRandomUtil.getRandom(15);
	static {
		HEADER.put("Accept", "*/*");
		HEADER.put("User-Agent", USER_AGENT);
		HEADER.put("Connection", "Keep-Alive");
		HEADER.put("Content-Type", "multipart/form-data; boundary="+boundary);
	}
	
	/**
	 * 发送Http请求(默认请求头)
	 * @param url 发送Http请求
	 * @param param 请求参数
	 * @return 返回响应结果
	 */
	public static String SendHttpRequest(String url,RequestMethod method,PRINT print,HashMap<String, ?> ... paramsMap) {
		String result = sendRequest(url,null,method,print,paramsMap);
		return result;
	}
	
	/**
	 * 发送Http请求(自定义请求头)
	 * @param url 发送Http请求
	 * @param param 请求参数
	 * @param header 请求头(若不设置则使用默认请求头)
	 * @return 返回响应结果
	 */
	public static String SendHttpRequest(String url,Map<String,String> header,RequestMethod method, PRINT print,HashMap<String, ?> ... paramsMap) {
		String result = sendRequest(url,header,method,print,paramsMap);
		return result;
	}
	
	/**
	 * 发送http请求
	 * @param link 链接地址
	 * @param params 参数
	 * @param header 请求头
	 * @return 响应消息
	 */
	private static String sendRequest(String link,Map<String,String> header,RequestMethod method,PRINT print,HashMap<String, ?> ... paramsMap) {
		OutputStream out = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if(method==RequestMethod.GET) {
				conn.setRequestMethod("GET");
			}else {
				conn.setRequestMethod("POST");
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			setRequest(conn,header);
			conn.setConnectTimeout(CONN_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			out = conn.getOutputStream();
			sendData(conn.getOutputStream(),print,paramsMap);
			out.write(("\r\n--"+boundary+"--").getBytes());
			out.flush();
			out.close();
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
	 * 发送数据
	 * @param out
	 * @param paramsMap
	 * @throws IOException
	 */
	private static void sendData(OutputStream out , PRINT print,HashMap<String, ?> ... paramsMap) throws IOException{
		for (HashMap<String, ?> hashMap : paramsMap) {
			Set<String> keys = hashMap.keySet();
			for (String key : keys) {
				if(PRINT.check(print)) {
					System.out.println(key + " : " + hashMap.get(key));
				}
				if(hashMap.get(key) instanceof String) {
					sendStr(key,(String)hashMap.get(key),out);
				}else if(hashMap.get(key) instanceof File) {
					sendFile(key,(File)hashMap.get(key),out);
				}else if(hashMap.get(key) instanceof File[]){
					for (File file : (File [])hashMap.get(key)) {
						sendFile(key,file,out);
					}
				}
			}
		}
	}
	
	/**
	 * 发送字符串
	 * @param key
	 * @param value
	 * @param out
	 * @throws IOException
	 */
	private static void sendStr(String key, String value, OutputStream out) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n").append("--").append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\""+key+"\"; "+ "\r\n\r\n");
		sb.append(value).append("\r\n\r\n");
		out.write(sb.toString().getBytes());
		out.flush();
	}
	
	/**
	 * 发送文件
	 * @param key
	 * @param file
	 * @param out
	 */
	private static void sendFile(String key, File file,OutputStream out){
		StringBuffer sb = new StringBuffer();
		try {
			String contextType = "";
			Path path = Paths.get(file.getPath());
			contextType = Files.probeContentType(path);
			sb.append("\r\n").append("--").append(boundary).append("\r\n");
			sb.append("Content-Disposition: form-data; name=\""+key+"\"; filelength=\""+file.length()+"\";filename=\"" + file.getName() + "\"\r\n");
			sb.append("Content-Type: " + contextType + "\r\n\r\n");
			out.write(sb.toString().getBytes());
			InputStream in = new FileInputStream(file);
			byte [] datas = new byte[1024];
			int end = 0;
			while((end = in.read(datas))!= -1) {
				out.write(datas, 0, end);
			}
			out.flush();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

}