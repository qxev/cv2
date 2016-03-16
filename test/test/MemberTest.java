package test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.clickvalue.cv2.services.util.DiscuzPassportUtils;
import cn.clickvalue.cv2.services.util.Security;

public class MemberTest {

	public static void main(String[] args) throws UnsupportedEncodingException {
		Map<String, String> member = new LinkedHashMap<String, String>();
		member.put("cookietime", String.valueOf(30*24*3600));
		member.put("time", String.valueOf(new Date().getTime()));
		member.put("username", "david");
		member.put("password", "95cb156b3232dd195ac3a73ee473923e");
		member.put("email", "yuweijun1978@gmail.com");
		String key = "1234567890";
		String enc = DiscuzPassportUtils.passportEncode(member);
		String auth = DiscuzPassportUtils.passportEncrypt(enc, key);
		System.out.println(auth); // 输出加密结果
		enc = DiscuzPassportUtils.passortDecrypt(auth, key);
		System.out.println(enc); // 输出解密结果

		String forward = "http://www.clickvalue.cn/bbs";
		String str = "login" + auth + forward + key;
		String verify = Security.MD5(str);

		String location = "http://www.clickvalue.cn/bbs"
				+ "/api/passport.php?action=login&auth="
				+ URLEncoder.encode(auth, "UTF-8") + "&forward="
				+ URLEncoder.encode(forward, "UTF-8") + "&verify="
				+ URLEncoder.encode(verify, "UTF-8");
		System.out.println(location);
	}
}
