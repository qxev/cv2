package cn.clickvalue.cv2.pages;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import cn.clickvalue.cv2.model.Bonus;
import cn.clickvalue.cv2.model.CommissionAccount;
import cn.clickvalue.cv2.model.MailType;
import cn.clickvalue.cv2.model.MailTypeUser;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserGroup;
import cn.clickvalue.cv2.model.UserInfo;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.BonusService;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionAccountService;
import cn.clickvalue.cv2.services.logic.MailTypeService;
import cn.clickvalue.cv2.services.logic.MailTypeUserService;
import cn.clickvalue.cv2.services.logic.UserGroupService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.services.util.Security;

public class AffiliateRegister {

	private Object nextPage;

	@Persist("flash")
	private String name;

	private String userPassword;

	private String userRepassword;

	@Persist("flash")
	private String email;

	@InjectComponent
	private Form registrationForm;

	@Inject
	private UserService userService;

	@Inject
	private UserGroupService userGroupService;

	@Inject
	private BusinessMailSender businessMailSender;

	@Inject
	private CommissionAccountService commissionAccountService;

	@Inject
	private MailTypeService mailTypeService;

	@Inject
	private CampaignService campaignService;

	@Inject
	private BonusService bonusService;

	@Inject
	private BbsSynService bbsSynService;

	@Inject
	private MailTypeUserService mailTypeUserService;

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
	 * <pre>
	 * 
	 */
	void onSuccess() {

		UserGroup userGroup = userGroupService.get(2);
		UserInfo userInfo = new UserInfo();
		User user = userService.createUser();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(Security.MD5(userPassword));
		user.setLanguage(0);
		user.setUserInfo(userInfo);
		user.setUserGroup(userGroup);

		userService.save(user);

		initAffiliate(user);

		// 同步到bbs
		bbsSynService.userRegister(user, requestGlobals.getHTTPServletRequest().getRemoteAddr());
		// 发激活邮件
		businessMailSender.sendRegfidMail(user);

		HttpServletRequest request = requestGlobals.getHTTPServletRequest();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() != 80 ? ":" + request.getServerPort() : "") + request.getContextPath() + "/";

		String str = basePath + "thanks1.html?tid=" + user.getId();
		try {
			url = new URL(str);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private void initAffiliate(User user) {

		// 创建网站主关联的邮件接收方案 mailTypeUser
		// FIXME
		// 在这里对这个中间表做初始化，就失去了当初这个功能设计成多对多的意义，设计成多对多是为了新增邮件类型的方便，这里如果做初始化的话，将来新增邮件类型怎么办
		List<MailType> mailTypes = mailTypeService.find("From MailType m Where m.forced = ?", 0);
		for (MailType mailType : mailTypes) {
			MailTypeUser mailTypeUser = new MailTypeUser();
			mailTypeUser.setCreatedAt(new Date());
			mailTypeUser.setUpdatedAt(new Date());
			mailTypeUser.setMailType(mailType);
			mailTypeUser.setUser(user);
			mailTypeUser.setChecked(true);
			mailTypeUserService.save(mailTypeUser);
		}

		// 创建网站主的commissionAccount
		CommissionAccount commissionAccount = commissionAccountService.createCommissionAccount();
		commissionAccount.setAffiliateId(user.getId());
		commissionAccountService.save(commissionAccount);

		// bonus, 奖励新注册成功的网站主10元
		Bonus bonus = bonusService.createBonus();
		BigDecimal value = BigDecimal.valueOf(10.000);
		bonus.setBonusValue(value);
		// campaignId=2, 达闻2008联盟网站主招募
		bonus.setCampaign(campaignService.get(2));
		bonus.setDescription("网站主注册成功奖励");
		bonus.setUser(user);
		// 直接加到总帐上
		commissionAccount.setTotalIncome(commissionAccount.getTotalIncome().add(value));
		commissionAccountService.save(commissionAccount);
		bonusService.save(bonus);

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

	/**
	 * 提交表单完成后，返回注册成功页面。
	 * 
	 * @return url
	 */
	URL onSubmit() {
		return url;
	}

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
