package com.MissX.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.Security;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.MissX.utils.MapUtil;
import com.MissX.utils.net.HttpUtil.RequestMethod;
import com.MissX.utils.sign.SignUtil;


/**
 * 微信订单工具类
 * Time 2018年10月13日 下午5:27:41
 * @author H
 * Admin
 */
public class WXPayOrderUtil {
	
	/**
	 * 订单异常，下单异常时抛出此异常
	 * 2019年1月19日 下午6:36:02
	 * @author H
	 * TODO
	 * Admin
	 */
	public static class OrderException extends RuntimeException{
		private static final long serialVersionUID = 1L;
		public OrderException(String message) {
			super(message);
		}
	}
	
	/**
	 * 发送Post请求 请求支付
	 * @param create_url 微信支付接口
	 * @param param 微信支付参数
	 * @return 下单结果
	 */
	public static String CreateWXOrder(String create_url,TreeMap<String,String> params) {
		String param = MapUtil.MapToXMLString(params);
		String result = HttpUtil.SendHttpRequest(create_url, param,RequestMethod.POST);
		return result;
	}
	
	/**
	 * 微信支付二次签名
	 * @param post
	 * @param date
	 * @return 
	 * @return
	 * @throws Exception 
	 * @throws DocumentException
	 */
	public static TreeMap<String, String> twiceSign(String paramStr,String key) throws Exception{
		try {
			Long time = new Date().getTime();
			Document document = DocumentHelper.parseText(paramStr);
			@SuppressWarnings("unchecked")
			List<Element> elements = document.getRootElement().elements();
			TreeMap<String,String> WXPayMap = new TreeMap<String,String>();
			String appId = "";
			String nonceStr = "";
			String prepay_id = "";
			for (Element element : elements) {
				if("return_code".equals(element.getName())) {
					if((element.getText().indexOf("FAIL")!=-1)) {
						for (Element err : elements) {
							if("return_msg".equals(err.getName())) {
								throw new OrderException(err.getText());
							}
						}
					}
				}
				if("err_code_des".equals(element.getName())) {
					throw new RuntimeException(element.getText());
				}
				if("appid".equals(element.getName())) {
					appId = element.getText();
				}
				if("nonce_str".equals(element.getName())) {
					nonceStr = element.getText();
				}
				if("prepay_id".equals(element.getName())) {
					prepay_id = "prepay_id="+element.getText();
				}
			}
			time = time/1000;
			WXPayMap.put("appId", appId);
			WXPayMap.put("nonceStr", nonceStr);
			WXPayMap.put("package", prepay_id);
			WXPayMap.put("signType", "MD5");
			WXPayMap.put("timeStamp", time.toString());
			String param = HttpUtil.readlyParams(WXPayMap);
			
			WXPayMap.put("sign", SignUtil.getMD5(param+"&key="+key));
			return WXPayMap;
		}catch(Exception e) {
			throw e;
		}
	}
	
	/**
	 * 发起微信退款
	 * @param param 发起退款所需的参数(XML格式)
	 * @param certIs 读取证书的流
	 * @param password 证书密钥
	 * @param out_url 微信退款链接
	 * @return 微信返回的退款信息
	 */
	public static String OutWXOrder(String param,InputStream certIs,String password,String out_url) {
		BufferedReader in = null;
		String result = "";
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(certIs, password.toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, password.toCharArray()).build();
			@SuppressWarnings("deprecation")
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			HttpPost httppost = new HttpPost(out_url);
			StringEntity se = new StringEntity(param);
            httppost.setEntity(se);
            CloseableHttpResponse responseEntry = httpclient.execute(httppost);
            HttpEntity entity = responseEntry.getEntity();
            System.out.println(responseEntry.getStatusLine());
            Header [] header = responseEntry.getAllHeaders();
            for (Header header2 : header) {
				System.out.println(header2);
			}
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("Post请求异常:" + e);
			e.printStackTrace();
		}
		finally {
			try {
				if (in != null) {
					in.close();
				}
				if (certIs != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 对微信返回的加密数据进行解密
	 * @param keyStr 微信API密钥
	 * @param req_str	返回的加密数据
	 * @return	解密之后的XML文件
	 * @throws Exception 密钥错误时会解密失败
	 */
	public static String DecodeResult(String keyStr,String req_str) throws Exception{
		try {
			Security.addProvider(new BouncyCastleProvider());	//引用AES256解码支持
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding","BC");	//设置解密方式
			byte [] data_byte = Base64.decode(req_str);	//对加密串A做base64解码，得到加密串B的byte数组
			String key_str = SignUtil.getMD5(keyStr);//对商户key做md5，得到32位小写key
			SecretKeySpec key = new SecretKeySpec(key_str.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE,key);
			String resultStr = new String(cipher.doFinal(data_byte));	//对加密串B进行解密
			return resultStr;
		}catch(Exception e) {
			System.err.println(e.getMessage());
			throw e;
		}
	}
}