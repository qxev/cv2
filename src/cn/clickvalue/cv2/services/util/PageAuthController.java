package cn.clickvalue.cv2.services.util;

import java.io.IOException;

import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

/**
 * 用户访问页面权限前置过滤器[before:PageRender]
 * 
 * @author larry
 * 
 */
public class PageAuthController extends AuthController {

	private final ComponentClassResolver resolver;

	/**
	 * Receive all the services needed as constructor arguments. When we bind this service, T5 IoC will provide all the services !
	 */
	public PageAuthController(ApplicationStateManager asm, ComponentSource componentSource, ComponentClassResolver resolver) {
		super(asm, componentSource);
		this.resolver = resolver;
	}

	public boolean dispatch(Request request, Response response) throws IOException {
		String path = request.getPath();
		if (path.equals("")) {
			return false;
		}
		int nextslashx = path.length();
		String pageName;
		boolean endWith = false;
		int nextPos = -1;
		while (true) {
			pageName = path.substring(1, nextslashx);
			endWith = pageName.endsWith("/");
			if (!endWith && resolver.isPageName(pageName)) {
				break;
			}
			nextPos = nextslashx - 1;
			nextslashx = path.lastIndexOf('/', nextPos);
			if (nextslashx <= 1) {
				return false;
			}
		}
		return super.checkAuth(pageName, request, response);
	}
}
