package cn.clickvalue.cv2.common.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.darwinmarketing.configs.ConfigReader;

public class RealPath {

	public static final String WEB_ROOT;

	public static final String CLASS_PATH;

	public static final String WEB_ROOT_SYS;

	static {
		WEB_ROOT = replaceFileSeparator(ConfigReader.appRootDir);
		WEB_ROOT_SYS = new File(WEB_ROOT).getPath();
		CLASS_PATH = WEB_ROOT.concat("WEB-INF").concat("/classes");
	}

	private static String replaceFileSeparator(String str) {
		if (str == null || "".equals(str)) {
			return "";
		}
		Pattern pattern = Pattern.compile(Pattern.quote("\\"),Pattern.CANON_EQ);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, Matcher.quoteReplacement("/"));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 获取网站真实路径
	 * 
	 * @return String
	 */
	public static String getRoot() {
		// String path = RealPath.class.getClass().getResource("/").getPath();
		return WEB_ROOT;
	}

	public static String getRoot(String appendPath) {
		if (StringUtils.isBlank(appendPath)) {
			return getRoot();
		}

		StringBuffer buf = new StringBuffer(getRoot());
		buf.append(appendPath);
		mkDirs(buf.toString());
		return buf.toString();
	}

	public static void mkDirs(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public static String getUploadPath(String path) {
		StringBuffer sb = new StringBuffer();
		sb.append(ConfigReader.appRootDir);
		sb.append("public/uploads");
		if (StringUtils.isNotBlank(path)) {
			sb.append("/").append(path);
		}
		sb.append("/");
		return sb.toString();
	}

	/**
	 * @param path
	 *            classpath:开头代表相对于classes目录,否则代表相对于web根目录
	 * @return
	 */
	public static String getRealPath(String path) {
		if (path.startsWith("classpath:")) {
			return path.replaceFirst("classpath:", CLASS_PATH);
		} else {
			return WEB_ROOT.concat(path);
		}
	}

	/**
	 * @param absolutePath
	 *            绝对路径
	 * @return 以WEB_ROOT为根的绝对路径
	 */
	public static String getPathBaseOnRoot(String absolutePath) {
		File file = new File(absolutePath);
		return file.getPath().substring(WEB_ROOT_SYS.length()).replace(File.separator, "/");
	}
}
