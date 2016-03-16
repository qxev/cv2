package cn.clickvalue.cv2.common.util;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class ValidateUtils {

	protected static final Log logger = LogFactory.getLog(ValidateUtils.class);

	public static boolean isAlipay(String alipay) {
		Pattern pattern = null;
		for (String str : new String[] { RegexPattern.EMAIL, RegexPattern.MOBILE_NUM }) {
			pattern = getPattern(str);
			if (matcher(alipay, pattern)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMobile(String mobile) {
		Pattern pattern = null;
		pattern = getPattern(RegexPattern.MOBILE_NUM);
		return matcher(mobile, pattern);
	}

	public static boolean isEmail(String email) {
		Pattern pattern = null;
		pattern = getPattern(RegexPattern.EMAIL);
		return matcher(email, pattern);
	}

	public static boolean isZip(String zip) {
		Pattern pattern = null;
		pattern = getPattern(RegexPattern.ZIP_CODE);
		return matcher(zip, pattern);
	}

	public static boolean isTel(String tel) {
		Pattern pattern = null;
		pattern = getPattern(RegexPattern.TEL);
		return matcher(tel, pattern);
	}

	public static boolean isQQ(String qq) {
		Pattern pattern = null;
		pattern = getPattern(RegexPattern.QQ);
		return matcher(qq, pattern);
	}

	public static boolean isWebSiteUrl(String url) {
		Pattern pattern = null;
		pattern = getPattern(RegexPattern.WEBSITE_URL);
		return matcher(url, pattern);
	}

	public static boolean isPrice(String price) {
		Pattern pattern = null;
		pattern = getPattern(RegexPattern.PRICE);
		return matcher(price, pattern);
	}

	public static Pattern getPattern(String regexPattern) {
		PatternCompiler compiler = new Perl5Compiler();
		try {
			return compiler.compile(regexPattern);
		} catch (MalformedPatternException e) {
			logger.error("format error");
			return null;
		}
	}

	public static boolean isDateBefore(Date date1, Date date2) {
		return date1.before(date2);
	}

	public static boolean matcher(String input, Pattern pattern) {
		PatternMatcher matcher = new Perl5Matcher();
		return matcher.contains(input, pattern);
	}

	public static void main(String[] args) {
		// System.out.println(isTel("64165145"));
	}
}
