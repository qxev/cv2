package cn.clickvalue.cv2.pages.advertiser;

import java.math.BigDecimal;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.jms.core.JmsTemplate;

import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.AdvertiserAccount;
import cn.clickvalue.cv2.model.CampaignHistory;
import cn.clickvalue.cv2.model.CommissionIncome;
import cn.clickvalue.cv2.services.logic.AdvertiserAccountService;
import cn.clickvalue.cv2.services.logic.CampaignHistoryService;
import cn.clickvalue.cv2.services.logic.CommissionAccountService;
import cn.clickvalue.cv2.services.logic.CommissionIncomeService;
import cn.clickvalue.cv2.services.logic.JmsService;
import cn.clickvalue.cv2.services.logic.SummaryCommissionService;

public class CommisionPaymentPage extends BasePage {

	@Inject
	private CommissionAccountService commissionAccountService;

	@Inject
	private CommissionIncomeService commissionIncomeService;

	private CommissionIncome commissionIncome;

	private AdvertiserAccount advertiserAccount;

	@Inject
	private CampaignHistoryService campaignHistoryService;

	@Inject
	private AdvertiserAccountService advertiserAccountService;

	@Inject
	private SummaryCommissionService summaryCommissionService;

	@InjectPage
	private CommisionPaymentPage commisionpaymentpage;

	private CampaignHistory campaignHistory;

	private List<CampaignHistory> campaignHistorys;

	private List<CampaignHistory> confirmCampaignHistorys;

	private CampaignHistory confirmCampaignHistory;

	@Component(parameters = { "source=confirmCampaignHistorys", "value=confirmCampaignHistory" })
	private Loop confirmCampaignHistoryLoop;

	@Component(parameters = { "source=campaignHistorys", "value=campaignHistory" })
	private Loop campaignHistoryLoop;

	@Inject
	private JmsService jmsService;

	@SetupRender
	public void setupRender() {
		advertiserAccount = advertiserAccountService.findAdvertiserAccountByUserId(getClientSession().getId());

		campaignHistorys = campaignHistoryService.findCampaignHistoryByUserId(getClientSession().getId(), false);

		confirmCampaignHistorys = campaignHistoryService.findCampaignHistoryByUserId(getClientSession().getId(), true);
	}

	/**
	 * 支付
	 * 
	 * <pre>
	 * 1.确认时间=当前时间 
	 * 2.读取advertiseraccount 利用 session中的userid读取
	 * 3.广告主advertiseraccount 表中 totalexpense 的金额增加 回写advertiserAccount表中
	 * 4.summaryCommission表操作 
	 * 5.网站主冲钱 commissionincome
	 * 6.网站主收录总帐
	 * </pre>
	 */
	public Object onPay(Integer campaignHistoryId) {
		// FIXME 需要再次确认查询
		CampaignHistory confirmeCampaignHistory = campaignHistoryService.get(campaignHistoryId);
		if (confirmeCampaignHistory.getConfirmDate() == null) {
			commissionIncomeService.payCommission(confirmeCampaignHistory, getClientSession().getId());
			
			//发送消息计算积分
			jmsService.sendBuildPointsMessage(confirmeCampaignHistory.getCampaign().getId(), confirmeCampaignHistory.getStartDate(),
					confirmeCampaignHistory.getEndDate());
		} else {
			addError(getMessages().get("the_commission_already_paid"), false);
		}

		// 发送消息计算积分
		return commisionpaymentpage;
	}

	public CampaignHistory getCampaignHistory() {
		return campaignHistory;
	}

	public List<CampaignHistory> getCampaignHistorys() {
		return campaignHistorys;
	}

	public void setCampaignHistory(CampaignHistory campaignHistory) {
		this.campaignHistory = campaignHistory;
	}

	public List<CampaignHistory> getConfirmCampaignHistorys() {
		return confirmCampaignHistorys;
	}

	public void setConfirmCampaignHistorys(List<CampaignHistory> confirmCampaignHistorys) {
		this.confirmCampaignHistorys = confirmCampaignHistorys;
	}

	public CampaignHistory getConfirmCampaignHistory() {
		return confirmCampaignHistory;
	}

	public void setConfirmCampaignHistory(CampaignHistory confirmCampaignHistory) {
		this.confirmCampaignHistory = confirmCampaignHistory;
	}

	public AdvertiserAccount getAdvertiserAccount() {
		return advertiserAccount;
	}

	public void setAdvertiserAccount(AdvertiserAccount advertiserAccount) {
		this.advertiserAccount = advertiserAccount;
	}

	public boolean isShowPayButton() {
		BigDecimal confirmMoney = advertiserAccount.getConfirmMoney();
		BigDecimal countCommission = campaignHistory.getCountCommission();
		if (confirmMoney.compareTo(countCommission) > 0)
			return true;
		return false;
	}
}