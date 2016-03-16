package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.services.logic.ReportSummaryService;

public class ReportSummaryServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private ReportSummaryService reportSummaryService;

	public ReportSummaryService getReportSummaryService() {
		return reportSummaryService;
	}

	public void setReportSummaryService(ReportSummaryService reportSummaryService) {
		this.reportSummaryService = reportSummaryService;
	}

	public void testFindReportByCampaign() {
//		reportSummaryService.findReportByCampaign(DateUtil.stringToDate("2009-09-28"), DateUtil.stringToDate("2009-09-29"), "139手机邮箱注册CPL广告", "", Constants.REPORT_TYPE_OF_COLLECT_BY_DATE, null, null, null, 1, 15);
		reportSummaryService.findReportByCampaign(DateUtil.stringToDate("2009-09-28"), DateUtil.stringToDate("2009-09-29"), "139手机邮箱注册CPL广告", "", Constants.REPORT_TYPE_OF_COLLECT_BY_MEMO, null, null, null, 0, 1);
	}

}
