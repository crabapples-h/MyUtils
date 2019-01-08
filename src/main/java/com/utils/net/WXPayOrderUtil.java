package com.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.Security;
import java.util.Map;

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

import com.utils.MapUtil;
import com.utils.net.HttpUtil;
import com.utils.net.SignUtil;


/**
 * 微信订单工具类
 * Time 2018年10月13日 下午5:27:41
 * @author H
 * Admin
 */
public class WXPayOrderUtil {
	/**
	 * 发送Post请求 请求支付
	 * @param create_url 微信支付接口
	 * @param param 微信支付参数
	 * @return 下单结果
	 */
	public static String CreateWXOrder(String create_url,Map<String,String> params) {
		String param = MapUtil.MapToXMLString(params);
		String result = HttpUtil.SendHttpRequest(create_url, param);
		return result;
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