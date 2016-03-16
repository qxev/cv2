package cn.clickvalue.cv2.pages.advertiser;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionRuleService;

public class CommisionRuleEditPage extends BasePage {

	@InjectPage
	private MessagePage messagePage;

	@Property
	private Integer ruleId;

	@Property
	@Persist
	private Integer campaignId;

	@Persist("flash")
	@Property
	private CommissionRule commissionRule;

	@Property
	@Persist("flash")
	private Campaign campaign;

	@InjectComponent
	private Form commisionRuleForm;

	private Object redirect;

	@Inject
	@Service(value = "CommissionRuleService")
	private CommissionRuleService commissionRuleService;

	@Inject
	@Service(value = "CampaignService")
	private CampaignService campaignService;

	@Environmental
	private RenderSupport render;

	@Inject
	private ComponentResources resources;

	void afterRender() {
		render.addScript("changeDiv();");
	}

	void setupRender() {
		campaign = campaignService.get(campaignId);

		if (ruleId == null) {
			commissionRule = commissionRuleService.createCommissionRule();
		} else {
			commissionRule = commissionRuleService.get(ruleId);
		}
	}

	void onActivate(Integer campaignId) {
		this.campaignId = campaignId;
	}

	void onActivate(Integer campaignId, Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Object[] onPassivate() {
		if (ruleId == null) {
			return new Object[]{this.campaignId};
		}
		return new Object[]{this.campaignId, this.ruleId};
	}

	@OnEvent(value = "selected", component = "saveRule")
	void onSaveRule() {
		messagePage.setNextPage("advertiser/commisionrulelistpage/" + campaignId);
		redirect = messagePage;
	}

	@OnEvent(value = "selected", component = "saveAndAddLandingPage")
	void onSaveAndAddLandingPage() {
		messagePage.setNextPage("advertiser/landingpageedit/" + campaignId);
		redirect = messagePage;
	}

	Object onSubmit() {
		if (commisionRuleForm.getHasErrors()) {
			return this;
		} else {
			commissionRule.setStartDate(campaign.getStartDate());
			commissionRule.setEndDate(campaign.getEndDate());
			commissionRule.setCampaign(campaign);
			messagePage.setMessage(getMessages().get("action_success"));
			commissionRuleService.save(commissionRule);
			return redirect;
		}
	}

	Link onClicked() {
		return resources.createPageLink("advertiser/commisionrulelistpage", false, campaign.getId());
	}

	void onValidateForm() {
		if (commissionRule.getRuleType() == null) {
			commisionRuleForm.recordError(getMessages().get("type_of_commission_can_not_be_empty"));
		} else if (commissionRule.getCommissionValue() == null
				|| !ValidateUtils.isPrice(String.valueOf(commissionRule.getCommissionValue()))) {
			commisionRuleForm.recordError(getMessages().get("commission_input_not_correct"));
		} else if (String.valueOf(commissionRule.getCommissionValue()).length() > 8) {
			commisionRuleForm.recordError(getMessages().get("commission_can_not_be_greater_than_eight"));
		} else if (!ValidateUtils.isPrice(String.valueOf(commissionRule.getDarwinCommissionValue()))) {
			commisionRuleForm.recordError(getMessages().get("incorrect_commission_of_darwin"));
		} else if (this.checkRuleExists()) {
			commisionRuleForm.recordError(getMessages().get("already_exists_a_similar_commission_rules"));
		}
	}

	/**
	 * @return
	 * 
	 * 如果广告活动没有上过线，每一种佣金规则只能创建一个
	 */
	private boolean checkRuleExists() {
		Campaign campaign = campaignService.get(campaignId);
		if(campaign.getActived()==1)
			return false;
		List<CommissionRule> rules = commissionRuleService.find(
				"from CommissionRule c where c.campaign.id = ? and c.ruleType = ?", campaignId, commissionRule
						.getRuleType());
		return rules.size() == 0 ? false : true;
	}

	private Date getCorrectStartDate(CommissionRule commissionRule, Integer campaignId) {
		Date currentDate = commissionRule.getStartDate();
		List<CommissionRule> rules = commissionRuleService.find(
				"from CommissionRule cr where cr.campaign.id = ? and cr.ruleType = ?", campaignId, commissionRule
						.getRuleType());
		Date maxDate = rules.get(0).getEndDate();
		for (int i = 0; i < rules.size(); i++) {
			if (isDateBefore(maxDate, rules.get(i).getEndDate())) {
				maxDate = rules.get(i).getEndDate();
			}

		}
		if (isDateBefore(currentDate, maxDate)) {
			currentDate = maxDate;
		}
		return currentDate;

	}

	private boolean isDateBefore(Date date1, Date date2) {
		return date1.before(date2);
	}

	void cleanupRender() {
		commisionRuleForm.clearErrors();
	}

}