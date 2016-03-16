package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.PimReportDataService;

public class PimReportDataServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private PimReportDataService pimReportDataService;

	public void setPimReportDataService(PimReportDataService pimReportDataService) {
		this.pimReportDataService = pimReportDataService;
	}

	public void test() {
		pimReportDataService.findByAffiliateId(2);
	}
}
