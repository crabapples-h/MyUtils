package utilTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;

import entryTest.EntryData;
import entryTest.Train;

public class TestCase {
	public int CONN_TIMEOUT = 30000;	//链接超时时间
	public int READ_TIMEOUT = 30000;	//响应超时时间
	public String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";	//请求头
	public String str = "https://kyfw.12306.cn/otn/leftTicket/query";
	
	
	@Test
	public void send() throws IOException{
		String from = "UMW";
		String to = "GIW";
		String date = "2018-12-21";
		String url = str;
		String data = setParam(from, to, date);
		String result = sendRequest(url, data);
		Gson gson = new Gson();
		EntryData entry = gson.fromJson(result, EntryData.class);
		String info = entry.getData().getResult().toString();
		info = info.substring(1,info.length()-1);
		String [] infos = info.split(",");
		List<List<String>> lists = new ArrayList<List<String>>();
		for (String strs : infos) {
			String [] str = strs.split("\\|");
			List<String> list = new ArrayList<String>();
			for (String string : str) {
				list.add(string);
			}
			lists.add(list);
		}
		formatData(lists);
		
	}
	
	public void formatData(List<List<String>> lists){
		for (List<String> list : lists) {
			Train train = new Train();
			train.setId(list.get(0));
			train.setStatus(list.get(1));
			train.setTest1(list.get(2));
			train.setIndex(list.get(3));
			train.setStart_station(list.get(4));
			train.setEnd_station(list.get(5));
			train.setFrom_station(list.get(6));
			train.setTo_station(list.get(7));
			train.setStart_time(list.get(8));
			train.setEnd_time(list.get(9));
			train.setUse_time(list.get(10));
			train.setCanBuy(list.get(11));
			train.setTest2(list.get(12));
			train.setDate(list.get(13));
			
			
			for (int i = 0; i < 14; i++) {
				list.remove(0);
			}
			System.out.println(list);
		}
	}
	/**
	 * 设置请求参数
	 * @param from
	 * @param to
	 * @param date
	 * @return
	 * @throws IOException
	 */
	public String setParam(String from,String to,String date) throws IOException{
		Map <String,String> map = new LinkedHashMap<String,String>();
		map.put("leftTicketDTO.train_date", date);
		map.put("leftTicketDTO.from_station", from);
		map.put("leftTicketDTO.to_station", to);
		map.put("purpose_codes", "ADULT");
		String data = readlyParams(map);
//		data = JSONObject.fromObject(map).toString();
		return data;
	}
	
	/**
	 * URLConnection 发送请求post
	 * @param str
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public String sendRequest(String str,String data) throws IOException {
		str = str+"?"+data;
//		System.out.println(str);
		
		URL url = new URL(str);
		URLConnection conn = url.openConnection();
//		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("userAgent", userAgent);
		conn.setRequestProperty("Accept-Charset", "utf-8");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setConnectTimeout(CONN_TIMEOUT);
		conn.setReadTimeout(READ_TIMEOUT);
//		OutputStream os = conn.getOutputStream();
//		OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
//		osw.write(data);
		InputStream in = conn.getInputStream();
		String result = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
//		byte [] b = new byte[10];
//		while(in.read(b)!= -1) {
//			System.out.println(b);
//			result += new String(b);
//		}
		result = br.readLine();
		in.close();
//		osw.close();
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
			data += entry.getKey()+"="+URLEncoder.encode(entry.getValue(), "utf-8")+"&";
		}
		return data.substring(0,data.length()-1);
	}
}