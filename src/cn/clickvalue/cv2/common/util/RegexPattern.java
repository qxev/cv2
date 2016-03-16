package cn.clickvalue.cv2.common.util;

/**
 * 常用正则表达式.
 * 
 * 猫猫
 */
public class RegexPattern {
	/**
	 * Email
	 */
	public static final String EMAIL = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	/**
	 * 邮编
	 */
	public static final String ZIP_CODE = "^[0-9]{6}$";

	/**
	 * 日期
	 */
	public static final String DATE = "^((19)|(20))[0-9]{2}-([0-9]|(0[0-9])|(1[0-2]))-([0-9]|([0-2][0-9])|([3][0-1]))$";

	/**
	 * 手机号码
	 */
	public static final String MOBILE_NUM = "^\\d{11}$";

	/**
	 * 可以有小数点的数字
	 */
	public static final String NUMERIC = "^\\d+\\.?\\d*$";
	
	/**
	 * 国内电话号码
	 */
	public static final String TEL = "(d+-)?(d{4}-?d{7}|d{3}-?d{8}|^d{7,8})(-d+)?";
	
	/**
	 * 腾讯QQ号
	 */
	public static final String QQ = "^[1-9]*[1-9][0-9]*$";
	
	/**
	 * 网址URL 
	 */
	public static final String WEBSITE_URL = "^https?://";
	
	/**
	 * 匹配价钱
	 */
	
	public static final String PRICE = "^\\d+(?:\\.\\d+)?$";

}
