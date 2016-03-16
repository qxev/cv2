package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.PimReportSummary;
import cn.clickvalue.cv2.services.logic.PimReportSummaryService;

public class PimReportSummaryServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private PimReportSummaryService pimReportSummaryService;

	public void setPimReportSummaryService(PimReportSummaryService pimReportSummaryService) {
		this.pimReportSummaryService = pimReportSummaryService;
	}

	public void testFindAll() {
		List<PimReportSummary> list = pimReportSummaryService.findAll();
		for (PimReportSummary p : list) {
			System.out.printf("%s : %s", p.getAffiliate().getName(), String.valueOf(p.getPoints()));
		}
	}
}
