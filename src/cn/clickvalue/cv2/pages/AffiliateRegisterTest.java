package cn.clickvalue.cv2.pages;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserGroup;
import cn.clickvalue.cv2.services.logic.BonusService;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionAccountService;
import cn.clickvalue.cv2.services.logic.MailTypeService;
import cn.clickvalue.cv2.services.logic.UserGroupService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.services.util.Security;

public class AffiliateRegisterTest {

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

	@InjectPage
	private MessagePage messagePage;

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
	private RequestGlobals requestGlobals;

	private URL url;

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
		User user = userService.createUser();

		

		user.setName(name);
		user.setEmail(email);
		user.setPassword(Security.MD5(userPassword));
		user.setLanguage(0);
		user.setUserGroup(userGroup);
		user.setId(100000);

		// 发激活邮件
		businessMailSender.sendRegfidMail(user);


		messagePage.setMessage("激活邮件已经发出！");
		messagePage.setNextPage("start");
		setNextPage("messagePage");


		String str = "http://cv2.clickvalue.cn/thanks.html?tid=" + user.getId();
		try {
			url = new URL(str);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	void onValidateForm() {
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
