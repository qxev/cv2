package cn.clickvalue.cv2.services.util;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserGroup;

/**
 * 子类必须实现dispatch方法，并调用父类构造函数初始化 asm，componentSource；
 * 
 * <pre>
 * 用户访问权限控制前置过滤器[after RootPath and Assets]
 * 首先检查请求的page class是否有设置@Auth这个annotation
 * 如果有@Auth
 *      则当前page需要验证用户权限
 *      根据用户的组别控制用户访问此page
 *      如果用户所在组别在@Auth的value数组中
 *          将request移交给分发链上的下一个分发器dispatcher，通过后即会渲染此page
 *      否则
 *          用户是否登录成功
 *          如果用户已经登录
 *              重定向到一个错误页面
 *          否则
 *              重定向到登录页面
 * 否则
 *      直接return false，将request移交给分发链上的下一个分发器dispatcher
 *      一般即展示页面给访问者
 * </pre>
 * 
 * @author yu
 * 
 */
public abstract class AuthController implements Dispatcher {

	protected final static String LOGIN_PAGE = "/UserLogin";

	protected final static String ACCESS_ERROR_PAGE = "/AccessError";

	private ApplicationStateManager asm;

	private final ComponentSource componentSource;

	/**
	 * Receive all the services needed as constructor arguments. When we bind this service, T5 IoC will provide all the services !
	 */
	public AuthController(ApplicationStateManager asm, ComponentSource componentSource) {
		this.asm = asm;
		this.componentSource = componentSource;
	}

	/**
	 * Check the rights of the user for the page requested
	 * 
	 * @throws IOException
	 */
	protected boolean checkAuth(String pageName, Request request, Response response) throws IOException {

		boolean canAccess = true;

		Component page = componentSource.getPage(pageName);
		AnnotatedElement ae = page.getClass();
		boolean pageNeedAuth = ae.isAnnotationPresent(Auth.class);

		if (!pageNeedAuth) {
			ae = page.getClass().getSuperclass();
			pageNeedAuth = page.getClass().getSuperclass().isAnnotationPresent(Auth.class);
		}

		if (pageNeedAuth) {
			canAccess = false;
			boolean existUserClass = asm.exists(User.class);
			if (existUserClass && asm.get(User.class).getId() != null) {
				UserGroup group = asm.get(UserGroup.class);
				Auth authAnnotation = ae.getAnnotation(Auth.class);
				Auth.Role[] roles = authAnnotation.value();
				String roleName = null;
				String groupName = null;
				String regExp = "^ADMIN\\d$";
				Pattern p = Pattern.compile(regExp);
				Matcher r = null;
				boolean sameRole = false;
				boolean lowerSame = false;
				for (Auth.Role role : roles) {
					roleName = role.toString();
					groupName = group.getName().toUpperCase();
					r = p.matcher(roleName);
					sameRole = roleName.equals(groupName);
					lowerSame = (r.find() && (groupName.compareToIgnoreCase(roleName) < 0));
					if (sameRole || lowerSame) {
						canAccess = true;
						break;
					}
				}
			} else {
				String cpth = request.getContextPath();
				String newUrl = cpth.concat(LOGIN_PAGE);
				response.sendRedirect(newUrl);
				return true;
			}
		}

		if (!canAccess) {
			String cpth = request.getContextPath();
			String newUrl = cpth.concat(ACCESS_ERROR_PAGE);
			response.sendRedirect(newUrl);
			return true;
		}

		return false;
	}
}
