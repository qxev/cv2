package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.common.grid.Sort;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.DetailReportCondition;
import cn.clickvalue.cv2.model.ReportInfo;
import cn.clickvalue.cv2.services.logic.ReportTrackMatchService;

public class ReportTrackMatchServiceTest extends AbstractTransactionalDataSourceSpringContextTests {
	
	private ReportTrackMatchService reportTrackMatchService;
	
	public ReportTrackMatchService getReportTrackMatchService() {
		return reportTrackMatchService;
	}

	public void setReportTrackMatchService(ReportTrackMatchService reportTrackMatchService) {
		this.reportTrackMatchService = reportTrackMatchService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}
	
	public void testSimpFind(){
		DetailReportCondition condition = new DetailReportCondition();
		condition.setBeginDate(DateUtil.stringToDate("2008-09-01"));
		condition.setEndDate(DateUtil.stringToDate("2008-09-30"));
		condition.setCampaignId(2);
		condition.setSiteId(1);
		condition.setRuleType(101);
		int count = reportTrackMatchService.count(condition);
		System.out.println(count);
		List<ReportInfo> list = reportTrackMatchService.findReport(condition, 0, 10, new Sort("trackTime","ASC"));
		System.out.println(list);
	}

}
