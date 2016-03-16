package cn.clickvalue.cv2.common.util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.velocity.texen.util.PropertiesUtil;

/**
 * 属性文件读取
 */
public class PropertiesUtils {
	public static Properties loadProperties(String resource) {
		Properties props = new Properties();
		if ((resource == null) || (resource.trim().equals(""))) {
			return props;
		}
		if (!resource.endsWith("properties")) {
			resource = resource + ".properties";
		}
		InputStream propertiesStream = PropertiesUtil.class.getClassLoader()
				.getResourceAsStream(resource);
		if (propertiesStream == null) {
			propertiesStream = PropertiesUtil.class.getClassLoader()
					.getResourceAsStream("/" + resource);
		}
		if (propertiesStream != null) {
			try {
				props.load(propertiesStream);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return props;
	}
}
