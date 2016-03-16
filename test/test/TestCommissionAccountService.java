package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.CommissionAccountService;

public class TestCommissionAccountService extends
		AbstractTransactionalDataSourceSpringContextTests {

	private CommissionAccountService commissionAccountService;

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

	public void setCommissionAccountService(
			CommissionAccountService commissionAccountService) {
		this.commissionAccountService = commissionAccountService;
	}

	public void testUpdateCommissionAccount() {
		// 2008-09-10
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.YEAR, 2008);
//		calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
//		calendar.set(Calendar.DATE, 10);
//		commissionAccountService.updateCommissionAccount(calendar.getTime(), 74);
	}
}
