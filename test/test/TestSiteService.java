package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.logic.SiteService;

public class TestSiteService extends
		AbstractTransactionalDataSourceSpringContextTests {

	private SiteService siteService;

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public void testGetSiteNotInCampaignSite() {
//		List<Site> siteNotInCampaignSite = siteService
//				.getSiteNotInCampaignSite(2, 2);
////		System.out.println(siteNotInCampaignSite.size());
		List<Site> listForSelect = siteService.getListForSelect("a", 10);
	}
	
}
