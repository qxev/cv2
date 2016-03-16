package cn.clickvalue.cv2.pages.affiliate;

import java.math.BigDecimal;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Bonus;
import cn.clickvalue.cv2.model.CommissionAccount;
import cn.clickvalue.cv2.model.CommissionIncome;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.AccountService;
import cn.clickvalue.cv2.services.logic.BonusService;
import cn.clickvalue.cv2.services.logic.CommissionAccountService;
import cn.clickvalue.cv2.services.logic.CommissionExpenseService;
import cn.clickvalue.cv2.services.logic.CommissionIncomeService;
import cn.clickvalue.cv2.services.logic.SummaryCommissionService;
import cn.clickvalue.cv2.services.logic.UserService;

/**
 * TODO: 当前页面上的列表都是获取最新的15条记录，更多记录需要点击更多进入详细查询页面。
 * 
 * @author yu
 * 
 */
public class AffiliatePerAccountPage extends BasePage {

	@InjectPage
	private AllocateCommissionPage allocateCommissionPage;

	@Property
	private String value;

	@Inject
	private BonusService bonusService;

	@Inject
	private AccountService accountService;

	@Inject
	private SummaryCommissionService summaryCommissionService;

	@Inject
	private CommissionIncomeService commissionIncomeService;

	@Inject
	private CommissionAccountService commissionAccountService;

	@Property
	private List<Bonus> bonuses;

	private List<CommissionIncome> commissionIncomes;

	@Component(parameters = {"source=commissionIncomes", "value=commissionIncome"})
	private Loop commissionIncomesLoop;

	@Component(parameters = {"source=bonuses", "value=bonus"})
	private Loop bonusLoop;

	@Property
	private Bonus bonus;

	private List<Object[]> summaryCommissions;

	@Component(parameters = {"source=summaryCommissions", "value=obj"})
	private Loop summaryCommissionLoop;

	private Object[] obj;

	private CommissionAccount commissionAccount;

	private CommissionIncome commissionIncome;
	
	/**
	 * 已支付佣金
	 */
	@Property
	private BigDecimal paidCommission;
	
	/**
	 * 待支付佣金
	 */
	@Property
	private BigDecimal appliedCommission;
	
	/**
	 * 未支付佣金
	 */
	@Property
	private BigDecimal unpaidCommission;

	private boolean isShow;

	private User user;

	@Inject
	private UserService userService;
	
	@Inject
	private CommissionExpenseService commissionExpenseService;

	void onActivate() {
		bonuses = bonusService.getBonuses(this.getClientSession().getId());
		commissionIncomes = commissionIncomeService.getCommissionIncomes(getClientSession().getId());
//		commissionIncomes = commissionIncomeService.getCommissionIncomes(getClientSession().getId());
		commissionAccount = commissionAccountService.getCommissionAccount(this.getClientSession().getId());
		summaryCommissions = summaryCommissionService.findSummaryCommissionsByUserId(getClientSession().getId(), 0);
		user = userService.load(getClientSession().getId());
		
		//佣金统计数据
		appliedCommission = commissionExpenseService.appliedCommission(getClientSession().getId());
		paidCommission = commissionAccount.getTotalexpense().subtract(appliedCommission);
		unpaidCommission = commissionAccount.getTotalIncome().subtract(paidCommission);
	}

	public List<Object[]> getSummaryCommissions() {
		return summaryCommissions;
	}

	public void setSummaryCommissions(List<Object[]> summaryCommissions) {
		this.summaryCommissions = summaryCommissions;
	}

	public List<CommissionIncome> getCommissionIncomes() {
		return commissionIncomes;
	}

	public void setCommissionIncomes(List<CommissionIncome> commissionIncomes) {
		this.commissionIncomes = commissionIncomes;
	}

	public CommissionIncome getCommissionIncome() {
		return commissionIncome;
	}

	public void setCommissionIncome(CommissionIncome commissionIncome) {
		this.commissionIncome = commissionIncome;
	}

	public CommissionAccount getCommissionAccount() {
		return commissionAccount;
	}

	public void setCommissionAccount(CommissionAccount commissionAccount) {
		this.commissionAccount = commissionAccount;
	}

	public Object[] getObj() {
		return obj;
	}

	public void setObj(Object[] obj) {
		this.obj = obj;
	}

	/**
	 * 是否显示链接 1.必须有一个银行账户. 2.用户未点确认.
	 * 第二点废弃掉，不用了（09-02-11）
	 * @return boolean
	 */
	public boolean getIsShow() {
		if (!accountService.hasAccount(2, 0, getClientSession().getId())) {
			return false;
		}
		return true;
	}
	
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	Object onApplyAccount() {
//		if (!getIsShow()) {
//			addError("不符合申请条件", false);
//			return this;
//		} else {
//			allocateCommissionPage.setConfirmMoney(commissionAccount.getConfirmMoney());
			return allocateCommissionPage;
//		}
	}
	
}