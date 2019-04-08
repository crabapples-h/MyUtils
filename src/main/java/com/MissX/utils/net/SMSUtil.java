package com.MissX.utils.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.MissX.utils.net.HttpUtil.RequestMethod;
import com.MissX.utils.sign.SignUtil;

public class SMSUtil {
    private String url;
    private String username;
    private String password;
    
	public SMSUtil(String url,String username,String password) {
		this.username = username;
		this.password = password;
		this.url = url;
	}
    private static String getRequest(Map<String,String> params,String url){
        String result = net(url, params);
        return result;
    }
    
    public String sendCodeYouXin(String phone,String text) {
    	Map<String,String> params = new HashMap<String,String>();//请求参数
    	String time = new SimpleDateFormat("MMddHHmmss").format(new Date());
        params.put("UserName",username);
        params.put("Timestemp",time);
        String sign = SignUtil.getMD5(username+password+time);
        params.put("Key",sign);
        params.put("Content","text ");
        params.put("CharSet","utf-8");
        params.put("Mobiles",phone);
        params.put("Priority","1");
        params.put("SMSID",(new Integer((int)Math.random())).toString());
        String json = getRequest(params,url);
        return json;
  }
    
    public String sendMeassageYouXin(String phone,String text) {
    	Map<String,String> params = new HashMap<String,String>();//请求参数
    	String time = new SimpleDateFormat("MMddHHmmss").format(new Date());
    	params.put("UserName",username);
    	params.put("Timestemp",time);
    	String sign = SignUtil.getMD5(username+password+time);
    	params.put("Key",sign);
    	params.put("Content",text);
    	params.put("CharSet","utf-8");
    	params.put("Mobiles",phone);
    	params.put("Priority","1");
    	params.put("SMSID",(new Integer((int)Math.random())).toString());
    	String json = getRequest(params,url);
    	return json;
    }
 
    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    private static String net(String strUrl, Map<String,String> params) {
    	String result = HttpUtil.SendHttpRequest(strUrl, urlencode(params),RequestMethod.POST);
        return result;
    }
 
    //将map型转为请求参数型
    private static String urlencode(Map<String,String>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,String> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}