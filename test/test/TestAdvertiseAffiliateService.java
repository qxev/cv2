package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.AdvertiseAffiliateService;

public class TestAdvertiseAffiliateService extends
		AbstractTransactionalDataSourceSpringContextTests {
	private AdvertiseAffiliateService advertiseAffiliateService;

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

	public void setAdvertiseAffiliateService(
			AdvertiseAffiliateService advertiseAffiliateService) {
		this.advertiseAffiliateService = advertiseAffiliateService;
	}

	public void testIsExistByAdvertiseAndSiteId() {
		assertEquals(true, advertiseAffiliateService.isExistByAdvertiseAndSiteId(135, 10318));
	}
}
