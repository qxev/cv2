package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.services.logic.CommissionRuleService;

public class CommissionRuleServiceTest extends AbstractTransactionalDataSourceSpringContextTests {
	private CommissionRuleService commissionRuleService;

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	public void setCommissionRuleService(CommissionRuleService commissionRuleService) {
		this.commissionRuleService = commissionRuleService;
	}

	public void testGetVerifiedCommissionRuleByRuleType() {
		List<CommissionRule> cmmissionRules = commissionRuleService.getCurrentEffecteCommissionRule(2);
		for (CommissionRule commissionRule : cmmissionRules) {
			System.out.println(commissionRule.getId());
			System.out.print(commissionRule.getStartDate());
			System.out.print("--");
			System.out.println(commissionRule.getEndDate());
		}
	}

}
