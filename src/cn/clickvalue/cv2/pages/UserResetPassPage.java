package cn.clickvalue.cv2.pages;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.services.util.Security;
import cn.clickvalue.cv2.web.CaptchaServiceSingleton;

public class UserResetPassPage {

	private String message;

	/**
	 * 记下这个user是为了防止有人从邮件点过来后却去改别人的密码
	 */
	@Persist
	private User userFromEmail;

	@Persist
	private String userName;

	private String checkStr;

	@Property
	private String oldPassword;

	@Property
	private String password;

	@Property
	private String confirmedPassword;

	@Property
	private String validateCode;

	@Inject
	private RequestGlobals globals;

	@Property
	@Persist
	private boolean isNotChecked = true;

	@InjectComponent
	private Form myForm;

	@Inject
	private UserService userService;

	@InjectPage
	private MessagePage messagePage;

	void onActivate(String parameter) {
		if (parameter != null && parameter.length() > 32) {
			this.checkStr = parameter;
		}
	}

	void setupRender() {
		if (StringUtils.isNotBlank(checkStr)) {
			Integer userId = 0;
			try {
				userId = Integer.parseInt(checkStr.substring(32));
			} catch (NumberFormatException e) {
			}

			User userItem = userService.findUniqueBy("id", userId);
			String checkStrItem = userService.getMD5User(userItem);
			if (checkStr.equals(checkStrItem)) {
				this.userName = userItem.getName();
				this.userFromEmail = userItem;
				isNotChecked = false;
			}
		}
	}

	void onValidateForm() {
		// 验证码
		if (StringUtils.isBlank(validateCode)) {
			myForm.recordError("请输入验证码");
		} else {
			boolean isValidate = CaptchaServiceSingleton.getInstance(1).validateCaptchaResponse(validateCode,
					globals.getHTTPServletRequest().getSession());
			if (!isValidate) {
				myForm.recordError("验证码不正确");
			}
		}

		if (userFromEmail != null) {
			if (!userName.equals(userFromEmail.getName())) {
				myForm.recordError("请不要试图修改别人的密码！");
			}
		}

		if (StringUtils.isBlank(userName)) {
			myForm.recordError("用户名不能为空");
		} else if (userName.trim().length() < 4) {
			myForm.recordError("用户名长度不能小于4位");
		} else if (!userName.matches("^\\w+$")) {
			myForm.recordError("用户名包含非法字符");
		}

		if (StringUtils.isBlank(password) || StringUtils.isBlank(confirmedPassword)) {
			myForm.recordError("密码不能为空!");
		} else if (!password.equals(confirmedPassword)) {
			myForm.recordError("两次密码输入不一致!");
		} else if (!password.matches("^[0-9a-zA-Z_]{6,16}$")) {
			myForm.recordError("请使用长度在6到16且不包含非法字符的密码");
		}
	}

	void onSuccess() {
		User user = null;
		if (isNotChecked) {
			user = userService.authenticate(userName, oldPassword);
		} else {
			user = userService.get(userFromEmail.getId());
		}

		if (user != null) {
			user.setPassword(Security.MD5(password));
			userService.save(user);
			messagePage.setMessage("密码修改成功");
			messagePage.setNextPage("userlogin");
			isNotChecked = true;
		} else {
			myForm.recordError("用户名或旧密码错误");
		}
	}

	Object onSubmit() {
		if (myForm.isValid()) {
			return messagePage;
		} else {
			return null;
		}
	}

	void cleanupRender() {
		myForm.clearErrors();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
