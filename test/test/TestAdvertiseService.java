package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.AdvertiseService;

public class TestAdvertiseService extends
		AbstractTransactionalDataSourceSpringContextTests {

	private AdvertiseService advertiseService;

	public AdvertiseService getAdvertiseService() {
		return advertiseService;
	}

	public void setAdvertiseService(AdvertiseService advertiseService) {
		this.advertiseService = advertiseService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

//	public void testUpdateAdvertiseByLandingPageId() {
//		advertiseService.updateAdvertiseByLandingPageId(132);
//	}
	
	
	public void testDeleteAdvertisesByIds() {
		advertiseService.deleteAdvertisesByIds(165, 959, 960, 961, 962, 963, 964, 965, 967, 966, 968);
	}
}
