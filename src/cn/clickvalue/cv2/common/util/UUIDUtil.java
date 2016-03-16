package cn.clickvalue.cv2.common.util;

import org.hibernate.HibernateException;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;

/**
 * 猫猫 UUID生成器助手类............. 中国的软件业好垃圾
 */
public class UUIDUtil {

	public static String getUUID() {
		IdentifierGenerator uuid = new UUIDHexGenerator();
		try {
			return uuid.generate(null, null).toString();
		} catch (HibernateException e) {
			return org.apache.commons.lang.RandomStringUtils
					.randomAlphabetic(19)
					+ System.currentTimeMillis();
		}
	}

	public static void main(String[] args) {
//		System.out.println(getUUID());
//		System.exit(0);
	}
}
