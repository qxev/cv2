package cn.clickvalue.cv2.pages.admin.account;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.services.logic.AccountService;

public class AccountViewPage extends BasePage{
    
    @SuppressWarnings("unused")
    @Property
    private Account account;
    
    @Inject
    private AccountService accountService;
    
    void onActivate(int id) {
        account = accountService.get(id);
    }
    
    /**
	 * @return 审核状态
	 */
	public String getVerified() {
		Integer verified = account.getVerified();
		String str = Constants.formatVerified(getMessages(), verified);
		return str;
	}
}