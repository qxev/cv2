package cn.clickvalue.cv2.components;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.services.util.Security;

public class PasswordEdit {

	@Component(id = "edit")
	private Form form;

	@Property
	private String password;

	@Property
	private String confirmPassword;

	@ApplicationState
	@Property
	private User user;

	@InjectPage
	private MessagePage messagePage;

	@Inject
	private UserService userService;

	@Inject
	private BbsSynService bbsSynService;

	void onValidateForm() {
		if (password.trim().length() < 6) {
			form.recordError("密码长度不能小于6位");
		}
		if (!StringUtils.equals(password, confirmPassword)) {
			form.recordError("2次输入的密码不一致");
		}
	}

	void cleanupRender() {
		form.clearErrors();
	}

	void beginRender() {
		if (user == null) {
			form.recordError("请重新登录系统修改密码");
		}
	}

	Object onSuccess() {
		try {
			user.setPassword(Security.MD5(password));
			userService.save(user);

			bbsSynService.userEdit(user.getName(), null, password, null);

			messagePage.setMessage("用戶密码修改成功");
			messagePage.setNextPage("Start");
			return messagePage;
		} catch (Exception ex) {
			form.recordError("系统出现问题,用户密码修改失败");
			ex.printStackTrace();
		}
		return this;
	}
}
