package cn.clickvalue.cv2.pages.admin.bonus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import cn.clickvalue.cv2.model.AffiliateCampaignRelation;
import cn.clickvalue.cv2.model.Bonus;
import cn.clickvalue.cv2.model.CommissionAccount;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.BonusService;
import cn.clickvalue.cv2.services.logic.CommissionAccountService;

public class BonusNewPage extends BasePage {

	@Property
	@Persist
	private List<AffiliateCampaignRelation> affiliateCampaignRelations = new ArrayList<AffiliateCampaignRelation>();

	@Property
	private AffiliateCampaignRelation affiliateCampaignRelation;

	@Property
	@Persist
	private Integer sign;

	@Property
	@Persist
	private BigDecimal bonusValue;

	@Property
	@Persist
	private String description;

	@Inject
	private BonusService bonusService;

	@Inject
	private CommissionAccountService commissionAccountService;

	@Component
	private Form form;

	@InjectPage
	private MessagePage messagePage;

	@Environmental
	private RenderSupport render;

	@SetupRender
	void setupRender() {
	}

	void onValidateForm() {
		if (sign == null) {
			form.recordError("请选择奖励或者惩罚");
		} else {
			if (sign != 0) {
				if (bonusValue == null || bonusValue.floatValue() <= 0) {
					form.recordError("奖惩佣金必须为正数");
				}
			} else {
				form.recordError("请选择奖励或者惩罚");
			}
		}
	}

	Object onSubmit() {
		if (form.isValid()) {
			for (AffiliateCampaignRelation affiliateCampaignRelation : affiliateCampaignRelations) {
				CommissionAccount commissionAccount = commissionAccountService
						.getCommissionAccount(affiliateCampaignRelation
								.getAffiliate().getId());
				BigDecimal value = new BigDecimal(0);
				Bonus bonus = new Bonus();
				if (sign == 0) {
					// value = new BigDecimal(sign);
				} else {
					value = bonusValue.multiply(new BigDecimal(sign));
				}

				bonus.setBonusValue(value);
				bonus.setCampaign(affiliateCampaignRelation.getCampaign());
				bonus.setUser(affiliateCampaignRelation.getAffiliate());
				bonus.setDescription(description);

				// 网站主收入总账中直接加上奖励或扣掉惩罚
				commissionAccount.setTotalIncome(commissionAccount
						.getTotalIncome().add(value));
				commissionAccountService.save(commissionAccount);
				bonusService.save(bonus);
			}
			messagePage.setMessage("创建成功");
			messagePage.setNextPage("admin/bonus/listpage");
			return messagePage;
		} else {
			return this;
		}
	}

	public List<AffiliateCampaignRelation> getAffiliateCampaignRelations() {
		return affiliateCampaignRelations;
	}

	public void setAffiliateCampaignRelations(
			List<AffiliateCampaignRelation> affiliateCampaignRelations) {
		this.affiliateCampaignRelations = affiliateCampaignRelations;
	}

	void afterRender() {
//		render.addScript("initShowOrHidenByRadio('bonusValue');");
	}
}
