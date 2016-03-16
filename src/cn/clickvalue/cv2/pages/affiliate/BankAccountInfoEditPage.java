package cn.clickvalue.cv2.pages.affiliate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.AccountService;
import cn.clickvalue.cv2.services.logic.UserService;

public class BankAccountInfoEditPage extends BasePage {

	@Component(id = "subBank", parameters = { "value=account.subBank" })
	private TextField subBank;

	@InjectPage
	private MessagePage messagePage;

	@Inject
	private SelectModelUtil selectModelUtil;

	@Inject
	private UserService userService;

	@Component
	private Form editBankAccountForm;

	@Persist
	private Account account;

	@Property
	private boolean isCheck;

	private String editOrAdd;

	@Persist
	private Boolean isAdd;

	@Inject
	private AccountService accountService;

	@Property
	private String operate;

	@Property
	private Integer id;

	@ApplicationState
	private User user;

	@Persist
	private boolean disable = false;

	void setupRender() {
		if (account.getDefaultAccount() == 1) {
			this.isCheck = true;
		} else {
			this.isCheck = false;
		}

		if (isAdd) {
			this.editOrAdd = getMessages().get("add_bank_account");
		} else {
			this.editOrAdd = getMessages().get("Edit_bank_accounts");
		}
	}

	/**
	 * 表单的验证
	 */
	void onValidateForm() {
		if (StringUtils.isBlank(account.getBankName())) {
			editBankAccountForm.recordError(getMessages().get("Bank_name_can_not_be_empty"));
		}
		if (StringUtils.isBlank(account.getCardNumber())) {
			editBankAccountForm.recordError(getMessages().get("Bank_account_can_not_be_empty"));
		}
		if (account.getOwnerName() == null) {
			editBankAccountForm.recordError(getMessages().get("The_owner_of_account_cannot_be_spatial"));
		}
		if (StringUtils.isBlank(account.getPostcode())) {
			editBankAccountForm.recordError(getMessages().get("The_post_code_cannot_be_spatial"));
		} else if (!ValidateUtils.isZip(String.valueOf(account.getPostcode()))) {
			editBankAccountForm.recordError(getMessages().get("the_post_code_form_is_not_correct"));
		}
		if (StringUtils.isBlank(account.getIdCardNumber())
				|| !account.getIdCardNumber().matches(
						"^\\d{15}(?:\\d{2}.{1})?$")) {
			editBankAccountForm.recordError(getMessages().get("The_ID_card_form_is_not_correct"));
		}else if(!accountService.vaildateIdCardNumber(account)){
			editBankAccountForm.recordError(getMessages().get("ID_card_and_the_name_does_not_match"));
		}
		if (account.getSubBank() == null) {
			editBankAccountForm.recordError(getMessages().get("sub-branch_cannot_be_spatial"));
		}
	}

	Object onSuccess() {
		if (isCheck) {
			// 如果有默认的则变为0
			Account oldDefaultAccount = accountService.findDefaultAccount(
					account, getClientSession().getId());
			if (oldDefaultAccount != null) {
				oldDefaultAccount.setDefaultAccount(new Integer(0));
				accountService.save(oldDefaultAccount);
			}
			account.setDefaultAccount(Integer.valueOf(1));
		}

		if (isAdd) {
			messagePage.setMessage(getMessages().get("add_the_bank_account_to_succeed"));
		} else {
			messagePage.setMessage(getMessages().get("update_the_bank_account_to_succeed"));
		}
		accountService.save(account);
		messagePage.setNextPage("affiliate/BankAccontListPage");
		return messagePage;
	}

	/**
	 * @return 用户点击返回时触发
	 */
	Object onClicked() {
		return "affiliate/BankAccountEditPage";
	}

	public SelectModel getOperateModel() {
		return selectModelUtil.getBankNamesModel();
	}

	/**
	 * @return SelectModel
	 */
	public SelectModel getFatherSelectModel() {
		return selectModelUtil.getProvinceNameSelectModel();
	}

	/**
	 * 获取子项
	 * 
	 * @return List
	 */
	public List<SelectModel> getChildSelectModels() {
		return selectModelUtil.getCityNameSelectModels();
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public String getEditOrAdd() {
		return editOrAdd;
	}

	public void setEditOrAdd(String editOrAdd) {
		this.editOrAdd = editOrAdd;
	}

	public Boolean getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Boolean isAdd) {
		this.isAdd = isAdd;
	}
}