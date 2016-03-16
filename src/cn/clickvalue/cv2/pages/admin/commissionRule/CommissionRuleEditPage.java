package cn.clickvalue.cv2.pages.admin.commissionRule;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.Enum.PimCoefficientEnum;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionRuleService;
import cn.clickvalue.cv2.services.logic.PimCoefficientService;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;

public class CommissionRuleEditPage extends BasePage {

	@InjectPage
	private MessagePage messagePage;

	@Property
	private Integer ruleId;

	@Property
	private Integer campaignId;

	@Property
	private boolean haveCampaignId;

	@Property
	@Persist("flash")
	private CommissionRule commissionRule;

	@Property
	@Persist("flash")
	private Float coefficient;

	@Property
	private Campaign campaign;

	@InjectComponent
	private Form commisionRuleForm;

	private Object redirect;

	@Inject
	private CommissionRuleService commissionRuleService;

	@Inject
	private PimCoefficientService pimCoefficientService;

	@Inject
	private CampaignService campaignService;

	@Inject
	private AuditingService auditingService;

	@Environmental
	private RenderSupport render;

	@Inject
	private ComponentResources resources;

	void setupRender() {
		commissionRule = commissionRuleService.get(ruleId);
		coefficient = commissionRule.getCoefficient();
		if (coefficient == null) {
			coefficient = PimCoefficientEnum.getByRuleType(commissionRule.getRuleType()).getDefaultValue();
		}
		if (campaignId == null || campaignId == 0) {
			haveCampaignId = false;
		} else {
			haveCampaignId = true;
		}
	}

	void onPrepare() {
		campaign = commissionRule.getCampaign();
		campaignId = campaign.getId();
	}

	void onActivate(Integer ruleId, Integer campaignId) {
		this.ruleId = ruleId;
		this.campaignId = campaignId;
	}

	public Integer[] onPassivate() {
		return new Integer[] { this.ruleId, this.campaignId };
	}

	void afterRender() {
		render.addScript("changeDiv();");
	}

	void onSuccess() {
		commissionRuleService.save(commissionRule);
		if (commissionRule.getVerified() == 2) {
			auditingService.updateCampaignCpa(commissionRule.getCampaign().getId());
		}
		messagePage.setMessage("保存佣金规则成功！");
		messagePage.setNextPage("admin/commissionrule/listpage");
		redirect = messagePage;
	}

	void onFailure() {
		redirect = this;
	}

	Object onSubmit() {
		return redirect;
	}

	void onValidateForm() {
		if (commissionRule.getRuleType() == null) {
			commisionRuleForm.recordError("佣金类型不能为空");
		} else if (commissionRule.getCommissionValue() == null
				|| !ValidateUtils.isPrice(String.valueOf(commissionRule.getCommissionValue()))) {
			commisionRuleForm.recordError("佣金输入不正确");
		} else if (String.valueOf(commissionRule.getCommissionValue()).length() > 8) {
			commisionRuleForm.recordError("佣金不能大于八位");
		} else if (!ValidateUtils.isPrice(String.valueOf(commissionRule.getDarwinCommissionValue()))) {
			commisionRuleForm.recordError("达闻佣金不正确");
		} else if (coefficient == null) {
			commisionRuleForm.recordError("积分系数不能为空");
		} else if (coefficient < 0) {
			commisionRuleForm.recordError("积分系数不呢功能小于0");
		}
	}

	public String getRecommendCoefficient() {
		PimCoefficientEnum coefficientEnum = PimCoefficientEnum.getByRuleType(commissionRule.getRuleType());
		return coefficientEnum.getRecommend();
	}

	void cleanupRender() {
		commisionRuleForm.clearErrors();
	}

}
