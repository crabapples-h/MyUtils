package utilTest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.junit.Test;

import com.google.gson.Gson;


public class TestCase3 {
	public int CONN_TIMEOUT = 50000;	//链接超时时间
	public int READ_TIMEOUT = 50000;	//响应超时时间
	public String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6799.400 QQBrowser/10.3.2908.400";	//请求头
	
	
	@Test
	public void send() throws IOException, KeyManagementException, NoSuchAlgorithmException{
		Map<String,String> map = new LinkedHashMap<String, String>();
		map.put("username", "18768772932");
		map.put("password", "062410140402JF");
		map.put("appid", "otn");
		map.put("answer", "172,47");
		String login = "https://kyfw.12306.cn/passport/web/login";
		String check = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand&0.32534953888081053";
		String param = readlyParams(map);
//		sendRequest(check, param, map);
		sendRequest1(login, param, map);
	}
	
	/**
	 * URLConnection 发送请求get
	 * @param str
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public void sendRequest(String str,String data,Map<String,String> map) throws IOException {
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
		bis.close();
		bos.close();
	}
	@SuppressWarnings("unused")
	public String sendRequest1(String str,String data,Map<String,String> map) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		SSLTest sslTest = new SSLTest();
		String param = new Gson().toJson(map);
		SSLContext ssl = SSLContext.getInstance("TLS");
		ssl.init(null, new TrustManager[] {sslTest}, null);
//		str+="?"+data;
		PrintWriter pw = null;
//		BufferedReader in = null;
		System.out.println("请求地址:"+str);
		System.out.println("请求参数:"+data);
		System.out.println("请求参数1:"+param);
		URL url = new URL(str);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setSSLSocketFactory(ssl.getSocketFactory());
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		conn.setRequestProperty("User-Agent", userAgent);
		
		pw = new PrintWriter(conn.getOutputStream());
		pw.println(data);
		pw.flush();
		pw.close();
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
		System.out.println();
		in.close();
		return result;
	}
	
	/**
	 * 拼接请求参数
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public String readlyParams(Map<String,String> params) throws IOException {
		String data = "";
		Set<Entry<String, String>> entrys = params.entrySet();
		for (Entry<String, String> entry : entrys) {
			data += entry.getKey()+"="+entry.getValue()+"&";
		}
		return data.substring(0,data.length()-1);
	}
}