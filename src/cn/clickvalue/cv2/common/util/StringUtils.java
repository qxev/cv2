package cn.clickvalue.cv2.common.util;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static String trunStrWithStrLen(String str, int size) {
		if (size <= 0) {
			size = 10;
		}
		if (str == null) {
			str = "";
		}
		if (str.length() > size) {
			str = str.substring(0, (size - 2)) + "...";
		}
		return str;
	}

	public static String empty2Zero(Object str) {
		if (str == null) {
			return "0";
		}
		return str.toString();
	}

	public static String[] strs2Array(String str, String regex) {
		return str.split(regex);
	}

	public static String object2String(Object object) {

		if (object == null) {
			return "";
		}

		if (object instanceof String) {
			return (String) object;
		}

		if (object instanceof Date) {
			return DateUtil.dateToString((Date) object, "yyyy-MM-dd");
		}

		return object.toString();
	}

	public static String sortStringAsArray(String string, String regex) {
		String[] strs = string.split(regex);
		Arrays.sort(strs);
		return Arrays.toString(strs);
	}

	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 * 
	 * @param char c, 需要判断的字符
	 * @return boolean, 返回true,Ascill字符
	 */
	private static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 获取字符长度：汉、日、韩文字符长度为2，ASCII码等字符长度为1
	 * 
	 * @param c
	 *            字符
	 * @return 字符长度
	 */
	private static int getSpecialCharLength(char c) {
		if (isLetter(c)) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 获取一段字符的长度，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1
	 * 
	 * @param chars
	 *            一段字符
	 * @param specialCharsLength
	 *            输入长度，汉、日、韩文字符长度为2
	 * @return 输出长度，所有字符均长度为1
	 */
	private static int getSpecialLength(char[] chars, int specialCharsLength) {
		int count = 0;
		int normalCharsLength = 0;
		for (int i = 0; i < chars.length; i++) {
			int specialCharLength = getSpecialCharLength(chars[i]);
			if (count <= specialCharsLength - specialCharLength) {
				count += specialCharLength;
				normalCharsLength++;
			} else {
				break;
			}
		}
		return normalCharsLength;
	}

	/**
	 * 获取一段字符的长度，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1
	 * 
	 * @param chars
	 *            一段字符
	 * @param specialCharsLength
	 *            输入长度，汉、日、韩文字符长度为2
	 * @return 输出长度，所有字符均长度为1
	 */
	public static int getSpecialLength(String str) {
		if (str == null || "".equals(str)) {
			return 0;
		}
		char[] chars = str.toCharArray();
		int count = 0;
		for (int i = 0; i < chars.length; i++) {
			count += getSpecialCharLength(chars[i]);
		}
		return count;
	}

	/**
	 * 截取一段字符的长度(汉、日、韩文字符长度为2),不区分中英文,如果数字不正好，则少取一个字符位
	 * 
	 * @param str
	 *            原始字符串
	 * @param specialCharsLength
	 *            截取长度(汉、日、韩文字符长度为2)
	 * @return
	 */
	public static String specialTrim(String str, int specialCharsLength) {
		if (str == null || "".equals(str) || specialCharsLength < 1) {
			return "";
		}
		char[] chars = str.toCharArray();
		int charsLength = getSpecialLength(chars, specialCharsLength);
		return new String(chars, 0, charsLength);
	}

	public static String toHumpcase(String str) {
		if (str == null || "".equals(str)) {
			return "";
		}
		Pattern pattern = Pattern.compile("_(\\w)");
		Matcher matcher = pattern.matcher(str.toLowerCase());
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String c = matcher.group(1);
			matcher.appendReplacement(sb, Matcher.quoteReplacement(c.toUpperCase()));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}
