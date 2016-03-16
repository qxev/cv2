package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.common.util.ResultObject;
import cn.clickvalue.cv2.services.logic.SummaryCommissionService;

public class SummaryCommissionServiceTest extends
		AbstractTransactionalDataSourceSpringContextTests {

	private SummaryCommissionService summaryCommissionService;

	public void setSummaryCommissionService(
			SummaryCommissionService summaryCommissionService) {
		this.summaryCommissionService = summaryCommissionService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

	// public void testUpdateSummaryCommission() {
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(Calendar.MONTH, 5 - 1);
	// calendar.set(Calendar.YEAR, 2008);
	// calendar.set(Calendar.DATE, 9);
	//
	// Calendar calendar1 = Calendar.getInstance();
	// calendar1.set(Calendar.MONTH, 12 - 1);
	// calendar1.set(Calendar.YEAR, 2008);
	// calendar1.set(Calendar.DATE, 5);
	//
	// summaryCommissionService.updateSummaryCommission(74,
	// calendar.getTime(), calendar1.getTime());
	// }

	// public void testFindSummaryCommission() {
	//
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(Calendar.MONTH, 5 - 1);
	// calendar.set(Calendar.YEAR, 2008);
	// calendar.set(Calendar.DATE, 9);
	//
	// Calendar calendar1 = Calendar.getInstance();
	// calendar1.set(Calendar.MONTH, 12 - 1);
	// calendar1.set(Calendar.YEAR, 2008);
	// calendar1.set(Calendar.DATE, 5);
	// List<Object[]> summaryCommissions = summaryCommissionService
	// .findSummaryCommission(74, calendar.getTime(), calendar1
	// .getTime());
	// System.out.println(summaryCommissions.size());
	// }

	// public void testFindSummaryCommissionsByUserId() {
	// // a.campaign.name ,sum(a.commission)
	// List<Object[]> findSummaryCommissionsByUserId = summaryCommissionService
	// .findSummaryCommissionsByUserId(3, 0);
	// if (findSummaryCommissionsByUserId != null
	// && findSummaryCommissionsByUserId.size() > 0) {
	// for (int i = 0; i < findSummaryCommissionsByUserId.size(); i++) {
	// Object[] object = findSummaryCommissionsByUserId.get(i);
	// System.out.println(object[0]);
	// System.out.println(object[1]);
	// System.out.println("------------------------");
	// }
	// }
	// }

//	public void testFindSummaryCommission() {
//		List<Object> findSummaryCommission = summaryCommissionService.findSummaryCommissionByAffiliateId(3);
//		System.out.println(findSummaryCommission);
//	}
	
	public void testfindSummaryCommissionByAdvertiserId(){
		List<ResultObject> resultObjects = summaryCommissionService.findSummaryCommissionByAffiliateId(2);
		System.out.println(resultObjects.size());
	}
}
