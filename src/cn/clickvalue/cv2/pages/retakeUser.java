package cn.clickvalue.cv2.pages;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import cn.clickvalue.cv2.components.BasePage;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.web.CaptchaServiceSingleton;

public class retakeUser extends BasePage {

	@Persist
	private String email;

	private String validateCode;

	@Inject
	private RequestGlobals globals;

	@InjectComponent
	private Form myForm;

	@Inject
	private UserService userService;

	@Inject
	private BusinessMailSender businessMailSender;

	@InjectPage
	private MessagePage messagePage;

	void onValidateForm() {
		//验证码
		if (StringUtils.isBlank(validateCode)) {
			myForm.recordError("请输入验证码");
		} else {
			boolean isValidate = CaptchaServiceSingleton.getInstance(1)
					.validateCaptchaResponse(getValidateCode(),
							globals.getHTTPServletRequest().getSession());
			if (!isValidate) {
				myForm.recordError("验证码不正确");
			}
		}
		//邮箱
		if (StringUtils.isBlank(email)) {
			myForm.recordError("用户邮箱不能为空");
		} else if (!email
				.matches("^\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,6}$")) {
			myForm.recordError("用户邮箱不合法!");
		}
	}

	void onSuccess() {
		List<User> users = userService.find("From User u where u.email = ? and u.deleted = 0", email);
		if (users.size() < 1) {
			myForm.recordError("您输入的邮箱不正确");
			return;
		}
		businessMailSender.sendRetakeUserMail(users.get(0));
		messagePage.setMessage("提交成功请到邮箱中接收邮件！"); 
		messagePage.setNextPage("index.html");
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
}
