package com.utils.net;
import java.security.MessageDigest;

/**
 * 加密工具类
 * 2019年1月8日 下午2:15:52
 * @author H
 * TODO 对字符串进行加密
 * Admin
 */
public class SignUtil {
    /**
     * SHA1加密
     * @param appSecret
     * @param nonce
     * @param curTime
     * @return
     */
    public static String getSHA1(String string) {
        return encode("sha1", string);
    }

   /**
    * MD5加密
    * @param requestBody
    * @return
    */
    public static String getMD5(String string) {
        return encode("md5", string);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}