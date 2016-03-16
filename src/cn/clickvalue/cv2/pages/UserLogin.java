package cn.clickvalue.cv2.pages;

import java.util.Hashtable;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserGroup;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.web.CaptchaServiceSingleton;
import cn.clickvalue.cv2.web.ClientSession;

public class UserLogin {

	@SuppressWarnings("unused")
	@ApplicationState
	private User user;

	@Inject
	private Messages messages;

	@Inject
	private RequestGlobals globals;

	@ApplicationState
	private UserGroup userGroup;

	@Component
	private Form userLoginForm;

	@Property
	@Persist
	private String userName;

	@Property
	private String password;

	@Property
	private String validateCode;

	@Property
	private String nextPage;

	@Inject
	@Service("userService")
	private UserService userService;

	@Inject
	private BbsSynService bbsSynService;

	@ApplicationState
	private ClientSession clientSession;

	@InjectPage
	private UnActivatedPage unActivatedPage;

	@Inject
	private Request request;

	@Inject
	private Cookies cookies;

	@Inject
	private ComponentResources componentResources;

	@InjectPage
	private MessagePage messagePage;

	void onActivate() {
		nextPage = request.getParameter("nextPage");
	}

	void onPrepareForSubmit() {
		nextPage = request.getParameter("nextPage");
		userName = request.getParameter("userName");
		password = request.getParameter("password");
		validateCode = request.getParameter("validateCode");
	}

	void onValidateForm() {
		// TODO 提示信息改掉
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(userName)) {
			userLoginForm.recordError(messages.get("user_name_or_password_is_null"));
			return;
		}
		CaptchaServiceSingleton cap = CaptchaServiceSingleton.getInstance();
		if (!cap.validateCaptchaResponse(validateCode, globals.getHTTPServletRequest().getSession())) {
			userLoginForm.recordError(messages.get("identifying_code_wrong"));
			return;
		}
	}

	Object onSubmitFromUserLoginForm() {
		if (!userLoginForm.isValid()) {
			return UserLogin.class;
		}
		// 身份验证
		User authenticatedUser = userService.authenticate(userName, password);
		if (authenticatedUser == null) {
			userLoginForm.recordError(messages.get("user_name_or_password_is_wrong"));
			return UserLogin.class;
		}
		// 判断是否激活过，不对admin进行判断
		if (authenticatedUser.getUserGroup().getId() < 4 && authenticatedUser.getActived() == 0) {
			unActivatedPage.setUser(authenticatedUser);
			return unActivatedPage;
		}
		// 设置session
		setSession(authenticatedUser);

		String result = bbsSynService.userSynLogin(userName, password);
		messagePage.setMessage(authenticatedUser.getName().concat("，欢迎回来！"));
		messagePage.setHidden(result);

		if (nextPage == null) {
			messagePage.setNextPage(homePageForGroup());
		} else {
			messagePage.setNextPage(customNextPage());
		}

		return messagePage;
	}

	private String customNextPage() {
		String[] urls = StringUtils.split(nextPage, "?");
		if (urls.length > 1) {
			String[] pairs = StringUtils.split(urls[1], "&");
			Hashtable<String, String> values = new Hashtable<String, String>();
			for (int i = 0; i < pairs.length; i++) {
				String[] v = StringUtils.split(pairs[0], "=");
				values.put(v[0], v[1]);
			}
			String cookiev = values.get("uid");
			if (cookies != null && cookiev != null) {
				cookies.writeCookieValue("uid", cookiev);
			}
		}

		String link = "";
		String paramsStr = "";
		Object[] params = null;
		int pageIndex = urls[0].toLowerCase().indexOf("page/");
		if (pageIndex > 0) {
			link = urls[0].toLowerCase().substring(0, pageIndex + 4);
			paramsStr = urls[0].toLowerCase().substring(pageIndex + 5, urls[0].length());
			params = paramsStr.split("/");
			return componentResources.createPageLink(link, true, params).toURI();
		} else {
			link = urls[0];
			return link;
		}
	}

	private String homePageForGroup() {

		String group = userGroup.getName();

		if (Constants.USER_GROUP_ADVERTISER.equals(group)) {
			return "advertiser/homepage";
		}

		if (Constants.USER_GROUP_AFFILIATE.equals(group)) {
			return "affiliate/homepage";
		}

		return "admin/campaign/campaignlistpage";

	}

	private void setSession(User authenticatedUser) {
		this.user = authenticatedUser;
		this.userGroup = authenticatedUser.getUserGroup();

		clientSession.setId(authenticatedUser.getId());
		clientSession.setUserName(authenticatedUser.getName());
		clientSession.setUserGroupName(authenticatedUser.getUserGroup().getName());
		clientSession.setLanguage(authenticatedUser.getLanguage());
	}

	void cleanupRender() {
		userLoginForm.clearErrors();
	}
}
