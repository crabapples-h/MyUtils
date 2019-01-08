package com.MissX.utils.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.MissX.utils.check.EmptyCheckUtil;
import com.MissX.utils.check.EmptyCheckUtil.CheckException;
import com.MissX.utils.net.SignUtil;
import com.MissX.utils.random.CreateRandomUtil;

public class TestMain5 {
	public static void main(String [] args) throws IOException, InterruptedException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("from", "666666");
		params.put("to", "88888888");
		params.put("msgtype", "0");
		params.put("attach", "你好");
		params.put("accid", "a1v2c3d4");
//		String str = IMUtil.SendHttpRequest(IMUtil.CREATE_USER,params);
		String str = IMUtil.SendHttpRequest(IMUtil.SEND_ATTACH,params);
		System.out.println(str);
//		62055d247608310c421f81de4aaa7c77
//		System.out.println(CheckSumBuilder.getMD5("4ba4908f42e16397bd17142d408c7c1e"));
	}
	
	public static class IMUtil {
		public static final String CREATE_USER = "https://api.netease.im/nimserver/user/create.action";//创建用户
		public static final String SEND_ATTACH = "https://api.netease.im/nimserver/msg/sendAttachMsg.action";	//推送消息
		private static final String AppKey = "4499e304c4259858ecb92f0c7fb88fe7";
		private static final String AppSecret = "aa482cd674aa";
		private static final int CONN_TIMEOUT = 30000;
		private static final int READ_TIMEOUT = 30000;
		private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6799.400 QQBrowser/10.3.2908.400";
		public static String SendHttpRequest(String _url,Map<String,String> param) {
			Map<String,String> header = getHttpHeader();
			String params = readlyParams(param);
			String result = sendRequestAsPost(_url,header,params);
			return result;
		}
		
		private static String sendRequestAsPost(String _url,Map<String,String> header,String params) {
			PrintWriter print = null;
			BufferedReader reader = null;
			try {
				URL url = new URL(_url);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestProperty("Accept", "*/*");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				conn.setRequestProperty("User-Agent", USER_AGENT);
				conn.setRequestProperty("AppKey", header.get("AppKey"));
				conn.setRequestProperty("Nonce", header.get("Nonce"));
				conn.setRequestProperty("CurTime", header.get("CurTime"));
				conn.setRequestProperty("CheckSum", header.get("checkSum"));
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
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "-1";
		}
			
		private static String readlyParams(Map<String,String> params) {
			try {
				if(null != params) {
					Map<String,Object> map = new HashMap<String,Object>(params);
					if(EmptyCheckUtil.EmptyCheckMapByParamsIsArray(map, false, false)) {
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
			
		private static Map<String, String> getHttpHeader(){
			String random = CreateRandomUtil.getRandom(20);
			Long time = ((new Date().getTime())/1000);
			String checkSum = SignUtil.getSHA1(AppSecret+random+time.toString());
			Map<String,String> header = new HashMap<String,String>();
			header.put("AppKey", AppKey);
			header.put("Nonce", random);
			header.put("CurTime", time.toString());
			header.put("checkSum", checkSum);
			return header;
		}
	}
}