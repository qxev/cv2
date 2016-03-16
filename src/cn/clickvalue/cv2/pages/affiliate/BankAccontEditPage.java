package cn.clickvalue.cv2.pages.affiliate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
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
import cn.clickvalue.cv2.web.ClientSession;

public class BankAccontEditPage extends BasePage {

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

    @Property
    private String editOrAdd;

    @Inject
    private AccountService accountService;

    @Property
    private String operate;

    @Property
    private Integer id;

    @ApplicationState
    private User user;
    
    @SetupRender
    public void setupRender() {
        if (id != null) {
            this.account = accountService.get(id);
        } else if(account == null) {
    		this.account = accountService.createAccount();
        }
        if (account.getDefaultAccount() == 1) {
            this.isCheck = true;
        }
        if (account.getId() == null) {
            this.editOrAdd = getMessages().get("add_bank_account");
        } else {
            this.editOrAdd = getMessages().get("Edit_bank_accounts");
        }
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

    public SelectModel getOperateModel() {
        return selectModelUtil.getBankNamesModel();
    }

    void cleanupRender() {
        editBankAccountForm.clearErrors();
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
        if(StringUtils.isBlank(account.getPostcode())){
        	editBankAccountForm.recordError(getMessages().get("The_post_code_cannot_be_spatial"));
        }else if(!ValidateUtils.isZip(String.valueOf(account.getPostcode()))){
        	editBankAccountForm.recordError(getMessages().get("the_post_code_form_is_not_correct"));
        }
        if (StringUtils.isBlank(account.getIdCardNumber())
                || !account.getIdCardNumber().matches(
                        "^\\d{15}(?:\\d{2}.{1})?$")) {
            editBankAccountForm.recordError(getMessages().get("The_ID_card_form_is_not_correct"));
        }
        if (account.getSubBank() == null) {
            editBankAccountForm.recordError(getMessages().get("sub-branch_cannot_be_spatial"));
        }
        if (accountService.vaildateCardNumberUniqueForAffiliate(account,user)) {
        	editBankAccountForm.recordError(getMessages().get("You_had_already_founded_this_bank_account_number"));
        }
    }

    /**
     * 增加操作 无更新
     * 
     * @return Object
     */
    Object onSuccessFromEditBankAccountForm() {
        // 检查是否默认
        if (isCheck) {
            // 如果有默认的则变为0
            Account oldDefaultAccount = accountService.findDefaultAccount(
                    account, getClientSession().getId());
            if (oldDefaultAccount != null) {
                oldDefaultAccount.setDefaultAccount(new Integer(0));
                accountService.save(oldDefaultAccount);
            }
            account.setDefaultAccount(new Integer(1));
        }

        // 判断是否是新增,如果是则和user建立关系
        if (account.getId() == null) {
            account.setUser(userService.load(this.getClientSession().getId()));
            // 判断用户是否 已经有了一个银行
            if (!userService.hasBank(getClientSession().getId())) {
                user.setHasBank(1);
                userService.save(user);
            }
            messagePage.setMessage(getMessages().get("add_the_bank_account_to_succeed"));
        } else {
            // 更新状态 重新设置为0
            account.setVerified(Integer.valueOf(0));
            messagePage.setMessage(getMessages().get("update_the_bank_account_to_succeed"));
        }
        accountService.save(account);
        messagePage.setNextPage("affiliate/BankAccontListPage");
        return messagePage;
    }

    void onActivate(Integer id) {
        this.id = id;
    }

    public Integer onPassivate() {
        return id;
    }

    Object onClicked() {
        return BankAccontListPage.class;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
	}
}