package cn.clickvalue.cv2.pages.affiliate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.AuthenticationPage;
import cn.clickvalue.cv2.services.logic.AccountService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.web.ClientSession;

public class BankAccountEditPage extends BasePage {

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
	private ClientSession clientSession;

	@ApplicationState
	private User user;

	@InjectPage
	private BankAccountInfoEditPage bankAccountInfoEditPage;

	@InjectPage
	private AuthenticationPage authenticationPage;
	
	@Inject
	private RequestGlobals globals;
	
	@Property
    private String editOrAdd;
	
	@Persist
	@Property
	private Boolean isAdd;
	
	public Object onCreate(){
		this.accountId = null;
		this.account = null;
		return this;
	}
	
	void onActivate(Integer id) {
		this.accountId = id;
	}

	Integer onPassivate() {
		return accountId;
	}
	
	void setupRender() {
		if (accountId != null) {
			this.account = accountService.get(user.getId(), accountId);
		} else if (account == null) {
			this.account = accountService.createAccount();
		}
		
		if(account != null){
			if (account.getId() == null) {
				this.editOrAdd = getMessages().get("add_bank_account");
				this.isAdd = true;
			} else {
				this.editOrAdd = getMessages().get("Edit_bank_accounts");
				this.isAdd = false;
			}
		}
	}
	
//	@Override
	public boolean isAccess() {
		return account != null;
	}
	
	void onValidateForm() {
		if (StringUtils.isBlank(account.getCardNumber())) {
			form.recordError(getMessages().get("Bank_account_can_not_be_empty"));
		}
		if (accountService.vaildateCardNumberUniqueForAffiliate(account, user)) {
			form.recordError(getMessages().get("You_had_already_founded_this_bank_account_number"));
		}
	}

	Object onSuccess() {

		// 判断是否已经存在相同的银行账号正在提交审核，或者已经审核通过
		List<Account> accountsByCardNumber = accountService.findSameAccountByCardNumber(account);
		if (accountsByCardNumber != null && accountsByCardNumber.size() > 0) {
			// 该用户的其他用户ID
			List<Integer> anotherUserIds = clientSession.getAnotherUserIds();
			if (accountService.vaildateCardNumberByUserIds(anotherUserIds,account)) {
				Account anotherAccount = accountsByCardNumber.get(0);
				account.setBankName(anotherAccount.getBankName());
				account.setCity(anotherAccount.getCity());
				account.setIdCardNumber(anotherAccount.getIdCardNumber());
				account.setOwnerAddress(anotherAccount.getOwnerAddress());
				account.setOwnerName(anotherAccount.getOwnerName());
				account.setOwnerTelephone(anotherAccount.getOwnerTelephone());
				account.setPostcode(anotherAccount.getPostcode());
				account.setProvince(anotherAccount.getProvince());
				account.setSubBank(anotherAccount.getSubBank());
				bankAccountInfoEditPage.setDisable(true);
			}else{
				for (Account accountByCardNumber : accountsByCardNumber) {
					Integer userId = accountByCardNumber.getUser().getId();
					authenticationPage.addFiniteId(userId);
				}
				Object url;
				try {
					url = new URL(globals.getHTTPServletRequest().getRequestURL().toString());
				} catch (MalformedURLException e) {
					url = "affiliate/bankaccounteditpage";
				}
				authenticationPage.setNextPage(url);
				authenticationPage.setLastPage("affiliate/bankaccounteditpage");
				return authenticationPage;
			}
		}else{
			bankAccountInfoEditPage.setDisable(false);
		}
		
		if (isAdd) {
 			account.setUser(userService.load(this.getClientSession().getId()));
 			// 判断用户是否 已经有了一个银行
 			if (!userService.hasBank(getClientSession().getId())) {
 				user.setHasBank(1);
 				userService.save(user);
 			}
 		} else {
 			// 更新状态 重新设置为0
 			account.setVerified(Integer.valueOf(0));
 		}

		accountService.save(account);
		
		bankAccountInfoEditPage.setAccount(account);
		bankAccountInfoEditPage.setIsAdd(isAdd);
		bankAccountInfoEditPage.addInfo(getMessages().get("Preserves_successful"), false);
		return bankAccountInfoEditPage;
	}
	
	void cleanupRender() {
		form.clearErrors();
	}

}