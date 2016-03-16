package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.CampaignSiteService;

public class CampaignSiteServiceTest extends
		AbstractTransactionalDataSourceSpringContextTests {
	
	private CampaignSiteService campaignSiteService;
	
	public void setCampaignSiteService(CampaignSiteService campaignSiteService) {
		this.campaignSiteService = campaignSiteService;
	}
	
	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}
	
	public void testgetCamaignIds(){
		System.out.println(campaignSiteService.isRepeat(5, 32125));
		
//		List<Integer> camaignIds = campaignSiteService.getCamaignIds(3);
//		System.out.println(camaignIds.size());
	}
}
