package cn.clickvalue.cv2.pages.affiliate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.AccountService;
import cn.clickvalue.cv2.services.logic.UserService;

public class AlipayAccountEditPage extends BasePage {

	private Integer accountId;

	@Persist
	@Property
	private Account account;

	@Inject
	private AccountService accountService;

	@Inject
	private UserService userService;

	@Component
	private Form form;

	@ApplicationState
	private User user;

	private String editOrAdd;

	@Persist
	@Property
	private Boolean isAdd;

	@Property
	private boolean isCheck;

	@InjectPage
	private MessagePage messagePage;

	public Object onCreate() {
		this.accountId = null;
		this.account = null;
		return this;
	}

	void onActivate(String id) {
		if (NumberUtils.isDigits(id)) {
			this.accountId = NumberUtils.toInt(id);
		} else {
			this.accountId = null;
		}
	}

	Integer onPassivate() {
		return accountId;
	}

	void setupRender() {
		if (accountId != null) {
			this.account = accountService.getAlipay(user.getId(), accountId);
		} else if (account == null) {
			this.account = accountService.createAlipay();
		}

		if (account != null) {
			if (account.getId() == null) {
				this.editOrAdd = "新增支付宝帐号";
				this.isAdd = true;
			} else {
				this.editOrAdd = "修改支付宝帐号";
				this.isAdd = false;
			}
			isCheck = (account.getDefaultAccount() != null && account.getDefaultAccount() == 1);
		}
	}

	// @Override
	public boolean isAccess() {
		return account != null;
	}

	void onValidateForm() {
		if (StringUtils.isBlank(account.getCardNumber())) {
			form.recordError("支付宝帐号不能为空！");
		} else if (!ValidateUtils.isAlipay(account.getCardNumber())) {
			form.recordError("帐号格式不对（email或手机号）！");
		}
		if (StringUtils.isBlank(account.getOwnerName())) {
			form.recordError("开户人不能为空！");
		}
		if (accountService.vaildateCardNumberUniqueForAffiliate(account, user)) {
			form.recordError("您已经创建了相同的帐号！");
		}
	}

	Object onSuccess() {

		if (isCheck) {
			// 如果有默认的则变为0
			Account oldDefaultAccount = accountService.findDefaultAccount(account, getClientSession().getId());
			if (oldDefaultAccount != null) {
				oldDefaultAccount.setDefaultAccount(new Integer(0));
				accountService.save(oldDefaultAccount);
			}
			account.setDefaultAccount(Integer.valueOf(1));
		} else {
			account.setDefaultAccount(Integer.valueOf(0));
		}

		if (isAdd) {
			account.setUser(userService.load(this.getClientSession().getId()));
			if (!userService.hasBank(getClientSession().getId())) {
				user.setHasBank(1);
				userService.save(user);
			}
			messagePage.setMessage("新增支付宝帐号成功！");
		} else {
			account.setVerified(Integer.valueOf(0));
			messagePage.setMessage("修改支付宝帐号成功！");
		}
		account.setBankName("支付宝");
		account.setSubBank("");
		accountService.save(account);
		messagePage.setNextPage("affiliate/alipayaccountlistpage");
		return messagePage;
	}

	void cleanupRender() {
		form.clearErrors();
	}

	public String getEditOrAdd() {
		return editOrAdd;
	}

	public void setEditOrAdd(String editOrAdd) {
		this.editOrAdd = editOrAdd;
	}
}