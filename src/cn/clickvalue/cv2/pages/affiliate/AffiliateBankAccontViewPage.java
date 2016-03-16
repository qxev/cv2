package cn.clickvalue.cv2.pages.affiliate;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.AccountService;

public class AffiliateBankAccontViewPage extends BasePage {

	private Integer id;

	@Property
	@Persist
	private Account account;

	@Inject
	private AccountService accountService;

	@InjectPage
	private MessagePage messagePage;

	@SetupRender
	public void setupRender() {
		account = (Account) accountService.findAccount(id, getClientSession()
				.getId());
		if (account == null) {
			addInfo("账号不存在!", false);
		}
	}
	
	public Object onClicked() {
		return BankAccontListPage.class;
	}

	/**
	 * 进入审核期
	 * @return
	 */
	public Object onAudio() {
		account.setVerified(Integer.valueOf(1));
		this.accountService.save(account);
		messagePage.setMessage(getMessages().get("action_success"));
		messagePage.setNextPage("affiliate/BankAccontListPage");
		return messagePage;
	}

	
	public Object onDelete() {
		account.setDeleted(1);
		accountService.save(account);
		messagePage.setMessage(getMessages().get("Deletes_successful"));
		messagePage.setNextPage("affiliate/BankAccontListPage");
		return messagePage;
	}

	public String getVerifiedState(Integer index) {
		return Constants.getVerifiedState(getMessages(), index);
	}

	public String getDefaultState(Integer index) {
		return Constants.getDEFAULT_STATE(getMessages(), index);
	}

	public boolean isDelete() {
		return account.getVerified() == 0 ? true : false;
	}

	public boolean isAudio() {
		return account.getVerified() == 0 ? true : false;
	}

	public void onActivate(Integer id) {
		this.id = id;
	}

	public Integer onPassivate() {
		return id;
	}
}