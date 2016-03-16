package cn.clickvalue.cv2.pages;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Session;
import org.apache.tapestry5.util.TextStreamResponse;

import cn.clickvalue.cv2.common.util.HttpUtils;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserGroup;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.web.ClientSession;

public class UserApi {

	@SuppressWarnings("unused")
	@ApplicationState
	private User user;

	@SuppressWarnings("unused")
	@ApplicationState
	private UserGroup userGroup;

	@ApplicationState
	private ClientSession clientSession;

	private boolean clientSessionExists;

	@Inject
	private UserService userService;

	@Inject
	private BbsSynService bbsSynService;

	@Inject
	private RequestGlobals globals;

	Object onActivate() {
		Request request = globals.getRequest();
		String action = request.getParameter("action");
		if ("login".equals(action)) {
			return synLogin(request);
		} else if ("logout".equals(action)) {
			return synLogout(request);
		} else if ("getgroup".equals(action)) {
			return getGroup(request);
		} else {
			return new TextStreamResponse("text/plain", "");
		}
	}

	private Object getGroup(Request request) {
		String userName = request.getParameter("username");
		String result = "0";
		try {
			if (StringUtils.isNotEmpty(userName)) {
				User user = userService.getUserByName(HttpUtils.urlDecode(userName));
				if (user != null) {
					UserGroup group = user.getUserGroup();
					result = group.getName().startsWith("admin") ? "admin" : group.getName();
				}
			}
		} catch (Exception e) {
		}
		return new TextStreamResponse("text/plain", result);
	}

	private Object synLogout(Request request) {
		Integer id = 0;
		if (clientSessionExists) {
			id = clientSession.getId();
		}
		clearSession(request);
		return new TextStreamResponse("text/plain", String.valueOf(id));
	}

	private Object synLogin(Request request) {
		String result = "0";
		String uid = request.getParameter("uid");
		String password = request.getParameter("password");
		String salt = request.getParameter("salt");
		if (StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(salt) && NumberUtils.isDigits(uid) && NumberUtils.toInt(uid) > 0) {
			User u = userService.get(NumberUtils.toInt(uid));
			if (u.getUserGroup().getId() < 4 && u.getActived() == 0) {
			} else {
				String passwordFortrans = bbsSynService.getPasswordFortrans(salt, u.getPassword());
				if (password.equals(passwordFortrans)) {
					setSession(u);
					result = String.valueOf(u.getId());
				}
			}
		}
		return new TextStreamResponse("text/plain", result);
	}

	private void setSession(User u) {
		this.user = u;
		this.userGroup = u.getUserGroup();
		clientSession.setId(u.getId());
		clientSession.setUserName(u.getName());
		clientSession.setUserGroupName(u.getUserGroup().getName());
		clientSession.setLanguage(u.getLanguage());
	}

	private void clearSession(Request request) {
		Session session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}
}
