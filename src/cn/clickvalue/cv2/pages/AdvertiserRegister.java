package cn.clickvalue.cv2.pages;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import cn.clickvalue.cv2.model.AdvertiserAccount;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserGroup;
import cn.clickvalue.cv2.model.UserInfo;
import cn.clickvalue.cv2.services.logic.AdvertiserAccountService;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;
import cn.clickvalue.cv2.services.logic.UserGroupService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.services.util.Security;

public class AdvertiserRegister {

	private Object nextPage;

	@Persist("flash")
	private String name;

	private String userPassword;

	private String userRepassword;

	@Persist("flash")
	private String email;

	@InjectComponent
	private Form registrationForm;

	@ApplicationState
	private User user;

	@Inject
	@Service("userService")
	private UserService userService;

	@InjectPage
	private MessagePage messagePage;

	@Inject
	@Service("userGroupService")
	private UserGroupService userGroupService;

	@Inject
	private AdvertiserAccountService advertiserAccountService;

	@Inject
	private BusinessMailSender businessMailSender;

	@Inject
	private BbsSynService bbsSynService;

	@Inject
	private RequestGlobals requestGlobals;

	private URL url;
	
	@Component(id = "name")
	private TextField nameField;

	@Component(id = "userPassword")
	private PasswordField userPasswordField;

	@Component(id = "userRepassword")
	private PasswordField userRepasswordField;

	@Component(id = "email")
	private TextField emailField;

	public UserGroupService getUserGroupService() {
		return userGroupService;
	}

	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserRepassword() {
		return userRepassword;
	}

	public void setUserRepassword(String userRepassword) {
		this.userRepassword = userRepassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Form getRegistrationForm() {
		return registrationForm;
	}

	public void setRegistrationForm(Form registrationForm) {
		this.registrationForm = registrationForm;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// public IUserModule getUserModule() {
	// return userModule;
	// }
	//
	// public void setUserModule(IUserModule userModule) {
	// this.userModule = userModule;
	// }

	/**
	 * 处理表单提交事件，将注册用户的信息写入到数据库后。
	 * 
	 * <pre>
	 * 判断email、password是否符合相应，repassword是否和password相同
	 * 如果全部正确
	 *   查询数据库内是否有与email相同的用户名
	 *     如果没有重复
	 *       进行入库操作，并设置跳转页
	 *     否则
	 *       不执行操作，显示相应错误提示信息
	 * 否则
	 * 不继续执行操作，显示相应错误提示信息
	 * 
	 * </pre>
	 */
	void onSuccess() {

		UserGroup userGroup = userGroupService.get(1);
		UserInfo userInfo = new UserInfo();
		User user = userService.createUser();

		user.setName(name);
		user.setEmail(email);
		user.setPassword(Security.MD5(userPassword));
		user.setLanguage(0);
		user.setUserInfo(userInfo);
		user.setUserGroup(userGroup);

		userService.save(user);

		// 初始化advertiserAccount
		AdvertiserAccount advertiserAccount = advertiserAccountService.createAdvertiserAccount();
		advertiserAccount.setAdvertiserId(user.getId());
		advertiserAccountService.save(advertiserAccount);

		// 发激活邮件
		businessMailSender.sendRegfidMail(user);

		// 同步到bbs
		bbsSynService.userRegister(user, requestGlobals.getHTTPServletRequest().getRemoteAddr());

		messagePage.setMessage("激活邮件已经发出！");
		messagePage.setNextPage("start");
		setNextPage("messagePage");

		HttpServletRequest request = requestGlobals.getHTTPServletRequest();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() != 80 ? ":" + request.getServerPort() : "") + request.getContextPath() + "/";

		String str = basePath + "thanks2.html?tid=" + user.getId();
		try {
			url = new URL(str);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	void onValidateForm() {
		if (StringUtils.isBlank(name)) {
			registrationForm.recordError(nameField, "用户名不能为空!");
		} else if (name.trim().length() < 4) {
			registrationForm.recordError(nameField, "用户名长度不能小于4位!");
		} else if (!name.matches("^\\w+$")) {
			registrationForm.recordError(nameField, "用户名包含非法字符!");
		} else {
			List<User> users = userService.find("From User u where u.name = ?", name);
			if (users.size() > 0) {
				registrationForm.recordError(nameField, "用户名已经存在!");
			}
		}

		if (StringUtils.isBlank(email)) {
			registrationForm.recordError(emailField, "用户邮箱不能为空!");
		} else if (!email.matches("^\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,6}$")) {
			registrationForm.recordError(emailField, "用户邮箱不合法!");
		} else {
			List<User> users = userService.find("From User u where u.email = ?", email);
			if (users.size() > 0) {
				registrationForm.recordError(emailField, "用户邮箱已经被使用!");
			}
		}

		if (StringUtils.isBlank(userPassword) || StringUtils.isBlank(userRepassword)) {
			if (StringUtils.isBlank(userPassword)) {
				registrationForm.recordError(userPasswordField, "密码不能为空!");
			}
			if (StringUtils.isBlank(userRepassword)) {
				registrationForm.recordError(userRepasswordField, "重复密码不能为空!");
			}
		} else if (!userPassword.equals(userRepassword)) {
			registrationForm.recordError(userRepasswordField, "两次密码输入不一致!");
		} else if (!userPassword.matches("^[0-9a-zA-Z_]{6,16}$")) {
			registrationForm.recordError(userPasswordField, "请使用长度在6到16且不包含非法字符的密码!");
		}
	}

	void cleanupRender() {
		registrationForm.clearErrors();
	}

	URL onSubmit() {
		return url;
	}

	// @OnEvent(value="selected",component="cancel")
	void onActionFormCancel() {
		registrationForm.clearErrors();
	}

	public Object getNextPage() {
		return nextPage;
	}

	public void setNextPage(Object nextPage) {
		this.nextPage = nextPage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
