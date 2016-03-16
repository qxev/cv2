package cn.clickvalue.cv2.pages.affiliate;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.CommissionAccount;
import cn.clickvalue.cv2.model.CommissionExpense;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.AccountService;
import cn.clickvalue.cv2.services.logic.CommissionAccountService;
import cn.clickvalue.cv2.services.logic.CommissionExpenseService;
import cn.clickvalue.cv2.services.logic.UserService;

public class AllocateCommissionPage extends BasePage {

	@Property
	private List<CommissionExpense> commissionExpenses;

	@Inject
	private CommissionAccountService commissionAccountService;

	@Property
	private CommissionAccount commissionAccount;

	// @Persist
	// private BigDecimal confirmMoney;

	@Component(parameters = { "source=accounts", "row=account", "model=beanModel" })
	private Grid grid;

	@Component(parameters = { "source=commissionExpenses", "row=commissionExpense", "model=beanModel1" })
	private Grid grid1;

	@Inject
	private UserService userService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private BeanModelSource beanModelSource1;

	private BeanModel<Account> beanModel;

	private BeanModel<CommissionExpense> beanModel1;

	@Persist
	@Property(write = false)
	private List<Account> accounts;

	@Inject
	private ComponentResources resources;

	@Inject
	private AccountService accountService;

	@Inject
	private CommissionExpenseService commissionExpenseService;

	@Property
	private CommissionExpense commissionExpense;

	private Account account;

	private User user;

	/**
	 * 已支付佣金，未支付佣金，待支付佣金
	 */
	@SuppressWarnings("unused")
	@Property
	private BigDecimal paidCommission, unpaidCommission, appliedCommission;

	public BeanModel<CommissionExpense> getBeanModel1() {
		this.beanModel1 = beanModelSource1.create(CommissionExpense.class, true, resources);
		beanModel1.add("name", null).label("名称").sortable(false);
		beanModel1.add("account.cardNumber").label("帐号").sortable(false);
		beanModel1.add("account.ownerName").label(getMessages().get("Owner_of_account")).sortable(false);
		beanModel1.get("bankFee").label(getMessages().get("Bank_charges")).sortable(false);
		beanModel1.get("commission").label(getMessages().get("Account_commission_assignment")).sortable(false);
		beanModel1.get("personTax").label(getMessages().get("tax")).sortable(false);
		beanModel1.get("revenue").label(getMessages().get("real_revenue")).sortable(false);
		beanModel1.include("name", "account.cardNumber", "account.ownerName", "commission", "personTax", "bankFee", "revenue");
		return beanModel1;
	}

	public BeanModel<Account> getBeanModel() {
		this.beanModel = beanModelSource.create(Account.class, true, resources);
		beanModel.add("name", null).label("名称").sortable(false);
		beanModel.get("cardNumber").label("帐号").sortable(false);
		beanModel.get("ownerName").label(getMessages().get("Owner_of_account")).sortable(false);
		beanModel.add("configuration", null).label(getMessages().get("operate")).sortable(false);
		beanModel.include("name", "cardNumber", "ownerName", "configuration");
		return beanModel;
	}

	void onSuccess() {
	}

	public String getPaid() {
		return commissionExpense.getPaid() == 0 ? getMessages().get("has_not_paied") : getMessages().get("pay_for");
	}

	public String getPaidSuccessed() {
		// TODO NLS
		return commissionExpense.getPaidSuccessed() == 0 ? "不成功" : "成功";
	}

	void onActivate() {
		this.user = userService.load(this.getClientSession().getId());
		this.accounts = accountService.findAlipay(2, 0, this.getClientSession().getId());
		this.commissionAccount = commissionAccountService.getCommissionAccount(this.getClientSession().getId());
		this.commissionExpenses = commissionExpenseService.findUnPaidCommissionExpenseByAffiliateId(this.getClientSession().getId());

		// 佣金统计数据
		appliedCommission = commissionExpenseService.appliedCommission(getClientSession().getId());
		paidCommission = commissionAccount.getTotalexpense().subtract(appliedCommission);
		unpaidCommission = commissionAccount.getTotalIncome().subtract(paidCommission);
	}

	/**
	 * @return 是否是佣金申请日
	 */
	public boolean isApplyDay() {
		return commissionAccountService.isApplyDay();
	}

	public String getAccountName() {
		return getAccountName(account);
	}

	public String getAccountName1() {
		return getAccountName(commissionExpense.getAccount());
	}

	private String getAccountName(Account account) {
		return StringUtils.trimToEmpty(account.getBankName()) + StringUtils.trimToEmpty(account.getSubBank());
	}

	public boolean getHavePersonTax() {
		return commissionExpense.getPersonTax().compareTo(BigDecimal.ZERO) > 0;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}