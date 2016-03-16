package cn.clickvalue.cv2.pages.admin.bonus;

import java.math.BigDecimal;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Bonus;
import cn.clickvalue.cv2.model.CommissionAccount;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.BonusService;
import cn.clickvalue.cv2.services.logic.CommissionAccountService;

public class BonusEditPage extends BasePage {

	@Property
	@Persist
	private Bonus bonu;

	@Property
	@Persist
	private Integer sign;

	@Property
	@Persist
	private BigDecimal bonusValue;

	private Integer bonuId;

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

	void onActivate(Integer bonuId) {
		this.bonuId = bonuId;
	}

	Integer onPassivate() {
		return bonuId;
	}

	@SetupRender
	void setupRender() {
		bonu = bonusService.get(bonuId);
		if (bonu.getBonusValue().floatValue() == 0) {
			sign = 0;
		} else {
			bonusValue = bonu.getBonusValue().abs();
			sign = (bonu.getBonusValue().divide(bonusValue)).intValue();
		}
	}

	void onValidateForm() {
		if (sign == null) {
			form.recordError("请选择奖励或者惩罚");
		} else {
			if (sign != 0) {
				if (bonusValue == null || bonusValue.floatValue() <= 0) {
					form.recordError("奖惩佣金必须为正数");
				}
			}else{
				form.recordError("请选择奖励或者惩罚");
			}
		}
	}

	Object onSubmit() {
		if (form.isValid()) {
			CommissionAccount commissionAccount = commissionAccountService
					.getCommissionAccount(bonu.getUser().getId());
			BigDecimal value = new BigDecimal(0);
			BigDecimal oldValue = bonu.getBonusValue();
			if (sign == 0) {
//				value = new BigDecimal(sign);
			} else {
				value = bonusValue.multiply(new BigDecimal(sign));
			}
			bonu.setBonusValue(value);
			
			//修改网站主总帐
			BigDecimal gapValue = value.subtract(oldValue);
			commissionAccount.setTotalIncome(commissionAccount.getTotalIncome().add(gapValue));
			commissionAccountService.save(commissionAccount);
			
			bonusService.save(bonu);
			messagePage.setMessage("修改成功");
			messagePage.setNextPage("admin/bonus/listpage");
			return messagePage;
		} else {
			return this;
		}
	}

	void afterRender() {
//		render.addScript("initShowOrHidenByRadio('bonusValue');");
	}
}
