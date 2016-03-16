package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.PartnerIdService;

public class PartnerIdServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	private PartnerIdService partnerIdService;

	public PartnerIdService getPartnerIdService() {
		return partnerIdService;
	}

	public void setPartnerIdService(PartnerIdService partnerIdService) {
		this.partnerIdService = partnerIdService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

	public void testFindOurIdsByType() {
		List<String> ourIds = partnerIdService.findOurIdsByType(2);
		System.out.println(ourIds);
	}
	
	public void testFindPartnerIdsByType() {
		List<String> ourIds = partnerIdService.findPartnerIdsByType(2);
		System.out.println(ourIds);
	}

}
