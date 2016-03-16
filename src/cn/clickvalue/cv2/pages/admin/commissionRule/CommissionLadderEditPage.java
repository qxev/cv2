package cn.clickvalue.cv2.pages.admin.commissionRule;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionLadder;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionLadderService;

public class CommissionLadderEditPage extends BasePage {

	@Property
	@Persist
	private CommissionLadder commissionLadder;

	@Persist
	private Integer commissionId;

	@Persist
	private Integer campaignId;

	@Property
	private String title;

	@Property
	@Persist
	private Campaign campaign;

	@Component
	private Form form;

	@Inject
	private CampaignService campaignService;

	@Inject
	private CommissionLadderService commissionLadderService;

	@InjectPage
	private MessagePage messagePage;

	private Object nextPage;

	@Environmental
	private RenderSupport render;

	void onActivate(Integer campaignId) {
		this.campaignId = campaignId;
	}

	void onActivate(Integer campaignId, Integer commissionId) {
		this.commissionId = commissionId;
	}

	@SetupRender
	void setupRender() {

		campaign = campaignService.get(campaignId);
		if (commissionId == null || commissionId == 0) {
			commissionLadder = commissionLadderService.createCommissionLadder();
			commissionLadder.setCampaign(campaign);
			title = "新建";
		} else {
			commissionLadder = commissionLadderService.get(commissionId);
			title = "修改";
		}
	}

	void onValidateForm() {
		if (commissionLadder.getIsRange() == null) {
			form.recordError("请选择是否范围佣金");
		} else if (commissionLadder.getIsRange() == 0) {
			if (commissionLadder.getStartCommission() == null) {
				form.recordError("阶梯佣金不能为空");
			}
		} else if (commissionLadder.getIsRange() == 1) {
			if (commissionLadder.getStartCommission() == null || commissionLadder.getEndCommission() == null) {
				form.recordError("阶梯佣金不能为空");
			}
		} else {
			form.recordError("请选择是否范围佣金");
		}
	}

	Object onSubmit() {
		if (form.isValid()) {
			if (commissionLadder.getIsRange() == 0) {
				commissionLadder.setEndCommission(null);
			}
			commissionLadderService.save(commissionLadder);
			messagePage.setMessage("保存成功");
			messagePage.setNextPage("admin/commissionrule/commissionladderlistpage/" + campaign.getId());
			nextPage = messagePage;
			return nextPage;
		}
		return this;
	}

	void cleanupRender() {
		form.clearErrors();
	}

	void afterRender() {
		render.addScript("initRange();");
	}
}
