package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.AffiliateCampaignService;

public class AffiliateCampaignServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	private AffiliateCampaignService affiliateCampaignService;

	public void setAffiliateCampaignService(AffiliateCampaignService affiliateCampaignService) {
		this.affiliateCampaignService = affiliateCampaignService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	public void testIsConcerned() {
		for (;;) {
			boolean concerned = affiliateCampaignService.isConcerned(2, 2, 1);
			System.out.println(concerned);
		}
	}
}
