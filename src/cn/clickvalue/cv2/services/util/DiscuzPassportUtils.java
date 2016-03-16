package cn.clickvalue.cv2.services.util;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import sun.misc.BASE64Decoder;

/**
 * 
 * @author 李杰
 * 
 */
public class DiscuzPassportUtils {

	public static String passportEncrypt(String src, String key) {
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		String rand = "" + random.nextInt() % 32000;
		String encryptKey = Security.MD5(rand);
		int ctr = 0;
		StringBuilder tmp = new StringBuilder();

		for (int i = 0; i < src.length(); ++i) {
			ctr = (ctr == encryptKey.length() ? 0 : ctr);
			tmp.append(encryptKey.charAt(ctr));
			char c = (char) (src.charAt(i) ^ encryptKey.charAt(ctr));
			tmp.append(c);
			ctr++;
		}
		String passportKey = passportKey(tmp.toString(), key);
		return new sun.misc.BASE64Encoder().encode(passportKey.getBytes());
	}

	public static String passortDecrypt(String src, String key) {
		byte[] bytes = null;
		try {
			bytes = new BASE64Decoder().decodeBuffer(src);
			src = new String(bytes);
		} catch (Exception e) {
			return null;
		}
		src = passportKey(src, key);

		StringBuilder tmp = new StringBuilder();
		for (int i = 0; i < src.length(); ++i) {
			char c = (char) (src.charAt(i) ^ src.charAt(++i));
			tmp.append(c);
		}
		return tmp.toString();
	}

	public static String passportKey(String src, String key) {
		String encryptKey = Security.MD5(key);
		int ctr = 0;
		StringBuilder tmp = new StringBuilder();
		for (int i = 0; i < src.length(); ++i) {
			ctr = (ctr == encryptKey.length() ? 0 : ctr);
			char c = (char) (src.charAt(i) ^ encryptKey.charAt(ctr));
			tmp.append(c);
			ctr++;
		}
		return tmp.toString();
	}

	public static String passportEncode(Map<String, String> data) {
		Set<String> keys = data.keySet();
		StringBuilder ret = new StringBuilder();
		for (String key : keys) {
			ret.append(key).append("=").append(data.get(key)).append("&");
		}
		if (ret.length() > 0)
			return ret.substring(0, ret.length() - 1);
		return "";
	}
}