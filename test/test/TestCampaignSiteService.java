package test;

import java.util.Calendar;
import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CampaignSite;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;

public class TestCampaignSiteService extends
		AbstractTransactionalDataSourceSpringContextTests {

	private CampaignSiteService campaignSiteService;
	
	public void setCampaignSiteService(CampaignSiteService campaignSiteService) {
		this.campaignSiteService = campaignSiteService;
	}
	

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

	public void testGetSiteIds(){
		
		List<CampaignSite> campaignSites = campaignSiteService.getCampaignSites(2,2);
		
		campaignSiteService.getSiteIds(campaignSites);
	}

	
}
