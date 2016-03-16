package cn.clickvalue.cv2.pages.advertiser;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.services.logic.CommissionRuleService;

public class CommisionRuleViewPage extends BasePage {

	@Property
	private CommissionRule commissionRule;

	private Form back;

	@Inject
	@Service(value = "CommissionRuleService")
	private CommissionRuleService commissionRuleService;

	void onActivate(int id) {
		commissionRule = commissionRuleService.findUniqueBy("id", id);
	}

	public String getFormatRuleType() {
		return Constants.formatCommissionType(commissionRule);
	}

	Object onSubmit() {
		return CommisionRuleListPage.class;

	}

}