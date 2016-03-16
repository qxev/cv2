package test;

import java.util.List;
import java.util.Map;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.services.logic.CampaignService;

public class TestCampaignService extends AbstractTransactionalDataSourceSpringContextTests {

	private CampaignService campaignService;

	public void setCampaignService(CampaignService campaignService) {
		this.campaignService = campaignService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

	// public void testIsBetweenStartAndEnd() {
	// // List<Campaign> campaigns = campaignService.getCampaign();
	// // System.out.println(campaignService.getCampaignCount());
	// // for (Campaign campaign : campaigns) {
	// // System.out.println(campaign.getName());
	// // }
	// }
	//	
	// public void testHasCampaign(){
	// System.out.println(campaignService.hasCampaign(69, 0, 0, 25));
	// }

	// public void testGetCampaignAtReportSummary() {
	// Calendar c1 = Calendar.getInstance();
	// Calendar c2 = Calendar.getInstance();
	//	    
	// c1.set(2008, 1, 1);
	// c2.set(2008, 11, 1);
	// List<Campaign> findCampaignByName =
	// campaignService.findCampaignByName("达闻", c1.getTime(), c2.getTime());
	// for(Campaign campaign : findCampaignByName) {
	// System.out.println(campaign.getName()+"------------");
	// }
	// }

	// public void testIndustrySummary(){
	// Map<String,Integer> map = campaignService.getIndustrySummary();
	// System.out.println(map);
	// }

	// public void testFindRecommendCampaigns() {
	// List<Campaign> campaigns = campaignService.findNewestCampaigns();
	// for (Campaign campaign : campaigns) {
	// System.out.println(DateUtil.dateToString(campaign.getStartDate()));
	// }
	// }

	public void test() {
		List<Campaign> cs = campaignService.findCampaigns("碧欧泉CPS广告", true, null, 0, new Integer[] {});
		for (Campaign campaign : cs) {
			System.out.println(campaign.getName());
		}
	}

}
