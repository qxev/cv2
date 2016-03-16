package test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.CampaignHistoryService;
import cn.clickvalue.cv2.services.logic.CommissionIncomeService;

public class TestCommissionIncomeService extends
		AbstractTransactionalDataSourceSpringContextTests {

	private CommissionIncomeService commissionIncomeService;

	private CampaignHistoryService campaignHistoryService;

	public void setCommissionIncomeService(
			CommissionIncomeService commissionIncomeService) {
		this.commissionIncomeService = commissionIncomeService;
	}

	public void setCampaignHistoryService(
			CampaignHistoryService campaignHistoryService) {
		this.campaignHistoryService = campaignHistoryService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

//	public void testInsertCommissionIncome() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.MONTH, 5 - 1);
//		calendar.set(Calendar.YEAR, 2008);
//		calendar.set(Calendar.DATE, 9);
//
//		Calendar calendar1 = Calendar.getInstance();
//		calendar1.set(Calendar.MONTH, 12 - 1);
//		calendar1.set(Calendar.YEAR, 2008);
//		calendar1.set(Calendar.DATE, 5);
//
//		Calendar calendar2 = Calendar.getInstance();
//		calendar2.set(Calendar.MONTH, 8);
//		calendar2.set(Calendar.YEAR, 2008);
//		calendar2.set(Calendar.DATE, 10);
//
//		commissionIncomeService.insertCommissionIncome(74, calendar.getTime(),
//				calendar1.getTime(), calendar2.getTime());
//	}

//	 public void testPayCommission() {
//		CampaignHistory campaignHistory = campaignHistoryService.get(1);
//		commissionIncomeService.payCommission(campaignHistory, 25);
//	}
	
	@SuppressWarnings("unchecked")
	public void testCheckAffiliateIncome(){
		List<ListOrderedMap> list = commissionIncomeService.checkAffiliateIncome();
		List<String> keys = new ArrayList<String>();
		if(list != null && list.size()>0){
			keys = list.get(0).keyList();
			StringBuffer sb = new StringBuffer();
			for(String key : keys){
				sb.append(key);
				sb.append("			");
			}
			System.out.println(sb.toString());
			for(Object obj : list){
				ListOrderedMap map = (ListOrderedMap)obj;
				StringBuffer sb2 = new StringBuffer();
				for(String key : keys){
					sb2.append(map.get(key).toString());
					sb2.append("		");
				}
				System.out.println(sb2.toString());
			}
		}
	}
}
