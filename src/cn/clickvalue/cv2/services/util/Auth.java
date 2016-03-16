package cn.clickvalue.cv2.services.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 * 判断用户是否已经登录成功
 * 如果登录成功
 *      根据@Auth中提供的userGroup.name数组判断用户是否有权限访问此page
 * 如果登录失败
 *      返回UserLogin
 * </pre>
 * 
 * @author yu
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {
	public static enum Role {
		ADVERTISER, AFFILIATE, BD, ADMIN1, ADMIN2, ADMIN3
	};

	Role[] value();
}
