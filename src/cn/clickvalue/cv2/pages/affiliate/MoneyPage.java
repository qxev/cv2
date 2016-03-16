package cn.clickvalue.cv2.pages.affiliate;

import java.math.BigDecimal;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.CommissionAccount;
import cn.clickvalue.cv2.model.CommissionExpense;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.AccountService;
import cn.clickvalue.cv2.services.logic.CommissionAccountService;
import cn.clickvalue.cv2.services.logic.CommissionExpenseService;

//FIXME 严重问题，PO不可以被Persist，用户按住control多次点击某个链接打开一个页面，然后多次提交统一表单，会出现漏洞
//在setupRender中拿出commissionAccount和commissionExpense，然后persist掉。
//在提交的时候直接保存。
//这样，在验证的时候commissionAccount和commissionExpense的数值不能反映实时的情况，而是setupRender时的情况，可能setupRender后，数值已经被修改
//FIXME @Persist 与 onPassivate()其实是不一样的，一个变量的@Persist对于一个用户的一个page只能存在一个，所以用户对一个page打开多个页面会后面的覆盖前面的
public class MoneyPage extends BasePage {

	@Persist
	private BigDecimal money;

	@InjectPage
	private MessagePage messagePage;

	@Component
	private Form editCommission;

	@Inject
	private CommissionExpenseService commissionExpenseService;

	@Inject
	private CommissionAccountService commissionAccountService;

	private BigDecimal confirmMoney;

	private Integer accountId;

	@Inject
	private AccountService accountService;

	private boolean isAccess = true;

	private CommissionAccount commissionAccount;

	private CommissionExpense commissionExpense;

	private Account account;

	public BigDecimal getConfirmMoney() {
		return confirmMoney;
	}

	public void setConfirmMoney(BigDecimal confirmMoney) {
		this.confirmMoney = confirmMoney;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	void onActivate(Integer accountId) {
		this.accountId = accountId;
	}

	Integer onPassivate() {
		return accountId;
	}

	private boolean init() {
		account = accountService.findAccount(accountId, this.getClientSession().getId());

		// account
		if (account == null) {
			return false;
		}

		commissionAccount = commissionAccountService.getCommissionAccount(this.getClientSession().getId());
		commissionExpense = commissionExpenseService.findCommissionExpenseByUseridAndAccountId(this.getClientSession().getId(), account
				.getId());

		// 用户当前可支配的钱为＝收入－支出，支出＝总支出－待支出。
		// 这个临时输入只做显示用，不可用来验证，否则会存在漏洞。
		// 比如，用户按住control键多次点击分配，打开多个页面，分别提交。
		if (commissionExpense == null) {
			this.setMoney(BigDecimal.ZERO);
		} else {
			this.setMoney(commissionExpense.getCommission());
		}
		this.setConfirmMoney(commissionAccount.getTotalIncome().subtract(commissionAccount.getTotalexpense()).add(getMoney()));
		return true;
	}

	/**
	 * Your methods may be void, or return a boolean value. Returning a value
	 * can force phases to be skipped, or even be re-visited. In the diagram,
	 * solid lines show the normal processing path. Dashed lines are alternate
	 * flows that are triggered when your render phase methods return false
	 * instead of true (or void).
	 * 
	 * @return
	 */
	void setupRender() {
		if (!init()) {
			isAccess = false;
			return;
		}
	}

	/**
	 * 页面刷出来时和提交时都会调用这个方法
	 */
	void onPrepareForSubmit() {
		init();
	}

	// @Override
	public boolean isAccess() {
		return isAccess;
	}

	/**
	 * 表单的验证
	 */
	void onValidateForm() {
		if (money == null || money.toEngineeringString().equals("")) {
			editCommission.recordError(getMessages().get("The_input_amount_cannot_for_spatial"));
		} else if (money.compareTo(confirmMoney) > 0) {
			editCommission.recordError(getMessages().get("The_input_amount_should_surpasses_commission_which_earns"));
		} else if (money.compareTo(BigDecimal.valueOf(50)) < 0) {
			editCommission.recordError("输入金额不能小于50元");
		}
	}

	Object onSuccess() {
		if (commissionExpense == null) {
			commissionExpense = commissionExpenseService.createCommissionExpense();
			commissionExpense.setAffiliateId(this.getClientSession().getId());
			commissionExpense.setAccount(account);
		}

		// 佣金价格累加
		commissionAccount.setTotalexpense(commissionAccount.getTotalexpense().subtract(commissionExpense.getCommission()).add(getMoney()));
		commissionExpense.setCommission(getMoney());
		commissionExpense.setFee(BigDecimal.ZERO);
		commissionExpenseService.save(commissionExpense);
		commissionAccountService.save(commissionAccount);
		messagePage.setMessage(getMessages().get("The_commission_assigns_successful"));
		messagePage.setNextPage("affiliate/AllocateCommissionPage");
		return messagePage;
	}

	public boolean isCanPay() {
		if (getConfirmMoney().compareTo(BigDecimal.valueOf(50.00)) < 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isApplyDay() {
		return commissionAccountService.isApplyDay();
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	void cleanupRender() {
		editCommission.clearErrors();
	}
}